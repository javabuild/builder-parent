package test;

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
	public void afterProjectsRead(MavenSession session)
			throws MavenExecutionException {
		MavenProject project = session.getCurrentProject();

		project.addTestCompileSourceRoot(BuilderFolders.BUILD_SOURCES);
		project.addTestCompileSourceRoot(BuilderFolders.BUILD_RESOURCES);
		project.addCompileSourceRoot(BuilderFolders.GENERATED_SOURCES);
		project.addCompileSourceRoot(BuilderFolders.GENERATED_RESOURCES);
		project.addTestCompileSourceRoot(BuilderFolders.GENERATED_TEST_SOURCES);
		project.addTestCompileSourceRoot(BuilderFolders.GENERATED_TEST_RESOURCES);

		Plugin plugin = new Plugin();
		plugin.setGroupId("test");
		plugin.setArtifactId("builder-maven-plugin");
		plugin.setVersion("0.0.1-SNAPSHOT");
		addPluginExecution(plugin, "compile", Phase.GENERATE_SOURCES);
		Phase[] lifecyclePhases = Phase.values();
		for (int i = 0; i < lifecyclePhases.length; i++) {
			addPluginExecution(plugin, "execute", lifecyclePhases[i]);
		}
		project.getBuild().addPlugin(plugin);
		logger.info("Maven builder extension initialized");
	}

	private void addPluginExecution(Plugin plugin, String goal, Phase phase) {
		PluginExecution pluginExecution = new PluginExecution();
		pluginExecution.addGoal(goal);
		pluginExecution.setPhase(phase.toString());
		plugin.addExecution(pluginExecution);
	}
}
