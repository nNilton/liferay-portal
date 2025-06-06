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

	const filter = `projectType eq '${projectKey}' and issueType eq '${pathname.split('/').filter(Boolean).pop()}'`

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
					filterSchema: 'issues',
					display: {columns: false},
					title: i18n.translate('jira-'+ pathname.split('/').filter(Boolean).pop()),
				}}
				resource={`/issues/?filter='`}
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
				variables={{
					filter: filter,
				}}
			/>
		</PageContainer>
	);
};

export default IssuesList;