package com.easy.deploy.impl;

import com.easy.annotation.tag;
import com.easy.deploy.abs.DeployBuilder;
import com.easy.deploy.util.LogUtil;
import com.easy.deploy.util.NpmUtil;
import com.easy.deploy.util.SftpUtil;
import com.easy.enums.EasyJenkinsEnum;

import java.io.IOException;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@tag(name = "vue")
public class VueBuilder extends DeployBuilder {

    @Override
    public void npmBuild() {
        LogUtil.info("开始构造vue项目", deployConnect.getConnectId(), deployConnect.getHost());
        try {
            NpmUtil.runExecution(deployConnect.getVueRootLocalPath(), "npm run build");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        LogUtil.info("vue项目构造完成", deployConnect.getConnectId(), deployConnect.getHost());
    }

    @Override
    public void delete() throws IOException {
        LogUtil.info("开始删除远程的文件", deployConnect.getConnectId(), deployConnect.getHost());
        SftpUtil.exec(
                deployConnect.getHost(),
                deployConnect.getPort(),
                deployConnect.getUsername(),
                deployConnect.getPassword(),
                "rm -rf " + deployConnect.getServerPath(),1);
        LogUtil.info("删除完成", deployConnect.getConnectId(), deployConnect.getHost());
    }

    @Override
    public void uploading() {
        LogUtil.info("开始批量上传...", deployConnect.getConnectId(), deployConnect.getHost());
        SftpUtil.uploadFiles(deployConnect.getHost(), deployConnect.getPort(),
                deployConnect.getUsername(), deployConnect.getPassword(),deployConnect.getConnectId(),
                deployConnect.getLocalPath(),deployConnect.getServerPath());
        LogUtil.info(EasyJenkinsEnum.SUCCESSFULLY_DEPLOYED.getParam(), deployConnect.getConnectId(), deployConnect.getHost());
    }
}
