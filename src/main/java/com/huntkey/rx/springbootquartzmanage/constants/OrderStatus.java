package com.huntkey.rx.springbootquartzmanage.constants;


/**
 * 定义单据的状态常量。
 *
 * @author yaoss
 */
public interface OrderStatus {

    /**
     * 单据状态：临时
     */
    String ORDE_STATUS_1 = "1";
    /**
     * 单据状态：待审
     */
    String ORDE_STATUS_2 = "2";
    /**
     * 单据状态：待核
     */
    String ORDE_STATUS_3 = "3";
    /**
     * 单据状态：待批
     */
    String ORDE_STATUS_4 = "4";
    /**
     * 单据状态：完成
     */
    String ORDE_STATUS_5 = "5";
    /**
     * 单据状态：退回
     */
    String ORDE_STATUS_6 = "6";

}
