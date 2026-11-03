# AI Chat (POC)

Read-only AI chat embedded in the Liferay Data Platform (LDP) UI. The assistant answers
questions about the analytics data by calling the Liferay MCP server
(`/o/mcp/ldp`) and can render charts in the chat.

This is a **dev-only POC**. It runs as a `webpack-dev-server` middleware (no
separate process, no production deploy) and the chat widget is gated behind
`FARO_DEV_MODE`.

## How it works

```
<AiChatWidget/>  ──POST /ai-chat (SSE blocks)──▶  ai-chat-server middleware
   (src/main/js/ai-chat)                              (Anthropic Agent SDK)
                                                            │
                                          mcp__ldp__*  (GET only)
                                          mcp__faro_chart__render_chart
                                                            │
                                                   https://<FARO_URL>/o/mcp/ldp
```

- `readOnlyGuard.js` — `canUseTool` chokepoint: only the MCP read tools and
  `render_chart` are allowed; `call-http-endpoint` is blocked for any non-GET.
- `chartSpec.js` — validates the chart spec the model emits.
- `middleware.js` — runs the agent loop and translates SDK messages into the
  SSE block protocol (`text` | `tool_activity` | `chart` | `error` | `done`).

## Configuration

The Anthropic API key, the model, and the LDP (MCP) authentication
are configured in the UI — **Settings → AI Assistant** — and persisted in
`localStorage`. The chat widget sends them with every request; the middleware
does not read any of them from the environment.

| Setting              | Where         | Purpose                                                          |
| -------------------- | ------------- | ---------------------------------------------------------------- |
| Anthropic API Key    | Settings page | Key the Agent SDK uses.                                          |
| Model                | Settings page | Claude model for the chat.                                       |
| LDP auth             | Settings page | `Basic` credentials sent as the `/o/mcp/ldp` `Authorization` header. |

Optional environment knobs (still read from `.env`):

| Variable            | Default       | Purpose                                       |
| ------------------- | ------------- | --------------------------------------------- |
| `FARO_URL`          | analytics-stg | Upstream base for `/o/mcp/ldp` and the dev proxy. |
| `LDP_CHAT_MAX_TURNS` | `20`          | Max agent turns per question.                 |

## Run

```bash
yarn start            # dev server on :3000, chat widget appears bottom-right
```

## Test

```bash
node --test ai-chat-server/__tests__/*.test.js
```
