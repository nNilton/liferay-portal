package com.liferay.site.initializer.extender.internal.auto.deploy;

import com.liferay.portal.kernel.deploy.auto.AutoDeployException;
import com.liferay.portal.kernel.deploy.auto.AutoDeployListener;

import com.liferay.portal.kernel.deploy.auto.context.AutoDeploymentContext;
import com.liferay.site.initializer.SiteInitializerFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Nilton Vieira
 */
@Component(service = AutoDeployListener.class)
public class SiteInitializerAutoDeployListener implements AutoDeployListener {

	public int deploy(AutoDeploymentContext autoDeploymentContext)
		throws AutoDeployException{
		System.out.println("It's workinggggg");

		try {
			_siteInitializerFactory.create(autoDeploymentContext.getFile(), null);
		}
		catch (Exception e) {
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
