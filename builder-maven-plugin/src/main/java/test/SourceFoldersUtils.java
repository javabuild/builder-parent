package test;

import java.io.File;

import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;

public class SourceFoldersUtils {

	public static void addSourceFolders(MavenProject project) {
		project.addTestCompileSourceRoot(BuilderFolders.BUILD_SOURCES);
		project.addTestResource(toResource(BuilderFolders.BUILD_RESOURCES,
				project));
		project.addCompileSourceRoot(BuilderFolders.GENERATED_SOURCES);
		project.addResource(toResource(BuilderFolders.GENERATED_RESOURCES,
				project));
		project.addTestCompileSourceRoot(BuilderFolders.GENERATED_TEST_SOURCES);
		project.addTestResource(toResource(
				BuilderFolders.GENERATED_TEST_RESOURCES, project));
	}

	private static Resource toResource(String folderString, MavenProject project) {
		File folder = new File(project.getBasedir(), folderString);
		Resource resource = new Resource();
		resource.setDirectory(folder.getAbsolutePath());
		return resource;
	}

}
