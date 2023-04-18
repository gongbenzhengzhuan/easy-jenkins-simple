package com.easy.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class PageUtil<T> {

    public List<T> page(List<T> dataList, int pageSize, int currentPage) {
        List<T> currentPageList = new ArrayList<>();
        if (dataList != null && dataList.size() > 0) {
            int currIdx = (currentPage > 1 ? (currentPage - 1) * pageSize : 0);
            for (int i = 0; i < pageSize && i < dataList.size() - currIdx; i++) {
                currentPageList.add(dataList.get(currIdx + i));
            }
        }
        return currentPageList;
    }

}
