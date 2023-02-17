package com.liferay.site.initializer.extender.internal.auto.deploy;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.deploy.auto.AutoDeployException;
import com.liferay.portal.kernel.deploy.auto.AutoDeployListener;

import com.liferay.portal.kernel.deploy.auto.context.AutoDeploymentContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.vulcan.util.GroupUtil;
import com.liferay.site.initializer.SiteInitializer;
import com.liferay.site.initializer.SiteInitializerFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Locale;
import java.util.Map;

/**
 * @author Nilton Vieira
 */
@Component(service = AutoDeployListener.class)
public class SiteInitializerAutoDeployListener implements AutoDeployListener {

	public int deploy(AutoDeploymentContext autoDeploymentContext)
		throws AutoDeployException{
		System.out.println("It's workinggggg");

		String name="test1";

		Map<Locale, String> nameMap = HashMapBuilder.put(
			LocaleUtil.getDefault(), name
		).build();

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setCompanyId(20097);
		serviceContext.setScopeGroupId(20121);
		serviceContext.setUserId(20125);



		try {
			Group group = GroupLocalServiceUtil.addGroup(
				20125, 0, null, 0,
				GroupConstants.DEFAULT_LIVE_GROUP_ID, nameMap,
				HashMapBuilder.put(
					LocaleUtil.getDefault(), name
				).build(),
				GroupConstants.TYPE_SITE_OPEN, true, 0,
					StringPool.SLASH + FriendlyURLNormalizerUtil.normalize(name), true,
				true, serviceContext);
			serviceContext.setScopeGroupId(group.getGroupId());
			ServiceContextThreadLocal.pushServiceContext(serviceContext);
			SiteInitializer siteInitializer = _siteInitializerFactory.create(autoDeploymentContext.getFile(), null);
			siteInitializer.initialize(group.getGroupId());
		}
		catch (PortalException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return 0;
	}

	public boolean isDeployable(AutoDeploymentContext autoDeploymentContext)
		throws AutoDeployException{
		return true;
	}

	@Reference
	private SiteInitializerFactory _siteInitializerFactory;

}
