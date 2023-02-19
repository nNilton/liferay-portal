package com.liferay.site.initializer.extender.internal.auto.deploy;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.deploy.auto.AutoDeployException;
import com.liferay.portal.kernel.deploy.auto.AutoDeployListener;
import com.liferay.portal.kernel.deploy.auto.context.AutoDeploymentContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.GroupService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.vulcan.util.GroupUtil;
import com.liferay.site.initializer.SiteInitializer;
import com.liferay.site.initializer.SiteInitializerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Nilton Vieira
 */
@Component(service = AutoDeployListener.class)
public class SiteInitializerAutoDeployListener implements AutoDeployListener {

	public int deploy(AutoDeploymentContext autoDeploymentContext)
		throws AutoDeployException {

		System.out.println("It's workinggggg");

		String name = "test1";

		Map<Locale, String> nameMap = HashMapBuilder.put(
			LocaleUtil.getDefault(), name
		).build();

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setCompanyId(20097);
		serviceContext.setScopeGroupId(20116);
		serviceContext.setUserId(20125);

		Group group = null;
		try {
			group = _groupService.addGroup(
				20125, 0, null, 0,
				GroupConstants.DEFAULT_LIVE_GROUP_ID, nameMap,
				HashMapBuilder.put(
					LocaleUtil.getDefault(), name
				).build(),
				GroupConstants.TYPE_SITE_OPEN, true, 0,
				StringPool.SLASH + FriendlyURLNormalizerUtil.normalize(name), true,
				true, serviceContext);
		}
		catch (PortalException e) {
			throw new RuntimeException(e);
		}

		serviceContext.setScopeGroupId(group.getGroupId());
		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		File tempFolder = null;

		try {



			PermissionThreadLocal.setPermissionChecker(
				PermissionCheckerFactoryUtil.create(_userLocalService.getUser(20125)));


			File tempFile = FileUtil.createTempFile();

			FileUtil.write(
				tempFile,
				new FileInputStream(autoDeploymentContext.getFile()));

			File tempDir1 = FileUtil.createTempFolder();

			FileUtil.unzip(tempFile, tempDir1);

			tempFile.delete();

			SiteInitializer siteInitializer = _siteInitializerFactory.create(
				new File(tempDir1, "site-initializer"), null);

			siteInitializer.initialize(group.getGroupId());
		}
		catch (Exception e) {
			try {
				tempFolder.delete();
				_groupService.deleteGroup(group);
			}
			catch (PortalException ex) {
				throw new RuntimeException(ex);
			}
			e.printStackTrace();

			throw new RuntimeException(e);
		}

		return 0;
	}

	public boolean isDeployable(AutoDeploymentContext autoDeploymentContext)
		throws AutoDeployException {

		return true;
	}

	@Reference
	private GroupLocalService _groupService;
	@Reference
	private UserLocalService _userLocalService;

	@Reference
	private SiteInitializerFactory _siteInitializerFactory;

}