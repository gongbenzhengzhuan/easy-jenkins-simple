package com.easy.deploy.dir;


import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.easy.annotation.IfMapper;
import com.easy.annotation.find.ReturnObject;
import com.easy.deploy.abs.DeployBuilder;
import com.easy.deploy.util.LogUtil;
import com.easy.deploy.vo.DeployConnect;
import com.easy.service.QueryDataStructureService;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@IfMapper(pack = "com.easy.deploy.impl")
@Component
public class DeployDirector {

    DeployBuilder deployBuilder;

    @Autowired
    QueryDataStructureService queryDataStructureService;

    @Async
    public void start(String tag, DeployConnect deployConnect) {
        this.deployBuilder = ReturnObject.getObj(DeployDirector.class, tag);
        this.deployBuilder.setDeployConnect(deployConnect);
        try {
            deployBuilder.mavenClean();
            deployBuilder.mavenInstall();
            deployBuilder.npmBuild();
            deployBuilder.processNumber();
            deployBuilder.closeProcess();
            deployBuilder.delete();
            deployBuilder.initDeployPath();
            deployBuilder.mkdir();
            deployBuilder.uploading();
            deployBuilder.run();
            deployBuilder.saveDeployRecord();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final ArrayBlockingQueue workQueue = new ArrayBlockingQueue(10);

    @Async
    public void bach(JSONArray jsonArray){
        int maximumPoolSizeSize = 100;
        int corePoolSize = 10;
        long keepAliveTime = 1;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSizeSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                new ThreadFactoryBuilder().setNamePrefix("XX-task-%d").build());
        executor.execute(()-> vue(jsonArray));
        executor.execute(()-> springboot(jsonArray));
    }

    public void vue(JSONArray jsonArray){
        JSONObject vueObj = (JSONObject) jsonArray.get(0);
        DeployConnect vueDeployConnect = queryDataStructureService.getById(vueObj.getStr("connectId"));
        start(vueDeployConnect.getTypeName(), vueDeployConnect);
    }

    public void springboot(JSONArray jsonArray){
        while (true) {
            if (LogUtil.getDeployStateSleep()) {
                JSONObject obj = (JSONObject) jsonArray.get(1);
                DeployConnect deployConnect = queryDataStructureService.getById(obj.getStr("connectId"));
                start(deployConnect.getTypeName(), deployConnect);
                break;
            }
        }
    }

}
