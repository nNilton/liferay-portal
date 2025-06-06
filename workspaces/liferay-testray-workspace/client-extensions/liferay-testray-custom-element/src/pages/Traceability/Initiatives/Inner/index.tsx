/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import Container from '~/components/Layout/Container';
import ListView from '~/components/ListView';
import {useHeader} from '~/hooks';
import i18n from '~/i18n';
import {useNavigate, useOutletContext, useParams} from 'react-router-dom';
import { TestrayJiraIssue } from '~/services/rest';

type OutletContext = {
	testrayJiraIssue: TestrayJiraIssue;
};

const ChildIssues = () => {
	const {issueKey} = useParams();

	const {testrayJiraIssue} : OutletContext = useOutletContext();

	const clickable = testrayJiraIssue.issueType.name !== 'Story' && testrayJiraIssue.issueType.name !== 'Task';

	return (
		<Container className="mt-4">
			<ListView
				initialContext={{
					sort: {
						direction: 'ASC',
						key: 'title',
					},
				}}
				managementToolbarProps={{
					applyFilters: true,
					display: {columns: false},
					title: i18n.translate('jira-child-issue'),
				}}
				resource={`/issues/?filter=r_parentIssue_c_issueERC eq '${issueKey}'`}
				tableProps={{
					columns: [
						{
							clickable: clickable,
							key: 'externalReferenceCode',
							size: 'sm',
							value: i18n.translate('issueKey'),
						},
						{
							clickable: clickable,
							size: 'sm',
							key: 'issueType',
							render: (_, {issueType}) =>
								issueType.name,
							value: i18n.translate('issue-type'),
						},
						{
							clickable: clickable,
							key: 'title',
							size: 'lg',
							value: i18n.translate('title'),
						},
					],
					navigateTo: (issue) => `../${issue.externalReferenceCode}`,
				}}
			/>
		</Container>
	);
};

export default ChildIssues;