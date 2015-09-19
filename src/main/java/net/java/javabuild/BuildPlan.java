package net.java.javabuild;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
