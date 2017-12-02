package com.museum.api.common.exception;





import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;


/**
 * SOA异常的断言，当断言不成立时，抛出SOA异常
 * @author 沈杭春
 * @since 2016-11-7
 */


public class InheaterSOAExceptionAssert {
	/**
	 * Assert a boolean expression, throwing <code>GMSOAException</code>
	 * if the test result is <code>false</code>.
	 * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
	 * @param expression a boolean expression
	 * @param message the exception message to use if the assertion fails
	 * @throws InheaterSOAException if expression is <code>false</code>
	 */
	public static void isTrue(boolean expression, InheaterSOAExceptionCode code, String message) {
		isTrue(expression, code, message, null);
	}
	
	public static void isTrue(boolean expression, InheaterSOAExceptionCode code, String message, Object errorData) {
		if (!expression) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}


	/**
	 * Assert that an object is <code>null</code> .
	 * <pre class="code">Assert.isNull(value, "The value must be null");</pre>
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws InheaterSOAException if the object is not <code>null</code>
	 */
	public static void isNull(Object object, InheaterSOAExceptionCode code, String message) {
		isNull(object, code, message, null);
	}
	
	public static void isNull(Object object, InheaterSOAExceptionCode code, String message, Object errorData) {
		if (object != null) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}
	
	/**
	 * 判断值是否为空，否则抛出异常
	 * @param object
	 * @param code
	 * @param message
	 * @author 沈杭春
	* @since 2016-11-7
	 */
	
	public static void isNotNull(Object object, InheaterSOAExceptionCode code, InheaterSOAExceptionMessage message) {
		isNotNull(object, code, message.toString(), null);
	}
	
	public static void isNotNull(Object object, InheaterSOAExceptionCode code, String message) {
		isNotNull(object, code, message, null);
	}
	
