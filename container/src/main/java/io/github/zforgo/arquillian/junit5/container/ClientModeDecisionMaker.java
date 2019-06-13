package io.github.zforgo.arquillian.junit5.container;

import io.github.zforgo.arquillian.junit5.extension.RunModeEvent;
import org.jboss.arquillian.container.spi.client.deployment.Deployment;
import org.jboss.arquillian.container.test.impl.RunModeUtils;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.event.suite.Before;

class ClientModeDecisionMaker {

	@Inject
	private Instance<Deployment> deployment;
	private ThreadLocal<Boolean> isClientMode = ThreadLocal.withInitial(() -> Boolean.TRUE);

	public void enrich(@Observes(precedence = 99) Before event) throws Exception {
		isClientMode.set(RunModeUtils.isRunAsClient(deployment.get(), event.getTestClass(), event.getTestMethod()));
	}

	public void on(@Observes RunModeEvent event) throws Throwable {
		event.setRunAsClient(isClientMode.get());
	}
}
