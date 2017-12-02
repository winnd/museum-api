package com.museum.api.common.constant;



public class Constants {

	/**
	 * 200 调用成功，运行结果参考API说明
	 */
	public static final String SUCCESS = "200";

	/**
	 * 300 调用成功，业务错误
	 */
	public static final String FAIL_BUSINESS_ERROR = "300";

	/**
	 * 400 无效数据
	 */
	public static final String FAIL_INVALID_DATA = "400";

	/**
	 * 401 用户认证失败
	 */
	public static final String FAIL_INVALID_USER = "401";

	/**
	 * 403 用户无权访问
	 */
	public static final String FAIL_INVALID_AUTH = "403";

	/**
	 * 404 无效URI
	 */
	public static final String FAIL_INVALID_URI = "404";

	/**
	 * 500 服务端错误
	 */
	public static final String FAIL_SERVER_ERROR = "500";
	
	public static final String INITIAL_PASSWORD ="123456";
	
	public static final String TRUE = "T";
	
	public static final String FALSE ="F";
	
	public static final String YES ="Y";
	
	public static final String NO = "N";
	
	public static final String WAIT ="O";
	
	public static final Integer STATUS_0 =0;
	
	public static final Integer STATUS_1 =1;
	
	public static final Integer STATUS_2 =2;
	
	/**
	 * 好评
	 */
	public static final String EVALUATE_HIGH ="H";
	
	/**
	 * 中评
	 */
	public static final String EVALUATE_MID ="M";
	
	/**
	 * 差评
	 */
	public static final String EVALUATE_LOW ="L";
	
	
	/**
	 * 分页 每页显示条数
	 */
	public static final Integer PAGE_SIZE =8;
	
	/**
	 * 用户类型 企业
	 */
	public static final String USER_TYPE_E ="E";
	/**
	 * 用户类型 团队
	 */
	public static final String USER_TYPE_T ="T";
	
	/**
	 * 用户类型 个人
	 */
	public static final String USER_TYPE_P ="P";
	
	/**
	 * 用户类型 政府/平台
	 */
	public static final String USER_TYPE_A ="A";
	
	
	/**
	 * 角色类型 企业
	 */
	public static final String ROLE_TYPE_E ="E";
	
	
	/**
	 * 角色类型 平台/政府
	 */
	public static final String ROLE_TYPE_A ="A";
	
	
	/**
	 * 团队角色 班组长
	 */
	public static final String GROUP_USER_TYPE_L ="L";
	
	/**
	 * 团队角色 普通成员
	 */
	public static final String GROUP_USER_TYPE_M ="M";
	
	/**
	 *  推送类型 即时推送
	 */
	public static final int NOTICE_SEND_TYPE_IMMEDIATELY = 1;
	
	/**
	 *  推送类型 定时推送
	 */
	public static final int NOTICE_SEND_TYPE_TIMING = 2;
	
	/**
	 *  通知类型 结算
	 */
	public static final int NOTICE_TYPE_1 =1;
	
	/**
	 *  通知类型 支付
	 */
	public static final int NOTICE_TYPE_2 =2;
	
	/**
	 * 项目状态  未开始
	 */
	public static final String PROJECT_STATE_OPEN ="00";
	
	/**
	 * 项目状态 已开始
	 */
	public static final String PROJECT_STATE_START ="02";
	
	/**
	 * 项目状态 异常
	 */
	public static final String PROJECT_STATE_EXCEPTION ="99";
	
	/**
	 * 项目状态 已结束
	 */
	public static final String PROJECT_STATE_END ="01";
	
	/**
	 * 工单状态 发布工单 '工单状态，\n00:发布工单\n01:招聘结束\n10:结算确认\n11:支付确认\n20:工单结束\n21:已评价\n90:取消招聘\n91:提前结束\n99:异常\n',
	 */
	public static final String ORDER_STATE_PUBLISH ="00";
	
	/**
	 * 工单状态 招聘结束
	 */
	public static final String ORDER_STATE_START ="01";
	
	/**
	 * 工单状态 结算确认
	 */
	public static final String ORDER_STATE_COMFIRM1 ="10";
	
	/**
	 * 工单状态 支付确认
	 */
	public static final String ORDER_STATE_COMFIRM2 ="11";
	/**
	 * 结束
	 */
	public static final String ORDER_STATE_END ="20";
	/**
	 * 已评价
	 */
	public static final String ORDER_STATE_EVALUATED ="21";
	/**
	 * 取消招聘
	 */
	public static final String ORDER_STATE_CANCEL ="90";
	/**
	 * 提前结束
	 */
	public static final String ORDER_STATE_ADVANCE ="91";
	/**
	 * 异常
	 */
	public static final String ORDER_STATE_EXCEPTION ="99";
	
	/**
	 * code_type
	 */
	public static final String CODE_TYPE_USER_TYPE ="user_type";
	
	public static final String CODE_TYPE_MESSAGE_TYPE ="message_type";
	
	public static final String CODE_TYPE_JOBS ="jobs";
	
	public static final String CODE_TYPE_ORDER_STATUS ="order_status";
	
	public static final String CODE_TYPE_COMPANY_TYPE ="company_type";
	
	public static final String CODE_TYPE_COMPANY_SIZE="company_size";
	
	public static final String CODE_TYPE_EVALUATE="evaluate";


	/**
	 * 存储当前登录用户id的字段名
	 */
	public static final String CURRENT_USER_ID = "CURRENT_USER_ID";

	/**
	 * token有效期（小时）
	 */
	public static final int TOKEN_EXPIRES_HOUR = 72;

	/**
	 * 存放Authorization的header字段
	 */
	public static final String AUTHORIZATION = "authorization";

	/**
	 * token的密匙
	 */
	public static final String SECRET = "museum-key";
	
	
}
