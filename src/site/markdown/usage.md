## Usage

The plugin requires minimum maven 3.1 

To use the plugin in your project:

   1. declare the plugin in the project's pom.xml
   1. add dependency to builder-api
   1. create a folder "src/build/java"
   1. create classes and use the annotations
   
```xml
<build>
	<plugins>
		<plugin>
			<groupId>test</groupId>
			<artifactId>builder-maven-plugin</artifactId>
			<version>1.0</version>
			<extensions>true</extensions>
			<!-- For m2eclipse to detect the source folder -->
			<executions>
				<execution>
					<id>add-source</id>
					<goals>
						<goal>add-source</goal>
					</goals>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
<dependencies>
	<dependency>
		<groupId>test</groupId>
		<artifactId>builder-api</artifactId>
		<version>1.0</version>
	</dependency>
```

```java
@Builder
public class MyCustomBuilder {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(MyCustomBuilder.class);

	@Execute(phase = Phase.GENERATE_SOURCES)
	public void sayHello() {
		LOGGER.info("Hello world");
	}
```

## Conventions

In your build classes, you can generate java sources, test sources, resources, add files to a webapp (for war projects) and add pages to the project's site. You just have to place the files in the following folders:

   * build classes: "src/build/java/"
   * build resources: "src/build/resources/"
   * generated sources: "target/builder/main/java/"
   * generated resources: "target/builder/main/resources/"
   * generated test sources: "target/builder/test/java/"
   * generated test resources: "target/builder/test/resources/"
   * additional files to include in the war file (for war projects): "target/builder/main/webapp/"
   * additional html pages to deploy in the project's site: "target/builder/site/"

For examples, look at project "builder-maven-plugin-test".
