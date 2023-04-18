package com.easy.deploy.impl;


import com.easy.annotation.tag;
import com.easy.deploy.abs.DeployBuilder;
import com.easy.deploy.util.LogUtil;
import com.easy.deploy.util.MavenUtil;
import com.easy.deploy.util.SftpUtil;
import com.easy.enums.EasyJenkinsEnum;

import java.io.IOException;
import java.util.HashSet;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@tag(name = "springboot")
public class SpringBootBuilder extends DeployBuilder {

    @Override
    public void mavenClean() {
        LogUtil.info("开始执行maven命令：clean", deployConnect.getConnectId(), deployConnect.getHost());
        MavenUtil.mvn(deployConnect.getMavenPath(), deployConnect.getPomXmlPath(), "clean");
        LogUtil.info("清除完成", deployConnect.getConnectId(), deployConnect.getHost());
    }

    @Override
    public void mavenInstall() {
        LogUtil.info("开始执行maven命令：install", deployConnect.getConnectId(), deployConnect.getHost());
        MavenUtil.mvn(deployConnect.getMavenPath(), deployConnect.getPomXmlPath(), "install");
        LogUtil.info("打包完成", deployConnect.getConnectId(), deployConnect.getHost());
    }

    @Override
    public void processNumber() throws IOException {
        LogUtil.info("开始获取进程号", deployConnect.getConnectId(), deployConnect.getHost());
        deployServer.setProcessNumber(SftpUtil.exec(
                deployConnect.getHost(),
                deployConnect.getPort(),
                deployConnect.getUsername(),
                deployConnect.getPassword(),
                "lsof -i:" + deployConnect.getProjectPort(), new HashSet<>()));
        LogUtil.info("获取完成", deployConnect.getConnectId(), deployConnect.getHost());
    }

    @Override
    public void closeProcess() throws IOException {
        LogUtil.info("开始杀死进程", deployConnect.getConnectId(), deployConnect.getHost());
        if (deployServer.getProcessNumber().size() != 0) {
            StringBuilder result = new StringBuilder();
            for (String number : deployServer.getProcessNumber()) {
                if (result.toString().equals("")) {
                    result = new StringBuilder(number);
                } else {
                    result.append(" ").append(number);
                }
            }
            SftpUtil.exec(
                    deployConnect.getHost(),
                    deployConnect.getPort(),
                    deployConnect.getUsername(),
                    deployConnect.getPassword(),
                    "kill -9 " + result.toString(),1);
        } else {
            LogUtil.info("当前进程为空", deployConnect.getConnectId(), deployConnect.getHost());
        }
    }

    @Override
    public void delete() {
        LogUtil.info("文件开始删除", deployConnect.getConnectId(), deployConnect.getHost());
        SftpUtil.delete(deployConnect.getHost(),
                deployConnect.getPort(),
                deployConnect.getUsername(),
                deployConnect.getPassword(),
                deployConnect.getServerPath(),
                deployConnect.getJarName());
        LogUtil.info("删除完成", deployConnect.getConnectId(), deployConnect.getHost());
    }

    @Override
    public void uploading() {
        LogUtil.info("文件开始上传...", deployConnect.getConnectId(), deployConnect.getHost());
        SftpUtil.upload(deployConnect.getHost(),
                deployConnect.getPort(),
                deployConnect.getUsername(),
                deployConnect.getPassword(),
                deployConnect.getServerPath() + "/" + deployConnect.getJarName(),
                deployConnect.getLocalPath(), deployConnect.getConnectId());
        LogUtil.info(LogUtil.msg(), deployConnect.getConnectId(), deployConnect.getHost());
    }

    @Override
    public void run() throws IOException {
        LogUtil.info("开始部署", deployConnect.getConnectId(), deployConnect.getHost());
        LogUtil.info("当前执行命令：" + deployConnect.getExec(), deployConnect.getConnectId(), deployConnect.getHost());
        SftpUtil.exec(
                deployConnect.getHost(),
                deployConnect.getPort(),
                deployConnect.getUsername(),
                deployConnect.getPassword(), deployConnect.getExec(),2);
        LogUtil.info(EasyJenkinsEnum.SUCCESSFULLY_DEPLOYED.getParam(), deployConnect.getConnectId(), deployConnect.getHost());
    }
}
