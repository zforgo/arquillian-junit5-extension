package io.github.zforgo.arquillian.junit5;

import org.jboss.arquillian.test.spi.LifecycleMethodExecutor;

public abstract class ArquillianTestClassLifecycleManager extends AdaptorManager {

    protected void handleSuiteLevelFailure(Throwable initializationException) {
        throw new RuntimeException(
            "Arquillian initialization has already been attempted, but failed. See previous exceptions for cause",
            initializationException);
    }

    protected void handleBeforeSuiteFailure(Exception e) throws Exception {
        State.runnerFinished();
        if (State.isLastRunner()) {
            State.clean();
        }
        throw e;
    }


    public void beforeTestClassPhase(Class<?> testClass) throws Exception {
        beforeTestClassPhase(testClass, LifecycleMethodExecutor.NO_OP);
    }

    public void beforeTestClassPhase(Class<?> testClass, LifecycleMethodExecutor methodExecutor) throws Exception {
        State.runnerStarted();
        initializeAdaptor();

        // initialization ok, run children
        if (State.hasTestAdaptor()) {
            setAdaptor(State.getTestAdaptor());
        }

        getAdaptor().beforeClass(testClass, methodExecutor);
    }

    public void afterTestClassPhase(Class<?> testClass) throws Exception {
        getAdaptor().afterClass(testClass, LifecycleMethodExecutor.NO_OP);
        shutdown(getAdaptor());
    }
}
