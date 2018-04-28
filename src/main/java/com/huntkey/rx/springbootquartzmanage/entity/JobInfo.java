package com.huntkey.rx.springbootquartzmanage.entity;

import java.util.Date;

/**
 * Created by sunwei on 2018-02-08 Time:14:53:10
 */
public class JobInfo {

    /**
     * 计划id
     */
    private String jobPlanId;

    /**
     * 计划名字
     */
    private String jobPlanName;

    /**
     * 计划类型1.执行一次2.执行多次
     */
    private String jobPlanType;

    /**
     * 计划开始时间
     */
    private Date jobPlanStartDate;

    /**
     * 计划结束时间
     */
    private Date jobPlanEndDate;

    /**
     * 分组
     */
    private String jobGroupName;

    /**
     * 定时方法参数
     */
    private Object params;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 方法执行url
     */
    private String edmmArithmeticDesc;

    /**
     * 方法请求类型 get post delete put
     */
    private String edmmRequestType;

    public String getJobPlanId() {
        return jobPlanId;
    }

    public void setJobPlanId(String jobPlanId) {
        this.jobPlanId = jobPlanId;
    }

    public String getJobPlanName() {
        return jobPlanName;
    }

    public void setJobPlanName(String jobPlanName) {
        this.jobPlanName = jobPlanName;
    }

    public String getJobPlanType() {
        return jobPlanType;
    }

    public void setJobPlanType(String jobPlanType) {
        this.jobPlanType = jobPlanType;
    }

    public Date getJobPlanStartDate() {
        return jobPlanStartDate;
    }

    public void setJobPlanStartDate(Date jobPlanStartDate) {
        this.jobPlanStartDate = jobPlanStartDate;
    }

    public Date getJobPlanEndDate() {
        return jobPlanEndDate;
    }

    public void setJobPlanEndDate(Date jobPlanEndDate) {
        this.jobPlanEndDate = jobPlanEndDate;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getEdmmArithmeticDesc() {
        return edmmArithmeticDesc;
    }

    public void setEdmmArithmeticDesc(String edmmArithmeticDesc) {
        this.edmmArithmeticDesc = edmmArithmeticDesc;
    }

    public String getEdmmRequestType() {
        return edmmRequestType;
    }

    public void setEdmmRequestType(String edmmRequestType) {
        this.edmmRequestType = edmmRequestType;
    }
}
