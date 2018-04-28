package com.huntkey.rx.springbootquartzmanage.entity;

import java.util.Date;

/**
 * Created by sunwei on 2018-02-24 Time:9:14:38
 */
public class MethodInfo {

    /**
     * 方法执行url
     */
    private String edmmArithmeticDesc;
    /**
     * 定时任务开始时间
     */
    private Date startTime;

    /**
     * 定时任务结束时间
     */
    private Date endTime;

    /**
     * 定时任务执行类型1.执行一次2.重复执行
     */
    private String planType;

    /**
     * 方法请求类型 get post delete put
     */
    private String edmmRequestType;

    public String getEdmmArithmeticDesc() {
        return edmmArithmeticDesc;
    }

    public void setEdmmArithmeticDesc(String edmmArithmeticDesc) {
        this.edmmArithmeticDesc = edmmArithmeticDesc;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getEdmmRequestType() {
        return edmmRequestType;
    }

    public void setEdmmRequestType(String edmmRequestType) {
        this.edmmRequestType = edmmRequestType;
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "edmmArithmeticDesc='" + edmmArithmeticDesc + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", planType='" + planType + '\'' +
                ", edmmRequestType='" + edmmRequestType + '\'' +
                '}';
    }
}
