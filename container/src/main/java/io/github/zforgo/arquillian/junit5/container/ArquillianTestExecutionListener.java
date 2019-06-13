package io.github.zforgo.arquillian.junit5.container;

import org.jboss.arquillian.test.spi.TestResult;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

import static org.junit.platform.engine.TestDescriptor.Type.TEST;

public class ArquillianTestExecutionListener implements TestExecutionListener {

	private TestResult testResult = TestResult.notRun();

	public void executionSkipped(TestIdentifier testIdentifier, String reason) {
		testResult = TestResult.skipped(reason);
	}

	public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
		TestExecutionResult.Status status = testExecutionResult.getStatus();
		if (TEST == testIdentifier.getType()) {
			if (status == TestExecutionResult.Status.FAILED) {
				testResult = TestResult.failed(testExecutionResult.getThrowable().orElse(null));

			} else {
//			State.caughtTestException(null);
				testResult = TestResult.passed();
			}
		}
	}

	public TestResult getTestResult() {
		return testResult;
	}
}
