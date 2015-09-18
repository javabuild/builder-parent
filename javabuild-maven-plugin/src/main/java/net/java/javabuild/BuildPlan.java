package net.java.javabuild;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.java.javabuild.Phase;

public class BuildPlan {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(BuildPlan.class);

	private Map<Phase, List<MethodInvocation>> phases = new HashMap<Phase, List<MethodInvocation>>();

	public void execute(Phase phase) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		List<MethodInvocation> methodInvocations = phases.get(phase);
		if (methodInvocations != null) {
			for (Iterator<MethodInvocation> iterator = methodInvocations
					.iterator(); iterator.hasNext();) {
				MethodInvocation methodInvocation = iterator.next();
				LOGGER.info("Invoking: " + methodInvocation.toString());
				methodInvocation.execute();
			}
		} else {
			LOGGER.info("Nothing to execute");
		}
	}

	public void addMethodExecution(Phase phase, Object object, Method method) {
		List<MethodInvocation> methodInvocation = phases.get(phase);
		if (methodInvocation == null) {
			methodInvocation = new ArrayList<MethodInvocation>();
			phases.put(phase, methodInvocation);
		}
		methodInvocation.add(new MethodInvocation(object, method));
	}

}
