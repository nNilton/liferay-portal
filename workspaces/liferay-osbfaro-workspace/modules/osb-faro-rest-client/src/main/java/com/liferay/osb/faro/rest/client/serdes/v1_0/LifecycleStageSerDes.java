/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.client.serdes.v1_0;

import com.liferay.osb.faro.rest.client.dto.v1_0.LifecycleStage;
import com.liferay.osb.faro.rest.client.json.BaseJSONParser;

import jakarta.annotation.Generated;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Leslie Wong
 * @generated
 */
@Generated("")
public class LifecycleStageSerDes {

	public static LifecycleStage toDTO(String json) {
		LifecycleStageJSONParser lifecycleStageJSONParser =
			new LifecycleStageJSONParser();

		return lifecycleStageJSONParser.parseToDTO(json);
	}

	public static LifecycleStage[] toDTOs(String json) {
		LifecycleStageJSONParser lifecycleStageJSONParser =
			new LifecycleStageJSONParser();

		return lifecycleStageJSONParser.parseToDTOs(json);
	}

	public static String toJSON(LifecycleStage lifecycleStage) {
		if (lifecycleStage == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (lifecycleStage.getLifecycleName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"lifecycleName\": ");

			sb.append("\"");

			sb.append(_escape(lifecycleStage.getLifecycleName()));

			sb.append("\"");
		}

		if (lifecycleStage.getStageType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"stageType\": ");

			sb.append("\"");

			sb.append(_escape(lifecycleStage.getStageType()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		LifecycleStageJSONParser lifecycleStageJSONParser =
			new LifecycleStageJSONParser();

		return lifecycleStageJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(LifecycleStage lifecycleStage) {
		if (lifecycleStage == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (lifecycleStage.getLifecycleName() == null) {
			map.put("lifecycleName", null);
		}
		else {
			map.put(
				"lifecycleName",
				String.valueOf(lifecycleStage.getLifecycleName()));
		}

		if (lifecycleStage.getStageType() == null) {
			map.put("stageType", null);
		}
		else {
			map.put("stageType", String.valueOf(lifecycleStage.getStageType()));
		}

		return map;
	}

	public static class LifecycleStageJSONParser
		extends BaseJSONParser<LifecycleStage> {

		@Override
		protected LifecycleStage createDTO() {
			return new LifecycleStage();
		}

		@Override
		protected LifecycleStage[] createDTOArray(int size) {
			return new LifecycleStage[size];
		}

		@Override
		protected boolean parseMaps(String jsonParserFieldName) {
			if (Objects.equals(jsonParserFieldName, "lifecycleName")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "stageType")) {
				return false;
			}

			return false;
		}

		@Override
		protected void setField(
			LifecycleStage lifecycleStage, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "lifecycleName")) {
				if (jsonParserFieldValue != null) {
					lifecycleStage.setLifecycleName(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "stageType")) {
				if (jsonParserFieldValue != null) {
					lifecycleStage.setStageType((String)jsonParserFieldValue);
				}
			}
		}

	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		for (String[] strings : BaseJSONParser.JSON_ESCAPE_STRINGS) {
			string = string.replace(strings[0], strings[1]);
		}

		return string;
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
			sb.append(entry.getKey());
			sb.append("\": ");

			Object value = entry.getValue();

			sb.append(_toJSON(value));

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static String _toJSON(Object value) {
		if (value == null) {
			return "null";
		}

		if (value instanceof Collection) {
			Collection<?> collection = (Collection<?>)value;

			return _toJSON(collection.toArray());
		}

		if (value instanceof Map) {
			return _toJSON((Map)value);
		}

		Class<?> clazz = value.getClass();

		if (clazz.isArray()) {
			StringBuilder sb = new StringBuilder("[");

			Object[] values = (Object[])value;

			for (int i = 0; i < values.length; i++) {
				sb.append(_toJSON(values[i]));

				if ((i + 1) < values.length) {
					sb.append(", ");
				}
			}

			sb.append("]");

			return sb.toString();
		}

		if (value instanceof String) {
			return "\"" + _escape(value) + "\"";
		}

		return String.valueOf(value);
	}

}
// LIFERAY-REST-BUILDER-HASH:-192121382