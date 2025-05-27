/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import Container from '~/components/Layout/Container';
import ListView from '~/components/ListView';
import {useHeader} from '~/hooks';
import i18n from '~/i18n';
import {useNavigate, useParams} from 'react-router-dom';

//import useProjectActions from './useProjectActions';

type InitiativesProps = {
	PageContainer?: React.FC;
};

const Initiatives: React.FC<InitiativesProps> = ({PageContainer = Container}) => {
	const {issueKey} = useParams();

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
					title: i18n.translate('jira-initiatives'),
				}}
				resource={`/issues/?filter=r_parentIssue_c_issueERC eq '${issueKey}'`}
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
					navigateTo: (issue) => issue.externalReferenceCode,
				}}
			/>
		</PageContainer>
	);
};

export default Initiatives;