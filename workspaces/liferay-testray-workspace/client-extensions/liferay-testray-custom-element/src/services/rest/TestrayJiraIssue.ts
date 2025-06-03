/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import TestrayError from '../../TestrayError';
import Rest from '../../core/Rest';
import SearchBuilder from '../../core/SearchBuilder';
import i18n from '../../i18n';
import yupSchema from '../../schema/yup';
import {APIResponse, TestrayJiraIssue} from './types';

type JiraIssue = typeof yupSchema.jiraIssue.__outputType;

class TestrayJiraIssueImpl extends Rest<JiraIssue, TestrayJiraIssue> {
	constructor() {
		super({
			adapter: ({title, externalReferenceCode, description}) => ({
				title, externalReferenceCode, description
			}),
			fields: 'id,title,externalReferenceCode,description',
			nestedFields: '',
			transformData: (testrayJiraIssue) => {
				return {
					...testrayJiraIssue,
				};
			},
			uri: 'issues',
		});
	}
}

export const testrayJiraIssueImpl = new TestrayJiraIssueImpl();
