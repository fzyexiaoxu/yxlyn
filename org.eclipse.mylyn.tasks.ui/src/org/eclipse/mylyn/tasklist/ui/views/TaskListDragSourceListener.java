/*******************************************************************************
 * Copyright (c) 2004 - 2005 University Of British Columbia and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     University Of British Columbia - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylar.tasklist.ui.views;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.mylar.tasklist.ui.ITaskListElement;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

/**
 * @author Mik Kersten
 */
class TaskListDragSourceListener implements DragSourceListener {

	private final TaskListView view;

	/**
	 * @param view
	 */
	public TaskListDragSourceListener(TaskListView view) {
		this.view = view;
	}

	public void dragStart(DragSourceEvent event) {
		if (((StructuredSelection) this.view.getViewer().getSelection()).isEmpty()) {
			event.doit = false;
		}
	}

	public void dragSetData(DragSourceEvent event) {
		StructuredSelection selection = (StructuredSelection) this.view.getViewer().getSelection();
		if (selection.getFirstElement() instanceof ITaskListElement) {
			ITaskListElement element = (ITaskListElement) selection.getFirstElement();
	
			if (!selection.isEmpty() && element.isDragAndDropEnabled()) {
				event.data = "" + element.getHandleIdentifier();
			} else {
				event.data = "null";
			}
		}
	}

	public void dragFinished(DragSourceEvent event) {
		// don't care if the drag is done
	}
}