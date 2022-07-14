





public class JiraIntegrationSiteSettingsConfigurationScreenContributor implements SiteSettingsConfigurationScreenContributor{

	@Override
	public String getCategoryKey() {
		return "jira-integration";
	}

	@Override
	public String getJspPath() {
		return "/site_settings/jira_integration.jsp";
	}

	@Override
	public String getKey() {
		return "site-configuration-jira-integration";
	}

	@Override
	public String getName(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return _language.get(resourceBundle, "jira-integration");
	}

	@Override
	public String getSaveMVCActionCommandName() {
		return "/jira_integration/save_site_configuration";
	}

	@Override
	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	public void setAttributes(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		ClickToChatConfiguration clickToChatConfiguration =
			ClickToChatConfigurationUtil.getClickToChatConfiguration(
				themeDisplay.getCompanyId(), themeDisplay.getSiteGroupId());

		httpServletRequest.setAttribute(
			ClickToChatConfiguration.class.getName(),
			ClickToChatConfigurationUtil.getClickToChatConfiguration(
				themeDisplay.getCompanyId(), 0));

		httpServletRequest.setAttribute(
			ClickToChatWebKeys.CLICK_TO_CHAT_CHAT_PROVIDER_ACCOUNT_ID,
			clickToChatConfiguration.chatProviderAccountId());
		httpServletRequest.setAttribute(
			ClickToChatWebKeys.CLICK_TO_CHAT_CHAT_PROVIDER_ID,
			clickToChatConfiguration.chatProviderId());
		httpServletRequest.setAttribute(
			ClickToChatWebKeys.CLICK_TO_CHAT_ENABLED,
			clickToChatConfiguration.enabled());
		httpServletRequest.setAttribute(
			ClickToChatWebKeys.CLICK_TO_CHAT_GUEST_USERS_ALLOWED,
			clickToChatConfiguration.guestUsersAllowed());
		httpServletRequest.setAttribute(
			ClickToChatWebKeys.CLICK_TO_CHAT_HIDE_IN_CONTROL_PANEL,
			clickToChatConfiguration.hideInControlPanel());
	}

	@Reference
	private Language _language;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.click.to.chat.web)",
		unbind = "-"
	)
	private ServletContext _servletContext;

}