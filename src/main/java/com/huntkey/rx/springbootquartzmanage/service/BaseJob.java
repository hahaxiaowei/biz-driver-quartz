package com.huntkey.rx.springbootquartzmanage.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by sunwei on 2018-02-24 Time:9:08:37
 */
public interface BaseJob extends Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException;
}
