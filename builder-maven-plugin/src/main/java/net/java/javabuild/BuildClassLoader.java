package net.java.javabuild;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;

/**
 * Loads the classes first from the project test classpath, if not found from
 * the current build classpath except for the plugin annotation classes.
 * 
 */
public class BuildClassLoader extends URLClassLoader {
	private final static String BUILDER_API_PACKAGE;
	private final static ClassLoader CLASS_LOADER = BuildClassLoader.class.getClassLoader();
	static {
		String builderClass = Builder.class.getName();
		BUILDER_API_PACKAGE = builderClass.substring(0, builderClass.lastIndexOf('.'));
	}

	public BuildClassLoader(List<String> testClasspathElements, String classesPath) throws MalformedURLException {
		super(toUrlArray(testClasspathElements, classesPath), CLASS_LOADER);
	}

	private static URL[] toUrlArray(List<String> testClasspathElements, String classesPath)
			throws MalformedURLException {
		URL[] urls = new URL[testClasspathElements.size() + 1];
		urls[0] = new File(classesPath).toURI().toURL();
		int i = 1;
		for (Iterator<String> iterator = testClasspathElements.iterator(); iterator.hasNext();) {
			String jar = iterator.next();
			urls[i] = new File(jar).toURI().toURL();
			i++;
		}
		return urls;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (name.startsWith(BUILDER_API_PACKAGE))
			return CLASS_LOADER.loadClass(name);
		else
			return super.loadClass(name);
	}

}
