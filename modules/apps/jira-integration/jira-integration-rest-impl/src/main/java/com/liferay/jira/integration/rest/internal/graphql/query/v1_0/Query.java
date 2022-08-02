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

package com.liferay.jira.integration.rest.internal.graphql.query.v1_0;

import com.liferay.jira.integration.rest.dto.v1_0.Issue;
import com.liferay.jira.integration.rest.resource.v1_0.IssueResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.pagination.Page;

import java.util.Map;
import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.constraints.NotEmpty;

import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Nilton Vieira
 * @generated
 */
@Generated("")
public class Query {

	public static void setIssueResourceComponentServiceObjects(
		ComponentServiceObjects<IssueResource>
			issueResourceComponentServiceObjects) {

		_issueResourceComponentServiceObjects =
			issueResourceComponentServiceObjects;
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {issue(issueId: ___, siteKey: ___){id, key, description, name, priority}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public Issue issue(
			@GraphQLName("siteKey") @NotEmpty String siteKey,
			@GraphQLName("issueId") String issueId)
		throws Exception {

		return _applyComponentServiceObjects(
			_issueResourceComponentServiceObjects,
			this::_populateResourceContext,
			issueResource -> issueResource.getIssue(
				Long.valueOf(siteKey), issueId));
	}

	@GraphQLName("IssuePage")
	public class IssuePage {

		public IssuePage(Page issuePage) {
			actions = issuePage.getActions();

			items = issuePage.getItems();
			lastPage = issuePage.getLastPage();
			page = issuePage.getPage();
			pageSize = issuePage.getPageSize();
			totalCount = issuePage.getTotalCount();
		}

		@GraphQLField
		protected Map<String, Map> actions;

		@GraphQLField
		protected java.util.Collection<Issue> items;

		@GraphQLField
		protected long lastPage;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(IssueResource issueResource)
		throws Exception {

		issueResource.setContextAcceptLanguage(_acceptLanguage);
		issueResource.setContextCompany(_company);
		issueResource.setContextHttpServletRequest(_httpServletRequest);
		issueResource.setContextHttpServletResponse(_httpServletResponse);
		issueResource.setContextUriInfo(_uriInfo);
		issueResource.setContextUser(_user);
		issueResource.setGroupLocalService(_groupLocalService);
		issueResource.setRoleLocalService(_roleLocalService);
	}

	private static ComponentServiceObjects<IssueResource>
		_issueResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private BiFunction<Object, String, Filter> _filterBiFunction;
	private GroupLocalService _groupLocalService;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private RoleLocalService _roleLocalService;
	private BiFunction<Object, String, Sort[]> _sortsBiFunction;
	private UriInfo _uriInfo;
	private com.liferay.portal.kernel.model.User _user;

}