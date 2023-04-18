package com.easy.deploy.abs;

import java.io.IOException;

public interface DeployBuilderInterface {

    void mavenClean();

    void mavenInstall();

    void npmBuild();

    void processNumber() throws IOException;

    void closeProcess() throws IOException;

    void delete() throws IOException;

    void uploading();

    void initDeployPath();

    void mkdir() throws IOException;

    void run() throws IOException;

    void saveDeployRecord();
}
