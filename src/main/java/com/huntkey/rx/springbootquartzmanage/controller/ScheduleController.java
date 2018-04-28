package com.huntkey.rx.springbootquartzmanage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.method.register.plugin.annotation.MethodRegister;
import com.huntkey.rx.sceo.method.register.plugin.entity.MethodExeFrequency;
import com.huntkey.rx.sceo.method.register.plugin.entity.ProgramCate;
import com.huntkey.rx.springbootquartzmanage.entity.JobbaseinfoDto;
import com.huntkey.rx.springbootquartzmanage.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/schedule")
public class ScheduleController {

    private Logger logger = LoggerFactory.getLogger(ScheduleController.class);
    @Autowired
    private ScheduleService scheduleService;


    @MethodRegister(
            edmClass = "jobbaseinfo",
            methodCate = "jobbaseinfo",
            programCate = ProgramCate.Java,
            methodExeFrequency = MethodExeFrequency.Loop,
            methodExeInterval = 10,
            methodDesc = "计划信息类查询方法",
            getReqParamsNameNoPathVariable = {"params"})
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public Result jbfoQuery(@RequestBody JSONObject params) throws Exception {
        return scheduleService.queryOrderList(params);
    }

    @MethodRegister(
            edmClass = "jobbaseinfo",
            methodCate = "jobbaseinfo",
            programCate = ProgramCate.Java,
            methodExeFrequency = MethodExeFrequency.Loop,
            methodExeInterval = 10,
            methodDesc = "计划信息类查询方法",
            getReqParamsNameNoPathVariable = {"id"})
    @RequestMapping(value = "/load", method = RequestMethod.GET)
    public Result jbfoLoadAddOrder(@RequestParam String id) throws Exception {
        return scheduleService.loadOrder(id);
    }

    @MethodRegister(
            edmClass = "jobbaseinfo",
            methodCate = "jobbaseinfo",
            programCate = ProgramCate.Java,
            methodExeFrequency = MethodExeFrequency.Loop,
            methodExeInterval = 10,
            methodDesc = "计划信息类保存方法",
            getReqParamsNameNoPathVariable = {"params"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result jbfoSaveScheduleOrder(@RequestBody JobbaseinfoDto params) throws Exception {

        return scheduleService.saveOrder(JSONObject.parseObject(JSON.toJSONString(params)));
    }

    @MethodRegister(
            edmClass = "jobbaseinfo",
            methodCate = "jobbaseinfo",
            programCate = ProgramCate.Java,
            methodExeFrequency = MethodExeFrequency.Loop,
            methodExeInterval = 10,
            methodDesc = "计划信息类提交方法",
            getReqParamsNameNoPathVariable = {"params"})
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Result jbfoSubmit(@RequestBody JobbaseinfoDto params) throws Exception {
        return scheduleService.submitOrder(JSONObject.parseObject(JSON.toJSONString(params)));
    }

    @MethodRegister(
            edmClass = "jobbaseinfo",
            methodCate = "jobbaseinfo",
            programCate = ProgramCate.Java,
            methodExeFrequency = MethodExeFrequency.Loop,
            methodExeInterval = 10,
            methodDesc = "计划信息类批准通过方法",
            getReqParamsNameNoPathVariable = {"orderInstanceId", "handlerType"})
    @RequestMapping(value = "/passOrder", method = RequestMethod.GET)
    public Result approve(@RequestParam(value = "orderInstanceId") String orderInstanceId,
                          @RequestParam(value = "handlerType") String handlerType) throws Exception {
        return scheduleService.passOrder(orderInstanceId, handlerType);
    }

    @MethodRegister(
            edmClass = "jobbaseinfo",
            methodCate = "jobbaseinfo",
            programCate = ProgramCate.Java,
            methodExeFrequency = MethodExeFrequency.Loop,
            methodExeInterval = 10,
            methodDesc = "计划信息类审批方法",
            getReqParamsNameNoPathVariable = {"auditSet"})
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    public Result audit(@RequestBody JSONObject auditSet) throws Exception {
        return scheduleService.auditOrder(auditSet);
    }

    @MethodRegister(
            edmClass = "jobbaseinfo",
            methodCate = "jobbaseinfo",
            programCate = ProgramCate.Java,
            methodExeFrequency = MethodExeFrequency.Loop,
            methodExeInterval = 10,
            methodDesc = "计划信息类删除方法"
    )
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    public Result jbfoRemove(@PathVariable(value = "id") String id) throws Exception {
        return scheduleService.remove(id);
    }

    @MethodRegister(
            edmClass = "jobbaseinfo",
            methodCate = "jobbaseinfo",
            programCate = ProgramCate.Java,
            methodExeFrequency = MethodExeFrequency.Loop,
            methodExeInterval = 10,
            methodDesc = "保存任务时判断方法名唯一性",
            getReqParamsNameNoPathVariable = {"params"})
    @RequestMapping(value = "/doVaild", method = RequestMethod.POST)
    public Result doVaildLogicController(@RequestBody JSONObject params) {
        return scheduleService.doVaild(params);
    }

    @MethodRegister(
            edmClass = "jobbaseInfo",
            methodCate = "jobbaseinfo",
            programCate = ProgramCate.Java,
            methodDesc = "根据方法id查询该方法对应的计划",
            getReqParamsNameNoPathVariable = {"methodId", "pageNum", "pageSize"}
    )
    @RequestMapping(value = "/selectPlanByMethodId", method = RequestMethod.GET)
    public Result selectPlanByMethodId(@RequestParam(value = "methodId") String methodId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "15") int pageSize) {
        Result result = scheduleService.selectPlanByMethodId(methodId, pageNum, pageSize);
        return result;
    }


    @MethodRegister(
            edmClass = "jobbaseInfo",
            methodCate = "jobbaseinfo",
            programCate = ProgramCate.Java,
            methodDesc = "根据计划id删除计划"
    )
    @RequestMapping(value = "/deletePlanById/{planId}", method = RequestMethod.DELETE)
    public Result deletePlanById(@PathVariable(value = "planId") String planId) {
        Result result;
        result = scheduleService.deletePlanById(planId);
        return result;
    }

    @MethodRegister(
            edmClass = "jobbaseInfo",
            methodCate = "jobbaseinfo",
            programCate = ProgramCate.Java,
            methodDesc = "查询单据名称是否存在"
    )
    @RequestMapping(value = "/isJobNameExist/{jobName}", method = RequestMethod.GET)
    public Result isJobNameExist(@PathVariable(value = "jobName") String jobName) {
        Result result = new Result();
        try {
            Boolean tag = scheduleService.isJobNameExist(jobName);
            result.setData(tag);
        } catch (Exception e) {
            logger.error("isJobNameExist方法执行出错", e);
            result.setErrMsg("isJobNameExist方法执行异常");
            result.setRetCode(Result.RECODE_ERROR);
            throw new RuntimeException(e);
        }
        return result;
    }

    @MethodRegister(
            edmClass = "jobbaseInfo",
            methodCate = "jobbaseinfo",
            programCate = ProgramCate.Java,
            methodDesc = "分页查询所有方法信息",
            getReqParamsNameNoPathVariable = {"pageNum", "pageSize"}
    )
    @RequestMapping(value = "/getAllMethods", method = RequestMethod.GET)
    public Result getAllMethods(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
        return scheduleService.getAllMethods(pageNum, pageSize);
    }
}
