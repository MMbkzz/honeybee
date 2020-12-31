package com.stackstech.honeybee.server.core.service;

import java.util.List;
import java.util.Map;

/**
 * 数据操作业务接口
 *
 * @author William
 * @date 2019-03-01
 * @since 1.0
 *
 * @param <T> Data entity class type
 */
public interface DataService<T> {

	/**
	 * 增加数据记录
	 *
	 * @param entity Data model
	 * @return boolean
	 */
	boolean add(T entity);

	/**
	 * 修改数据记录
	 *
	 * @param entity Data model
	 * @return boolean
	 */
	boolean update(T entity);

	/**
	 * 删除数据记录
	 *
	 * @param recordId Data record ID
	 * @return boolean
	 */
	boolean delete(Long recordId);

	/**
	 * 获取单个数据记录
	 *
	 * @param recordId Data record ID
	 * @return Data model
	 */
	T getSingle(Long recordId);

//	/**
//	 * 获取单个数据记录
//	 *
//	 * @param parameter Query parameters
//	 * @return Data model
//	 */
//	T getSingle(Map<String, Object> parameter);

	/**
	 * 获取多个数据记录
	 *
	 * @param parameter Query parameters
	 * @return List of Data model
	 */
	List<T> get(Map<String, Object> parameter);

	/**
	 * 获取多个数据记录数
	 *
	 * @param parameter Query parameters
	 * @return Integer
	 */
	Integer getTotalCount(Map<String, Object> parameter);
}
