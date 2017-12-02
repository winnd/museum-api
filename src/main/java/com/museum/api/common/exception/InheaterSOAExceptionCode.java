package com.museum.api.common.exception;

/**
 * 异常类型
 * @author 沈杭春
 * @since 2016-11-7
 */
public enum InheaterSOAExceptionCode {
	
	/** 参数错误 **/
    PARAM_REQUIRED_EXCEPTION("PARAM_ERROR"),
	/** 业务(逻辑)错误 **/
    BUNIESS_EXCEPTION("BUSINESS_ERROR"),
	/** 系统错误 **/
    SYSTEM_EXCEPTION("SYSTEM_ERROR"),
    /** 未知异常 **/
    UNKNOWN_EXCEPTION("UNKNOWN_EXCEPTION");
    
    private String code;
    
    private InheaterSOAExceptionCode(String code) {
		this.code = code;
	}
    
    @Override
    public String toString() {
    	return this.code;
    }
}
