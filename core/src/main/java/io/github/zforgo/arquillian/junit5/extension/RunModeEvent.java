package io.github.zforgo.arquillian.junit5.extension;

import org.jboss.arquillian.test.spi.LifecycleMethodExecutor;
import org.jboss.arquillian.test.spi.event.suite.ClassEvent;
import org.jboss.arquillian.test.spi.event.suite.TestEvent;
import org.jboss.arquillian.test.spi.event.suite.TestLifecycleEvent;

import java.lang.reflect.Method;

public class RunModeEvent extends TestLifecycleEvent {
	private boolean runAsClient = false;

	public RunModeEvent(Object testInstance, Method testMethod) {
		super(testInstance, testMethod);
	}

	public RunModeEvent(Object testInstance, Method testMethod, LifecycleMethodExecutor executor) {
		super(testInstance, testMethod, executor);
	}

	/*
		public RunModeEvent(Class<?> testClass, Method testMethod) {
			super(testClass);
			this.testMethod = testMethod;
		}
	*/
//
//	public Method getTestMethod() {
//		return testMethod;
//	}

	public void setRunAsClient(boolean runAsClient) {
		this.runAsClient = runAsClient;
	}

	public boolean isRunAsClient() {
		return runAsClient;
	}
}
