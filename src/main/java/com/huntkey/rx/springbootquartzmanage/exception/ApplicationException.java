package com.huntkey.rx.springbootquartzmanage.exception;

/**
 *
 * 自定义 业务异常，用于编写 业务逻辑 捕获异常并抛出ApplicationException
 * @author liangh
 * @date 2017/11/26
 */
public class ApplicationException extends RuntimeException {

    private int code;

    public ApplicationException(int code, String message) {
        super(message, null, true, true);
        this.code = code;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

}
