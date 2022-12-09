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

package com.liferay.site.initializer.testray.dispatch.task.executor.internal.dispatch.executor.autofill.util;

import com.liferay.object.rest.dto.v1_0.ObjectEntry;

/**
 * @author Nilton Vieira
 */
public class SiteInitializerTestrayAutoFillWrapper
	implements SiteInitializerTestrayAutoFill {

	public SiteInitializerTestrayAutoFillWrapper() {
	}

	public SiteInitializerTestrayAutoFillWrapper(
		SiteInitializerTestrayAutoFill siteInitializerTestrayAutoFill) {

		this.siteInitializerTestrayAutoFill = siteInitializerTestrayAutoFill;
	}

	@Override
	public void testrayAutoFill(
			long companyId, ObjectEntry objectEntry1, ObjectEntry objectEntry2)
		throws Exception {

		siteInitializerTestrayAutoFill.testrayAutoFill(
			companyId, objectEntry1, objectEntry2);
	}

	protected SiteInitializerTestrayAutoFill siteInitializerTestrayAutoFill;

}