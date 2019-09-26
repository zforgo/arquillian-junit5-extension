package io.github.zforgo.arquillian.junit5.container;

import io.github.zforgo.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.container.test.spi.TestRunner;
import org.jboss.arquillian.test.spi.TestResult;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

public class JUnitJupiterTestRunner implements TestRunner {

	@Override
	public TestResult execute(Class<?> testClass, String methodName) {

		TestResult testResult;
		ArquillianTestMethodExecutionListener listener = new ArquillianTestMethodExecutionListener();
		try {
			Launcher launcher = LauncherFactory.create();
			launcher.registerTestExecutionListeners(listener);
			LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
					.selectors(DiscoverySelectors.selectMethod(testClass.getCanonicalName(), methodName))
					.configurationParameter(ArquillianExtension.RUNNING_INSIDE_ARQUILLIAN, "true")
					.build();
			launcher.execute(request);

			testResult = listener.getTestResult();
		} catch (Throwable t) {
			testResult = TestResult.failed(t);
		}
		testResult.setEnd(System.currentTimeMillis());
		return testResult;
	}

	private static class ArquillianTestMethodExecutionListener implements TestExecutionListener {

		private TestResult testResult;

		public void executionSkipped(TestIdentifier testIdentifier, String reason) {
			testResult = TestResult.skipped(reason);
		}

		public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
			TestExecutionResult.Status status = testExecutionResult.getStatus();

			if (!testIdentifier.isTest()) {
				return;
			}
			switch (status) {
				case FAILED:
					final Throwable exception = testExecutionResult.getThrowable()
							.orElseGet(() -> new Exception("Failed"));
					this.testResult = TestResult.failed(exception);
					break;
				case SUCCESSFUL:
					testResult = TestResult.passed();
					break;
				case ABORTED:
					if (testExecutionResult.getThrowable().isPresent()) {
						testResult = TestResult.skipped(testExecutionResult.getThrowable().get());
					} else {
						testResult = TestResult.skipped("Aborted");
					}
					break;
			}
		}

		private TestResult getTestResult() {
			return testResult;
		}
	}

}
