package com.huntkey.rx.springbootquartzmanage.dao;

import com.huntkey.rx.springbootquartzmanage.entity.JobAndTrigger;

import java.util.List;

public interface JobAndTriggerMapper {
    /**
     * 查询所有计划信息
     *
     * @return
     */
    List<JobAndTrigger> getJobAndTriggerDetails();

    /**
     * @param jobGroupName
     * @return com.huntkey.rx.springbootquartzmanage.entity.JobAndTrigger
     * @description 根据计划组名查询计划信息
     * @method selectJobByGroupName
     */
    JobAndTrigger selectJobByGroupName(String jobGroupName);

}
