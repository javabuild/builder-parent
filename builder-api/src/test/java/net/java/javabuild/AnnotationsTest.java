package net.java.javabuild;

import net.java.javabuild.Builder;
import net.java.javabuild.Execute;
import net.java.javabuild.Phase;

@Builder
public class AnnotationsTest {

	@Execute(phase = Phase.DEPLOY)
	public void sayHello() {

	}

}
