package test;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name = "add-source", defaultPhase = LifecyclePhase.INITIALIZE, threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE)
public class AddSourceMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

	public void execute() {
		project.addTestCompileSourceRoot(BuilderFolders.BUILD_SOURCES);
		project.addTestCompileSourceRoot(BuilderFolders.BUILD_RESOURCES);
		project.addCompileSourceRoot(BuilderFolders.GENERATED_SOURCES);
		project.addCompileSourceRoot(BuilderFolders.GENERATED_RESOURCES);
		project.addTestCompileSourceRoot(BuilderFolders.GENERATED_TEST_SOURCES);
		project.addTestCompileSourceRoot(BuilderFolders.GENERATED_TEST_RESOURCES);
	}

}