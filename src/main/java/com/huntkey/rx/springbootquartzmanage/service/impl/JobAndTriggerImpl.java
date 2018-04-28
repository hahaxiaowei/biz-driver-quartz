package com.huntkey.rx.springbootquartzmanage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.springbootquartzmanage.dao.JobAndTriggerMapper;
import com.huntkey.rx.springbootquartzmanage.entity.JobAndTrigger;
import com.huntkey.rx.springbootquartzmanage.service.IJobAndTriggerService;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JobAndTriggerImpl implements IJobAndTriggerService {

    private Logger logger = LoggerFactory.getLogger(JobAndTriggerImpl.class);

    @Autowired
    private JobAndTriggerMapper jobAndTriggerMapper;

    @Override
    public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndTriggerDetails();
        PageInfo<JobAndTrigger> page = new PageInfo<JobAndTrigger>(list);
        return page;
    }

    @Override
    public List<JobAndTrigger> queryAllJobs() {
        List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndTriggerDetails();
        return list;
    }

    @Override
    public JobAndTrigger selectJobByGroupName(String jobGroupName) {

        return jobAndTriggerMapper.selectJobByGroupName(jobGroupName);
    }

    @Override
    public Result deleteJob(String jobGroupName, Scheduler scheduler, String jobClassName) throws SchedulerException {
        Result result = new Result();
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
        scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
        logger.info("删除计划--" + "删除计划分组为：" + jobGroupName);
        logger.info("deleteJob方法成功：" + jobGroupName);
        result.setRetCode(Result.RECODE_SUCCESS);
        return result;
    }

//    @Override
//    public TriggerInfo selectTriggerStateByTriggerGroup(String triggerGroupName) {
//        return jobAndTriggerMapper.selectTriggerStateByTriggerGroup(triggerGroupName);
//    }
}