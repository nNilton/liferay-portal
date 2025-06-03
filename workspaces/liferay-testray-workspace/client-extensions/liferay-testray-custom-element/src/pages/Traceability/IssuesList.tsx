/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import Container from '~/components/Layout/Container';
import ListView from '~/components/ListView';
import {useHeader} from '~/hooks';
import i18n from '~/i18n';
import {useLocation, useNavigate, useParams} from 'react-router-dom';
import { console } from 'inspector';

//import useProjectActions from './useProjectActions';

type IssuesListProps = {
	PageContainer?: React.FC;
};

const IssuesList: React.FC<IssuesListProps> = ({PageContainer = Container}) => {
	const {projectKey} = useParams();
	const {pathname} = useLocation();

	useHeader({
		dropdown: [],
		headerActions: {actions: []},
		heading: [
			{
				category: i18n.translate('initiatives'),
				title: i18n.translate('jira-project-directory'),
			},
		],
		icon: 'polls',
	});

	return (
		<PageContainer>
			<ListView
				initialContext={{
					sort: {
						direction: 'ASC',
						key: 'title',
					},
				}}
				managementToolbarProps={{
					applyFilters: true,
					display: {columns: false},
					title: i18n.translate('jira-'+ pathname.split('/').filter(Boolean).pop()),
				}}
				resource={`/issues/?filter=projectType eq '${projectKey}' and issueType eq '${pathname.split('/').filter(Boolean).pop()}'`}
				tableProps={{
					columns: [
						{
							key: 'externalReferenceCode',
							size: 'sm',
							value: i18n.translate('issueKey'),
						},
						{
							clickable: true,
							key: 'title',
							size: 'lg',
							sorteable: true,
							value: i18n.translate('title'),
						}
					],
					navigateTo: (issue) => `../${issue.externalReferenceCode}`,
				}}
			/>
		</PageContainer>
	);
};

export default IssuesList;