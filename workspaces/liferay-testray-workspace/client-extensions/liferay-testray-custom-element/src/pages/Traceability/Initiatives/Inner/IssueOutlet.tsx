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
    JiraIssue,
	TestrayBuild,
	TestrayProject,
	TestrayRoutine,
	testrayBuildImpl,
} from '../../../../services/rest';
import IssueOverview from './IssueOverview';

type BuildOutletProps = {
	ignorePaths: string[];
};

type OutletContext = {
	testrayProject: TestrayProject;
	testrayRoutine: TestrayRoutine;
};

const BuildOutlet: React.FC<BuildOutletProps> = ({ignorePaths}) => {
	//const {actions} = useBuildActions({isHeaderActions: true});
	const {projectKey, issueKey, ...otherParams} = useParams();
	const {pathname} = useLocation();
	const {testrayProject, testrayRoutine}: OutletContext = useOutletContext();

    const {data: jiraIssue} = useFetch<JiraIssue>(
            `/issues/by-external-reference-code/${issueKey}`
        );

    console.log(jiraIssue)

	const hasOtherParams = !!Object.values(otherParams).length;

	const {setHeaderActions, setHeading, setTabs} = useHeader({
		shouldUpdate: !hasOtherParams,
	});

	const isCurrentPathIgnored = ignorePaths.some((ignorePath) =>
		pathname.includes(ignorePath)
	);

	const basePath = `/traceability/${projectKey}/../${issueKey}/`;

    useEffect(() => {
		setTabs([
			{
				active: pathname === basePath,
				path: basePath,
				title: i18n.translate('current'),
			},
			{
				active: pathname === `${basePath}/archived`,
				path: `${basePath}/archived`,
				title: i18n.translate('archived'),
			},
			{
				active: pathname === `${basePath}/duration`,
				path: `${basePath}/duration`,
				title: i18n.translate('duration'),
			},
		]);
	}, [basePath, pathname, setTabs]);

	useEffect(() => {
		if (testrayProject && testrayRoutine) {
			setHeading([
				{
					category: i18n.translate('project').toUpperCase(),
					path: `/traceability/${projectKey}/`,
					title: testrayProject.name,
				},
				{
					category: i18n.translate('routine').toUpperCase(),
					path: `/traceability/${projectKey}/routines/${testrayRoutine.id}`,
					title: testrayRoutine.name,
				},
			]);
		}
	}, [setHeading, testrayProject, testrayRoutine]);

	useEffect(() => {
		if (jiraIssue?.title) {
			setHeading([
				{
					category: i18n.translate('project').toUpperCase(),
					path: `/traceability/${testrayProject.id}/`,
					title: testrayProject?.name,
				},
				{
					category: i18n.translate('routine').toUpperCase(),
					path: `/traceability/${testrayProject.id}/routines/${testrayRoutine.id}`,
					title: testrayRoutine?.name,
				},
				{
					category: i18n.translate('build').toUpperCase(),
					path: `/traceability/${testrayProject.id}/routines/${testrayRoutine.id}/build/${jiraIssue.externalReferenceCode}`,
					title: jiraIssue?.externalReferenceCode + ' ' + jiraIssue?.title,
				},
			]);
		}
	}, [pathname, setHeading, testrayBuild, testrayProject, testrayRoutine]);

	useEffect(() => {
		if (!isCurrentPathIgnored) {
			setTabs([
				{
					active: pathname === basePath,
					path: basePath,
					title: i18n.translate('results'),
				},
				{
					active: pathname === `${basePath}/runs`,
					path: `${basePath}/runs`,
					title: i18n.translate('runs'),
				},
				{
					active: pathname === `${basePath}/teams`,
					path: `${basePath}/teams`,
					title: i18n.translate('teams'),
				},
				{
					active: pathname === `${basePath}/components`,
					path: `${basePath}/components`,
					title: i18n.translate('components'),
				},
				{
					active: pathname === `${basePath}/case-types`,
					path: `${basePath}/case-types`,
					title: i18n.translate('case-types'),
				},
			]);
		}
	}, [basePath, isCurrentPathIgnored, pathname, setTabs]);

	return (
		<PageRenderer error={error} loading={loading}>
			<>
				{!isCurrentPathIgnored && testrayBuild && (
					<IssueOverview testrayBuild={testrayBuild} />
				)}

				<Outlet
					context={{
						actions: testrayBuild?.actions,
						mutateBuild,
						testrayBuild,
						testrayProject,
						testrayRoutine,
					}}
				/>
			</>
		</PageRenderer>
	);
};

export default BuildOutlet;
