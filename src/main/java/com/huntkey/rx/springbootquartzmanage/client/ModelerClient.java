package com.huntkey.rx.springbootquartzmanage.client;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.rest.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangyu
 */
//开发调试可以指定URL，不允许提交到GIT
@Component
@FeignClient(value = "modeler-provider")
public interface ModelerClient {

    /**
     * 获取编号方法
     *
     * @param object {"edmnEncode":"HR01","edmnType":"1"}
     * @return
     */
    @RequestMapping(value = "/numbers", method = RequestMethod.POST)
    Result getNumbers(@RequestBody JSONObject object);

    /**
     * 根据edm方法id，获取具体方法参数信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/methods/{id}", method = RequestMethod.GET)
    Result getMethodById(@PathVariable(value = "id") String id);

    /**
     * 获取计划具体信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/jobs/plan/{id}", method = RequestMethod.GET)
    Result getJobPlan(@PathVariable(value = "id") String id);

    /**
     * 通过方法id恢复或删除方法
     *
     * @param id
     * @param isDel
     * @return
     */
    @RequestMapping(value = "/methods/updateEdmmIsDel", method = RequestMethod.PUT)
    public Result delAndRecover(@RequestParam(value = "id") String id,
                                @RequestParam(value = "isDel") String isDel);

    /**
     * 根据方法Id查询该方法所具有的计划,包含分页
     *
     * @param methodId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/jobs/planByMethodId/{methodId}", method = RequestMethod.GET)
    Result planByMethodId(@PathVariable(value = "methodId") String methodId, @RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize);

    /**
     * @param MethodId
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 根据计划Id删除EDM中计划表里的计划信息
     * @method jobs
     */
    @RequestMapping(value = "/jobs/{planId}", method = RequestMethod.DELETE)
    Result jobs(@PathVariable(value = "planId") String MethodId);

    /**
     * 根据方法Id查询该方法所具有的计划,不分页
     *
     * @param methodId
     * @return
     */
    @RequestMapping(value = "/jobs/getAllPlansByMethodId/{methodId}", method = RequestMethod.GET)
    Result getAllPlansByMethodId(@PathVariable(value = "methodId") String methodId);

}