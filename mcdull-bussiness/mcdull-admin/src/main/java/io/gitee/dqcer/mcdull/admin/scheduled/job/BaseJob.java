package io.gitee.dqcer.mcdull.admin.scheduled.job;


/**
 * job interface
 *
 * @author dqcer
 * @since 2022/12/30
 */
public abstract class BaseJob implements Runnable {

    /**
     * execute job
     */
    abstract void execute();


    @Override
    public void run() {
        execute();
    }
}
