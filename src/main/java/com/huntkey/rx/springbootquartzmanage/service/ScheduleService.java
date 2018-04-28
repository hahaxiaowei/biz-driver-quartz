package com.huntkey.rx.springbootquartzmanage.service;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.JobbaseinfoEntity;
import com.huntkey.rx.springbootquartzmanage.base.IBaseOrderService;


public interface ScheduleService extends IBaseOrderService<JobbaseinfoEntity> {
    Result remove(String id) throws Exception;

    Result doVaild(JSONObject params);

    Result selectPlanByMethodId(String methodId, int pageNum, int pageSize);

    Result deletePlanById(String planId);

    Boolean isJobNameExist(String jobName);

    Result getAllMethods(int pageNum, int pageSize);
}
