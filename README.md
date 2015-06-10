# A maven plugin to use custom java code inside your maven build

With this plugin, you can put some java code in a source folder "src/build/java". The code will be executed during the maven build. Use annotations to bind the build classes to the maven lifecycle phases.

Requires minimum maven 3.1 

To test the plugin, de a "mvn install" on the projects in this order:

   1. builder-api
   1. builder-maven-plugin

Then to use the plugin in your project:

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
				<version>0.0.1-SNAPSHOT</version>
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
			<version>0.0.1-SNAPSHOT</version>
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

Contents of this repository:

   * builder-api: annotations to be used in your build classes
   * builder-maven-plugin: maven plugin/extension to be declared in the project's pom.xml
   * builder-maven-plugin-test: an example project
   