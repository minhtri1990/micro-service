package com.its.module.utils;

public class MathUtils {
    public static int calcNumPages(int numEl, int pageSize) {
        if(pageSize == 0) return 0;
        return (numEl + pageSize - 1)/pageSize;
    }
}
