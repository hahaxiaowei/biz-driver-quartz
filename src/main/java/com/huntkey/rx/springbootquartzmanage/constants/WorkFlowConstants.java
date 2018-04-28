package com.huntkey.rx.springbootquartzmanage.constants;

/**
 * 流程相关常量
 *
 * @author zhaomj
 */
public interface WorkFlowConstants {
    /**
     * 参数名-流程实例ID
     */
    String PARAM_ACT_INSTANCE_ID = "actInstanceId";
    /**
     * 审批意见文本
     */
    String PARAM_OPINION = "opinion";
    /**
     * 审批意见 pass，revoke, returnBack 枚举
     */
    String PARAM_AUDITKEY = "auditKey";
    /**
     * 单据编辑状态：0不可以编辑，1可以编辑
     */
    String PARAM_FORMSTATE = "formState";
    /**
     * 单据对象 当单据可以编辑，也就是说在审批中修改单据的一些数据信息的话，前端需要传递关于单据的一些信息
     */
    String PARAM_ORDER_OBJ = "auditMsg";

    interface FormState {
        String EDITABLE = "1";
    }

    interface AuditKey {
        String PASS = "pass";
    }
}
