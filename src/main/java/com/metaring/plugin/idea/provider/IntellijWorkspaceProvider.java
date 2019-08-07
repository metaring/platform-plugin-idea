/**
 * Copyright 2019 MetaRing s.r.l.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.metaring.plugin.idea.provider;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.messages.MessageBus;
import com.metaring.plugin.ProjectProvider;
import com.metaring.plugin.WorkspaceProvider;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IntellijWorkspaceProvider extends WorkspaceProvider {

    public IntellijWorkspaceProvider() {
        super();
    }

    @Override
    protected void startUpdateCallback() {
        ProjectManager PM = ProjectManager.getInstance();
        Project[] allProjects = PM.getOpenProjects();
        for (Project project : allProjects) {
            MessageBus messageBus = project.getMessageBus();
            messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
                @Override
                public void after(@NotNull List<? extends VFileEvent> events) {
                    if (events.size() > 0) {
                        update();
                    }
                }
            });
        }
    }

    @Override
    public ProjectProvider[] listAllProjects() {
        List<ProjectProvider> list = new ArrayList<>();
        try {
            ProjectManager PM = ProjectManager.getInstance();
            Project[] allProjects = PM.getOpenProjects();
            for (Project project : allProjects) {
                String name = project.getName();
                if (name.startsWith("/")) {
                    name = name.substring(1);
                }
                String path = new File(Objects.requireNonNull(project.getBasePath())).getAbsolutePath().replace("\\", "/");
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                if (!path.endsWith("/")) {
                    path += "/";
                }
                list.add(new IntellijProject(name, path, project));
            }
        } catch (Exception e) {
        }
        return list.toArray(new ProjectProvider[list.size()]);
    }

    private final class IntellijProject extends ProjectProvider {
        private final Project project;

        private IntellijProject(String name, String path, Project project) {
            super(name, path);
            this.project = project;
        }

        @Override
        protected final void doRefresh() throws Exception {
            VfsUtil.markDirtyAndRefresh(true, true, true, ProjectRootManager.getInstance(project).getContentRoots());
        }
    }
}