package com.stackstech.dcp.core.page.impl;

import com.github.pagehelper.PageInfo;
import com.stackstech.dcp.core.page.IProcessPage;
import com.stackstech.dcp.core.page.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页实现
 */
@Service
public class ProcessPage implements IProcessPage {

    @Override
    public Map<String, Object> process(List<?> queryData) {
        Map<String, Object> map = new HashMap<String, Object>(0);
        PageInfo<?> pageInfo = new PageInfo(queryData);

        Page outputPage = new Page();
        //页码
        outputPage.setPageNo(pageInfo.getPageNum());
        //每页显示条数
        outputPage.setPageSize(pageInfo.getPageSize());
        //总条数
        outputPage.setTotalElements((int) pageInfo.getTotal());
        //总页数
        outputPage.setTotalPages(pageInfo.getPages());
        //当前页有多少条数据
        outputPage.setNumberElements(pageInfo.getSize());

        //输出分页
        map.put("page", outputPage);
        //查询的数据
        map.put("data", queryData);
        return map;
    }

}
