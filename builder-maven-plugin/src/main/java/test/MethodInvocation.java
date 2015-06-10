package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvocation {
	private final Object object;
	private final Method method;

	public MethodInvocation(Object object, Method method) {
		this.object = object;
		this.method = method;
	}

	public void execute() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		method.invoke(object);
	}

	@Override
	public String toString() {
		return object.getClass().getName() + "." + method.getName() + "()";
	}
}
