package test;

public class BuilderFolders {
	private BuilderFolders() {
		// Do not instantiate, only constants
	}

	public final static String BUILD_SOURCES = "src/build/java/";
	public final static String BUILD_RESOURCES = "src/build/resources/";
	public final static String GENERATED_SOURCES = "target/builder/main/java/";
	public final static String GENERATED_RESOURCES = "target/builder/main/resources/";
	public final static String GENERATED_TEST_SOURCES = "target/builder/test/java/";
	public final static String GENERATED_TEST_RESOURCES = "target/builder/test/resources/";
	public final static String WEBAPP_RESOURCES = "target/builder/main/webapp/";
	public final static String SITE = "target/builder/site/";
}
