package test;

import org.junit.Assert;
import org.junit.Test;

public class MyDaoTest {

	@Test
	public void testReadLine() throws Exception {
		MyDao myDao = new MyDao();
		String result = myDao.readLine(2);
		Assert.assertEquals("Hello World 2", result);
	}

}
