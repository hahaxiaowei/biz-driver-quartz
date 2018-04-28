package com.huntkey.rx.springbootquartzmanage.client;

import com.huntkey.rx.commons.utils.rest.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by sunwei on 2018-04-26 Time:17:14:35
 */
@Component
@FeignClient(value = "modeler-client")
public interface ModelerProvider {

    /**
     * @param edmmType
     * @param edmmProgramType
     * @param edmmName
     * @param edmmClasses
     * @param edmmStatus
     * @param pageNum
     * @param pageSize
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 根据方法参数查询方法信息，支持多参数查询
     * @method getMethods
     */
    @RequestMapping(value = "/v1/methods", method = RequestMethod.GET)
    Result getMethods(@RequestParam(required = false, value = "edmmType") String edmmType,
                      @RequestParam(required = false, value = "edmmProgramType") String edmmProgramType,
                      @RequestParam(required = false, value = "edmmName") String edmmName,
                      @RequestParam(required = false, value = "edmmClasses") String edmmClasses,
                      @RequestParam(required = false, value = "edmmStatus") String edmmStatus,
                      @RequestParam(defaultValue = "1", value = "pageNum") int pageNum,
                      @RequestParam(defaultValue = "10", value = "pageSize") int pageSize);

    /**
     * @param id
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 根据方法id删除方法信息
     * @method delete
     */
    @RequestMapping(value = "/v1/methods/{id}", method = RequestMethod.DELETE)
    Result delete(@PathVariable(value = "id") String id);
}
