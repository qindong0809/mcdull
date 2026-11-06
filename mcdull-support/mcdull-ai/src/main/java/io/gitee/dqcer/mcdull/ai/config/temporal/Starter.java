package io.gitee.dqcer.mcdull.ai.config.temporal;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class Starter {

    public static void main(String[] args) {
        WorkflowServiceStubs serviceStubs = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient workflowClient = WorkflowClient.newInstance(serviceStubs);

        SayHelloWorkflow sayHelloWorkflow = workflowClient.newWorkflowStub(SayHelloWorkflow.class, WorkflowOptions.newBuilder()
            .setTaskQueue("my-task-queue").setWorkflowId("sayHello-workflow-id").build());
        String terry = sayHelloWorkflow.sayHello("terry");
        System.out.println(terry);
    }
}
