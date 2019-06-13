package io.github.zforgo.arquillian.junit5.extension;

import io.github.zforgo.arquillian.junit5.State;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.test.spi.TestResult;
import org.jboss.arquillian.test.spi.TestResult.Status;
import org.jboss.arquillian.test.spi.annotation.TestScoped;
import org.jboss.arquillian.test.spi.event.suite.AfterTestLifecycleEvent;

class UpdateTestResultBeforeAfter {

    @Inject
    @TestScoped
    private InstanceProducer<TestResult> testResult;

    public void update(@Observes(precedence = 99) EventContext<AfterTestLifecycleEvent> context, TestResult result) {
        if (State.caughtExceptionAfterJunit() != null) {
            result.setStatus(Status.FAILED);
            result.setThrowable(State.caughtExceptionAfterJunit());
        } else {
            result.setStatus(Status.PASSED);
            result.setThrowable(null);
        }
        context.proceed();
    }
}
