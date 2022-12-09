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

package com.liferay.site.initializer.testray.dispatch.task.executor.internal.dispatch.executor;

import com.liferay.dispatch.executor.DispatchTaskExecutor;
import com.liferay.dispatch.executor.DispatchTaskExecutorOutput;
import com.liferay.dispatch.model.DispatchTrigger;
import com.liferay.object.rest.dto.v1_0.ObjectEntry;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.site.initializer.testray.dispatch.task.executor.internal.dispatch.executor.autofill.util.SiteInitializerTestrayAutoFillBuilds;
import com.liferay.site.initializer.testray.dispatch.task.executor.internal.dispatch.executor.autofill.util.SiteInitializerTestrayAutoFillRuns;
import com.liferay.site.initializer.testray.dispatch.task.executor.internal.dispatch.executor.autofill.util.SiteInitializerTestrayAutoFillWrapper;

/**
 * @author Nilton Vieira
 */
@Component(
	property = {
		"dispatch.task.executor.cluster.mode=single-node",
		"dispatch.task.executor.name=testray-autofill",
		"dispatch.task.executor.overlapping=false",
		"dispatch.task.executor.type=testray-autofill"
	},
	service = DispatchTaskExecutor.class
)
public class SiteInitializerTestrayAutoFillDispatchTaskExecutor
	extends BaseSiteInitializerTestrayDispatchTaskExecutor {

	@Override
	public void doExecute(
			DispatchTrigger dispatchTrigger,
			DispatchTaskExecutorOutput dispatchTaskExecutorOutput)
		throws Exception {

		UnicodeProperties unicodeProperties =
			dispatchTrigger.getDispatchTaskSettingsUnicodeProperties();

		if (Validator.isNull(unicodeProperties.getProperty("testrayEntity1")) ||
			Validator.isNull(unicodeProperties.getProperty("testrayEntity2")) ||
			Validator.isNull(unicodeProperties.getProperty("autoFillType"))) {

			_log.error("The required properties are not set");

			return;
		}

		User user = _userLocalService.getUser(dispatchTrigger.getUserId());

		defaultDTOConverterContext = new DefaultDTOConverterContext(
			false, null, null, null, null, LocaleUtil.getSiteDefault(), null,
			user);

		PermissionChecker originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user));

		String originalName = PrincipalThreadLocal.getName();

		PrincipalThreadLocal.setName(user.getUserId());

		try {
			loadObjectDefinitions(dispatchTrigger.getCompanyId());

			_process(dispatchTrigger.getCompanyId(), unicodeProperties);
		}
		finally {
			PermissionThreadLocal.setPermissionChecker(
				originalPermissionChecker);

			PrincipalThreadLocal.setName(originalName);
		}
	}

	@Override
	public String getName() {
		return "testray-autofill";
	}

	private void _process(long companyId, UnicodeProperties unicodeProperties)
		throws Exception {

		long testrayEntityId1 = GetterUtil.getLong(
			unicodeProperties.getProperty("testrayEntityId1"));
		long testrayEntityId2 = GetterUtil.getLong(
			unicodeProperties.getProperty("testrayEntityId1"));
		String autoFillType = GetterUtil.getString(
			unicodeProperties.getProperty("autoFillType"));

		ObjectEntry objectEntry1 = getObjectEntry(
			autoFillType, testrayEntityId1);
		ObjectEntry objectEntry2 = getObjectEntry(
			autoFillType, testrayEntityId2);

		if (StringUtil.equals(autoFillType, "Run")) {
			SiteInitializerTestrayAutoFillWrapper
				siteInitializerTestrayAutoFillWrapper =
					new SiteInitializerTestrayAutoFillRuns();

			siteInitializerTestrayAutoFillWrapper.testrayAutoFill(
				companyId, objectEntry1, objectEntry2);
		}
		else if (StringUtil.equals(autoFillType, "Build")) {
			SiteInitializerTestrayAutoFillWrapper
				siteInitializerTestrayAutoFillWrapper =
					new SiteInitializerTestrayAutoFillBuilds();

			siteInitializerTestrayAutoFillWrapper.testrayAutoFill(
				companyId, objectEntry1, objectEntry2);
		}
		else {
			_log.error("AutoFill type selected is not available");
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SiteInitializerTestrayAutoFillDispatchTaskExecutor.class);

	@Reference
	private UserLocalService _userLocalService;

}