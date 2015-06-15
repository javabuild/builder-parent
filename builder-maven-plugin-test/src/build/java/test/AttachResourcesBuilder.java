package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder that copies resources from a webjar.
 */
@Builder
public class AttachResourcesBuilder {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(AttachResourcesBuilder.class);

	@Execute(phase = Phase.PREPARE_PACKAGE)
	public void sayHelloFromInstallPhase() throws IOException {
		LOGGER.info("Hello world from package phase!");
		File warRoot = new File(
				"./target/maven-builder-plugin-test-0.0.1-SNAPSHOT/");
		warRoot.mkdirs();
		File test = new File(warRoot, "test.txt");
		FileUtils.writeStringToFile(test, "Hello world!");
	}
}
