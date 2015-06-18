package test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder that copies resources from a webjar into the war.
 */
@Builder
public class CopyResourcesToWarBuilder {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(CopyResourcesToWarBuilder.class);

	private File warRoot = new File(BuilderFolders.WEBAPP_RESOURCES);

	@Execute(phase = Phase.PREPARE_PACKAGE)
	public void addFileToWebapp() throws IOException {
		LOGGER.info("Hello world from package phase!");
		warRoot.mkdirs();
		copyResourceFromWebjar("bootstrap/3.3.4/css/bootstrap.css");
		copyResourceFromWebjar("bootstrap/3.3.4/js/bootstrap.js");
	}

	private void copyResourceFromWebjar(String src) throws IOException {
		File targetFile = new File(warRoot, src);
		InputStream srcInputStream = this.getClass().getClassLoader()
				.getResourceAsStream("META-INF/resources/webjars/" + src);
		FileUtils.writeByteArrayToFile(targetFile,
				IOUtils.toByteArray(srcInputStream));
	}
}
