package com.huntkey.rx.springbootquartzmanage.service.impl;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.springbootquartzmanage.service.BaseJob;
import com.huntkey.rx.springbootquartzmanage.utils.RestUtil;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by sunwei on 2018-02-24 Time:9:12:25
 */
@Service
public class BaseJobImpl implements BaseJob {

    private static final Logger logger = LoggerFactory.getLogger(BaseJobImpl.class);

    private static String timeOut = "20000000";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String execUrl = String.valueOf(jobDataMap.get("execUrl"));
        String reqType = String.valueOf(jobDataMap.get("reqType"));
        Object params = jobDataMap.get("params");
        Result result = execTimingMethod(execUrl, reqType, params);
        logger.info("执行结果为" + result);
        logger.info("execUrl为****************************" + execUrl);
        logger.info("reqType为****************************" + reqType);
    }

    public Result execTimingMethod(String execUrl, String reqType, Object params) {
        Result result;
        try {
            result = RestUtil.doExec(execUrl, reqType, params, null, Integer.valueOf(timeOut));
        } catch (Exception e) {
            logger.error("方法执行出错", e);
            throw new RuntimeException(e);
        }
        return result;
    }
}
