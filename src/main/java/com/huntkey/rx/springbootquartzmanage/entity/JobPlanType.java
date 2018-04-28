package com.huntkey.rx.springbootquartzmanage.entity;

/**
 * Created by sunwei on 2018-02-27 Time:14:35:39
 */
public enum JobPlanType {

    executeOnce("execute_once", "执行一次"),

    executeRepeat("execute_repeat", "重复执行");

    /**
     * 计划执行类型 重复 一次
     */
    String jobPlanType;

    /**
     * 计划执行类型描述
     */
    String jobPlanTypeDesc;

    JobPlanType(String jobPlanType, String jobPlanTypeDesc) {
        this.jobPlanType = jobPlanType;
        this.jobPlanTypeDesc = jobPlanTypeDesc;
    }

    public String getJobPlanType() {
        return jobPlanType;
    }

    public void setJobPlanType(String jobPlanType) {
        this.jobPlanType = jobPlanType;
    }

    public String getJobPlanTypeDesc() {
        return jobPlanTypeDesc;
    }

    public void setJobPlanTypeDesc(String jobPlanTypeDesc) {
        this.jobPlanTypeDesc = jobPlanTypeDesc;
    }

    @Override
    public String toString() {
        return "JobPlanType{" +
                "jobPlanType='" + jobPlanType + '\'' +
                ", jobPlanTypeDesc='" + jobPlanTypeDesc + '\'' +
                '}';
    }
}
