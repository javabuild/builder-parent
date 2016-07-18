package net.java.javabuild;

import java.io.IOException;
import java.util.Properties;

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

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.slf4j.Logger;

@Component(role = AbstractMavenLifecycleParticipant.class)
public class MavenBuilderExtension extends AbstractMavenLifecycleParticipant {
	@Requirement
	private Logger logger;

	@Override
	public void afterProjectsRead(MavenSession session) throws MavenExecutionException {
		MavenProject project = session.getCurrentProject();
		SourceFoldersUtils.addSourceFolders(project);
		addPluginExecutions(project);
		logger.info("Maven builder extension initialized");
	}

	private void addPluginExecutions(MavenProject project) throws MavenExecutionException {
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getResourceAsStream("version.properties"));
		} catch (IOException e) {
			throw new MavenExecutionException("Could not read plugin properties", e);
		}
		Plugin plugin = new Plugin();
		plugin.setGroupId(properties.getProperty("groupId"));
		plugin.setArtifactId(properties.getProperty("artifactId"));
		plugin.setVersion(properties.getProperty("version"));
		addPluginExecution(plugin, "compile", Phase.GENERATE_SOURCES);
		addPluginExecution(plugin, "compile", Phase.PRE_SITE);
		Phase[] lifecyclePhases = Phase.values();
		for (int i = 0; i < lifecyclePhases.length; i++) {
			addPluginExecution(plugin, "execute", lifecyclePhases[i]);
		}
		project.getBuild().addPlugin(plugin);
	}

	private void addPluginExecution(Plugin plugin, String goal, Phase phase) {
		PluginExecution pluginExecution = new PluginExecution();
		pluginExecution.addGoal(goal);
		pluginExecution.setPhase(phase.toString());
		plugin.addExecution(pluginExecution);
	}
}
