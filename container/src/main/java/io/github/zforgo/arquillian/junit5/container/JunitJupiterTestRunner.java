package io.github.zforgo.arquillian.junit5.container;

import org.jboss.arquillian.container.spi.client.deployment.Deployment;
import org.jboss.arquillian.container.test.spi.TestRunner;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.spi.TestResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

public class JunitJupiterTestRunner implements TestRunner {


	@Inject
	private Instance<Deployment> deployment;

	@Override
	public TestResult execute(Class<?> testClass, String methodName) {
		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
				.selectors(DiscoverySelectors.selectMethod(testClass, methodName))
				.build();
		ArquillianTestExecutionListener listener = new ArquillianTestExecutionListener();
		Launcher launcher = LauncherFactory.create();
		launcher.registerTestExecutionListeners(listener);

		launcher.execute(request);
		TestResult testResult = listener.getTestResult();
		testResult.setEnd(System.currentTimeMillis());
		return testResult;
	}
}
