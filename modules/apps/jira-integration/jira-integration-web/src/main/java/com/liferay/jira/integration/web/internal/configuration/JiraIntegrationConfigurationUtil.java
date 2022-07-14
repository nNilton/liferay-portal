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

package com.liferay.jira.integration.web.internal.configuration;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Objects;

/**
 * @author Nilton Vieira
 */
public class JiraIntegrationConfigurationUtil {

	public static JiraIntegrationConfiguration getJiraIntegrationConfiguration(
		long companyId, long groupId) {

		try {
			JiraIntegrationConfiguration companyJiraIntegrationConfiguration =
				ConfigurationProviderUtil.getCompanyConfiguration(
					JiraIntegrationConfiguration.class, companyId);

			if ((groupId == 0) ||
				Objects.equals(
					companyJiraIntegrationConfiguration.siteSettingsStrategy(),
					"always-inherit")) {

				return companyJiraIntegrationConfiguration;
			}

			JiraIntegrationConfiguration groupJiraIntegrationConfiguration =
				ConfigurationProviderUtil.getGroupConfiguration(
					JiraIntegrationConfiguration.class, groupId);

			if (Objects.equals(
					companyJiraIntegrationConfiguration.siteSettingsStrategy(),
					"always-override")) {

				return groupJiraIntegrationConfiguration;
			}

			if (Objects.equals(
					companyJiraIntegrationConfiguration.siteSettingsStrategy(),
					"inherit-or-override")) {

				if (Validator.isNotNull(
						groupJiraIntegrationConfiguration.jiraUsername()) &&
					Validator.isNotNull(
						groupJiraIntegrationConfiguration.jiraPassword())) {

					return groupJiraIntegrationConfiguration;
				}

				return companyJiraIntegrationConfiguration;
			}

			return companyJiraIntegrationConfiguration;
		}
		catch (ConfigurationException configurationException) {
			return ReflectionUtil.throwException(configurationException);
		}
	}

}