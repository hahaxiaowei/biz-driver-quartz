package com.huntkey.rx.springbootquartzmanage.client.fallback;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.springbootquartzmanage.client.ModelerProvider;
import org.springframework.stereotype.Component;

/**
 * Created by sunwei on 2018-04-26 Time:17:16:13
 */
@Component
public class ModelerProviderFallBack implements ModelerProvider {
    @Override
    public Result getMethods(String edmmType, String edmmProgramType, String edmmName, String edmmClasses, String edmmStatus, int pageNum, int pageSize) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用Modeler系统接口服务降级处理！");
        return result;
    }

    @Override
    public Result delete(String id) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用Modeler系统接口服务降级处理！");
        return result;
    }
}