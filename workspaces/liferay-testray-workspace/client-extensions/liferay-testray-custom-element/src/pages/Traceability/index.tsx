/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import Container from '~/components/Layout/Container';
import ListView from '~/components/ListView';
import {useHeader} from '~/hooks';
import i18n from '~/i18n';

//import useProjectActions from './useProjectActions';

type TraceabilityProps = {
	PageContainer?: React.FC;
};

const Traceability: React.FC<TraceabilityProps> = ({PageContainer = Container}) => {
	//const {actions} = useProjectActions();

	useHeader({
		dropdown: [],
		headerActions: {actions: []},
		heading: [
			{
				category: i18n.translate('project'),
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
						key: 'name',
					},
				}}
				managementToolbarProps={{
					applyFilters: true,
					display: {columns: false},
					title: i18n.translate('jira-projects'),
				}}
				resource="/list-type-definitions/by-external-reference-code/JIRA-PROJECTS/list-type-entries"
				tableProps={{
					columns: [
						{
							clickable: true,
							key: 'name',
							size: 'lg',
							sorteable: true,
							value: i18n.translate('project'),
						},
						{
							key: 'key',
							size: 'lg',
							value: i18n.translate('key'),
						},
					],
					navigateTo: (listType) => `/traceability/${listType.key}/initiatives`,
				}}
			/>
		</PageContainer>
	);
};

export default Traceability;