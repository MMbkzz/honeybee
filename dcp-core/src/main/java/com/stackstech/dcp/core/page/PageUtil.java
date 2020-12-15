package com.stackstech.dcp.core.page;

import com.github.pagehelper.PageHelper;

/**
 * 将分页信息转成我们需要的格式
 */
public class PageUtil {

    public static void page(Page page) {
        PageHelper.startPage(page.getPageNo(), page.getPageSize(), page.getOrderBy());
    }

    public static void page(Page page, String sortType) {
        if (page.getPageNo() == null || page.getPageSize() == null) {
            page.setPageNo(1);
            page.setPageSize(50);
        }
        if (sortType == null || "".equals(sortType)) {
            page(page);
        } else {
            PageHelper.startPage(page.getPageNo(), page.getPageSize(), sortType);
        }
    }

}
