package com.museum.api.common.exception;



/**
 * SOA统一异常类
 * @author 沈杭春
 * @since 2016-11-7
 */
public class InheaterSOAException extends RuntimeException {
	private static final long serialVersionUID = 6029038041214105194L;
	
	/** 异常类型 **/
	private InheaterSOAExceptionType type;
	/** 异常代码 **/
	private InheaterSOAExceptionCode code;
	/** 异常数据 **/
	private Object errorData;
	
	public InheaterSOAException(InheaterSOAExceptionCode code, InheaterSOAExceptionType type, InheaterSOAExceptionMessage message) {
		super(message.toString());
		
		this.type = type;
		this.code = code;
	}
	
	public InheaterSOAException(InheaterSOAExceptionCode code, InheaterSOAExceptionType type, String message) {
		super(message);
		
		this.type = type;
		this.code = code;
	}
	
	public InheaterSOAException(InheaterSOAExceptionCode code, InheaterSOAExceptionType type, String message, Object errorData) {
		super(message);
		
		this.type = type;
		this.code = code;
		this.errorData = errorData;
	}

	public InheaterSOAExceptionType getType() {
		return type;
	}

	public InheaterSOAExceptionCode getCode() {
		return code;
	}

	public Object getErrorData() {
		return errorData;
	}	
}