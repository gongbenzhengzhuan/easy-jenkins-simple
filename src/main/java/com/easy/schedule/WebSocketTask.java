package com.easy.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.easy.deploy.util.LogUtil;
import com.easy.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@Configuration
@EnableScheduling
@Slf4j
public class WebSocketTask {

    @Autowired
    WebSocketServer webSocketServer;

    @Scheduled(cron = "0/1 * * * * ?")
    private void morning() {
        Map<String, String> deployMap = LogUtil.getDeployMap();
        webSocketServer.send(JSONUtil.toJsonStr(deployMap));
    }

}
