/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import TestrayError from '../../TestrayError';
import Rest from '../../core/Rest';
import SearchBuilder from '../../core/SearchBuilder';
import i18n from '../../i18n';
import yupSchema from '../../schema/yup';
import {APIResponse, TestrayCase, TestrayCaseDetail, TestrayJiraIssue} from './types';

type CaseDetail = typeof yupSchema.caseDetail.__outputType;

class TestrayCaseDetailImpl extends Rest<CaseDetail, TestrayCaseDetail> {
	constructor() {
		super({
			adapter: ({id, name, dueStatus}) => ({
				id, name, dueStatus
			}),
			fields: 'id,name,dueStatus',
			nestedFields: '',
			transformData: (testrayCaseDetail) => {
				return {
					...testrayCaseDetail,
				};
			},
			uri: 'casedetails',
		});
	}

	public async getAggregatedStatus(issueType: string, issueId: number) {
		return await this.fetcher<APIResponse<TestrayJiraIssue>>(
					`/casedetails?aggregationTerms=dueStatus&fields=id&filter=caseDetailsToIssues/r_${issueType}_c_issueId eq '${issueId}'&pageSize=10`
				);
	}
}

export const testrayCaseDetailImpl = new TestrayCaseDetailImpl();
