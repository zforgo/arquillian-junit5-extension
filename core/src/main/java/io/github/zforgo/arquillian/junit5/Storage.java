package io.github.zforgo.arquillian.junit5;

import org.jboss.arquillian.test.spi.TestResult;
import org.jboss.arquillian.test.spi.TestRunnerAdaptor;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

public class Storage {

    private static final String ARQUILLIAN_STORE_NAMESPACE = "arquillianJUnit5StoreNamespace";
    private static final String TEST_RUNNER_ADAPTOR_KEY = "testRunnerAdaptorKey";
    private static final String TEST_RESULT_KEY = "testResultKey";

    private ExtensionContext.Store store;

    Storage(ExtensionContext extensionContext) {
        ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(ARQUILLIAN_STORE_NAMESPACE);
        store = extensionContext.getStore(namespace);
    }

    void storeAdaptor(TestRunnerAdaptor testRunnerAdaptor) {
        store.put(TEST_RUNNER_ADAPTOR_KEY, testRunnerAdaptor);
    }

    TestRunnerAdaptor getAdaptor() {
        return store.get(TEST_RUNNER_ADAPTOR_KEY, TestRunnerAdaptor.class);
    }

    void storeTestResult(Method requiredTestMethod, TestResult testResult) {
        store.put(TEST_RESULT_KEY + requiredTestMethod, testResult);
    }

    TestResult getTestResult(Method requiredTestMethod) {
        TestResult testResult = store.get(TEST_RESULT_KEY + requiredTestMethod, TestResult.class);
        return testResult;
    }

    ExtensionContext.Store getStore() {
        return store;
    }
}
