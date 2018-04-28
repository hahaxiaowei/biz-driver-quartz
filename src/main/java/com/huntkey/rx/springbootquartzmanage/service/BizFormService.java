package com.huntkey.rx.springbootquartzmanage.service;

import com.huntkey.rx.springbootquartzmanage.entity.CurrentSessionEntity;

/**
 * 通用Service接口
 * 生成单据单号 和文件上传 以及 未来自定义通用服务
 *
 * @author zhangyu
 * @create 2018-01-02 11:49
 **/
public interface BizFormService {

    CurrentSessionEntity getCurrentSessionInfo();

    void submitWorkFlow(String orderDefId, String orderInstanceId);

    void audit(String actInstanceId, String opinion, String auditKey);
}
