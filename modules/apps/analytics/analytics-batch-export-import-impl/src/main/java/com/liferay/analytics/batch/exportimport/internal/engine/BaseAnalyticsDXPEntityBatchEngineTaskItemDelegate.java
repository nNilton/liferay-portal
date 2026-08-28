/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.analytics.batch.exportimport.internal.engine;

import com.liferay.analytics.batch.exportimport.internal.odata.entity.AnalyticsDXPEntityEntityModel;
import com.liferay.batch.engine.BaseBatchEngineTaskItemDelegate;
import com.liferay.batch.engine.pagination.Page;
import com.liferay.batch.engine.pagination.Pagination;
import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.odata.entity.EntityModel;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcos Martins
 */
public abstract class BaseAnalyticsDXPEntityBatchEngineTaskItemDelegate<T>
	extends BaseBatchEngineTaskItemDelegate<T> {

	public abstract Page<T> doRead(
			Filter filter, Pagination pagination, Sort[] sorts,
			Map<String, Serializable> parameters, String search)
		throws Exception;

	@Override
	public EntityModel getEntityModel(Map<String, List<String>> multivaluedMap)
		throws Exception {

		return _entityModel;
	}

	@Override
	public Page<T> read(
			Filter filter, Pagination pagination, Sort[] sorts,
			Map<String, Serializable> parameters, String search)
		throws Exception {

		checkPermission();

		return doRead(filter, pagination, sorts, parameters, search);
	}

	protected DynamicQuery buildDynamicQuery(
		long companyId, DynamicQuery dynamicQuery,
		Map<String, Serializable> parameters) {

		dynamicQuery.add(RestrictionsFactoryUtil.eq("companyId", companyId));

		Serializable resourceLastModifiedDate = parameters.get(
			"resourceLastModifiedDate");

		if (resourceLastModifiedDate == null) {
			return dynamicQuery;
		}

		dynamicQuery.add(
			RestrictionsFactoryUtil.gt(
				"modifiedDate", resourceLastModifiedDate));

		return dynamicQuery;
	}

	protected Predicate buildPredicate(
		BaseTable<?> baseTable, long companyId,
		Map<String, Serializable> parameters) {

		Column<?, Long> companyIdColumn = (Column<?, Long>)baseTable.getColumn(
			"companyId");

		Predicate predicate = companyIdColumn.eq(companyId);

		Serializable resourceLastModifiedDate = parameters.get(
			"resourceLastModifiedDate");

		if (resourceLastModifiedDate == null) {
			return predicate;
		}

		Column<?, Date> modifiedDateColumn =
			(Column<?, Date>)baseTable.getColumn("modifiedDate");

		return predicate.and(
			modifiedDateColumn.gt((Date)resourceLastModifiedDate));
	}

	protected void checkPermission() throws PortalException {
		long companyId = contextCompany.getCompanyId();
		long userId = contextUser.getUserId();

		if (userLocalService.hasRoleUser(
				companyId, RoleConstants.ADMINISTRATOR, userId, true) ||
			userLocalService.hasRoleUser(
				companyId, RoleConstants.ANALYTICS_ADMINISTRATOR, userId,
				true)) {

			return;
		}

		throw new PrincipalException.MustBeCompanyAdmin(userId);
	}

	@Reference
	protected UserLocalService userLocalService;

	private static final EntityModel _entityModel =
		new AnalyticsDXPEntityEntityModel();

}