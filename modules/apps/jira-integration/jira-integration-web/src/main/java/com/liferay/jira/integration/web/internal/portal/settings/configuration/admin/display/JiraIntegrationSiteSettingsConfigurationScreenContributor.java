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

package com.liferay.jira.integration.web.internal.portal.settings.configuration.admin.display;

import com.liferay.jira.integration.web.internal.configuration.JiraIntegrationConfiguration;
import com.liferay.jira.integration.web.internal.configuration.JiraIntegrationConfigurationUtil;
import com.liferay.jira.integration.web.internal.constants.JiraIntegrationWebKeys;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.site.settings.configuration.admin.display.SiteSettingsConfigurationScreenContributor;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Nilton Vieira
 */
public class JiraIntegrationSiteSettingsConfigurationScreenContributor
	implements SiteSettingsConfigurationScreenContributor {

	@Override
	public String getCategoryKey() {
		return "jira-integration";
	}

	@Override
	public String getJspPath() {
		return "/site_settings/jira_integration.jsp";
	}

	@Override
	public String getKey() {
		return "site-configuration-jira-integration";
	}

	@Override
	public String getName(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return _language.get(resourceBundle, "jira-integration");
	}

	@Override
	public String getSaveMVCActionCommandName() {
		return "/jira_integration/save_site_configuration";
	}

	@Override
	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	public void setAttributes(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		JiraIntegrationConfiguration jiraIntegrationConfiguration =
			JiraIntegrationConfigurationUtil.getJiraIntegrationConfiguration(
				themeDisplay.getCompanyId(), themeDisplay.getSiteGroupId());

		httpServletRequest.setAttribute(
			JiraIntegrationConfiguration.class.getName(),
			JiraIntegrationConfigurationUtil.getJiraIntegrationConfiguration(
				themeDisplay.getCompanyId(), 0));

		httpServletRequest.setAttribute(
			JiraIntegrationWebKeys.JIRA_INTEGRATION_ENABLED,
			jiraIntegrationConfiguration.enabled());
		httpServletRequest.setAttribute(
			JiraIntegrationWebKeys.JIRA_INTEGRATION_JIRA_PASSWORD,
			jiraIntegrationConfiguration.jiraPassword());
		httpServletRequest.setAttribute(
			JiraIntegrationWebKeys.JIRA_INTEGRATION_JIRA_USERNAME,
			jiraIntegrationConfiguration.jiraUsername());
	}

	@Reference
	private Language _language;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.jira.integration.web)",
		unbind = "-"
	)
	private ServletContext _servletContext;

}