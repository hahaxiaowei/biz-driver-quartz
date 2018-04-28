package com.huntkey.rx.springbootquartzmanage.entity;

public class JobbaseinfoDto {

    private String id;

    /**
     * 任务名
     */
    private String jbfoName;


    /**
     * 任务英文名
     */
    private String jbfoEngName;

    /**
     * 任务描述
     */
    private String jbfoDescribe;

    /**
     * 任务类型
     * 1    联动卷积
     * 2    多维交叉
     * 3    工作流
     * 4    EDM定时方法
     */
    private String jbfoType;

    /**
     * 是否启用
     */
    private String jbfoIsUse;

    /**
     * 是否创建
     */
    private String jbfoIsPlan;

    /**
     * 分组名
     */
    private String jbfoGroupName;

    /**
     * 状态
     * 1   草稿
     * 2   审批中
     * 3   审批成功
     * 4   审批失败
     */
    private String jbfoStatus;

    /**
     * 单据定义id
     */
    private String ordeRodeObj;
    /**
     * 制单人id
     */
    private String ordeAdduser;
    /**
     * 制单岗位id
     */
    private String ordeDuty;
    /**
     * 制单部门id
     */
    private String ordeDept;

    /**
     * edm信息id
     */
    private String jbfoEdmId;

    public String getJbfoEdmId() {
        return jbfoEdmId;
    }

    public void setJbfoEdmId(String jbfoEdmId) {
        this.jbfoEdmId = jbfoEdmId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getJbfoName() {
        return jbfoName;
    }

    public void setJbfoName(String jbfoName) {
        this.jbfoName = jbfoName;
    }

    public String getJbfoEngName() {
        return jbfoEngName;
    }

    public void setJbfoEngName(String jbfoEngName) {
        this.jbfoEngName = jbfoEngName;
    }

    public String getJbfoDescribe() {
        return jbfoDescribe;
    }

    public void setJbfoDescribe(String jbfoDescribe) {
        this.jbfoDescribe = jbfoDescribe;
    }

    public String getJbfoType() {
        return jbfoType;
    }

    public void setJbfoType(String jbfoType) {
        this.jbfoType = jbfoType;
    }

    public String getJbfoIsUse() {
        return jbfoIsUse;
    }

    public void setJbfoIsUse(String jbfoIsUse) {
        this.jbfoIsUse = jbfoIsUse;
    }

    public String getJbfoIsPlan() {
        return jbfoIsPlan;
    }

    public void setJbfoIsPlan(String jbfoIsPlan) {
        this.jbfoIsPlan = jbfoIsPlan;
    }

    public String getJbfoGroupName() {
        return jbfoGroupName;
    }

    public void setJbfoGroupName(String jbfoGroupName) {
        this.jbfoGroupName = jbfoGroupName;
    }

    public String getJbfoStatus() {
        return jbfoStatus;
    }

    public void setJbfoStatus(String jbfoStatus) {
        this.jbfoStatus = jbfoStatus;
    }

    public String getOrdeRodeObj() {
        return ordeRodeObj;
    }

    public void setOrdeRodeObj(String ordeRodeObj) {
        this.ordeRodeObj = ordeRodeObj;
    }

    public String getOrdeAdduser() {
        return ordeAdduser;
    }

    public void setOrdeAdduser(String ordeAdduser) {
        this.ordeAdduser = ordeAdduser;
    }

    public String getOrdeDuty() {
        return ordeDuty;
    }

    public void setOrdeDuty(String ordeDuty) {
        this.ordeDuty = ordeDuty;
    }

    public String getOrdeDept() {
        return ordeDept;
    }

    public void setOrdeDept(String ordeDept) {
        this.ordeDept = ordeDept;
    }
}
