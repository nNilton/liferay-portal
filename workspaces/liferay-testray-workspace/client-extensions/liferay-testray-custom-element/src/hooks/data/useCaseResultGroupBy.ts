/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {useCallback, useMemo} from 'react';

import SearchBuilder from '../../core/SearchBuilder';
import {APIResponse, FacetAggregation, TestrayBuild, Facets} from '../../services/rest';
import {chartColors} from '../../util/constants';
import {CaseResultStatuses} from '../../util/statuses';
import {useFetch} from '../useFetch';

function getStatusesMap(
	facetAggregation: FacetAggregation | undefined
): Map<string, number> {
	const facetValueMap: Map<string, number> = new Map();

	if (!facetAggregation?.facets) {
		return facetValueMap;
	}

	for (const facet of facetAggregation.facets) {
		for (const facetValue of facet.facetValues) {
			facetValueMap.set(facetValue.term, facetValue.numberOfOccurrences);
		}
	}

	return facetValueMap;
}

const getAggregationValue = (value: number | string) =>
	value ? Number(value) : 0;

const getDonutColumns = (
	data: TestrayBuild | Facets[]
  ) => useMemo(() => {
	if (!Array.isArray(data)) {

		return [
			[CaseResultStatuses.PASSED, getAggregationValue(data.caseResultPassed)],
			[CaseResultStatuses.FAILED, getAggregationValue(data.caseResultFailed)],
			[CaseResultStatuses.BLOCKED, getAggregationValue(data.caseResultBlocked)],
			[CaseResultStatuses.TEST_FIX, getAggregationValue(data.caseResultTestFix)],
			[
			  CaseResultStatuses.INCOMPLETE,
			  getAggregationValue(data.caseResultIncomplete) +
				getAggregationValue(data.caseResultUntested),
			],
		  ];
	}

	const facets = data as Facets[]
	  
	  const statusCounts: Record<string, number> = {
		PASSED: 0,
		FAILED: 0,
		BLOCKED: 0,
		TEST_FIX: 0,
		INCOMPLETE: 0,
	  };
  
	  facets[0].facetValues.forEach(({ term, numberOfOccurrences }) => {
		if (term in statusCounts) {
		  statusCounts[term] = numberOfOccurrences;
		}
	  });
  
	  return [
		[CaseResultStatuses.PASSED, statusCounts.PASSED],
		[CaseResultStatuses.FAILED, statusCounts.FAILED],
		[CaseResultStatuses.BLOCKED, statusCounts.BLOCKED],
		[CaseResultStatuses.TEST_FIX, statusCounts.TEST_FIX],
		[CaseResultStatuses.INCOMPLETE, statusCounts.INCOMPLETE],
	  ];
  }, [data]);

const useTotalTestCases = (data: TestrayBuild | Facets[]) => {
	const donutColumns = getDonutColumns(data);

	return useMemo(
		() => ({
			colors: chartColors,
			donut: {
				columns: donutColumns,
				total: donutColumns
					.map(([, totalCase]) => Number(totalCase))
					.reduce(
						(previousValue, currentValue) =>
							previousValue + currentValue
					),
			},
			ready: !!data,
			statuses: Object.values(CaseResultStatuses),
		}),
		[donutColumns, data]
	);
};

const useCaseResultGroupBy = (buildId: number = 0) => {
	const {data, loading} = useFetch<
		APIResponse<TestrayBuild> & FacetAggregation
	>('/caseresults', {
		params: {
			aggregationTerms: 'dueStatus',
			fields: 'id',
			filter: SearchBuilder.eq('buildId', buildId),
		},
	});

	const statuses = useMemo(() => getStatusesMap(data), [data]);

	const getStatusValue = useCallback(
		(status: string | number) => statuses.get(String(status)) || 0,
		[statuses]
	);

	const donutColumns = [
		[CaseResultStatuses.PASSED, getStatusValue(CaseResultStatuses.PASSED)],
		[CaseResultStatuses.FAILED, getStatusValue(CaseResultStatuses.FAILED)],
		[
			CaseResultStatuses.BLOCKED,
			getStatusValue(CaseResultStatuses.BLOCKED),
		],
		[
			CaseResultStatuses.TEST_FIX,
			getStatusValue(CaseResultStatuses.TEST_FIX),
		],
		[
			CaseResultStatuses.INCOMPLETE,
			getStatusValue(CaseResultStatuses.INCOMPLETE) +
				getStatusValue(CaseResultStatuses.UNTESTED),
		],
	];

	return {
		colors: chartColors,
		donut: {
			columns: donutColumns,
			total: donutColumns
				.map(([, totalCase]) => totalCase)
				.reduce(
					(previousValue, currentValue) =>
						Number(previousValue) + Number(currentValue)
				),
		},
		ready: !loading && statuses.size > 0,
		statuses: Object.values(CaseResultStatuses),
	};
};

export {useTotalTestCases};

export default useCaseResultGroupBy;
