package io.github.zforgo.arquillian.junit5.extension;

import org.jboss.arquillian.test.spi.event.suite.ClassEvent;

import java.lang.reflect.Method;

public class RunModeEvent extends ClassEvent {
	private boolean runAsClient = false;
	private final Method testMethod;

	public RunModeEvent(Class<?> testClass, Method testMethod) {
		super(testClass);
		this.testMethod = testMethod;
	}

	public Method getTestMethod() {
		return testMethod;
	}

	public void setRunAsClient(boolean runAsClient) {
		this.runAsClient = runAsClient;
	}

	public boolean isRunAsClient() {
		return runAsClient;
	}
}
