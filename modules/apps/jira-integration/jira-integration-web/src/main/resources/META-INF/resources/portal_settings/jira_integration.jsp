<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
JiraIntegrationConfiguration jiraIntegrationConfiguration = (JiraIntegrationConfiguration)request.getAttribute(JiraIntegrationConfiguration.class.getName());
%>

<div class="row">
	<div class="col-md-12">
		<aui:input checked="<%= jiraIntegrationConfiguration.enabled() %>" inlineLabel="right" label='<%= LanguageUtil.get(resourceBundle, "enabled") %>' labelCssClass="simple-toggle-switch" name="enabled" type="toggle-switch" value="<%= jiraIntegrationConfiguration.enabled() %>" />
	</div>
</div>

<div class="form-group row">
	<div class="col-md-12">
		<aui:select label="site-settings-strategy" name="siteSettingsStrategy" onchange='<%= liferayPortletResponse.getNamespace() + "onChangeJiraIntegrationSiteSettingsStrategy(event);" %>' required="<%= true %>" value="<%= jiraIntegrationConfiguration.siteSettingsStrategy() %>">
			<aui:option label="" value="" />

			<%
			for (String jiraIntegrationSiteSettingsStrategy : JiraIntegrationConstants.SITE_SETTINGS_STRATEGIES) {
			%>

				<aui:option label='<%= "site-settings-strategy-" + jiraIntegrationSiteSettingsStrategy %>' value="<%= jiraIntegrationSiteSettingsStrategy %>" />

			<%
			}
			%>

		</aui:select>

		<label class="text-secondary">
			<liferay-ui:message arguments="jira-integration" key="site-settings-strategy-description" />
		</label>
	</div>JiraIntegrationConfiguration
</div>

<div id="<portlet:namespace />jiraIntegrationProviderCredentials">
	<div class="mb-4">
		<liferay-learn:message
			key="general"
			resource="jira-integration-web"
		/>
	</div>

	<div class="form-group row">
		<div class="col-md-6">
			<aui:input label="jira-username" name="jiraUsername" type="text" value="<%= jiraIntegrationConfiguration.jiraUsername() %>" />
		</div>

		<div class="col-md-6">
			<aui:input label="jira-password" name="jiraPassword" type="text" value="<%= jiraIntegrationConfiguration.jiraPassword() %>" />
		</div>
	</div>
</div>

<script>
	function <portlet:namespace />onChangeJiraIntegrationSiteSettingsStrategy(
		event
	) {
		var jiraIntegrationProviderCredentialsElement = document.getElementById(
			'<portlet:namespace />jiraIntegrationProviderCredentials'
		);

		var jiraIntegrationSiteSettingsStrategyElement = document.getElementById(
			'<portlet:namespace />siteSettingsStrategy'
		);

		if (
			jiraIntegrationSiteSettingsStrategyElement.value === 'always-override'
		) {
			jiraIntegrationProviderCredentialsElement.classList.add('hide');
		}
		else {
			jiraIntegrationProviderCredentialsElement.classList.remove('hide');
		}
	}

	<portlet:namespace />onChangeJiraIntegrationSiteSettingsStrategy();
</script>