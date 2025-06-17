/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import Container from '~/components/Layout/Container';
import PageContainer from '~/components/Layout/Container'
import ListView from '~/components/ListView';
import {useHeader} from '~/hooks';
import i18n from '~/i18n';
import ClayIcon from '@clayui/icon';
import {useNavigate, useOutletContext, useParams} from 'react-router-dom';
import { TestrayJiraIssue } from '~/services/rest';
import StatusBadge from '~/components/StatusBadge';
import { StatusBadgeType } from '~/components/StatusBadge/StatusBadge';
import JiraLink from '~/components/JiraLink';
import { ClayTooltipProvider } from '@clayui/tooltip';

type OutletContext = {
	testrayJiraIssue: TestrayJiraIssue;
};

const IssueResults = () => {
	const {testrayJiraIssue} : OutletContext = useOutletContext();

    const filter = `caseDetailsToIssues/r_${testrayJiraIssue.issueType.key.toLowerCase()}_c_issueId eq '${testrayJiraIssue.id}'`;

	return (
		<Container>
			<ListView
				initialContext={{
					pageSize: 50,
                    sort:{
                        direction: 'ASC',
                        key: 'dueStatus'
                    },
                }}
				managementToolbarProps={{
					applyFilters: true,
					display: {columns: false},
                    filterSchema: 'issueResults',
					title: i18n.translate('jira-issue-results'),
				}}
				resource={`/casedetails/?nestedFields=caseToCaseDetails,caseDetailsToIssues`}
				tableProps={{
					columns: [
                        {
							clickable: true,
							key: 'flaky',
							render: (_, {caseToCaseDetails}) => (
								<>
									{caseToCaseDetails.flaky && (
										<ClayTooltipProvider>
											<span
												className="tr-table__row__flaky-icon"
												data-tooltip-align="top"
												title={i18n.translate(
													'this-is-a-possible-flaky-test'
												)}
											>
												<ClayIcon symbol="flag-full" />
											</span>
										</ClayTooltipProvider>
									)}
									{caseToCaseDetails.name}
								</>
							),
							size: 'md',
							value: i18n.translate('case'),
							width: '350',
						},
						{
							clickable: true,
							size: 'md',
							key: 'name',
							value: i18n.translate('test'),
						},
						{
							clickable: true,
							key: 'priority',
							render: (_, {caseToCaseDetails}) =>
								caseToCaseDetails.priority,
							size: 'sm',
							value: i18n.translate('priority'),
						},
						{
							clickable: true,
							key: 'dueStatus',
							render: (_, {dueStatus}) => (
								<StatusBadge
									type={dueStatus.key as StatusBadgeType}
								>
									{dueStatus.key}
								</StatusBadge>
							),
							value: i18n.translate('status'),
						},
						{
							key: 'issues',
							render: (_, {caseDetailsToIssues}) => (
                                <>
									{caseDetailsToIssues.map(
										(issue: TestrayJiraIssue, _: number) => (
                                            <JiraLink
                                                displayViewInJira={false}
                                                issue={issue.externalReferenceCode}
                                            />
										)
									)}
								</>
							),
							value: i18n.translate('issues'),
						},
					],
					navigateTo: (caseDetail) => `/project/${caseDetail.caseToCaseDetails?.r_projectToCases_c_projectId}/cases/${caseDetail.caseToCaseDetails?.id}`,
				}}
				variables={{
					filter: filter,
				}}
			/>
		</Container>
	);
};

export default IssueResults;