/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.dto.v1_0.converter;

import com.liferay.osb.faro.rest.dto.v1_0.Account;
import com.liferay.osb.faro.rest.dto.v1_0.CalculatedField;
import com.liferay.osb.faro.rest.dto.v1_0.LifecycleStage;
import com.liferay.portal.kernel.test.util.RandomTestUtil;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leslie Wong
 */
public class AccountDTOConverterTest {

	@Test
	public void testToDTO() {
		com.liferay.osb.faro.engine.client.model.Account engineClientAccount =
			new com.liferay.osb.faro.engine.client.model.Account();

		engineClientAccount.setAccountName(RandomTestUtil.randomString());
		engineClientAccount.setAccountType(RandomTestUtil.randomString());
		engineClientAccount.setActivitiesCount(RandomTestUtil.randomLong());
		engineClientAccount.setActivityStatus("ACTIVE");
		engineClientAccount.setAnnualRevenue(RandomTestUtil.randomDouble());
		engineClientAccount.setCalculatedFields(
			Arrays.asList(
				_createCalculatedField("stage@1", "lifecycle", "1"),
				_createCalculatedField(
					"closedWonAmount90d", "salesforce", "1000")));
		engineClientAccount.setFirstActivityDate(RandomTestUtil.nextDate());
		engineClientAccount.setId(RandomTestUtil.randomString());
		engineClientAccount.setLastActivityDate(RandomTestUtil.nextDate());

		com.liferay.osb.faro.engine.client.model.Account.LifecycleStage
			engineClientLifecycleStage =
				new com.liferay.osb.faro.engine.client.model.Account.
					LifecycleStage();

		engineClientLifecycleStage.setLifecycleName("Sales");
		engineClientLifecycleStage.setStageType("PIPELINE");

		engineClientAccount.setLifecycleStages(
			Collections.singletonList(engineClientLifecycleStage));

		engineClientAccount.setNumberOfEmployees(RandomTestUtil.randomInt());
		engineClientAccount.setWebsite(RandomTestUtil.randomString());

		Account account = _accountDTOConverter.toDTO(
			new FaroDTOConverterContext(
				false, engineClientAccount.getId(), null),
			engineClientAccount);

		Assert.assertEquals(
			engineClientAccount.getAccountName(), account.getAccountName());
		Assert.assertEquals(
			engineClientAccount.getAccountType(), account.getAccountType());
		Assert.assertEquals(
			engineClientAccount.getActivitiesCount(),
			account.getActivitiesCount());
		Assert.assertEquals(
			engineClientAccount.getActivityStatus(),
			account.getActivityStatus());
		Assert.assertEquals(
			engineClientAccount.getAnnualRevenue(), account.getAnnualRevenue());

		CalculatedField[] calculatedFields = account.getCalculatedFields();

		Assert.assertEquals(
			Arrays.toString(calculatedFields), 1, calculatedFields.length);

		CalculatedField calculatedField = calculatedFields[0];

		Assert.assertEquals("closedWonAmount90d", calculatedField.getName());
		Assert.assertEquals("salesforce", calculatedField.getNamespace());
		Assert.assertEquals("1000", calculatedField.getValue());

		Assert.assertEquals(
			engineClientAccount.getFirstActivityDate(),
			account.getFirstActivityDate());
		Assert.assertEquals(engineClientAccount.getId(), account.getId());
		Assert.assertEquals(
			engineClientAccount.getLastActivityDate(),
			account.getLastActivityDate());

		LifecycleStage[] lifecycleStages = account.getLifecycleStages();

		Assert.assertEquals(
			Arrays.toString(lifecycleStages), 1, lifecycleStages.length);

		LifecycleStage lifecycleStage = lifecycleStages[0];

		Assert.assertEquals(
			engineClientLifecycleStage.getLifecycleName(),
			lifecycleStage.getLifecycleName());
		Assert.assertEquals(
			engineClientLifecycleStage.getStageType(),
			lifecycleStage.getStageType());

		Assert.assertEquals(
			engineClientAccount.getNumberOfEmployees(),
			account.getNumberOfEmployees());
		Assert.assertEquals(
			engineClientAccount.getWebsite(), account.getWebsite());

		Assert.assertNull(
			_accountDTOConverter.toDTO(
				new FaroDTOConverterContext(false, null, null), null));
	}

	private com.liferay.osb.faro.engine.client.model.Account.CalculatedField
		_createCalculatedField(String name, String namespace, String value) {

		com.liferay.osb.faro.engine.client.model.Account.CalculatedField
			calculatedField =
				new com.liferay.osb.faro.engine.client.model.Account.
					CalculatedField();

		calculatedField.setModifiedDate(RandomTestUtil.nextDate());
		calculatedField.setName(name);
		calculatedField.setNamespace(namespace);
		calculatedField.setValue(value);

		return calculatedField;
	}

	private final AccountDTOConverter _accountDTOConverter =
		new AccountDTOConverter();

}