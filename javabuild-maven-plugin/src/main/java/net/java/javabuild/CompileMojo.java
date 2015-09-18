package net.java.javabuild;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.compiler.CompilationFailureException;
import org.apache.maven.plugin.compiler.TestCompilerMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "compile", defaultPhase = LifecyclePhase.NONE, threadSafe = true, requiresDependencyResolution = ResolutionScope.TEST)
public class CompileMojo extends TestCompilerMojo {

	@Parameter(defaultValue = "${project.basedir}/src/build/java", readonly = true, required = true)
	private List<String> compileSourceRoots;

	@Parameter(defaultValue = "${project.build.directory}/build-classes", required = true, readonly = true)
	private File outputDirectory;

	@Override
	protected List<String> getCompileSourceRoots() {
		return compileSourceRoots;
	}

	@Override
	protected File getOutputDirectory() {
		return outputDirectory;
	}

	@Override
	public void execute() throws MojoExecutionException,
			CompilationFailureException {
		getLog().info("Compiling build classes");
		super.execute();
	}

}