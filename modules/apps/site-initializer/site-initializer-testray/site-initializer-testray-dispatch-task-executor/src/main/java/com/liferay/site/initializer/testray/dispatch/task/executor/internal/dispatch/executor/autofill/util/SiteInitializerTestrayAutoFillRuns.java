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
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nilton Vieira
 */
public class SiteInitializerTestrayAutoFillRuns
	extends SiteInitializerTestrayAutoFillWrapper {

	@Override
	public void testrayAutoFill(
			long companyId, ObjectEntry objectEntry1, ObjectEntry objectEntry2)
		throws Exception {

		Map<Long, ObjectEntry> testrayCaseResultObjectEntries1 =
			_getTestrayCaseResultObjectEntries(companyId, objectEntry1);

		Map<Long, ObjectEntry> testrayCaseResultObjectEntries2 =
			_getTestrayCaseResultObjectEntries(companyId, objectEntry2);

		for (Map.Entry<Long, ObjectEntry> entry :
				testrayCaseResultObjectEntries1.entrySet()) {

			ObjectEntry testrayCaseResultObjectEntry2 =
				testrayCaseResultObjectEntries2.get(entry.getKey());

			if (testrayCaseResultObjectEntry2 == null) {
				continue;
			}

			ObjectEntry testrayCaseResultObjectEntry1 = entry.getValue();

			String testrayCaseResultErrors1 = (String)getProperty(
				"errors", testrayCaseResultObjectEntry1);

			String testrayCaseResultErrors2 = (String)getProperty(
				"errors", testrayCaseResultObjectEntry2);

			if (Validator.isNull(testrayCaseResultErrors1) ||
				Validator.isNull(testrayCaseResultErrors2) ||
				!Objects.equals(
					testrayCaseResultErrors1, testrayCaseResultErrors2)) {

				continue;
			}

			siteInitializerTestrayAutoFill.testrayAutoFill(
				companyId, testrayCaseResultObjectEntry1,
				testrayCaseResultObjectEntry2);
		}
	}

	private Map<Long, ObjectEntry> _getTestrayCaseResultObjectEntries(
			long companyId, ObjectEntry testrayRunObjectEntry)
		throws Exception {

		Map<Long, ObjectEntry> testrayCaseResultObjectEntries = new HashMap<>();

		for (ObjectEntry objectEntry :
				getObjectEntries(
					null, companyId,
					"runId eq '" + testrayRunObjectEntry.getId() + "'",
					"CaseResult", null)) {

			testrayCaseResultObjectEntries.put(
				(Long)getProperty("r_caseToCaseResult_c_caseId", objectEntry),
				objectEntry);
		}

		return testrayCaseResultObjectEntries;
	}

}