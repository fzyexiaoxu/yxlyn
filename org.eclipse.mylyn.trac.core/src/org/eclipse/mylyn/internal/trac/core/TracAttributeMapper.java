/*******************************************************************************
 * Copyright (c) 2006, 2008 Steffen Pingel and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.internal.trac.core;

import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.internal.trac.core.client.ITracClient;
import org.eclipse.mylyn.internal.trac.core.util.TracUtil;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;

/**
 * Provides a mapping from Mylyn task keys to Trac ticket keys.
 * 
 * @author Steffen Pingel
 */
public class TracAttributeMapper extends TaskAttributeMapper {

	public enum Flag {
		READ_ONLY, ATTRIBUTE, PEOPLE
	};

	public static final String NEW_CC = "task.common.newcc"; //$NON-NLS-1$

	public static final String REMOVE_CC = "task.common.removecc"; //$NON-NLS-1$

	public static final EnumSet<Flag> NO_FLAGS = EnumSet.noneOf(Flag.class);

	private final ITracClient client;

	public static boolean isInternalAttribute(TaskAttribute attribute) {
		String type = attribute.getMetaData().getType();
		if (TaskAttribute.TYPE_ATTACHMENT.equals(type) || TaskAttribute.TYPE_OPERATION.equals(type)
				|| TaskAttribute.TYPE_COMMENT.equals(type)) {
			return true;
		}
		String id = attribute.getId();
		return TaskAttribute.COMMENT_NEW.equals(id) || TaskAttribute.ADD_SELF_CC.equals(id) || REMOVE_CC.equals(id)
				|| NEW_CC.equals(id);
	}

	public TracAttributeMapper(TaskRepository taskRepository, ITracClient client) {
		super(taskRepository);
		Assert.isNotNull(client);
		this.client = client;
	}

	@Override
	public Date getDateValue(TaskAttribute attribute) {
		return TracUtil.parseDate(attribute.getValue());
	}

	@Override
	public String mapToRepositoryKey(TaskAttribute parent, String key) {
		TracAttribute attribute = TracAttribute.getByTaskKey(key);
		return (attribute != null) ? attribute.getTracKey() : key;
	}

	@Override
	public void setDateValue(TaskAttribute attribute, Date date) {
		if (date == null) {
			attribute.clearValues();
		} else {
			attribute.setValue(TracUtil.toTracTime(date) + ""); //$NON-NLS-1$
		}
	}

	@Override
	public Map<String, String> getOptions(TaskAttribute attribute) {
		Map<String, String> options = getRepositoryOptions(attribute);
		return (options != null) ? options : super.getOptions(attribute);
	}

	public Map<String, String> getRepositoryOptions(TaskAttribute attribute) {
		if (client.hasAttributes()) {
			String attributeId = attribute.getId();
			if (TracAttribute.STATUS.getTracKey().equals(attributeId)) {
				return getOptions(client.getTicketStatus(), false);
			} else if (TracAttribute.RESOLUTION.getTracKey().equals(attributeId)) {
				return getOptions(client.getTicketResolutions(), false);
			} else if (TracAttribute.COMPONENT.getTracKey().equals(attributeId)) {
				return getOptions(client.getComponents(), false);
			} else if (TracAttribute.VERSION.getTracKey().equals(attributeId)) {
				return getOptions(client.getVersions(), true);
			} else if (TracAttribute.PRIORITY.getTracKey().equals(attributeId)) {
				return getOptions(client.getPriorities(), false);
			} else if (TracAttribute.SEVERITY.getTracKey().equals(attributeId)) {
				return getOptions(client.getSeverities(), false);
			} else if (TracAttribute.MILESTONE.getTracKey().equals(attributeId)) {
				return getOptions(client.getMilestones(), true);
			} else if (TracAttribute.TYPE.getTracKey().equals(attributeId)) {
				return getOptions(client.getTicketTypes(), false);
			}
		}
		return null;
	}

	private Map<String, String> getOptions(Object[] values, boolean allowEmpty) {
		if (values != null && values.length > 0) {
			Map<String, String> options = new LinkedHashMap<String, String>();
			if (allowEmpty) {
				options.put("", ""); //$NON-NLS-1$ //$NON-NLS-2$
			}
			for (Object value : values) {
				options.put(value.toString(), value.toString());
			}
			return options;
		}
		return null;
	}

}