	public static void isNotNull(Object object, InheaterSOAExceptionCode code, String message, Object errorData) {
		if (object == null) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws InheaterSOAException if the object is <code>null</code>
	 */
	public static void notNull(Object object, InheaterSOAExceptionCode code, String message) {
		notNull(object, code, message, null);
	}
	
	public static void notNull(Object object, InheaterSOAExceptionCode code, String message, Object errorData) {
		if (object == null) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}
	
	/**
	 * 判断是否为空， 如果为空（null/单个空格/多个空格），则抛出异常
	 * @param text 判断的字符串
	 * @param code 错误码
	 * @param message 错误信息
	 * @author 沈杭春
	* @since 2016-11-7
	 */
	public static void isNotBlank(String text, InheaterSOAExceptionCode code, InheaterSOAExceptionMessage message){
		isNotBlank(text, code, message.toString(), null);
	}
	
	public static void isNotBlank(String text, InheaterSOAExceptionCode code, String message){
		isNotBlank(text, code, message, null);
	}
	
	public static void isNotBlank(String text, InheaterSOAExceptionCode code, String message, Object errorData){
		if(!org.apache.commons.lang.StringUtils.isNotBlank(text)) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}
	
	/**
	 * 判断字符串是否为空，如果为空（null/单个空格），则抛出异常
	 * @param StringUtils 判断的字符串
	 * @param code 错误码
	 * @param message 错误信息
	 * @author 沈杭春
	* @since 2016-11-7
	 */
	public static void isNotEmpty(String text, InheaterSOAExceptionCode code, InheaterSOAExceptionMessage message) {
		isNotEmpty(text, code, message.toString(), null);
	}
	
	public static void isNotEmpty(String text, InheaterSOAExceptionCode code, String message) {
		isNotEmpty(text, code, message, null);
	}
	
	public static void isNotEmpty(String text, InheaterSOAExceptionCode code, String message, Object errorData) {
		if (org.apache.commons.lang.StringUtils.isEmpty(text)) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}
	
	/**
	 * 判断值是否有值， 如果为空或0，则抛出异常
	 * @param value 判断的值
	 * @param code 错误码
	 * @param message 错误信息
	 * @author 沈杭春
	* @since 2016-11-7
	 */
	public static void hasValue(Long value, InheaterSOAExceptionCode code, InheaterSOAExceptionMessage message){
		hasValue(value, code, message.toString(), null);
	}
	
	public static void hasValue(Long value, InheaterSOAExceptionCode code, String message){
		hasValue(value, code, message, null);
	}
	
	public static void hasValue(Long value, InheaterSOAExceptionCode code, String message, Object errorData){
		if(value == null || value.equals(0L)) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}
	
	/**
	 * 判断值是否有值， 如果为空 ,则抛出异常
	 * @param value 判断的值
	 * @param code 错误码
	 * @param message 错误信息
	 * @author 沈杭春
	* @since 2016-11-7
	 */
	public static void hasValue(Byte value, InheaterSOAExceptionCode code, InheaterSOAExceptionMessage message){
		hasValue(value, code, message.toString(), null);
	}
	
	public static void hasValue(Byte value, InheaterSOAExceptionCode code, String message){
		hasValue(value, code, message, null);
	}
	
	public static void hasValue(Byte value, InheaterSOAExceptionCode code, String message, Object errorData){
		if(value == null) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}
	
	/**
	 * 判断值是否有值， 如果为空或0，则抛出异常
	 * @param value 判断的值
	 * @param code 错误码
	 * @param message 错误信息
	 * @author 沈杭春
	* @since 2016-11-7
	 */
	public static void hasValue(Short value, InheaterSOAExceptionCode code, InheaterSOAExceptionMessage message){
		hasValue(value, code, message.toString(), null);
	}
	
	public static void hasValue(Short value, InheaterSOAExceptionCode code, String message){
		hasValue(value, code, message, null);
	}
	
	public static void hasValue(Short value, InheaterSOAExceptionCode code, String message, Object errorData){
		if(value == null || value.intValue() == 0) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}
	
	/**
	 * 判断值是否有值， 如果为空或0，则抛出异常
	 * @param value 判断的值
	 * @param code 错误码
	 * @param message 错误信息
	 * @author 沈杭春
	* @since 2016-11-7
	 */
	public static void hasValue(Integer value, InheaterSOAExceptionCode code, InheaterSOAExceptionMessage message){
		hasValue(value, code, message.toString(), null);
	}
	
	public static void hasValue(Integer value, InheaterSOAExceptionCode code, String message){
		hasValue(value, code, message, null);
	}
	
	public static void hasValue(Integer value, InheaterSOAExceptionCode code, String message, Object errorData){
		if(value == null || value.equals(0)) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}

	/**
	 * 判断值是否有值， 如果为空或0，则抛出异常
	 * @param value 判断的值
	 * @param code 错误码
	 * @param message 错误信息
	 * @author 沈杭春
	* @since 2016-11-7
	 */
	public static void hasValue(BigDecimal value, InheaterSOAExceptionCode code, InheaterSOAExceptionMessage message){
		hasValue(value, code, message.toString(), null);
	}
	
	public static void hasValue(BigDecimal value, InheaterSOAExceptionCode code, String message, Object errorData){
		if(value == null || value.compareTo(new BigDecimal(0L)) == 0) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}

	public static void hasValue(BigDecimal value, InheaterSOAExceptionCode code, String message){
		hasValue(value, code, message, null);
	}
	/**
	 * Assert that the given text does not contain the given substring.
	 * <pre class="code">Assert.doesNotContain(name, "rod", "Name must not contain 'rod'");</pre>
	 * @param textToSearch the text to search
	 * @param substring the substring to find within the text
	 * @param message the exception message to use if the assertion fails
	 */
	public static void doesNotContain(String textToSearch, String substring, InheaterSOAExceptionCode code, String message) {
		doesNotContain(textToSearch, substring, code, message, null);
	}
	
	public static void doesNotContain(String textToSearch, String substring, InheaterSOAExceptionCode code, String message, Object errorData) {
		if (!org.apache.commons.lang.StringUtils.contains(textToSearch, substring)) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}


	/**
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(array, "The array must have elements");</pre>
	 * @param array the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws InheaterSOAException if the object array is <code>null</code> or has no elements
	 */
	public static void isNotEmpty(Object[] array, InheaterSOAExceptionCode code, String message) {
		isNotEmpty(array, code, message, null);
	}
	
	public static void isNotEmpty(Object[] array, InheaterSOAExceptionCode code, String message, Object errorData) {
		if (ObjectUtils.isEmpty(array)) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}

	/**
	 * Assert that an array has no null elements.
	 * Note: Does not complain if the array is empty!
	 * <pre class="code">Assert.noNullElements(array, "The array must have non-null elements");</pre>
	 * @param array the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws InheaterSOAException if the object array contains a <code>null</code> element
	 */
	public static void hasNoNullElements(Object[] array, InheaterSOAExceptionCode code, String message) {
		hasNoNullElements(array, code, message, null);
	}
	
	public static void hasNoNullElements(Object[] array, InheaterSOAExceptionCode code, String message, Object errorData) {
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
			}
		}
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(collection, "Collection must have elements");</pre>
	 * @param collection the collection to check
	 * @param message the exception message to use if the assertion fails
	 * @throws InheaterSOAException if the collection is <code>null</code> or has no elements
	 */
	
