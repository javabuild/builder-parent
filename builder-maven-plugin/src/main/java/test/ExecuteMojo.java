package test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.InstantiationStrategy;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name = "execute", defaultPhase = LifecyclePhase.NONE, threadSafe = true, requiresDependencyResolution = ResolutionScope.TEST, instantiationStrategy = InstantiationStrategy.KEEP_ALIVE)
public class ExecuteMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

	@Parameter(defaultValue = "${project.basedir}/src/build/java", readonly = true)
	private String sourcePath;

	@Parameter(defaultValue = "${project.basedir}/target/build-classes", readonly = true)
	private String classesPath;

	@Parameter(defaultValue = "${mojoExecution}", readonly = true)
	private MojoExecution mojoExecution;

	/**
	 * The directory where the webapp is built (used for webapps only).
	 */
	@Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}", readonly = true)
	private File webappDirectory;

	private BuildPlan buildPlan;

	private Phase currentPhase;

	private URLClassLoader classLoader;

	public void execute() throws MojoExecutionException {
		currentPhase = Phase.fromString(mojoExecution.getLifecyclePhase());
		getLog().info("Phase: " + currentPhase.toString());
		if (buildPlan == null) {
			initClassLoader();
			createBuildPlan();
		}
		executePlanForCurrentPhase();
	}

	private void initClassLoader() throws MojoExecutionException {
		try {
			List<String> testClasspathElements = project
					.getTestClasspathElements();
			URL[] urls = new URL[testClasspathElements.size() + 1];
			urls[0] = new File(classesPath).toURI().toURL();
			int i = 1;
			for (Iterator<String> iterator = testClasspathElements.iterator(); iterator
					.hasNext();) {
				String jar = iterator.next();
				urls[i] = new File(jar).toURI().toURL();
				i++;
			}
			System.out.println("Classpath:");
			for (int j = 0; j < urls.length; j++) {
				System.out.println(urls[j]);

			}
			classLoader = new URLClassLoader(urls, this.getClass()
					.getClassLoader());
		} catch (Exception e) {
			throw new MojoExecutionException(
					"Failed to initalize project classpath", e);
		}
	}

	private void executePlanForCurrentPhase() throws MojoExecutionException {
		try {
			buildPlan.execute(currentPhase);
			if (Phase.PREPARE_PACKAGE.equals(currentPhase))
				afterPreparePackage();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | IOException e) {
			throw new MojoExecutionException(
					"Failed to execute build classes for phase " + currentPhase,
					e);
		}

	}

	private void afterPreparePackage() throws IOException {
		File webappResources = new File(BuilderFolders.WEBAPP_RESOURCES);
		FileUtils.copyDirectory(webappResources, webappDirectory);

	}

	private void createBuildPlan() throws MojoExecutionException {
		buildPlan = new BuildPlan();
		getLog().info("Looking for build classes");
		File sourceFolder = new File(sourcePath);
		try {
			findBuildClasses("", sourceFolder);
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | IOException e) {
			throw new MojoExecutionException("Failed to load build classes", e);
		}
	}

	private void findBuildClasses(String parent, File folder)
			throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException {
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				if (!"".equals(parent))
					parent = parent + ".";
				parent = parent + file.getName();
				findBuildClasses(parent, file);
			} else {
				String className = parent;
				if (!"".equals(className))
					className = className + ".";
				className = className + file.getName();
				if (className.endsWith(".java"))
					className = className.substring(0, className.length() - 5);
				processClass(className);
			}
		}
	}

	private void processClass(String className) throws MalformedURLException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException, IOException {
		Class<?> theClass = classLoader.loadClass(className);
		if (theClass.isAnnotationPresent(Builder.class)) {
			Method[] methods = theClass.getDeclaredMethods();
			for (int j = 0; j < methods.length; j++) {
				Method method = methods[j];
				Execute execute = method.getAnnotation(Execute.class);
				if (execute != null) {
					Object instance = theClass.newInstance();
					buildPlan.addMethodExecution(execute.phase(), instance,
							method);
				}
			}
		}
	}
}