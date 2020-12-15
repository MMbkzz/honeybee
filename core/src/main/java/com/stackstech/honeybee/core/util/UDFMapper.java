package com.stackstech.honeybee.core.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Project name is cms
 * Created by xuli on 2016-08-31 04:21
 * and my phone number is +86 15171494727
 * and my google plus account is leoricxu@gmail.com
 * and my qq number is 16982916
 * <p>
 * 此处使用自定义mapper 是为了方便分页，你们可以不使用自定义mapper来做手工分页
 * 可以避免很多生产问题，不推荐使用自定义mapper来做分页
 */
public interface UDFMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
