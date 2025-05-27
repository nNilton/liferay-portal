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

type IssueOverviewProps = {
	jiraIssue: JiraIssue;
};

const IssueOverview: React.FC<IssueOverviewProps> = ({jiraIssue}) => {

    const {data: response} = useFetch<APIResponse<JiraIssue>>(
            `aggregationTerms=dueStatus&filter=caseDetailsToIssues/r_story_c_issueId eq '${jiraIssue.id}'&fields=id`
        );

        console.log(response);

	const totalTestCasesGroup = useTotalTestCases(testrayBuild);
	const {chart, entity, loading} = useCaseResultsChart({
		buildId: testrayBuild.id,
	});

	const ref = useRef<any>();

	const [columnChartLoad, setColumnChartLoad] = useState(false);

	useEffect(() => {
		setColumnChartLoad(false);
		setTimeout(() => {
			setColumnChartLoad(true);
		}, 100);
	}, [entity]);

	return (
		<>
			<Container collapsable title={jiraIssue.externalReferenceCode + ' ' + jiraIssue.title}>
				<QATable
					items={[
						{
							title: i18n.translate('description'),

							value: (
								<div
									dangerouslySetInnerHTML={{
										__html: jiraIssue?.description,
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
						className={classNames('align-items-center d-flex', {
							'col': !entity,
							'col-4': entity,
						})}
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

					{entity && (
						<div className="col-8">
							{loading ||
								(!columnChartLoad && (
									<Loading className="py-10" />
								))}

							{columnChartLoad && !loading && (
								<ClayChart
									axis={{
										x: {
											categories:
												!!chart.testrayRunNumber
													.length &&
												chart.testrayRunNumber,
											label: {
												position: 'outer-center',
												text: i18n
													.translate(`${entity}`)
													.toUpperCase(),
											},
											tick: {
												show: chart.testrayRunNumber
													.length
													? true
													: false,
												text: {
													show: chart.testrayRunNumber
														.length
														? true
														: false,
												},
											},
											type: 'category',
										},
										y: {
											label: {
												position: 'outer-middle',
												text: i18n
													.translate('tests')
													.toUpperCase(),
											},
										},
									}}
									bar={{
										width: {
											max: 30,
										},
									}}
									data={{
										colors: chart.colors,
										columns: chart.columns,
										groups: [chart.statuses],
										type: 'bar',
									}}
									legend={{
										inset: {
											anchor: 'top-right',
											step: 1,
											x: 10,
											y: -20,
										},
										position: 'inset',
									}}
									padding={{
										bottom: 5,
										top: 20,
									}}
									tooltip={{
										format: {
											title: (index: number) =>
												chart.columnNames[index],
										},
										order: '',
									}}
								/>
							)}
						</div>
					)}
				</div>
			</Container>
		</>
	);
};

export default BuildOverview;
