package io.github.zforgo.arquillian.junit5;

import io.github.zforgo.arquillian.junit5.extension.RunModeEvent;
import org.jboss.arquillian.test.spi.LifecycleMethodExecutor;
import org.jboss.arquillian.test.spi.NoMethodExecutor;
import org.jboss.arquillian.test.spi.TestMethodExecutor;
import org.jboss.arquillian.test.spi.TestResult;
import org.jboss.arquillian.test.spi.TestRunnerAdaptor;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.platform.commons.JUnitException;

import java.lang.reflect.Method;

public class ArquillianExtension implements BeforeAllCallback, BeforeEachCallback, AfterTestExecutionCallback, InvocationInterceptor, AfterEachCallback, AfterAllCallback, TestExecutionExceptionHandler {
	private static final String CHAIN_EXCEPTION_MESSAGE_PREFIX = "Chain of InvocationInterceptors never called invocation";

	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		new JUnitJupiterTestClassLifecycleManager(extensionContext).beforeTestClassPhase(
				extensionContext.getRequiredTestClass());
	}

	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception {
		beforeEachTestMethod(new Storage(extensionContext).getAdaptor(), extensionContext.getRequiredTestInstance(),
				extensionContext.getRequiredTestMethod());
	}

	private void beforeEachTestMethod(TestRunnerAdaptor adaptor, Object testInstance, Method testMethod) throws Exception {
		adaptor.before(testInstance, testMethod, LifecycleMethodExecutor.NO_OP);
	}

	@Override
	public void interceptTestTemplateMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		doTEst(extensionContext, invocation);
	}

	@Override
	public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		doTEst(extensionContext, invocation);
	}

	private void doTEst(ExtensionContext extensionContext, Invocation<Void> invocation) throws Throwable {
		Storage storage = new Storage(extensionContext);

		TestMethodExecutor executor = new NoMethodExecutor(extensionContext.getRequiredTestMethod(), extensionContext.getRequiredTestInstance());
		storage.storeTestResult(executor.getMethod(), TestResult.notRun());
		storage.getAdaptor().test(executor);

		RunModeEvent event = new RunModeEvent(extensionContext.getRequiredTestClass(), extensionContext.getRequiredTestMethod());
		storage.getAdaptor().fire(event);
//		if (event.isRunAsClient()) {
//			invocation.proceed();
//		}
	}


	@Override
	public void afterTestExecution(ExtensionContext extensionContext) {
		Storage storage = new Storage(extensionContext);
		TestResult result = storage.getTestResult(extensionContext.getRequiredTestMethod());
		result.setEnd(System.currentTimeMillis());
		storage.storeTestResult(extensionContext.getRequiredTestMethod(), result);
	}

	@Override
	public void afterEach(ExtensionContext extensionContext) throws Exception {
		new Storage(extensionContext).getAdaptor()
				.after(
						extensionContext.getRequiredTestInstance(),
						extensionContext.getRequiredTestMethod(),
						LifecycleMethodExecutor.NO_OP);
	}

	@Override
	public void afterAll(ExtensionContext extensionContext) throws Exception {
		new JUnitJupiterTestClassLifecycleManager(extensionContext).afterTestClassPhase(
				extensionContext.getRequiredTestClass());
	}

	@Override
	public void handleTestExecutionException(ExtensionContext extensionContext, Throwable throwable) throws Throwable {
		if (throwable instanceof JUnitException && throwable.getMessage().startsWith(CHAIN_EXCEPTION_MESSAGE_PREFIX)) {
			return;
		}
		throw throwable;
	}


}
