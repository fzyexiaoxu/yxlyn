/*******************************************************************************
 * Copyright (c) 2004, 2007 Mylyn project committers and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.mylyn.tasks.core.data;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @author Steffen Pingel
 * @since 3.0
 */
// TODO EDITOR return null if attribute value invalid for primitive types? 
public abstract class AbstractAttributeMapper {

	public AbstractAttributeMapper() {
	}

	public String mapToRepositoryKey(TaskData taskData, String key) {
		return key;
	}

	public boolean getBooleanValue(TaskAttribute attribute) {
		String booleanString = attribute.getValue();
		if (booleanString != null && booleanString.length() > 0) {
			return Boolean.parseBoolean(booleanString);
		}
		return false;
	}

	public void setBooleanValue(TaskAttribute attribute, Boolean value) {
		attribute.setValue(Boolean.toString(value));
	}

	public Date getDateValue(TaskAttribute attribute) {
		String dateString = attribute.getValue();
		try {
			if (dateString != null && dateString.length() > 0) {
				return new Date(Long.parseLong(dateString));
			}
		} catch (NumberFormatException e) {
			// ignore
		}
		return null;
	}

	public void setDateValue(TaskAttribute attribute, Date date) {
		if (date != null) {
			attribute.setValue(Long.toString(date.getTime()));
		} else {
			attribute.clearValues();
		}
	}

	/**
	 * Returns labelByValue.
	 */
	public Map<String, String> getOptions(TaskAttribute attribute) {
		return attribute.getOptions();
	}

	public void setValue(TaskAttribute attribute, String value) {
		attribute.setValue(value);
	}

	public void setValues(TaskAttribute attribute, String[] values) {
		attribute.setValues(Arrays.asList(values));
	}

	public String[] getValues(TaskAttribute attribute) {
		return attribute.getValues().toArray(new String[0]);
	}

	public String getValue(TaskAttribute taskAttribute) {
		return taskAttribute.getValue();
	}

	public String getLabel(TaskAttribute taskAttribute) {
		return taskAttribute.getMetaData(TaskAttribute.META_LABEL);
	}

	public String getValueLabel(TaskAttribute taskAttribute) {
		StringBuilder sb = new StringBuilder();
		String sep = "";
		for (String key : taskAttribute.getValues()) {
			sb.append(sep).append(key);
			sep = ", ";
		}
		return sb.toString();
	}

	public String[] getValueLabels(TaskAttribute taskAttribute) {
		return taskAttribute.getValues().toArray(new String[0]);
	}

	public abstract String getType(TaskAttribute taskAttribute);

	public TaskAttachment getTaskAttachment(TaskAttribute taskAttribute) {
		TaskData taskData = taskAttribute.getTaskData();
		String attachmentId = "";
		TaskAttachment attachment = new TaskAttachment(taskData.getRepositoryUrl(), taskData.getConnectorKind(),
				taskData.getTaskId(), attachmentId);
		return attachment;
	}

	public TaskAttachment createTaskAttachment(TaskData taskData) {
		TaskAttachment attachment = new TaskAttachment(taskData.getRepositoryUrl(), taskData.getConnectorKind(),
				taskData.getTaskId(), "");
		return attachment;
	}

	public TaskComment getTaskComment(TaskAttribute taskAttribute) {
		TaskData taskData = taskAttribute.getTaskData();
		String commentId = "";
		TaskComment comment = new TaskComment(taskData.getRepositoryUrl(), taskData.getConnectorKind(),
				taskData.getTaskId(), commentId);
		return comment;
	}

	public TaskAttributeProperties getProperties(TaskAttribute taskAttribute) {
		TaskAttributeProperties properties = new TaskAttributeProperties();
		properties.readOnly = Boolean.parseBoolean(taskAttribute.getMetaData(TaskAttribute.META_READ_ONLY));
		properties.showInTaskEditor = Boolean.parseBoolean(taskAttribute.getMetaData(TaskAttribute.META_SHOW_IN_EDITOR));
		properties.showInToolTip = Boolean.parseBoolean(taskAttribute.getMetaData(TaskAttribute.META_SHOW_IN_TOOL_TIP));
		return properties;
	}

	public TaskAttribute getContainer(TaskData taskData, String key) {
		return taskData.getRoot().getAttribute(key);
	}

	public void setTaskOperation(TaskData taskData, TaskOperation operation) {
		taskData.getRoot().getAttribute(TaskAttribute.CONTAINER_OPERATIONS).setValue(operation.getOperationId());
	}

	public TaskAttribute getAssoctiatedAttribute(TaskAttribute taskAttribute) {
		String id = taskAttribute.getMetaData(TaskAttribute.META_ASSOCIATED_ATTRIBUTE_ID);
		if (id != null) {
			taskAttribute.getAttribute(id);
		}
		return null;
	}

	public TaskOperation getTaskOperation(TaskAttribute taskAttribute) {
		TaskData taskData = taskAttribute.getTaskData();
		String operationId = "";
		TaskOperation operation = new TaskOperation(taskData.getRepositoryUrl(), taskData.getConnectorKind(),
				taskData.getTaskId(), operationId);
		return operation;
	}

	public RepositoryPerson getRepositoryPerson(TaskAttribute taskAttribute) {
		TaskData taskData = taskAttribute.getTaskData();
		return new RepositoryPerson(taskData.getConnectorKind(), taskData.getRepositoryUrl(), taskData.getTaskId(),
				taskAttribute.getValue());
	}

}
