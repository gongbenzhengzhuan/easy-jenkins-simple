package com.easy.port;

import com.easy.bean.DataStructure;
import com.easy.service.QueryDataStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@Configuration
public class DynamicPort implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Autowired
    QueryDataStructureService queryDataStructureService;

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        DataStructure dataStructure = queryDataStructureService.getDataStructure();
        if (dataStructure != null) {
            // 此处设置端口号优先级高于 yml文件
            // yml 端口为默认端口
            factory.setPort(dataStructure.getEasyJenkinsPort());
        }
    }
}
