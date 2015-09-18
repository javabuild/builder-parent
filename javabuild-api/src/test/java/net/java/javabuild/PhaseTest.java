package net.java.javabuild;

import org.junit.Assert;
import org.junit.Test;

import net.java.javabuild.Phase;

public class PhaseTest {

	@Test
	public void testFromString() {
		Assert.assertNotNull(Phase.fromString("generate-sources"));
	}

}
