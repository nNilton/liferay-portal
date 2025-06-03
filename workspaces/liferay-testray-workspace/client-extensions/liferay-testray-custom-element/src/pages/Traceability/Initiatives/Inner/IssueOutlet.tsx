/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {useEffect} from 'react';
import {
	Outlet,
	useLocation,
	useOutletContext,
	useParams,
} from 'react-router-dom';
import PageRenderer from '~/components/PageRenderer';

import {useFetch} from '../../../../hooks/useFetch';
import useHeader from '../../../../hooks/useHeader';
import i18n from '../../../../i18n';
import {
	APIResponse,
	PickList,
	TestrayBuild,
	TestrayJiraIssue,
	TestrayProject,
	TestrayRoutine,
	testrayBuildImpl,
} from '../../../../services/rest';
import IssueOverview from './IssueOverview';
import { testrayJiraIssueImpl } from '~/services/rest/TestrayJiraIssue';
import { mutate } from 'swr';

type OutletContext = {
	testrayJiraProject: PickList;
};

const IssueOutlet = ({}) => {
	 const {projectKey, issueKey, ...otherParams} = useParams();
	 const {pathname} = useLocation();
	const {testrayJiraProject}: OutletContext = useOutletContext();

		const {data: jiraIssue,
			error,
			loading
		} = useFetch<TestrayJiraIssue>(
			testrayJiraIssueImpl.getResourceByExternalReferenceCode(issueKey as string),
			{
				transformData: (response) =>
					testrayJiraIssueImpl.transformData(response)
			}
		);

			const hasOtherParams = !!Object.values(otherParams).length;

			const {setHeading, setTabs} = useHeader({
				shouldUpdate: !hasOtherParams,
			});
		
			const basePath = `/traceability/${projectKey}/${issueKey}`;
		
			useEffect(() => {
				if (jiraIssue?.title) {
					setHeading([
						{
							category: i18n.translate('project').toUpperCase(),
							path: `/traceability/${projectKey}/initiatives`,
							title: testrayJiraProject?.name,
						},
						{
							category: i18n.translate('issue').toUpperCase(),
							path: `/project/${projectKey}/${issueKey}`,
							title: jiraIssue?.externalReferenceCode + ' ' +jiraIssue?.title,
						},
					]);
				}
			}, [setHeading, jiraIssue, testrayJiraProject]);
		
			useEffect(() => {
				if (!hasOtherParams) {
					setTabs([
						{
							active: pathname === basePath,
							path: basePath,
							title: i18n.translate('results'),
						}
					]);
				}
			}, [basePath, pathname, setTabs]);
		
			return (
				<PageRenderer error={error} loading={loading}>
					<>
						{jiraIssue && (
							<IssueOverview testrayJiraIssue={jiraIssue} />
						)}
		
						<Outlet
							context={{
								actions: jiraIssue?.actions,
								mutate,
								testrayJiraProject,
								jiraIssue
							}}
						/>
					</>
				</PageRenderer>
			);
};

export default IssueOutlet;
