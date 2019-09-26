package io.github.zforgo.arquillian.junit5;

import org.jboss.arquillian.test.spi.TestRunnerAdaptor;
import org.junit.jupiter.api.extension.ExtensionContext;

public class JUnitJupiterTestClassLifecycleManager extends ArquillianTestClassLifecycleManager {
	private static final String NAMESPACE_KEY = "arquillianNamespace";
	private static final String ADAPTOR_KEY = "testRunnerAdaptor";

	private ExtensionContext.Store store;

	public JUnitJupiterTestClassLifecycleManager(ExtensionContext context) {
		store = context.getStore(ExtensionContext.Namespace.create(NAMESPACE_KEY));
	}

	@Override
	protected void setAdaptor(TestRunnerAdaptor testRunnerAdaptor) {
		store.put(ADAPTOR_KEY, testRunnerAdaptor);
	}

	@Override
	protected TestRunnerAdaptor getAdaptor() {
		return store.get(ADAPTOR_KEY, TestRunnerAdaptor.class);
	}
}
