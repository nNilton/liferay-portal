/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.learn;

import com.liferay.client.extension.util.spring.boot3.BaseRestController;
import com.liferay.client.extension.util.spring.boot3.client.LiferayOAuth2AccessTokenManager;

import com.liferay.headless.admin.taxonomy.client.dto.v1_0.TaxonomyCategory;
import com.liferay.headless.admin.taxonomy.client.dto.v1_0.TaxonomyVocabulary;
import com.liferay.headless.admin.taxonomy.client.pagination.Page;
import com.liferay.headless.admin.taxonomy.client.pagination.Pagination;
import com.liferay.headless.admin.taxonomy.client.resource.v1_0.TaxonomyCategoryResource;
import com.liferay.headless.admin.taxonomy.client.resource.v1_0.TaxonomyVocabularyResource;
import com.liferay.headless.admin.user.client.dto.v1_0.UserAccount;
import com.liferay.headless.admin.user.client.resource.v1_0.UserAccountResource;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.StringUtil;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * @author Nilton Vieira
 */
@Component
public class LearnCommandLineRunner
	extends BaseRestController implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		_processAuthors();

		_loadTaxonomyCategories();

		_migrateZendeskArticles();
	}

	@Override
	protected String getWebClientBaseURL() {
		return "";
	}

	private long _getAuthorId(long authorId) throws Exception {
		JSONObject jsonObject = _authorIdMap.get(authorId);

		long id = jsonObject.getLong("userId");

		if (id != 0) {
			return id;
		}

		UserAccountResource userAccountResource = UserAccountResource.builder(
		).endpoint(
			new URL(_getLiferayURL())
		).header(
			HttpHeaders.AUTHORIZATION, _getLiferayAuthorization()
		).build();

		UserAccount userAccount =
			userAccountResource.getUserAccountByEmailAddress(
				jsonObject.getString("email"));

		if (userAccount == null) {
			userAccount = new UserAccount();

			userAccount.setName(() -> jsonObject.getString("name"));

			userAccount.setEmailAddress(() -> jsonObject.getString("email"));
			userAccount.setExternalReferenceCode(
				() -> String.valueOf(jsonObject.getLong("id")));

			userAccount = userAccountResource.postAccountUserAccount(
				_companyId, userAccount);
		}

		jsonObject.put("userId", userAccount.getId());

		return userAccount.getId();
	}

	private JSONObject _getKnowledgeArticleJSONObject(JSONObject jsonObject)
		throws Exception {

		JSONObject knowledgeArticleJSONObject = new JSONObject();

		knowledgeArticleJSONObject.put(
			"authorId", _getAuthorId(jsonObject.getLong("author_id"))
		).put(
			"content_i18n",
			new JSONObject(
			).put(
				"en_US", jsonObject.getString("body")
			)
		).put(
			"externalReferenceCode", jsonObject.getLong("id")
		).put(
			"legacy", true
		).put(
			"name", jsonObject.getString("name")
		).put(
			"permissions",
			new JSONObject(
			).put(
				"actionIds", new String[] {"VIEW"}
			).put(
				"roleName", "guest"
			)
		).put(
			"showDisclaimerMessage", true
		).put(
			"taxonomyCategoryIds",
			_processLabels(
				jsonObject.getJSONArray("labels")
			).toArray(
				new Long[0]
			)
		).put(
			"url", jsonObject.getString("html_url")
		);

		return knowledgeArticleJSONObject;
	}

	private String _getLiferayAuthorization() {
		return _liferayOAuth2AccessTokenManager.getAuthorization(
			_liferayOAuthApplicationExternalReferenceCodes);
	}

	private String _getLiferayURL() {
		return lxcDXPServerProtocol + "://" + lxcDXPMainDomain;
	}

	private String _getZendeskAuthorization() {
		return "Bearer " + _liferayLearnZendeskApiToken;
	}

	private void _loadTaxonomyCategories() throws Exception {
		TaxonomyVocabularyResource.Builder taxonomyVocabularyResourceBuilder =
			TaxonomyVocabularyResource.builder();

		TaxonomyVocabularyResource taxonomyVocabularyResource =
			taxonomyVocabularyResourceBuilder.header(
				"Authorization", _getLiferayAuthorization()
			).endpoint(
				new URL(_getLiferayURL())
			).build();

		TaxonomyCategoryResource.Builder taxonomyCategoryResourceBuilder =
			TaxonomyCategoryResource.builder();

		TaxonomyCategoryResource taxonomyCategoryResource =
			taxonomyCategoryResourceBuilder.header(
				"Authorization", _getLiferayAuthorization()
			).endpoint(
				new URL(_getLiferayURL())
			).build();

		Page<TaxonomyVocabulary> taxonomyVocabulariesPage =
			taxonomyVocabularyResource.getSiteTaxonomyVocabulariesPage(
				_siteGroupId, null, null, null, Pagination.of(-1, -1), null);

		for (TaxonomyVocabulary taxonomyVocabulary :
				taxonomyVocabulariesPage.getItems()) {

			Page<TaxonomyCategory> taxonomyCategoriesPage =
				taxonomyCategoryResource.
					getTaxonomyVocabularyTaxonomyCategoriesPage(
						taxonomyVocabulary.getId(), true, null, null, null,
						Pagination.of(-1, -1), null);

			for (TaxonomyCategory taxonomyCategory :
					taxonomyCategoriesPage.getItems()) {

				_taxonomyCategories.put(
					taxonomyCategory.getName(), taxonomyCategory.getId());
			}
		}
	}

	private void _migrateZendeskArticles() throws Exception {
		for (int page = 1;; page++) {
			JSONObject jsonObject = new JSONObject(
				get(
					_getZendeskAuthorization(),
					"https://liferay-support.zendesk.com/api/v2/help_center/en-us/articles?label_names=MIGRATION 1&page=" +
						page + "&per_page=100"));

			JSONArray jsonArray = jsonObject.getJSONArray("articles");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject1 = _getKnowledgeArticleJSONObject(
					jsonArray.getJSONObject(i));

				put(
					_getLiferayAuthorization(), jsonObject1.toString(),
					_getLiferayURL() +
						"/o/c/p2s3knowledgearticles/scopes/guest/by-external-reference-code/" +
							jsonObject1.getLong("externalReferenceCode"));
			}

			if (jsonObject.getInt("page_count") == page) {
				break;
			}
		}
	}

	private void _processAuthors() {
		for (Long authorId : _AUTHOR_IDS) {
			JSONObject jsonObject = new JSONObject(
				get(
					_getZendeskAuthorization(),
					"https://liferay-support.zendesk.com/api/v2/users/" +
						authorId));

			_authorIdMap.put(authorId, jsonObject);
		}
	}

	private List<Long> _processLabels(JSONArray jsonArray) {
		List<Long> taxonomyCategoryIds = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			String zendeskLabel = _zendeskLabelsMap.get(jsonArray.getString(i));

			if (zendeskLabel == null) {
				continue;
			}

			if (StringUtil.startsWith(zendeskLabel, "DXP ") ||
				StringUtil.startsWith(zendeskLabel, "Portal ")) {

				taxonomyCategoryIds.add(
					Long.valueOf(
						_taxonomyCategories.get("Liferay Self-Hosted")));
			}

			taxonomyCategoryIds.add(
				Long.valueOf(_taxonomyCategories.get(zendeskLabel)));
		}

		return taxonomyCategoryIds;
	}

	private static final Long[] _AUTHOR_IDS = {
		25774534826893L, 373185040091L, 6012626444045L, 407158172372L,
		372705873752L, 402827046671L, 366376802712L, 29923266459277L,
		384097290672L, 372625664892L, 373149011752L, 373184899791L,
		424031032911L, 8281671277197L, 28340825135629L, 385870175971L,
		402999848171L, 373184915451L, 375994556791L, 1903298695907L,
		29178802878861L, 366447371912L, 11438601141133L, 29017365453965L,
		373148995092L, 373860647592L, 20886482355597L, 383194348352L,
		10492726157453L, 419447428432L, 20442344151565L, 1904580184667L,
		373185092851L, 16878347054221L, 385002475392L, 421304055031L,
		372749968591L, 370270860211L, 407158464492L, 17676116379277L,
		366737325611L, 366931880911L, 424277722571L, 424031460891L,
		7578639775629L, 371937132592L, 10179332964237L, 370893685472L,
		403906895791L, 371038218371L, 379745023111L, 10098599333901L,
		373148967792L, 373184985751L, 25968542723085L, 366784167951L,
		20245565000845L, 373148917612L, 373149156712L, 370767034731L,
		366737976392L, 370970624412L, 366871082551L, 372363832491L,
		423037708032L, 377855535812L, 15262410539021L, 366013180451L,
		371091423991L, 372366622452L, 407630088092L, 412413393352L,
		372867984791L, 372923547331L, 6862776677901L, 373185070251L,
		407162261232L, 373035069351L, 373185214891L, 375158712852L,
		389210284951L, 372923546591L, 373185092791L, 421978411212L,
		367411867472L, 373184998571L, 373149066312L, 370623370492L,
		25773897965069L, 373148885832L, 15880641889805L, 373149057092L,
		17531209709581L, 5054395198093L, 5341977284877L, 372528405572L,
		7939158268429L, 372358433692L, 403895380432L, 367119381972L,
		21270730121869L, 373185282371L, 372937222932L, 17676165787917L,
		415192903552L, 371895917152L, 1903130316867L, 415792656351L,
		373185206871L, 19002500942221L, 17581992285709L, 373185220691L,
		373185163871L, 397337294051L, 366918656192L, 367119363532L,
		8281773134349L, 373149024412L, 399572727711L, 13013415006605L,
		373184831871L, 25968541772941L, 373185277551L, 12073805054861L,
		373185298031L, 368020490052L, 399951451392L, 372923547651L,
		392271342791L, 366354951311L, 373185241451L, 373184829291L,
		1902265153247L, 373407136812L, 367157916851L, 373148772232L,
		372999465071L, 405757266912L, 373184952931L, 4529113176717L,
		403307959271L, 372733531471L, 381295176812L, 8671038525709L,
		402826927811L, 373271535911L, 373185276931L, 366926529812L,
		422959208432L, 33003396953613L, 396692389411L, 367107243731L,
		367077572892L, 397592563151L, 366919861412L, 14397070520205L,
		372416455232L, 9807821097869L, 392156252372L, 373184917511L,
		373704979972L, 366925297912L, 422222403031L, 373888844811L,
		27950430947597L, 11729761361421L, 372705728332L, 373148911572L,
		394769506472L, 387901437451L, 373148773852L, 367721768532L,
		394156375352L, 14438092261517L, 18156512534285L, 415532210992L,
		373185141311L, 403906987551L, 366906675192L, 366924943552L,
		11274804041485L, 373184919591L, 391236021471L
	};

	private Map<Long, JSONObject> _authorIdMap;

	@Value("${liferay.learn.dxp.company.id}")
	private Long _companyId;

	@Value("${liferay.learn.zendesk.api.token}")
	private String _liferayLearnZendeskApiToken;

	@Autowired
	private LiferayOAuth2AccessTokenManager _liferayOAuth2AccessTokenManager;

	@Value("${liferay.oauth.application.external.reference.codes}")
	private String _liferayOAuthApplicationExternalReferenceCodes;

	@Value("${liferay.learn.dxp.site.group.id}")
	private long _siteGroupId;

	private final Map<String, String> _taxonomyCategories = new HashMap<>();
	private final Map<String, String> _zendeskLabelsMap = HashMapBuilder.put(
		"2023.Q3", "DXP 2023.Q3"
	).put(
		"2023.Q4", "DXP 2023.Q4"
	).put(
		"2024.Q1", "DXP 2024.Q1 (LTS)"
	).put(
		"2024.Q2", "DXP 2024.Q2"
	).put(
		"2024.Q3", "DXP 2024.Q3"
	).put(
		"2024.Q4", "DXP 2024.Q4"
	).put(
		"2025.Q1", "DXP 2025.Q1 (LTS)"
	).put(
		"Analytics", "Personalization"
	).put(
		"Analytics Cloud", "Analytics Cloud"
	).put(
		"APIs, Integrations and Extension Points", "Integration"
	).put(
		"Application Security", "Security"
	).put(
		"Backup/Recovery", "Cloud"
	).put(
		"Caching & Clustering", "Platform"
	).put(
		"Calendar", "Sites"
	).put(
		"Collaboration & Document Management", "Digital Asset Management"
	).put(
		"Commerce", "Commerce"
	).put(
		"Commerce", "Liferay Commerce"
	).put(
		"Configuration and Settings", "Platform"
	).put(
		"Connectivity", "Platform"
	).put(
		"Connectors", "Integration"
	).put(
		"Deployment, Environments", "Development and Tooling"
	).put(
		"DXP 7.0", "DXP 7.0"
	).put(
		"DXP 7.1", "DXP 7.1"
	).put(
		"DXP 7.2", "DXP 7.2"
	).put(
		"DXP 7.3", "DXP 7.3"
	).put(
		"DXP 7.4", "DXP 7.4"
	).put(
		"Forms", "Platform"
	).put(
		"Front-end Infrastructure", "Content Management System"
	).put(
		"Liferay API", "Integration"
	).put(
		"LXC", "Cloud"
	).put(
		"LXC", "Liferay SaaS"
	).put(
		"LXC-SM", "Cloud"
	).put(
		"LXC-SM", "Liferay PaaS"
	).put(
		"Monitoring", "Cloud"
	).put(
		"Networking", "Cloud"
	).put(
		"Objects", "Development and Tooling"
	).put(
		"Patching Tool", "DXP-SH Installation, Maintenance, and Admin"
	).put(
		"Performance", "DXP-SH Installation, Maintenance, and Admin"
	).put(
		"Portal 6.1", "Portal 6.1"
	).put(
		"Portal 6.2", "Portal 6.2"
	).put(
		"Search", "Search"
	).put(
		"Segmentation", "Personalization"
	).put(
		"Staging", "Platform"
	).put(
		"Sync", "Platform"
	).put(
		"System Availability", "Cloud"
	).put(
		"Tooling", "Development and Tooling"
	).put(
		"Upgrade", "DXP-SH Installation, Maintenance, and Admin"
	).put(
		"User & System Management",
		"DXP-SH Installation, Maintenance, and Admin"
	).put(
		"VPN", "Cloud"
	).put(
		"Web Experience Management", "Content Management System"
	).put(
		"Workflow", "Platform"
	).build();

}