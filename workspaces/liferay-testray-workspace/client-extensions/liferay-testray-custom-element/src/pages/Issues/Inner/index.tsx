/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {useOutletContext} from 'react-router-dom';
import Container from '~/components/Layout/Container';
import ListView from '~/components/ListView';
import ProgressBar from '~/components/ProgressBar';
import i18n from '~/i18n';
import {TestrayJiraIssue} from '~/services/rest';

type OutletContext = {
	testrayJiraIssue: TestrayJiraIssue;
};

const ChildIssues = () => {
	const {testrayJiraIssue}: OutletContext = useOutletContext();

	const clickable =
		testrayJiraIssue.issueType.key !== 'STORY' &&
		testrayJiraIssue.issueType.key !== 'TASK';

	return (
		<Container className="mt-4">
			<ListView
				initialContext={{
					columns: {
						inprogress: false,
						passed: false,
						testfix: false,
						total: false,
						untested: false,
					},
					columnsFixed: ['testrayIssueTitle'],
				}}
				managementToolbarProps={{
					applyFilters: true,
					display: {columns: true},
					title: i18n.translate('jira-child-issues'),
				}}
				resource={`/testray-status-metrics/by-testray-issueId/${testrayJiraIssue.id}/testray-issues-metrics`}
				tableProps={{
					columns: [
						{
							clickable,
							key: 'testrayIssueKey',
							size: 'sm',
							value: i18n.translate('issue-key'),
						},
						{
							clickable,
							key: 'testrayIssueType',
							size: 'sm',
							value: i18n.translate('issue-type'),
						},
						{
							clickable,
							key: 'testrayIssueTitle',
							size: 'lg',
							value: i18n.translate('title'),
						},
						{
							clickable: true,
							key: 'untested',
							render: (_, {testrayStatusMetric}) =>
								testrayStatusMetric.untested,
							value: i18n.translate('untested'),
						},
						{
							clickable: true,
							key: 'in-progress',
							render: (_, {testrayStatusMetric}) =>
								testrayStatusMetric.inProgress,
							value: i18n.translate('in-progress'),
						},
						{
							clickable: true,
							key: 'passed',
							render: (_, {testrayStatusMetric}) =>
								testrayStatusMetric.passed,
							value: i18n.translate('passed'),
						},
						{
							clickable: true,
							key: 'failed',
							render: (_, {testrayStatusMetric}) =>
								testrayStatusMetric.failed,
							value: i18n.translate('failed'),
						},
						{
							clickable: true,
							key: 'blocked',
							render: (_, {testrayStatusMetric}) =>
								testrayStatusMetric.blocked,
							value: i18n.translate('blocked'),
						},
						{
							clickable: true,
							key: 'test-fix',
							render: (_, {testrayStatusMetric}) =>
								testrayStatusMetric.testfix,
							value: i18n.translate('test-fix'),
						},
						{
							clickable: true,
							key: 'total',
							render: (_, {testrayStatusMetric}) =>
								testrayStatusMetric.total,
							value: i18n.translate('total'),
						},
						{
							clickable: true,
							key: 'testrayStatusMetric',
							render: (testrayStatusMetric) => (
								<ProgressBar
									chartOrder={[
										'passed',
										'failed',
										'blocked',
										'test_fix',
										'incomplete',
									]}
									items={{
										blocked: testrayStatusMetric?.blocked,
										failed: testrayStatusMetric?.failed,
										incomplete:
											testrayStatusMetric?.incomplete +
											testrayStatusMetric?.untested,
										passed: testrayStatusMetric?.passed,
										test_fix: testrayStatusMetric?.testfix,
									}}
								/>
							),
							value: i18n.translate('metrics'),
							width: '300',
						},
					],
					navigateTo: (issue) => `../${issue.testrayIssueKey}`,
					rowWrap: true,
				}}
			/>
		</Container>
	);
};

export default ChildIssues;
