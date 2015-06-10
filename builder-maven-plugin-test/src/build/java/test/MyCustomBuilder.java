package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Builder
public class MyCustomBuilder {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(MyCustomBuilder.class);

	@Execute(phase = Phase.GENERATE_SOURCES)
	public void sayHello() {
		LOGGER.info("Hello world");
	}

	@Execute(phase = Phase.INSTALL)
	public void sayHelloFromInstallPhase() {
		LOGGER.info("Hello world again!");
	}
}
