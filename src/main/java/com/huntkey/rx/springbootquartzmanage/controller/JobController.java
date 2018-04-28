package com.huntkey.rx.springbootquartzmanage.controller;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.springbootquartzmanage.client.ModelerClient;
import com.huntkey.rx.springbootquartzmanage.entity.JobAndTrigger;
import com.huntkey.rx.springbootquartzmanage.entity.JobInfo;
import com.huntkey.rx.springbootquartzmanage.entity.JobPlanType;
import com.huntkey.rx.springbootquartzmanage.service.BaseJob;
import com.huntkey.rx.springbootquartzmanage.service.IJobAndTriggerService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/job")
public class JobController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private IJobAndTriggerService iJobAndTriggerService;

    //加入Qulifier注解，通过名称注入bean
    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;

    private String jobClassName = "com.huntkey.rx.springbootquartzmanage.service.impl.BaseJobImpl";

    @Autowired
    private ModelerClient modelerClient;

    /**
     * @param jobInfo
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 添加计划
     * @method addjob
     */
    @RequestMapping(value = "/addJob", method = RequestMethod.POST)
    public Result addJob(@RequestBody JobInfo jobInfo) throws Exception {
        Result result = new Result();
        logger.info("计划分组为：" + jobInfo.getJobGroupName() + "cronTrigger表达式为：" + jobInfo.getCronExpression());
        //如果该计划没有添加过，直接添加计划
        if (StringUtil.isNullOrEmpty(jobInfo.getJobGroupName())) {
            result = addJobLogic(jobInfo);
            logger.info("计划没有添加过，直接添加进去计划，添加结果为：" + result);
            return result;
        }
        //如果计划已存在，先删除计划，再将当前计划添加进去
        Result deleteResult = deleteJob(jobInfo.getJobGroupName());
        logger.info("删除已有的计划,删除结果为：" + deleteResult);
        result = addJobLogic(jobInfo);
        return result;
    }


    /**
     * @param jobInfo
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description
     * @method addJob
     */
    public Result addJobLogic(JobInfo jobInfo) throws Exception {

        Result result = new Result();
        try {
            if (StringUtil.isNullOrEmpty(jobInfo.getJobPlanStartDate())) {
                result.setErrMsg("请设置计划开始时间");
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }
            // 启动调度器
            scheduler.start();

            //构建job信息
            JobDetail jobDetail = JobBuilder
                    .newJob(getClass(jobClassName).getClass())
                    .withIdentity(jobClassName, jobInfo.getJobGroupName())
                    .build();
            jobDetail.getJobDataMap().put("execUrl", jobInfo.getEdmmArithmeticDesc());
            jobDetail.getJobDataMap().put("reqType", jobInfo.getEdmmRequestType());
            jobDetail.getJobDataMap().put("params", jobInfo.getParams());

            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression());

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = addTriggerConfig(jobInfo, scheduleBuilder);

            Date date = scheduler.scheduleJob(jobDetail, trigger);
            result.setData(date.toString());
            result.setRetCode(Result.RECODE_SUCCESS);
            logger.info("添加计划成功！execUrl：" + jobInfo.getEdmmArithmeticDesc() + ",jobGroupName" + jobInfo.getJobGroupName() + ",reqType:" + jobInfo.getEdmmRequestType() + ",cronExpression:" + jobInfo.getCronExpression());
        } catch (SchedulerException e) {
            result.setErrMsg("添加计划失败，时间设置错误，永远不会触发");
            result.setRetCode(Result.RECODE_ERROR);
            logger.error("添加计划失败，时间设置错误，永远不会触发", e);
        } catch (Exception e) {
            result.setErrMsg("添加计划失败");
            result.setRetCode(Result.RECODE_ERROR);
            logger.error("添加计划失败", e);
            throw new RuntimeException(e);
        }
        return result;
    }


    public CronTrigger addTriggerConfig(JobInfo jobInfo, CronScheduleBuilder scheduleBuilder) {

        CronTrigger trigger;
        try {
            //如果是执行一次，则trigger不需要设置计划开始时间和结束时间
            if (JobPlanType.executeOnce.getJobPlanType().equals(jobInfo.getJobPlanType())) {
                trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(jobClassName, jobInfo.getJobGroupName())
                        .withSchedule(scheduleBuilder)
                        .build();
                return trigger;
            }
            //如果是执行多次，先判断执行多次有没有配置计划结束时间，如果计划结束时间也为空，则只需要配置计划开始时间
            if (StringUtil.isNullOrEmpty(jobInfo.getJobPlanEndDate())) {
                trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(jobClassName, jobInfo.getJobGroupName())
                        .withSchedule(scheduleBuilder)
                        .startAt(jobInfo.getJobPlanStartDate())
                        .build();
                return trigger;
            }
            //如果计划开始时间和结束时间都设置了，那么两者都需要配置
            trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobClassName, jobInfo.getJobGroupName())
                    .withSchedule(scheduleBuilder)
                    .startAt(jobInfo.getJobPlanStartDate())
                    .endAt(jobInfo.getJobPlanEndDate())
                    .build();
        } catch (Exception e) {
            logger.error("addTriggerConfig方法执行出错", e);
            throw new RuntimeException(e);
        }
        return trigger;
    }

    /**
     * @param jobGroupName
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 暂停计划
     * @method pausejob
     */
    @RequestMapping(value = "/pauseJob", method = RequestMethod.GET)
    public Result pauseJob(@RequestParam("jobGroupName") String jobGroupName) throws Exception {

        Result result = new Result();
        try {
            scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
            logger.info("暂停计划--" + "暂停计划分组为：" + jobGroupName);
            result.setRetCode(Result.RECODE_SUCCESS);
        } catch (Exception e) {
            result.setErrMsg("方法执行出错" + e);
            result.setRetCode(Result.RECODE_ERROR);
            logger.error("pausejob方法执行出错", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * @param jobGroupName
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 回复暂停的计划
     * @method resumejob
     */
    @RequestMapping(value = "/resumeJob", method = RequestMethod.GET)
    public Result resumeJob(@RequestParam("jobGroupName") String jobGroupName) throws Exception {

        Result result = new Result();
        try {
            scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
            logger.info("恢复计划--" + "计划分组为：" + jobGroupName);
        } catch (Exception e) {
            result.setErrMsg("方法执行出错" + e);
            result.setRetCode(Result.RECODE_ERROR);
            logger.error("resumejob方法执行出错", e);
            throw new RuntimeException(e);
        }
        return result;
    }
//
//    /**
//     * @param jobInfo
//     * @return com.huntkey.rx.commons.utils.rest.Result
//     * @description 更新计划
//     * @method rescheduleJob
//     */
//    @RequestMapping(value = "/rescheduleJob", method = RequestMethod.POST)
//    public Result rescheduleJob(@RequestBody JobInfo jobInfo) throws Exception {
//
//        Result result = new Result();
//        try {
//            if (StringUtil.isNullOrEmpty(selectJobByGroupName(jobInfo.getJobGroupName()))) {
//                result.setErrMsg("该分组计划不存在");
//                result.setRetCode(Result.RECODE_ERROR);
//                return result;
//            }
//            Result deleteResult = deleteJob(jobInfo.getJobGroupName());
//            logger.info("更新前删除该计划删除结果为：" + deleteResult);
//            result = addjob(jobInfo);
//        } catch (SchedulerException e) {
//            result.setErrMsg("方法执行出错");
//            result.setRetCode(Result.RECODE_ERROR);
//            logger.info("rescheduleJob方法执行出错" + e);
//            throw new Exception("更新定时任务失败");
//        }
//        return result;
//    }
//
//
//    public Result updateJobPlan(@RequestBody JobInfo jobInfo) throws Exception {
//        Result result = new Result();
//        try {
//            if (StringUtil.isNullOrEmpty(selectJobByGroupName(jobInfo.getJobGroupName()))) {
//                result.setErrMsg("该分组计划不存在");
//                result.setRetCode(Result.RECODE_ERROR);
//                return result;
//            }
//            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobInfo.getJobGroupName());
//            // 表达式调度构建器
//            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression());
//
//            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
//
//            // 按新的cronExpression表达式重新构建trigger
//            trigger = updateTriggerConfig(trigger, triggerKey, scheduleBuilder, jobInfo.getMethodInfo());
//
//            // 按新的trigger重新设置job执行
//            Date date = scheduler.rescheduleJob(triggerKey, trigger);
//            result.setData(date);
//        } catch (SchedulerException e) {
//            System.out.println("更新定时任务失败" + e);
//            throw new Exception("更新定时任务失败");
//        }
//        return result;
//    }
//
//    /**
//     * @param trigger
//     * @param triggerKey
//     * @param scheduleBuilder
//     * @param methodInfo
//     * @return org.quartz.CronTrigger
//     * @description 更新操作，计划配置
//     * @method updateTriggerConfig
//     */
//    public CronTrigger updateTriggerConfig(CronTrigger trigger, TriggerKey triggerKey, CronScheduleBuilder scheduleBuilder, MethodInfo methodInfo) {
//
//        CronTrigger updateTrigger;
//        try {
//            if (JobPlanType.executeOnce.getJobPlanType().equals(methodInfo.getPlanType())) {
//                updateTrigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
//                return updateTrigger;
//            }
//            if (StringUtil.isNullOrEmpty(methodInfo.getEndTime())) {
//                updateTrigger = trigger
//                        .getTriggerBuilder()
//                        .withIdentity(triggerKey)
//                        .withSchedule(scheduleBuilder)
//                        .startAt(methodInfo.getStartTime())
//                        .build();
//                return updateTrigger;
//            }
//            updateTrigger = trigger
//                    .getTriggerBuilder()
//                    .withIdentity(triggerKey)
//                    .withSchedule(scheduleBuilder)
//                    .startAt(methodInfo.getStartTime())
//                    .endAt(methodInfo.getEndTime())
//                    .build();
//        } catch (Exception e) {
//            logger.error("updateTriggerConfig方法执行出错", e);
//            throw new RuntimeException(e);
//        }
//        return updateTrigger;
//    }
//

    /**
     * @param jobGroupName
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 删除计划
     * @method deleteJob
     */
    @RequestMapping(value = "/deleteJob", method = RequestMethod.DELETE)
    public Result deleteJob(@RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {

        Result result = new Result();
        try {
            result = iJobAndTriggerService.deleteJob(jobGroupName, scheduler, jobClassName);
        } catch (Exception e) {
            result.setErrMsg("方法执行出错");
            result.setRetCode(Result.RECODE_ERROR);
            logger.error("deleteJob方法执行出错", e);
            throw new RuntimeException(e);
        }
        return result;
    }
//
//    /**
//     * @param pageNum
//     * @param pageSize
//     * @return java.util.Map<java.lang.String,java.lang.Object>
//     * @description 分页查询计划
//     * @method queryjob
//     */
//    @RequestMapping(value = "/queryjob", method = RequestMethod.GET)
//    public Map<String, Object> queryjob(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {
//
//        PageInfo<JobAndTrigger> jobAndTrigger = iJobAndTriggerService.getJobAndTriggerDetails(pageNum, pageSize);
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("JobAndTrigger", jobAndTrigger);
//        map.put("number", jobAndTrigger.getTotal());
//        return map;
//    }
//

    /**
     * @param
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 查询所有计划
     * @method queryAllJobs
     */
    @RequestMapping(value = "/queryAllJobs", method = RequestMethod.GET)
    public Result queryAllJobs() {

        Result result = new Result();
        try {
            List<JobAndTrigger> list = iJobAndTriggerService.queryAllJobs();
            result.setData(list);
        } catch (Exception e) {
            result.setErrMsg("方法执行出错");
            result.setRetCode(Result.RECODE_ERROR);
            logger.error("queryAllJobs方法执行出错", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * @param className
     * @return com.huntkey.rx.springbootquartzmanage.service.BaseJob
     * @description 利用反射机制，根据类名获取类
     * @method getClass
     */
    public static BaseJob getClass(String className) throws Exception {

        Class<?> class1 = Class.forName(className);
        return (BaseJob) class1.newInstance();
    }

    /**
     * @param jobGroupName
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 根据分组查询计划
     * @method selectJobByGroupName
     */
    @RequestMapping(value = "/selectJobByGroupName", method = RequestMethod.GET)
    public Result selectJobByGroupName(@RequestParam(value = "jobGroupName") String jobGroupName) {

        Result result = new Result();
        try {
            JobAndTrigger jobAndTrigger = iJobAndTriggerService.selectJobByGroupName(jobGroupName);
            if (StringUtil.isNullOrEmpty(jobAndTrigger)) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("该分组不存在");
                return result;
            }
            result.setData(jobAndTrigger);
        } catch (Exception e) {
            result.setErrMsg("方法执行出错");
            result.setRetCode(Result.RECODE_ERROR);
            logger.error("selectJobByGroupName方法执行出错", e);
            throw new RuntimeException(e);
        }
        return result;
    }
}
