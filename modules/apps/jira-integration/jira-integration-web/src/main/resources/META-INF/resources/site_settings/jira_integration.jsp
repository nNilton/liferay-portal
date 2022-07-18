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

<div class="form-group row">
	<div class="col-md-12">
		<label class="control-label">
			<liferay-ui:message key="site-settings-strategy" />

			<liferay-ui:icon-help message='<%= LanguageUtil.format(resourceBundle, "site-settings-strategy-description", "jira-integration") %>' />
		</label>
	</div>

	<c:if test="<%= Validator.isNotNull(jiraIntegrationConfiguration.siteSettingsStrategy()) %>">
		<div class="col-md-12">
			<liferay-ui:message key='<%= "site-settings-strategy-" + jiraIntegrationConfiguration.siteSettingsStrategy() %>' />
		</div>
	</c:if>
</div>

<div class="row">
	<div class="col-md-12">

		<%
		boolean jiraIntegrationEnabled = GetterUtil.getBoolean(request.getAttribute(JiraIntegrationWebKeys.JIRA_INTEGRATION_ENABLED));

		boolean disabled = false;

		if (Objects.equals(jiraIntegrationConfiguration.siteSettingsStrategy(), "always-inherit") || Validator.isNull(jiraIntegrationConfiguration.siteSettingsStrategy())) {
			disabled = true;
		}
		%>

		<aui:input checked="<%= jiraIntegrationEnabled %>" disabled="<%= disabled %>" inlineLabel="right" label='<%= LanguageUtil.get(resourceBundle, "enabled") %>' labelCssClass="simple-toggle-switch" name="enabled" type="toggle-switch" value="<%= jiraIntegrationEnabled %>" />
	</div>
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
			<aui:input disabled="<%= disabled %>" label="jira-username" name="jiraUsername" type="text" value="<%= GetterUtil.getString(request.getAttribute(JiraIntegrationWebKeys.JIRA_INTEGRATION_JIRA_USERNAME)) %>" />
		</div>

		<div class="col-md-6">
			<aui:input disabled="<%= disabled %>" label="jira-password" name="jiraPassword" type="text" value="<%= GetterUtil.getString(request.getAttribute(JiraIntegrationWebKeys.JIRA_INTEGRATION_JIRA_PASSWORD)) %>" />
		</div>
	</div>
</div>