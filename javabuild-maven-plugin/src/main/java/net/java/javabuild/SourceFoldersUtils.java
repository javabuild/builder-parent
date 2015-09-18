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

import java.io.File;

import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;

import net.java.javabuild.BuilderFolders;

public class SourceFoldersUtils {

	public static void addSourceFolders(MavenProject project) {
		project.addTestCompileSourceRoot(BuilderFolders.BUILD_SOURCES);
		project.addTestResource(toResource(BuilderFolders.BUILD_RESOURCES,
				project));
		project.addCompileSourceRoot(BuilderFolders.GENERATED_SOURCES);
		project.addResource(toResource(BuilderFolders.GENERATED_RESOURCES,
				project));
		project.addTestCompileSourceRoot(BuilderFolders.GENERATED_TEST_SOURCES);
		project.addTestResource(toResource(
				BuilderFolders.GENERATED_TEST_RESOURCES, project));
	}

	private static Resource toResource(String folderString, MavenProject project) {
		File folder = new File(project.getBasedir(), folderString);
		Resource resource = new Resource();
		resource.setDirectory(folder.getAbsolutePath());
		return resource;
	}

}
