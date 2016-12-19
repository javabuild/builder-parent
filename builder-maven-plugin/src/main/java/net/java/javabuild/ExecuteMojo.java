package net.java.javabuild;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

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

	/**
	 * The directory where to copy the generated site.
	 */
	@Parameter(defaultValue = "${project.build.directory}/generated-site", readonly = true)
	private File generatedSiteDirectory;

	private BuildPlan buildPlan;

	private Phase currentPhase;

	private ClassLoader classLoader;

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
			classLoader = new BuildClassLoader(project.getTestClasspathElements(), classesPath);
		} catch (Exception e) {
			throw new MojoExecutionException("Failed to initalize project classpath", e);
		}
	}

	private void executePlanForCurrentPhase() throws MojoExecutionException {
		try {
			buildPlan.execute(currentPhase);
			if (Phase.PREPARE_PACKAGE.equals(currentPhase))
				afterPreparePackage();
			else if (Phase.PRE_SITE.equals(currentPhase))
				afterPreSite();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException e) {
			throw new MojoExecutionException("Failed to execute build classes for phase " + currentPhase, e);
		}

	}

	private void afterPreparePackage() throws IOException {
		File webappResources = new File(BuilderFolders.WEBAPP_RESOURCES);
		if (webappResources.exists())
			FileUtils.copyDirectory(webappResources, webappDirectory);
	}

	private void afterPreSite() throws IOException {
		File site = new File(BuilderFolders.SITE);
		if (site.exists())
			FileUtils.copyDirectory(site, new File(generatedSiteDirectory, "resources/"));
	}

	private void createBuildPlan() throws MojoExecutionException {
		buildPlan = new BuildPlan();
		getLog().info("Looking for build classes");
		File sourceFolder = new File(sourcePath);
		try {
			findBuildClasses("", sourceFolder);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException e) {
			throw new MojoExecutionException("Failed to load build classes", e);
		}
	}

	private void findBuildClasses(String parent, File folder)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
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

	private void processClass(String className) throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
		Class<?> theClass = classLoader.loadClass(className);
		if (theClass.isAnnotationPresent(Builder.class)) {
			Object instance = theClass.newInstance();
			Method[] methods = theClass.getDeclaredMethods();
			for (int j = 0; j < methods.length; j++) {
				Method method = methods[j];
				Execute execute = method.getAnnotation(Execute.class);
				if (execute != null) {
					buildPlan.addMethodExecution(execute.phase(), instance, method);
				}
			}
		}
	}
}