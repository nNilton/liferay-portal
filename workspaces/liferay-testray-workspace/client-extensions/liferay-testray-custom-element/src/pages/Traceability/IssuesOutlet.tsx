/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {useCallback, useEffect} from 'react';
import {Outlet, useLocation, useParams} from 'react-router-dom';
import PageRenderer from '~/components/PageRenderer';

import {useFetch} from '../../hooks/useFetch';
import useHeader from '../../hooks/useHeader';
import i18n from '../../i18n';
import {APIResponse, PickList} from '../../services/rest';

const IssuesOutlet = () => {
	const {projectKey, ...otherParams} = useParams();
	const {pathname} = useLocation();
	const shouldUpdate = !Object.keys(otherParams).length;
	const {setDropdown, setHeading, setTabs} = useHeader({
		shouldUpdate,
	});

	const {data: dataJiraProjects} = useFetch<APIResponse<PickList>>(
		'/list-type-definitions/by-external-reference-code/JIRA-PROJECTS/list-type-entries',
		{
			params: {
				fields: 'key,name',
				pageSize: 100,
			},
		}
	);

    const {
		data: jiraProject,
		error,
		loading,
		mutate,
	} = useFetch<APIResponse<PickList>>(`/list-type-definitions/by-external-reference-code/JIRA-PROJECTS/list-type-entries?filter=key eq '${projectKey}'`);

	const jiraProjects = dataJiraProjects?.items;

	const hasOtherParams = !!Object.values(otherParams).length;

	const getPath = useCallback(
		(path: string) => {
			const relativePath = `/traceability/${projectKey}/${path}`;

			return {
				active: relativePath === pathname,
				path: relativePath,
			};
		},
		[projectKey, pathname]
	);

	useEffect(() => {
		if (jiraProjects) {
			setDropdown([
				{
					items: [
						{
							divider: true,
							label: i18n.translate('jira-directory'),
							path: '/traceability',
						},
						...jiraProjects.map((jiraProject) => ({
							label: jiraProject.name,
							path: `/traceability/${jiraProject.key}/initiative`,
						})),
					],
				},
			]);
		}
	}, [setDropdown, jiraProjects]);

	useEffect(() => {
		if (jiraProject?.items[0]) {
			setTimeout(() => {
				setHeading([
					{
						category: i18n.translate('project').toUpperCase(),
						path: `/traceability/${jiraProject?.items[0].id}/initiative`,
						title: jiraProject?.items[0].name,
					},
				]);
			}, 0);
		}
	}, [setHeading, jiraProject, hasOtherParams]);

	useEffect(() => {
		if (!hasOtherParams) {
			setTimeout(() => {
				setTabs([
					{
						...getPath('overview'),
						title: i18n.translate('overview'),
					},
					{
						...getPath('initiative'),
						title: i18n.translate('initiative'),
					},
					{
						...getPath('epic'),
						title: i18n.translate('epic'),
					},
					{
						...getPath('story'),
						title: i18n.translate('story'),
					}
				]);
			}, 0);
		}
	}, [getPath, setTabs, hasOtherParams]);

	return (
		<PageRenderer error={error} loading={loading}>
			<Outlet
				context={{
					actions: jiraProject?.actions,
					mutateTestrayProject: mutate,
					testrayJiraProject: jiraProject?.items[0],
				}}
			/>
		</PageRenderer>
	);
};

export default IssuesOutlet;
