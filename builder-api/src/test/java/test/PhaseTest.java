package test;

import org.junit.Assert;
import org.junit.Test;

public class PhaseTest {

	@Test
	public void testFromString() {
		Assert.assertNotNull(Phase.fromString("generate-sources"));
	}

}
