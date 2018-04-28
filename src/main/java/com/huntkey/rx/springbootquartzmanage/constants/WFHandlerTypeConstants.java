package com.huntkey.rx.springbootquartzmanage.constants;

/**
 * 用户单据审批pass方法中 关于审批操作的3种方式
 *
 * @author yaoss
 */
public interface WFHandlerTypeConstants {
    /**
     * 完成
     */
    String PASS = "pass";
    /**
     * 撤销
     */
    String REVOKE = "revoke";
    /**
     * 退回（否决）
     */
    String RETURN_BACK = "returnBack";

}
