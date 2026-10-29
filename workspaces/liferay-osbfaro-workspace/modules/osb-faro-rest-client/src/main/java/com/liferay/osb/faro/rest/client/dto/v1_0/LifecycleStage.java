/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.client.dto.v1_0;

import com.liferay.osb.faro.rest.client.function.UnsafeSupplier;
import com.liferay.osb.faro.rest.client.serdes.v1_0.LifecycleStageSerDes;

import jakarta.annotation.Generated;

import java.io.Serializable;

import java.util.Objects;

/**
 * @author Leslie Wong
 * @generated
 */
@Generated("")
public class LifecycleStage implements Cloneable, Serializable {

	public static LifecycleStage toDTO(String json) {
		return LifecycleStageSerDes.toDTO(json);
	}

	public String getLifecycleName() {
		return lifecycleName;
	}

	public void setLifecycleName(String lifecycleName) {
		this.lifecycleName = lifecycleName;
	}

	public void setLifecycleName(
		UnsafeSupplier<String, Exception> lifecycleNameUnsafeSupplier) {

		try {
			lifecycleName = lifecycleNameUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String lifecycleName;

	public String getStageType() {
		return stageType;
	}

	public void setStageType(String stageType) {
		this.stageType = stageType;
	}

	public void setStageType(
		UnsafeSupplier<String, Exception> stageTypeUnsafeSupplier) {

		try {
			stageType = stageTypeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String stageType;

	@Override
	public LifecycleStage clone() throws CloneNotSupportedException {
		return (LifecycleStage)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof LifecycleStage)) {
			return false;
		}

		LifecycleStage lifecycleStage = (LifecycleStage)object;

		return Objects.equals(toString(), lifecycleStage.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return LifecycleStageSerDes.toJSON(this);
	}

}
// LIFERAY-REST-BUILDER-HASH:-2105030313