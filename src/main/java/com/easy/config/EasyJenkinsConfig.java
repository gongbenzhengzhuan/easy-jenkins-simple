package com.easy.config;

import cn.hutool.json.JSONUtil;
import com.easy.bean.DataStructure;
import com.easy.service.QueryDataStructureService;
import com.easy.util.InterceptorBeanUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class EasyJenkinsConfig implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, Object> infoMap = new HashMap<>();
        QueryDataStructureService structureService = InterceptorBeanUtil.getBean(QueryDataStructureService.class, request);
        DataStructure dataStructure = structureService.getDataStructure();
        if (dataStructure != null) {
            return true;
        } else {
            infoMap.put("msg", "数据配置错误,请重新安装程序");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSONUtil.parse(infoMap).toString());
            out.flush();
            out.close();
            return false;
        }
    }

}
