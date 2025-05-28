/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayChart from '@clayui/charts';
import ClayIcon from '@clayui/icon';
import ClayPanel from '@clayui/panel';
import classNames from 'classnames';
import {useEffect, useMemo, useRef, useState} from 'react';
import Loading from '~/components/Loading';
import {useCaseResultsChart} from '~/hooks/useCaseResultsChart';
import {safeJSONParse} from '~/util';
import {useFetch} from '~/hooks/useFetch';
import {
	APIResponse,
	TestrayCaseResult,
	TestraySubtask,
	testraySubtaskImpl,
} from '../../../../services/rest';
import JiraLink from '../../../../components/JiraLink';
import Container from '../../../../components/Layout/Container';
import QATable from '../../../../components/Table/QATable';
import {useTotalTestCases} from '../../../../hooks/data/useCaseResultGroupBy';
import useIssuesFound from '../../../../hooks/data/useIssuesFound';
import i18n from '../../../../i18n';
import {JiraIssue, TestrayTask} from '../../../../services/rest';
import {formatUTCDate} from '../../../../util/date';
import {getDonutLegend} from '../../../../util/graph';
import {useNavigate, useParams} from 'react-router-dom';
import ChildIssues from '.';

type IssueOverviewProps = {
    PageContainer?: React.FC;
};

const IssueOverview: React.FC<IssueOverviewProps> = ({PageContainer = Container}) => {
    const {issueKey} = useParams();

    const {data: jiraIssue} = useFetch<JiraIssue>(
        `/issues/by-external-reference-code/${issueKey}`
    );

    console.log(jiraIssue)

    const {data: response} = useFetch<APIResponse<JiraIssue>>(
            `/casedetails/?aggregationTerms=&filter='&fields=id`, {
                params: {
                    aggregationTerms: 'dueStatus',
                    fields: 'issues',
                    filter: `caseDetailsToIssues/r_story_c_issueId eq '${jiraIssue?.id}`,
                    pageSize: 10,
                },
            }
        );

    console.log(response);

	const totalTestCasesGroup = useTotalTestCases(response?.facets ?? []);

	const ref = useRef<any>();

	return (
		<>
			<Container collapsable title={jiraIssue?.externalReferenceCode + ' ' + jiraIssue?.title}>
				<QATable
					items={[
						{
							title: i18n.translate('description'),

							value: (
								<div
									dangerouslySetInnerHTML={{
										__html: jiraIssue?.description ?? '',
									}}
								/>
							),
						}
					]}
				/>
			</Container>

			<Container
				className="mt-4"
				collapsable
				title={i18n.translate('total-test-cases')}
			>
				<div className="d-flex justify-content-between row">
					<div
						className={'align-items-center d-flex col'}
					>
						{totalTestCasesGroup.ready && (
							<div className="col-8">
								<ClayChart
									data={{
										colors: totalTestCasesGroup.colors,
										columns:
											totalTestCasesGroup.donut.columns,
										type: 'donut',
									}}
									donut={{
										expand: false,
										label: {
											show: false,
										},
										legend: {
											show: false,
										},
										title: totalTestCasesGroup.donut.total.toString(),
										width: 15,
									}}
									legend={{show: false}}
									onafterinit={() => {
										getDonutLegend(ref.current, {
											data: totalTestCasesGroup.donut.columns.map(
												([name]) => name
											),
											elementId:
												'testrayTotalMetricsGraphLegend',
											total: totalTestCasesGroup.donut
												.total as number,
										});
									}}
									ref={ref}
									size={{
										height: 200,
									}}
								/>
							</div>
						)}

						<div className="col-">
							<div id="testrayTotalMetricsGraphLegend" />
						</div>
					</div>
				</div>
			</Container>

            <Container className="mt-5" title={i18n.translate('child-issues')}>
				<ChildIssues />
			</Container>
		</>
	);
};

export default IssueOverview;
