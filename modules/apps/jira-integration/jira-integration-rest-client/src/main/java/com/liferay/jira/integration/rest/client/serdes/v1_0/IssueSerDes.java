/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.jira.integration.rest.client.serdes.v1_0;

import com.liferay.jira.integration.rest.client.dto.v1_0.Issue;
import com.liferay.jira.integration.rest.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Nilton Vieira
 * @generated
 */
@Generated("")
public class IssueSerDes {

	public static Issue toDTO(String json) {
		IssueJSONParser issueJSONParser = new IssueJSONParser();

		return issueJSONParser.parseToDTO(json);
	}

	public static Issue[] toDTOs(String json) {
		IssueJSONParser issueJSONParser = new IssueJSONParser();

		return issueJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Issue issue) {
		if (issue == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (issue.getDescription() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"description\": ");

			sb.append("\"");

			sb.append(_escape(issue.getDescription()));

			sb.append("\"");
		}

		if (issue.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append("\"");

			sb.append(_escape(issue.getId()));

			sb.append("\"");
		}

		if (issue.getKey() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"key\": ");

			sb.append("\"");

			sb.append(_escape(issue.getKey()));

			sb.append("\"");
		}

		if (issue.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(issue.getName()));

			sb.append("\"");
		}

		if (issue.getPriority() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"priority\": ");

			sb.append("\"");

			sb.append(_escape(issue.getPriority()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		IssueJSONParser issueJSONParser = new IssueJSONParser();

		return issueJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Issue issue) {
		if (issue == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (issue.getDescription() == null) {
			map.put("description", null);
		}
		else {
			map.put("description", String.valueOf(issue.getDescription()));
		}

		if (issue.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(issue.getId()));
		}

		if (issue.getKey() == null) {
			map.put("key", null);
		}
		else {
			map.put("key", String.valueOf(issue.getKey()));
		}

		if (issue.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(issue.getName()));
		}

		if (issue.getPriority() == null) {
			map.put("priority", null);
		}
		else {
			map.put("priority", String.valueOf(issue.getPriority()));
		}

		return map;
	}

	public static class IssueJSONParser extends BaseJSONParser<Issue> {

		@Override
		protected Issue createDTO() {
			return new Issue();
		}

		@Override
		protected Issue[] createDTOArray(int size) {
			return new Issue[size];
		}

		@Override
		protected void setField(
			Issue issue, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "description")) {
				if (jsonParserFieldValue != null) {
					issue.setDescription((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					issue.setId((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "key")) {
				if (jsonParserFieldValue != null) {
					issue.setKey((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					issue.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "priority")) {
				if (jsonParserFieldValue != null) {
					issue.setPriority((String)jsonParserFieldValue);
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

			Class<?> valueClass = value.getClass();

			if (value instanceof Map) {
				sb.append(_toJSON((Map)value));
			}
			else if (valueClass.isArray()) {
				Object[] values = (Object[])value;

				sb.append("[");

				for (int i = 0; i < values.length; i++) {
					sb.append("\"");
					sb.append(_escape(values[i]));
					sb.append("\"");

					if ((i + 1) < values.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(_escape(entry.getValue()));
				sb.append("\"");
			}
			else {
				sb.append(String.valueOf(entry.getValue()));
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

}