	public static void isNotEmpty(Collection<?> collection, InheaterSOAExceptionCode code, InheaterSOAExceptionMessage message) {
		isNotEmpty(collection, code, message.toString(), null);
	}
	
	public static void isNotEmpty(Collection<?> collection, InheaterSOAExceptionCode code, String message) {
		isNotEmpty(collection, code, message, null);
	}
	
	public static void isNotEmpty(Collection<?> collection, InheaterSOAExceptionCode code, String message, Object errorData) {
		if (CollectionUtils.isEmpty(collection)) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}
	
	public static void isEmpty(Collection<?> collection, InheaterSOAExceptionCode code, String message) {
		if (!CollectionUtils.isEmpty(collection)) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, null);
	}
	
	public static void isEmpty(Collection<?> collection, InheaterSOAExceptionCode code, String message, Object errorData) {
		if (!CollectionUtils.isEmpty(collection)) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}

	/**
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * <pre class="code">Assert.notEmpty(map, "Map must have entries");</pre>
	 * @param map the map to check
	 * @param message the exception message to use if the assertion fails
	 * @throws InheaterSOAException if the map is <code>null</code> or has no entries
	 */
	public static void isNotEmpty(Map<?, ?> map, InheaterSOAExceptionCode code, String message) {
		isNotEmpty(map, code, message, null);
	}
	
	public static void isNotEmpty(Map<?, ?> map, InheaterSOAExceptionCode code, String message, Object errorData) {
		if (CollectionUtils.isEmpty(map)) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, errorData);
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 * <pre class="code">Assert.instanceOf(Foo.class, foo);</pre>
	 * @param type the type to check against
	 * @param obj the object to check
	 * @param message a message which will be prepended to the message produced by
	 * the function itself, and which may be used to provide context. It should
	 * normally end in a ": " or ". " so that the function generate message looks
	 * ok when prepended to it.
	 * @throws InheaterSOAException if the object is not an instance of clazz
	 * @see Class#isInstance
	 */
	public static void isInstanceOf(Class<?> type, Object obj, InheaterSOAExceptionCode code, String message) {
		isInstanceOf(type, obj, code, message, null);
	}
	
	public static void isInstanceOf(Class<?> type, Object obj, InheaterSOAExceptionCode code, String message, Object errorData) {
		notNull(type, code, message + ": type is null");
		if (!type.isInstance(obj)) {
			throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message +
					"Object of class [" + (obj != null ? obj.getClass().getName() : "null") +
					"] must be an instance of " + type, errorData);
		}
	}

	/**
	 * Assert that <code>superType.isAssignableFrom(subType)</code> is <code>true</code>.
	 * <pre class="code">Assert.isAssignable(Number.class, myClass);</pre>
	 * @param superType the super type to check against
	 * @param subType the sub type to check
	 * @param message a message which will be prepended to the message produced by
	 * the function itself, and which may be used to provide context. It should
	 * normally end in a ": " or ". " so that the function generate message looks
	 * ok when prepended to it.
	 * @throws InheaterSOAException if the classes are not assignable
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType, InheaterSOAExceptionCode code, String message) {
		isAssignable(superType, subType, code, message, null);
	}
	
	public static void isAssignable(Class<?> superType, Class<?> subType, InheaterSOAExceptionCode code, String message, Object errorData) {
		notNull(superType, code, message + ": superType is null");
		if (subType == null || !superType.isAssignableFrom(subType)) throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message + subType + " is not assignable to " + superType, errorData);
	}
	
	/**
	 * 输入项至少一项不为空
	 * @param inputs
	 * @param code
	 * @param message
	 */
	public static void notAllBlank(String[] inputs, InheaterSOAExceptionCode code, String message){
		boolean allBlank = !ObjectUtils.isEmpty(inputs);
		for(String input: inputs){
			allBlank = allBlank && StringUtils.isEmpty(input);
		}
		if(allBlank){
			throw new InheaterSOAException(code, InheaterSOAExceptionType.BUSINESS, message, null);
		}
	}
}