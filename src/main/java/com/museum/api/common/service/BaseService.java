package com.museum.api.common.service;



import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.museum.api.common.util.DateUtil;
import com.museum.api.common.util.StringUtil;






public class BaseService {

	static String regx = "^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$"; //科学技术法的正则表达式
	

	public BaseService() {
		super();
	}

	@SuppressWarnings("rawtypes")
	protected void setCommonField(Object obj, Integer userId, Timestamp tmp) {
		try {
			Method[] methods = obj.getClass().getMethods();
			for (Method method : methods) {
				/**
				 * 因为这里只是调用bean中属性的set方法，属性名称不能重复 所以set方法也不会重复，所以就直接用方法名称去锁定一个方法
				 * （注：在java中，锁定一个方法的条件是方法名及参数）
				 **/
				if (method.getName().equals("setCreateTime") || method.getName().equals("setLastUpdateTime")) {
					Class[] parameterC = method.getParameterTypes();

					method.invoke(obj, parameterC[0].cast(tmp));
				}
				if (method.getName().equals("setCreateBy") || method.getName().equals("setLastUpdateBy")) {
					Class[] parameterC = method.getParameterTypes();

					method.invoke(obj, parameterC[0].cast(userId));
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	protected void setCommonFieldForUpdate(Object obj, Integer userId, Timestamp tmp) {
		try {
			Method[] methods = obj.getClass().getMethods();
			for (Method method : methods) {
				/**
				 * 因为这里只是调用bean中属性的set方法，属性名称不能重复 所以set方法也不会重复，所以就直接用方法名称去锁定一个方法
				 * （注：在java中，锁定一个方法的条件是方法名及参数）
				 **/
				if (method.getName().equals("setLastUpdateTime")) {
					Class[] parameterC = method.getParameterTypes();

					method.invoke(obj, parameterC[0].cast(tmp));
				}
				if (method.getName().equals("setLastUpdateBy")) {
					Class[] parameterC = method.getParameterTypes();

					method.invoke(obj, parameterC[0].cast(userId));
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	
	/**
	 *  将传入时间 去掉传入日期 只保存时间信息
	 * @param date
	 * @return 1970年1月1日的时间
	 */
	protected Date justGetTime(Date date){
		return DateUtil.parse(DateUtil.formatDate(date, DateUtil.COLON_TIME_FORMAT), DateUtil.COLON_TIME_FORMAT);
	}
	
	/** 有些不对应还需要重新set
	 * @param vo  
	 * @param model 数据库类型
	 * @return
	 */
	protected void voTOModel(Object vo,Object model){
		
		Class<? extends Object> voCls = vo.getClass();
		Class<? extends Object> modelCls = model.getClass();
		Field[] fields =voCls.getDeclaredFields();
		for(int i = 0 ;i<fields.length;i++){
			Field field = fields[i];
			String fieldName = field.getName();
			if(fieldName.equals("createTime") ||fieldName.equals("createBy") ||fieldName.equals("lastUpdateTime")||fieldName.equals("lastUpdateBy")){
				continue;
			}
			
			String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			try{
				
				
				Method getMethod = voCls.getMethod(getMethodName);
				Object value = getMethod.invoke(vo);
				Class<?> dataType = modelCls.getDeclaredField(fieldName).getType();
				
				if(value == null){ //空值不赋值
					continue;
				}
				
				/**
				 * 注册一个日期转换 支持  yyyy-MM-dd HH:mm:ss 和 yyyy-MM-dd HH:mm
				 */
				ConvertUtils.register(new Converter(){
					
					@Override
					public Object convert(Class type, Object value) {
					  if(value == null)  
		               {  
		                   return null;  
		               }
					  if(value instanceof Date){
						  return  value;
					  }else if(!(value instanceof String))  
		               {  
		                   throw new ConversionException("只支持字符串转换 !");  
		               } 
					  String str =  (String)value; 
					 
					  
					  if(StringUtil.isNull(str)){
						  return  null;
					  }
					  Date  date = DateUtil.parse(str.trim(),"yyyy-MM-dd HH:mm:ss");
					  
					  if(date ==null){
						  date = DateUtil.parse(str.trim(),"yyyy-MM-dd HH:mm"); //有些时间只到分
					  }
					  
					  return  date;
					}
					
				}, Date.class); //注册日期转换
				
				Pattern pattern = Pattern.compile(regx);
				
				if(value !=null && pattern.matcher(value.toString()).matches()){ //对科学计数法的String 进行处理
					value =new BigDecimal(value.toString()).toPlainString();
				}
				
				value = ConvertUtils.convert(value, dataType);
				
				
				Method setMethod =modelCls.getMethod(setMethodName, dataType);
				setMethod.invoke(model, value);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}

}
