package io.gitee.dqcer.mcdull.ai.config.temporal;

public class GreetActivitiesImpl implements GreetActivities{
    @Override
    public String greet(String name) {
        return "Hello " + name + "!";
    }
}
