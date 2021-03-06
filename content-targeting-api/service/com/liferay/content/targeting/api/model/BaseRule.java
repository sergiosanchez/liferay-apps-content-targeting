/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.content.targeting.api.model;

import com.liferay.content.targeting.model.RuleInstance;
import com.liferay.content.targeting.util.ContentTargetingContextUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.security.permission.ResourceActionsUtil;

import java.util.Locale;
import java.util.Map;

/**
 * @author Eduardo Garcia
 */
public abstract class BaseRule implements Rule {

	@Override
	public void activate() {
		if (_log.isDebugEnabled()) {
			_log.debug("Rule activate: " + getClass().getSimpleName());
		}
	}

	@Override
	public void deActivate() {
		if (_log.isDebugEnabled()) {
			_log.debug("Rule deactivate: " + getClass().getSimpleName());
		}
	}

	@Override
	public String getDescription(Locale locale) {
		String key = getClass().getName().concat(".description");

		String description = ResourceActionsUtil.getModelResource(locale, key);

		if (description.endsWith(key)) {
			description = getShortDescription(locale);
		}

		return description;
	}

	@Override
	public String getFormHTML(
		RuleInstance ruleInstance, Map<String, Object> context,
		Map<String, String> values) {

		String content = StringPool.BLANK;

		try {
			populateContext(ruleInstance, context, values);

			content = ContentTargetingContextUtil.parseTemplate(
				getClass(), getFormTemplatePath(), context);
		}
		catch (Exception e) {
			_log.error(
			"Error while processing rule form template " +
				_FORM_TEMPLATE_PATH,
			e);
		}

		return content;
	}

	@Override
	public String getIcon() {
		return "icon-retweet";
	}

	@Override
	public String getName(Locale locale) {
		return ResourceActionsUtil.getModelResource(
			locale, getClass().getName());
	}

	@Override
	public String getRuleCategoryKey() {
		return StringPool.BLANK;
	}

	@Override
	public String getRuleKey() {
		return getClass().getSimpleName();
	}

	@Override
	public String getShortDescription(Locale locale) {
		String key = getClass().getName().concat(".shortDescription");

		String shortDescription = ResourceActionsUtil.getModelResource(
			locale, key);

		if (shortDescription.endsWith(key)) {
			shortDescription = StringPool.BLANK;
		}

		return shortDescription;
	}

	@Override
	public boolean isInstantiable() {
		return false;
	}

	protected String getFormTemplatePath() {
		return _FORM_TEMPLATE_PATH;
	}

	protected void populateContext(
		RuleInstance ruleInstance, Map<String, Object> context,
		Map<String, String> values) {
	}

	protected static final String _FORM_TEMPLATE_PATH =
		"templates/ct_fields.ftl";

	private static Log _log = LogFactoryUtil.getLog(BaseRule.class);

}