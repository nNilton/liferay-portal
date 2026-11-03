/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

const MCP_SERVER = 'mcp__liferay-ac-stg__';
const CHART_TOOL = 'mcp__faro_chart__render_chart';

const CALL_HTTP_ENDPOINT = `${MCP_SERVER}call-http-endpoint`;
const READ_TOOL_PREFIX = `${MCP_SERVER}get`;

const WRITE_METHODS = new Set(['POST', 'PUT', 'PATCH', 'DELETE']);

function isAllowedTool(toolName) {
	return (
		toolName === CHART_TOOL ||
		toolName === CALL_HTTP_ENDPOINT ||
		toolName.startsWith(READ_TOOL_PREFIX)
	);
}

/**
 * Decides whether a tool call is allowed under the read-only POC contract.
 *
 * @param {string} toolName Fully-qualified tool name (MCP tools are namespaced).
 * @param {Record<string, unknown>} input The tool call input.
 * @returns {{allow: boolean, reason?: string}}
 */
function evaluateToolCall(toolName, input = {}) {
	if (!isAllowedTool(toolName)) {
		return {allow: false, reason: `Tool "${toolName}" is not allowed.`};
	}

	if (toolName === CALL_HTTP_ENDPOINT && input.method) {
		const method = String(input.method).toUpperCase();

		if (WRITE_METHODS.has(method)) {
			return {
				allow: false,
				reason: `Method ${method} is blocked: this assistant is read-only.`
			};
		}
	}

	return {allow: true};
}

module.exports = {evaluateToolCall};
