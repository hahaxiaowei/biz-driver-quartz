package com.huntkey.rx.springbootquartzmanage.service;

import com.github.pagehelper.PageInfo;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.springbootquartzmanage.entity.JobAndTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * Created by sunwei on 2018-02-02 Time:14:33:47
 */
public interface IJobAndTriggerService {
    PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize);

    List<JobAndTrigger> queryAllJobs();

    JobAndTrigger selectJobByGroupName(String jobGroupName);

    Result deleteJob(String jobGroupName, Scheduler scheduler, String jobClassName) throws SchedulerException;
}
