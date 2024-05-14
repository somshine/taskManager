package com.somshine.taskmanager.utils;

import com.somshine.taskmanager.model.PageModel;

public class PaginationUtil {
    public static PageModel getPaginationDetail(Integer pageNo, Integer pageSize) {
        PageModel pageModel = new PageModel();

        if (pageNo != null) {
            pageNo = pageNo - 1;
        } else {
            pageNo = 0;
        }
        if (pageSize == null) {
            pageSize = 50;
        }

        pageModel.setPageNo(pageNo);
        pageModel.setPageSize(pageSize);

        return pageModel;
    }
}
