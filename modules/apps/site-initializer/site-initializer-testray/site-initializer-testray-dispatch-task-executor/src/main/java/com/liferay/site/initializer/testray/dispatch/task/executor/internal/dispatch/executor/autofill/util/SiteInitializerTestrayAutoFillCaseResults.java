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
import com.liferay.site.initializer.testray.dispatch.task.executor.internal.dispatch.executor.BaseSiteInitializerTestrayDispatchTaskExecutor;

import java.util.List;
import java.util.Map;

/**
 * @author Nilton Vieira
 */
public class SiteInitializerTestrayAutoFillCaseResults
	implements SiteInitializerTestrayAutoFill {

	@Override
	public void testrayAutoFill(
			long companyId, ObjectEntry objectEntry1, ObjectEntry objectEntry2)
		throws Exception {

		ObjectEntry destinationTestrayCaseResultObjectEntry = null;
		ObjectEntry sourceTestrayCaseResultObjectEntry = null;
		List<ObjectEntry> sourceTestrayCaseResultsIssuesObjectEntries = null;

		List<ObjectEntry> testrayCaseResultsIssuesObjectEntries1 =
			BaseSiteInitializerTestrayDispatchTaskExecutor.getObjectEntries(
				null, companyId,
				"caseResultId eq '" + objectEntry1.getId() + "'",
				"CaseResultsIssues", null);

		List<ObjectEntry> testrayCaseResultsIssuesObjectEntries2 =
			getObjectEntries(
				null, companyId,
				"caseResultId eq '" + objectEntry2.getId() + "'",
				"CaseResultsIssues", null);

		if (((Long)getProperty("r_userToCaseResults_userId", objectEntry1) >
				0) &&
			!testrayCaseResultsIssuesObjectEntries1.isEmpty() &&
			((Long)getProperty("r_userToCaseResults_userId", objectEntry2) <=
				0) &&
			testrayCaseResultsIssuesObjectEntries2.isEmpty()) {

			destinationTestrayCaseResultObjectEntry = objectEntry2;
			sourceTestrayCaseResultObjectEntry = objectEntry1;
			sourceTestrayCaseResultsIssuesObjectEntries =
				testrayCaseResultsIssuesObjectEntries1;
		}
		else if (((Long)getProperty(
					"r_userToCaseResults_userId", objectEntry1) <= 0) &&
				 testrayCaseResultsIssuesObjectEntries1.isEmpty() &&
				 ((Long)getProperty(
					 "r_userToCaseResults_userId", objectEntry2) > 0) &&
				 !testrayCaseResultsIssuesObjectEntries2.isEmpty()) {

			destinationTestrayCaseResultObjectEntry = objectEntry1;
			sourceTestrayCaseResultObjectEntry = objectEntry2;
			sourceTestrayCaseResultsIssuesObjectEntries =
				testrayCaseResultsIssuesObjectEntries2;
		}

		if ((destinationTestrayCaseResultObjectEntry == null) ||
			(sourceTestrayCaseResultObjectEntry == null)) {

			return;
		}

		Map<String, Object> properties =
			destinationTestrayCaseResultObjectEntry.getProperties();

		properties.put(
			"dueStatus",
			getProperty("dueStatus", sourceTestrayCaseResultObjectEntry));
		properties.put(
			"r_userToCaseResults_userId",
			getProperty(
				"r_userToCaseResults_userId",
				sourceTestrayCaseResultObjectEntry));

		updateObjectEntry(
			"CaseResult", destinationTestrayCaseResultObjectEntry,
			destinationTestrayCaseResultObjectEntry.getId());

		for (ObjectEntry sourceTestrayCaseResultsIssuesObjectEntry :
				sourceTestrayCaseResultsIssuesObjectEntries) {

			long testrayIssueId = (long)getProperty(
				"r_issueToCaseResultsIssues_c_issueId",
				sourceTestrayCaseResultsIssuesObjectEntry);

			ObjectEntry testrayIssueObjectEntry = getObjectEntry(
				"Issue", testrayIssueId);

			if (testrayIssueObjectEntry == null) {
				continue;
			}

			_addTestrayCaseResultIssue(
				companyId, destinationTestrayCaseResultObjectEntry.getId(),
				(String)getProperty("name", testrayIssueObjectEntry));
		}
	}

}