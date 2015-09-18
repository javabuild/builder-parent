package net.java.javabuild;

public class BuilderFolders {
	private BuilderFolders() {
		// Do not instantiate, only constants
	}

	public final static String BUILD_SOURCES = "src/build/java/";
	public final static String BUILD_RESOURCES = "src/build/resources/";
	public final static String GENERATED_SOURCES = "target/javabuild/main/java/";
	public final static String GENERATED_RESOURCES = "target/javabuild/main/resources/";
	public final static String GENERATED_TEST_SOURCES = "target/javabuild/test/java/";
	public final static String GENERATED_TEST_RESOURCES = "target/javabuild/test/resources/";
	public final static String WEBAPP_RESOURCES = "target/javabuild/main/webapp/";
	public final static String SITE = "target/javabuild/site/";
}
