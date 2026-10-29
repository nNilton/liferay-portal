/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import jakarta.annotation.Generated;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Leslie Wong
 * @generated
 */
@Generated("")
@GraphQLName(
	description = "The stage an account is currently in, within one account lifecycle.",
	value = "LifecycleStage"
)
@io.swagger.v3.oas.annotations.media.Schema(
	description = "The stage an account is currently in, within one account lifecycle."
)
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "LifecycleStage")
public class LifecycleStage implements Serializable {

	public static LifecycleStage toDTO(String json) {
		return ObjectMapperUtil.readValue(LifecycleStage.class, json);
	}

	public static LifecycleStage unsafeToDTO(String json) {
		return ObjectMapperUtil.unsafeReadValue(LifecycleStage.class, json);
	}

	@io.swagger.v3.oas.annotations.media.Schema(
		description = "Name of the account lifecycle. Use `getWorkspaceGroupChannelAccountLifecyclesPage` to find its ID."
	)
	public String getLifecycleName() {
		if (_lifecycleNameSupplier != null) {
			lifecycleName = _lifecycleNameSupplier.get();

			_lifecycleNameSupplier = null;
		}

		return lifecycleName;
	}

	public void setLifecycleName(String lifecycleName) {
		this.lifecycleName = lifecycleName;

		_lifecycleNameSupplier = null;
	}

	@JsonIgnore
	public void setLifecycleName(
		UnsafeSupplier<String, Exception> lifecycleNameUnsafeSupplier) {

		_lifecycleNameSupplier = () -> {
			try {
				return lifecycleNameUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField(
		description = "Name of the account lifecycle. Use `getWorkspaceGroupChannelAccountLifecyclesPage` to find its ID."
	)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected String lifecycleName;

	@JsonIgnore
	private Supplier<String> _lifecycleNameSupplier;

	@io.swagger.v3.oas.annotations.media.Schema(
		description = "Stage type: AWARE, ENGAGED, PIPELINE, ONBOARDING, ESTABLISHED, or AT_RISK."
	)
	public String getStageType() {
		if (_stageTypeSupplier != null) {
			stageType = _stageTypeSupplier.get();

			_stageTypeSupplier = null;
		}

		return stageType;
	}

	public void setStageType(String stageType) {
		this.stageType = stageType;

		_stageTypeSupplier = null;
	}

	@JsonIgnore
	public void setStageType(
		UnsafeSupplier<String, Exception> stageTypeUnsafeSupplier) {

		_stageTypeSupplier = () -> {
			try {
				return stageTypeUnsafeSupplier.get();
			}
			catch (RuntimeException runtimeException) {
				throw runtimeException;
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		};
	}

	@GraphQLField(
		description = "Stage type: AWARE, ENGAGED, PIPELINE, ONBOARDING, ESTABLISHED, or AT_RISK."
	)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected String stageType;

	@JsonIgnore
	private Supplier<String> _stageTypeSupplier;

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
		StringBundler sb = new StringBundler();

		sb.append("{");

		String lifecycleName = getLifecycleName();

		if (lifecycleName != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"lifecycleName\": ");

			sb.append("\"");

			sb.append(_escape(lifecycleName));

			sb.append("\"");
		}

		String stageType = getStageType();

		if (stageType != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"stageType\": ");

			sb.append("\"");

			sb.append(_escape(stageType));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	@io.swagger.v3.oas.annotations.media.Schema(
		accessMode = io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY,
		defaultValue = "com.liferay.osb.faro.rest.dto.v1_0.LifecycleStage",
		name = "x-class-name"
	)
	public String xClassName;

	private static String _escape(Object object) {
		return StringUtil.replace(
			String.valueOf(object), _JSON_ESCAPE_STRINGS[0],
			_JSON_ESCAPE_STRINGS[1]);
	}

	private static boolean _isArray(Object value) {
		if (value == null) {
			return false;
		}

		Class<?> clazz = value.getClass();

		return clazz.isArray();
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(_escape(entry.getKey()));
			sb.append("\": ");

			Object value = entry.getValue();

			if (_isArray(value)) {
				sb.append("[");

				Object[] valueArray = (Object[])value;

				for (int i = 0; i < valueArray.length; i++) {
					if (valueArray[i] instanceof Map) {
						sb.append(_toJSON((Map<String, ?>)valueArray[i]));
					}
					else if (valueArray[i] instanceof String) {
						sb.append("\"");
						sb.append(valueArray[i]);
						sb.append("\"");
					}
					else {
						sb.append(valueArray[i]);
					}

					if ((i + 1) < valueArray.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof Map) {
				sb.append(_toJSON((Map<String, ?>)value));
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(_escape(value));
				sb.append("\"");
			}
			else {
				sb.append(value);
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static String _toJSON(Object value) {
		if (value instanceof Collection) {
			return String.valueOf(
				JSONFactoryUtil.createJSONArray((Collection<?>)value));
		}
		else if (value instanceof Map) {
			return String.valueOf(
				JSONFactoryUtil.createJSONObject((Map<?, ?>)value));
		}
		else if (value instanceof Object[]) {
			return String.valueOf(
				JSONFactoryUtil.createJSONArray(
					Arrays.asList((Object[])value)));
		}
		else if (value instanceof String) {
			return StringBundler.concat("\"", _escape(value), "\"");
		}

		return String.valueOf(value);
	}

	private static final String[][] _JSON_ESCAPE_STRINGS = {
		{"\\", "\"", "\b", "\f", "\n", "\r", "\t"},
		{"\\\\", "\\\"", "\\b", "\\f", "\\n", "\\r", "\\t"}
	};

	private Map<String, Serializable> _extendedProperties;

}
// LIFERAY-REST-BUILDER-HASH:-361353228