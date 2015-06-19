package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.context.Context;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * A builder that generates pages from the Thymeleaf pages of the project and
 * put them in the project site.
 */
@Builder
public class ThymeleafPagesGenerator {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(ThymeleafPagesGenerator.class);

	private Charset charset = Charset.forName("UTF-8");

	private File siteFolder;

	private TemplateEngine templateEngine;

	@Execute(phase = Phase.PRE_SITE)
	public void generatePages() throws IOException {
		init();
		generatePage("home", Locale.ENGLISH);
		generatePage("home", Locale.FRENCH);
	}

	private void init() {
		siteFolder = new File(BuilderFolders.SITE);
		siteFolder.mkdirs();
		TemplateResolver templateResolver = new TemplateResolver();
		templateResolver.setResourceResolver(new ProjectResourceResolver());
		templateResolver.setSuffix(".html");
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		templateEngine.initialize();
	}

	public void generatePage(String page, Locale locale) throws IOException {
		LOGGER.info("Generating page " + page + " for locale "
				+ locale.toString());
		File pageFile = new File(siteFolder, page + "_" + locale.toString()
				+ ".html");
		if (pageFile.exists())
			pageFile.delete();
		Context context = new Context(locale);
		context.setVariable("variable1", "This is a variable");
		StringWriter writer = new StringWriter();
		templateEngine.process(page, context, writer);
		FileUtils.writeStringToFile(pageFile, writer.toString(), charset);
	}

	private final class ProjectResourceResolver implements IResourceResolver {
		File srcMainResourceFolder = new File("src/main/resources/");

		@Override
		public InputStream getResourceAsStream(
				TemplateProcessingParameters templateProcessingParameters,
				String resourceName) {
			try {
				File file = new File(srcMainResourceFolder, resourceName);
				if (!file.exists())
					return null;
				LOGGER.info("Reading: " + file.toURI());
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				return null;
			}
		}

		@Override
		public String getName() {
			return srcMainResourceFolder.toString();
		}
	}
}
