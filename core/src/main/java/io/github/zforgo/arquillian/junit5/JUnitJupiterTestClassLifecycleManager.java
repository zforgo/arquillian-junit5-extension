package io.github.zforgo.arquillian.junit5;

import org.jboss.arquillian.test.spi.TestRunnerAdaptor;
import org.junit.jupiter.api.extension.ExtensionContext;

public class JUnitJupiterTestClassLifecycleManager extends ArquillianTestClassLifecycleManager {

    private Storage storage;

    public JUnitJupiterTestClassLifecycleManager(ExtensionContext extensionContext) {
        storage = new Storage(extensionContext);
    }

    protected void setAdaptor(TestRunnerAdaptor testRunnerAdaptor) {
        storage.storeAdaptor(testRunnerAdaptor);
    }

    protected TestRunnerAdaptor getAdaptor() {
        return storage.getAdaptor();
    }
}
