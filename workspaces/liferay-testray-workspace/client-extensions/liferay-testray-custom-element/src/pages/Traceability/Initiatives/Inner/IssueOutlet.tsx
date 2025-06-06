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
		
			const basePath = `/traceability/${projectKey}/${issueKey}`;

			const {setHeading, setTabs} = useHeader({
				shouldUpdate: !hasOtherParams,
				timeout: 100,
			});			
			
			useEffect(() => {
				const heading = [
					{
						category: i18n.translate('project').toUpperCase(),
						path: `/traceability/${projectKey}/initiative`,
						title: testrayJiraProject.name,
					},
				]

				if(jiraIssue?.initiativeERC){
					heading.push({
						category: 'INITIATIVE',
						path: `/traceability/${projectKey}/${jiraIssue.initiativeERC}`,
						title: jiraIssue.initiativeERC,
					})
				}

				if(jiraIssue?.epicERC){
					heading.push({
						category: 'EPIC',
						path: `/traceability/${projectKey}/${jiraIssue.epicERC}`,
						title: jiraIssue.epicERC,
					})
				}
		
				if(jiraIssue?.storyERC){
					heading.push({
						category: 'STORY',
						path: `/traceability/${projectKey}/${jiraIssue.storyERC}`,
						title: jiraIssue.storyERC,
					})
				}

				if(jiraIssue?.externalReferenceCode){
					heading.push({
						category: jiraIssue.issueType.name,
						path: `/traceability/${projectKey}/${jiraIssue.externalReferenceCode}`,
						title: jiraIssue.externalReferenceCode,
					})
				}

				setHeading(heading);
			}, [setHeading, jiraIssue, projectKey, testrayJiraProject]);
		
			useEffect(() => {
				if (!hasOtherParams) {
					setTabs([
						{
							active: pathname === basePath,
							path: basePath,
							title: i18n.translate('current'),
						},
						{	
							active: pathname === `${basePath}/results`,
							path: `${basePath}/results`,
							title: i18n.translate('results'),
						}
					]);
				}
			}, [basePath, pathname, hasOtherParams, setTabs]);
		
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
								testrayJiraIssue: jiraIssue
							}}
						/>
					</>
				</PageRenderer>
			);
};

export default IssueOutlet;
