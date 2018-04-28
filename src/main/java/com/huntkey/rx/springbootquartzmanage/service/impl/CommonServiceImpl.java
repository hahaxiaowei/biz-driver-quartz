package com.huntkey.rx.springbootquartzmanage.service.impl;


import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.springbootquartzmanage.client.InformationClient;
import com.huntkey.rx.springbootquartzmanage.exception.ApplicationException;
import com.huntkey.rx.springbootquartzmanage.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通用接口Service实现类
 *
 * @author zhangyu
 * @create 2018-01-02 11:53
 **/
@Service
public class CommonServiceImpl implements CommonService {

    Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private InformationClient informationClient;


    @Override
    public String getCode(String nbrlCode) {
        Result codeResult = informationClient.getNumbers(nbrlCode, null);
        if (codeResult.getData() == null) {
            throw new ApplicationException(Result.RECODE_ERROR, codeResult.getErrMsg());
        }
        return codeResult.getData().toString();
    }
}
