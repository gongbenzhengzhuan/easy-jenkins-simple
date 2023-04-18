package com.easy.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@Component
@ServerEndpoint("/socket/deploy")
@Slf4j
public class WebSocketServer {

    private Session session;

    private static final CopyOnWriteArrayList<WebSocketServer> webSocketServers = new CopyOnWriteArrayList<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketServers.add(this);
        log.info("新的连接，数量：{}", webSocketServers.size());
    }

    @OnClose
    public void onClose() {
        webSocketServers.remove(this);
        log.info("新的连接，数量：{}", webSocketServers.size());
    }

    @OnMessage
    public void onMessage(String msg) {
        log.info("来自客户端的消息" + msg);
    }

    public void send(String msg) {
        for (WebSocketServer webSocketServer : webSocketServers) {
            try {
                webSocketServer.session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
