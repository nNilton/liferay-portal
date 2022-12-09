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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nilton Vieira
 */
public class SiteInitializerTestrayAutoFillBuilds
	extends SiteInitializerTestrayAutoFillWrapper {

	public SiteInitializerTestrayAutoFillBuilds() {
	}

	public SiteInitializerTestrayAutoFillBuilds(
		SiteInitializerTestrayAutoFill siteInitializerTestrayAutoFill) {
		super(siteInitializerTestrayAutoFill);
	}

	@Override
	public void testrayAutoFill(
			long companyId, ObjectEntry objectEntry1, ObjectEntry objectEntry2)
		throws Exception {

		Map<Long, List<ObjectEntry>> testrayCaseResultObjectEntries1 =
			_getTestrayCaseResultObjectEntries(companyId, objectEntry1);

		Map<Long, List<ObjectEntry>> testrayCaseResultObjectEntries2 =
			_getTestrayCaseResultObjectEntries(companyId, objectEntry2);

		for (Map.Entry<Long, List<ObjectEntry>> entry :
				testrayCaseResultObjectEntries1.entrySet()) {

			List<ObjectEntry> testrayCaseResultCompositesB =
				testrayCaseResultObjectEntries2.get(entry.getKey());

			if (testrayCaseResultCompositesB == null) {
				continue;
			}

			List<ObjectEntry> testrayCaseResultCompositesA = entry.getValue();

			for (ObjectEntry testrayCaseResultCompositeA :
					testrayCaseResultCompositesA) {

				String testrayCaseResultErrors1 = (String)getProperty(
					"errors", testrayCaseResultCompositeA);

				if (Validator.isNull(testrayCaseResultErrors1)) {
					continue;
				}

				for (ObjectEntry testrayCaseResultCompositeB :
						testrayCaseResultCompositesB) {

					String testrayCaseResultErrors2 = (String)getProperty(
						"errors", testrayCaseResultCompositeB);

					if (Validator.isNull(testrayCaseResultErrors2) ||
						Objects.equals(
							testrayCaseResultErrors1,
							testrayCaseResultErrors2)) {

						continue;
					}

					siteInitializerTestrayAutoFill.testrayAutoFill(
						companyId, testrayCaseResultCompositeA,
						testrayCaseResultCompositeB);
				}
			}
		}
	}

	private Map<Long, List<ObjectEntry>> _getTestrayCaseResultObjectEntries(
		long companyId, ObjectEntry testrayBuildObjectEntry) {

		Map<Long, List<ObjectEntry>> testrayCaseResultObjectEntries =
			new HashMap<>();

		List<ObjectEntry> objectEntries = getObjectEntries(
			null, companyId,
			"runId eq '" + testrayBuildObjectEntry.getId() + "'", "CaseResult",
			null);

		for (ObjectEntry objectEntry : objectEntries) {
			long testrayCaseId = (Long)getProperty(
				"r_caseToCaseResult_c_caseId", objectEntry);

			List<ObjectEntry> matchingTestrayCaseResults =
				testrayCaseResultObjectEntries.get(testrayCaseId);

			if (matchingTestrayCaseResults == null) {
				matchingTestrayCaseResults = new ArrayList<>();

				testrayCaseResultObjectEntries.put(
					testrayCaseId, matchingTestrayCaseResults);
			}

			matchingTestrayCaseResults.add(objectEntry);
		}

		return testrayCaseResultObjectEntries;
	}

}