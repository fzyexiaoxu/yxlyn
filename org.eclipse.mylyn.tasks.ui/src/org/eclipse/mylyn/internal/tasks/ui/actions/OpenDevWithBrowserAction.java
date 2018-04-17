/*******************************************************************************
 * Copyright (c) 2004, 2010 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.internal.tasks.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.mylyn.internal.tasks.ui.util.TasksUiInternal;
import org.eclipse.mylyn.tasks.core.IRepositoryElement;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITask.PriorityLevel;
import org.eclipse.mylyn.tasks.core.ITaskActivationListener;
import org.eclipse.mylyn.tasks.core.ITaskActivityListener;
import org.eclipse.mylyn.tasks.core.ITaskContainer;
import org.eclipse.mylyn.tasks.core.TaskActivationAdapter;
import org.eclipse.mylyn.tasks.core.TaskActivityAdapter;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.ITasksUiConstants;
import org.eclipse.mylyn.tasks.ui.TaskElementLabelProvider;
import org.eclipse.mylyn.tasks.ui.TasksUi;
/**
 * @author Mik Kersten
 * @author Rob Elves
 * @author Steffen Pingel
 */
public class OpenDevWithBrowserAction extends BaseSelectionListenerAction {

	public static final String ID = "org.eclipse.mylyn.tasklist.actions.open.devtoolbrowser"; //$NON-NLS-1$
  public String devToolUrl = "http://localhost:8099";
	public OpenDevWithBrowserAction() {
		super(Messages.OpenDevWithBrowserAction_Open_with_Browser);
		setToolTipText(Messages.OpenDevWithBrowserAction_Open_with_Browser);
		setId(ID);
	}

	@Override
	public void run() {
		if (super.getStructuredSelection() != null) {
			for (Iterator<?> iter = super.getStructuredSelection().iterator(); iter.hasNext();) {
				runWithSelection(iter.next());
			}
		}
	}

	private void runWithSelection(Object selectedObject) {
		if (selectedObject instanceof IRepositoryElement) {
			 devToolUrl = System.getenv("ECLIPSE_DEVELOPTOOL_URL");
			if (devToolUrl == null || devToolUrl == "")
			   devToolUrl = "http://localhost:8099";
			ITask task = TasksUi.getTaskActivityManager().getActiveTask();
			if (task != null) {
				 TasksUiUtil.openDevtool(devToolUrl +"?taskid="   + task.getTaskId() 
				                                    +"&taskKey="  + task.getTaskKey() 
				                                    +"&taskKind=" + task.getTaskKind()
				                                    +"&taskSummary=" + task.getSummary()
				                                    );	
				}
		
		}
	}

	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		if (!selection.isEmpty()) {
			for (Object element : selection.toList()) {
				if (element instanceof IRepositoryElement) {
						if (TasksUiInternal.isValidUrl(devToolUrl)) {
							return true;
						}
				}
			}
		}
		return false;
	}

}
