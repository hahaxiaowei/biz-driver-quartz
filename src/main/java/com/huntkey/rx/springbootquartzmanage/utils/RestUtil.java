package com.huntkey.rx.springbootquartzmanage.utils;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lulx on 2017/10/14 0014 上午 10:37
 */
@Service
@Lazy(false)
public class RestUtil {
    private static Logger logger = LoggerFactory.getLogger(RestUtil.class);
    private static Pattern pa = Pattern.compile("\\{.*?\\}");

    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    RestTemplate restTemplate;

    private static RestTemplate templateHolder;

    private static final int CONNECTION_TIME_OUT = 5000;
    private static final int CONNECTION_REQUEST_TIME_OUT = 50000;
    private static HttpComponentsClientHttpRequestFactory requestFactory;

    @PostConstruct
    public void init() {
        if (requestFactory == null) {
            requestFactory = new HttpComponentsClientHttpRequestFactory();
        }
        restTemplate.setRequestFactory(requestFactory);
        templateHolder = restTemplate;
    }

    /**
     * @param timeOut
     * @return void
     * @description 设置连接超时时间和连接请求超时时间以及数据读取超时时间
     * @method timeOutConfiguration
     */
    public static void timeOutConfiguration(int timeOut) {
        requestFactory.setReadTimeout(timeOut);
        requestFactory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT);
        requestFactory.setConnectTimeout(CONNECTION_TIME_OUT);
    }


    public static Result doGet(String url, Map<String, Object> map, String auth, int timeOut, MultipartFile... file) {
        long start = System.currentTimeMillis();
        Result result = exchange(url, HttpMethod.GET, Result.class, map, map, auth, timeOut);
        //ResponseEntity<Result> responseEntity = templateHolder.getForEntity(url, Result.class, map);
        long end = System.currentTimeMillis();
        logger.info("[-doGet-] Spend time : {}", (end - start));
        return result;
    }

    /**
     * Send Post request
     *
     * @param url like http://HELLO-SERVICE/hello, HELLO-SERVICE can be discovered in Eureka.
     * @param obj
     * @param map
     * @return
     */
    public static Result doPost(String url, Object obj, Map<String, Object> map, String auth, int timeOut, MultipartFile... file) {
        timeOutConfiguration(timeOut);
        long start = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", auth);
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        HttpEntity<Object> formEntity = new HttpEntity<Object>(obj, headers);

        long end = System.currentTimeMillis();
        logger.info("[-doPost-] Spend time : {}", (end - start));

        if (null != file && file.length != 0 && null != file[0]) {
            File tempFile = null;
            try {
                tempFile = File.createTempFile("temp", "");
                logger.info("file len: {}, file[0]: {}", file.length, file[0]);
                logger.info("handle multipart file upload. fileName: {}", file[0].getName());

                file[0].transferTo(tempFile);

                FileSystemResource resource = new FileSystemResource(tempFile);
                MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
                param.add("file", resource);
                param.add("fileName", file[0].getName());

                Map<Object, Object> objMap = JSONObject.parseObject(JSONObject.toJSONString(obj), Map.class);
                if (!StringUtil.isNullOrEmpty(objMap)) {
                    for (Object key : objMap.keySet()) {
                        param.add(key.toString(), objMap.get(key));
                    }
                }

                headers.setContentType(MediaType.valueOf("multipart/form-data"));
                HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param, headers);
                ResponseEntity<Result> responseEntity = templateHolder.exchange(url, HttpMethod.POST, httpEntity, Result.class, map);

                return responseEntity.getBody();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (null != tempFile) {
                    tempFile.deleteOnExit();
                }
            }
        }
        ResponseEntity<Result> responseEntity = templateHolder.postForEntity(url, formEntity, Result.class, map);
        return responseEntity.getBody();
    }

    public static Result doPut(String url, Object params, Map<String, Object> map, String auth, int timeOut) {
        long start = System.currentTimeMillis();

        Result result = exchange(url, HttpMethod.PUT, Result.class, params, map, auth, timeOut);

        long end = System.currentTimeMillis();
        logger.info("[-doPut-] Spend time : {}", (end - start));
        return result;
    }

    public static Result doPatch(String url, Object params, Map<String, Object> map, String auth, int timeOut) {
        long start = System.currentTimeMillis();

        Result result = exchange(url, HttpMethod.PATCH, Result.class, params, map, auth, timeOut);

        long end = System.currentTimeMillis();
        logger.info("[-doPatch-] Spend time : {}", (end - start));
        return result;
    }

    public static Result doDelete(String url, Object params, Map<String, Object> map, String auth, int timeOut) {
        long start = System.currentTimeMillis();

        Result result = exchange(url, HttpMethod.DELETE, Result.class, params, map, auth, timeOut);

        long end = System.currentTimeMillis();
        logger.info("[-doDelete-] Spend time : {}", (end - start));
        return result;
    }

    /**
     * 发送/获取 服务端数据(主要用于解决发送put,delete方法无返回值问题).
     *
     * @param <T>      返回类型
     * @param url      绝对地址
     * @param method   请求方式
     * @param bodyType 返回类型
     * @param map
     * @return 返回结果(响应体)
     */
    public static <T> T exchange(String url, HttpMethod method, Class<T> bodyType, Object params, Map<String, Object> map, String auth, int timeOut) {
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", auth);
        MimeType mimeType = MimeTypeUtils.parseMimeType("application/json");
        MediaType mediaType = new MediaType(mimeType.getType(), mimeType.getSubtype(), Charset.forName("UTF-8"));
        // 请求体
        headers.setContentType(mediaType);
        //超时时间配置
        timeOutConfiguration(timeOut);
        // 发送请求
        HttpEntity<Object> entity = new HttpEntity<Object>(params, headers);
        ResponseEntity<T> resultEntity = null;
        try {
            resultEntity = templateHolder.exchange(url, method, entity, bodyType, map);
        } catch (Exception e) {
            if (e instanceof ResourceAccessException) {
                logger.info("执行超时：" + e.getMessage());
            } else {
                throw new RuntimeException(e);
            }
        }
        return resultEntity.getBody();
    }

    /**
     * 立即执行
     *
     * @param methodUrl http://formula-provider/variantMgr/loadSystemVariants
     *                  formula-provider： eureka 中注册的服务名
     * @param reqType   请求类型 get post delete put patch
     * @param params    前端传入的参数直接转发
     */

    public static Result doExec(String methodUrl, String reqType, Object params, String auth, int timeOut, MultipartFile... file) {
        Result result = new Result();
        Map<String, Object> map = new HashMap<String, Object>();
        Matcher mc = pa.matcher(methodUrl);
        String param;
        Boolean flag = true;
        while (mc.find()) {
            if (flag) {
                flag = false;
                if (!StringUtil.isNullOrEmpty(params)) {
                    map = JSONObject.parseObject(JSONObject.toJSONString(params), Map.class);
                }
            }
            param = mc.group().replace("{", "").replace("}", "");
            if (!map.containsKey(param)) {
                map.put(param, "");
            }
        }
        try {
            if (ReqType.PUT.getReqType().equalsIgnoreCase(reqType)) {
                logger.info("执行put请求类型函数：methodUrl：" + methodUrl + ",map:" + map + "，auth：" + auth + ",timeOut:" + timeOut);
                result = RestUtil.doPut(methodUrl, params, map, auth, timeOut);
                logger.info("执行put请求,执行结果为：" + result);
            } else if (ReqType.POST.getReqType().equalsIgnoreCase(reqType)) {
                logger.info("执行post请求类型函数：methodUrl：" + methodUrl + ",map:" + map + "，auth：" + auth + ",timeOut:" + timeOut + ",file:" + file);
                result = RestUtil.doPost(methodUrl, params, map, auth, timeOut, file);
                logger.info("执行post请求,执行结果为：" + result);
            } else if (ReqType.GET.getReqType().equalsIgnoreCase(reqType)) {
                logger.info("执行get请求类型函数：methodUrl：" + methodUrl + ",map:" + map + "，auth：" + auth + ",timeOut:" + timeOut + ",file:" + file);
                result = RestUtil.doGet(methodUrl, map, auth, timeOut, file);
                logger.info("执行get请求,执行结果为：" + result);
            } else if (ReqType.DELETE.getReqType().equalsIgnoreCase(reqType)) {
                logger.info("执行delete请求类型函数：methodUrl：" + methodUrl + ",map:" + map + "，auth：" + auth + ",timeOut:" + timeOut);
                result = RestUtil.doDelete(methodUrl, params, map, auth, timeOut);
                logger.info("执行delete请求,执行结果为：" + result);
            } else if (ReqType.PATCH.getReqType().equalsIgnoreCase(reqType)) {
                logger.info("执行patch请求类型函数：methodUrl：" + methodUrl + ",map:" + map + "，auth：" + auth + ",timeOut:" + timeOut);
                result = RestUtil.doPatch(methodUrl, params, map, auth, timeOut);
                logger.info("执行patch,执行结果为：" + result);
            } else {
                logger.error("doExec方法:" + methodUrl + "执行出错：");
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("doExec方法:" + methodUrl + " 非法请求类型：" + reqType);
            }
        } catch (Exception e) {
            if (e instanceof ResourceAccessException || e instanceof NullPointerException) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("doExec方法:" + methodUrl + " 执行超时：" + e.getMessage());
            } else if (e instanceof HttpClientErrorException) {
                if (((HttpClientErrorException) ((RuntimeException) e).getCause()).getStatusCode().name().equals("UNAUTHORIZED")) {
                    result.setRetCode(Result.RECODE_UNLOGIN);
                } else {
                    result.setRetCode(Result.RECODE_ERROR);
                }
            } else {
                logger.error("doExec方法:" + methodUrl + "执行出错：" + e.getMessage(), e);
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("doExec方法:" + methodUrl + "执行出错：" + e.getMessage());
            }
        }
        return result;
    }
}
