package com.zk.common.util;


import com.zk.common.vo.PageVo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author lzz
 */
public class PageUtil {

    public static PageVo getPage() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String pageStr = request.getParameter("page") == null ? "1" : request.getParameter("page");
        String sizeStr = request.getParameter("size") == null ? "10" : request.getParameter("size");

        Integer page = Integer.parseInt(pageStr);
        Integer size = Integer.parseInt(sizeStr);
        String keyword = request.getParameter("keyword");
        return new PageVo(page, size, keyword);
    }
}
