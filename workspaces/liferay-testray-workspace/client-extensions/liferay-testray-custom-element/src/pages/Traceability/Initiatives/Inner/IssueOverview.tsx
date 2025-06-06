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
	TestrayJiraIssue,
	TestraySubtask,
	testraySubtaskImpl,
} from '../../../../services/rest';
import JiraLink from '../../../../components/JiraLink';
import Container from '../../../../components/Layout/Container';
import QATable from '../../../../components/Table/QATable';
import {useTotalTestCasesByTestrayJiraIssue} from '../../../../hooks/data/useCaseResultGroupBy';
import useIssuesFound from '../../../../hooks/data/useIssuesFound';
import i18n from '../../../../i18n';
import {formatUTCDate} from '../../../../util/date';
import {getDonutLegend} from '../../../../util/graph';
import {useNavigate, useParams} from 'react-router-dom';
import ChildIssues from '.';
import { testrayJiraIssueImpl } from '~/services/rest/TestrayJiraIssue';
import { testrayCaseDetailImpl } from '~/services/rest/TestrayCaseDetail';

type IssueOverviewProps = {
	testrayJiraIssue: TestrayJiraIssue;
};

const IssueOverview: React.FC<IssueOverviewProps> = ({testrayJiraIssue}) => {

	const totalTestCasesGroup = useTotalTestCasesByTestrayJiraIssue(testrayJiraIssue);

	const ref = useRef<any>();

	return (
		<>
			<Container collapsable title={testrayJiraIssue?.title}>
				<ClayPanel
					collapsable
					defaultExpanded
					displayTitle={
						<div className="tr-small-heading">
							{i18n.translate('description')}
						</div>
					}
					displayType="default"
					showCollapseIcon
				>
					<ClayPanel.Body>
						<div className="c-py-2 c-px-3">
							<div className="tr-issue-description"
								dangerouslySetInnerHTML={{
									__html: testrayJiraIssue?.description ?? '',
								}}
							/>
						</div>
					</ClayPanel.Body>
				</ClayPanel>
			</Container>

			<Container
				className="mt-4"
				collapsable
				title={i18n.translate('total-test-cases')}
			>
				<div className="d-flex justify-content-between row">
					<div className='align-items-center d-flex col'>
						{totalTestCasesGroup.ready && (
							<div className="col-8">
								<ClayChart
									key={testrayJiraIssue.id}
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
										const elementId = 'testrayTotalMetricsGraphLegend';
										const legendContainer = document.getElementById(elementId);
									
										if (legendContainer) {
											legendContainer.innerHTML = ''; 
										}
									
										getDonutLegend(ref.current, {
											data: totalTestCasesGroup.donut.columns.map(([name]) => name),
											elementId,
											total: totalTestCasesGroup.donut.total as number,
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
		</>
	);
};

export default IssueOverview;
