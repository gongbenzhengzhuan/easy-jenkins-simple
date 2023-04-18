package com.easy.deploy.abs;


import cn.hutool.core.date.DateUtil;
import com.easy.bean.DataStructure;
import com.easy.bean.DeployRecord;
import com.easy.deploy.vo.DeployConnect;
import com.easy.deploy.vo.DeployPath;
import com.easy.deploy.vo.DeployServer;
import com.easy.service.DataStructureService;
import com.easy.service.QueryDataStructureService;

import java.io.IOException;
import java.util.List;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public abstract class DeployBuilder implements DeployBuilderInterface {

    protected DeployConnect deployConnect = new DeployConnect();

    protected DeployServer deployServer = new DeployServer();

    protected DeployPath deployPath;

    public DeployBuilder() {
    }

    @Override
    public void npmBuild() {

    }

    @Override
    public void mavenClean() {

    }

    @Override
    public void mavenInstall() {

    }

    @Override
    public void initDeployPath() {

    }

    @Override
    public void mkdir() throws IOException {

    }

    @Override
    public void processNumber() throws IOException {

    }

    @Override
    public void closeProcess() throws IOException {

    }

    @Override
    public void delete() throws IOException {

    }

    @Override
    public void uploading() {

    }

    @Override
    public void run() throws IOException {

    }

    @Override
    public void saveDeployRecord() {
        QueryDataStructureService dataStructureService = new QueryDataStructureService();
        DataStructure dataStructure = dataStructureService.getDataStructure();
        List<DeployRecord> deployRecordList = DataStructureService.initFile(dataStructure.getDataPath(), "record", dataStructure.getActive(), DeployRecord.class);
        deployRecordList.add(new DeployRecord(deployConnect.getTypeName(), deployConnect.getName(), DateUtil.now(), deployConnect.getHost()));
        DataStructureService.setFile(dataStructure.getDataPath(), "record", dataStructure.getActive(), deployRecordList);
    }

    public DeployConnect getDeployConnect() {
        return deployConnect;
    }

    public void setDeployConnect(DeployConnect deployConnect) {
        this.deployConnect = deployConnect;
    }

    public DeployServer getDeployServer() {
        return deployServer;
    }

    public void setDeployServer(DeployServer deployServer) {
        this.deployServer = deployServer;
    }

    @Override
    public String toString() {
        return "DeployBuilder{" +
                "deployConnect=" + deployConnect +
                ", deployServer=" + deployServer +
                '}';
    }
}
