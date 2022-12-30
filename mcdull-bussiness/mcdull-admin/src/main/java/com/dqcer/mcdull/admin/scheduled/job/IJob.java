package com.dqcer.mcdull.admin.scheduled.job;


/**
 * job interface
 *
 * @author dqcer
 * @version 2022/12/30
 */
public abstract class IJob implements Runnable {

    /**
     * execute job
     */
    abstract void execute();


    @Override
    public void run() {
        execute();
    }
}
