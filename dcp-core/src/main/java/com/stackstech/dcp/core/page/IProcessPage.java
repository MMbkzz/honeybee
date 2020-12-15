package com.stackstech.dcp.core.page;

import java.util.List;
import java.util.Map;

/**
 * 项目名 service
 * 该接口用来处理自定义显示输出分页的格式
 * 比如:假设输出格式是 {page:[page_num:1,page_size:12]}
 * 自定实现该接口转成需要的格式 如：{page:[page_no:1,page_size:12]}
 */
public interface IProcessPage {

    /**
     * 分页接口
     *
     * @param queryData
     * @return
     */
    Map<String, Object> process(List<?> queryData);

}
