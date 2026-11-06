package io.gitee.dqcer.mcdull.ai.config.temporal;

import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class SayHelloWorkflowImpl implements SayHelloWorkflow {

    private final GreetActivities greetActivities = Workflow.newActivityStub(GreetActivities.class, ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(5L)).build());
    @Override
    public String sayHello(String name) {
        return greetActivities.greet(name);
    }
}
