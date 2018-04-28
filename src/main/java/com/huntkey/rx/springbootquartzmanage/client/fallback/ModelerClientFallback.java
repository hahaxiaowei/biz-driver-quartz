package com.huntkey.rx.springbootquartzmanage.client.fallback;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.springbootquartzmanage.client.ModelerClient;
import org.springframework.stereotype.Component;

/**
 * @author zhangyu
 */
@Component
public class ModelerClientFallback implements ModelerClient {

    @Override
    public Result getNumbers(JSONObject object) {
        return getResult();
    }

    private Result getResult() {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用Modeler系统接口服务降级处理！");
        return result;
    }

    @Override
    public Result getMethodById(String id) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用Modeler系统接口服务降级处理！");
        return result;
    }

    @Override
    public Result getJobPlan(String id) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用Modeler系统接口服务降级处理！");
        return result;
    }

    @Override
    public Result delAndRecover(String id, String isDel) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用Modeler系统接口服务降级处理！");
        return result;
    }

    @Override
    public Result planByMethodId(String methodId, int pageNum, int pageSize) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用Modeler系统接口服务降级处理！");
        return result;
    }

    @Override
    public Result jobs(String MethodId) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用Modeler系统接口服务降级处理！");
        return result;
    }

    @Override
    public Result getAllPlansByMethodId(String methodId) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用Modeler系统接口服务降级处理！");
        return result;
    }
}

