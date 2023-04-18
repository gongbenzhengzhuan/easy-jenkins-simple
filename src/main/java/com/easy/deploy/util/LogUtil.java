package com.easy.deploy.util;

import com.easy.bean.DataStructure;
import com.easy.deploy.vo.DeployConnect;
import com.easy.enums.EasyJenkinsEnum;
import com.easy.service.DataStructureService;
import com.easy.service.QueryDataStructureService;
import com.easy.util.InterceptorBeanUtil;
import lombok.extern.slf4j.Slf4j;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tanyongpeng
 * <p></p>
 **/
@Slf4j
public class LogUtil {

    private static final List<String> deployList = new CopyOnWriteArrayList<>();

    private static final Map<String, String> deployMap = new ConcurrentHashMap<>(16);

    private static final Logger logger = Logger.getLogger(EasyJenkinsEnum.EASY_JENKINS.getParam());

    public static void severe(String msg) {
        logger.log(Level.SEVERE, msg);
    }

    public static void warning(String msg) {
        logger.log(Level.WARNING, msg);
    }

    public static void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    public static void info(String msg, String connectId, String host) {
        logger.log(Level.INFO, msg);
        deployList.add(connectId + "=" + msg);
        deployMap.put(host + "=" + connectId, msg);
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (msg.equals(EasyJenkinsEnum.SUCCESSFULLY_DEPLOYED.getParam())) {
            clearMsg(connectId);
            if (deployList.size() == 0){
                Thread[] threads = new Thread[Thread.activeCount()];
                Thread.enumerate(threads);
                for (Thread thread : threads) {
                    if (thread != null) {
                        SftpUtil.clearSSHClient();
                    }
                }
            }
        }
        if (deployMap.get(host + "=" + connectId) != null) {
            if (deployMap.get(host + "=" + connectId).equals(EasyJenkinsEnum.SUCCESSFULLY_DEPLOYED.getParam())) {
                deployMap.remove(host + "=" + connectId);
            }
        }
    }


    public static Map<String, String> getDeployMap() {
        return deployMap;
    }


    public static void clearMsg(String connectId) {
        deployList.removeIf(s -> s.contains(connectId));
    }

    public static boolean getDeployState(String connectId) {
        for (String deploy : deployList) {
            if (deploy.contains(connectId)) {
                return true;
            }
        }
        return false;
    }

    public static boolean getDeployStateSleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (String deploy : deployList) {
            if (deploy.contains("上传文件索引")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isStart(){
        return deployList.size() > 0;
    }

    public static String msg(){
        return deployMap.size()==1?"上传完成":"已完成部署";
    }

}
