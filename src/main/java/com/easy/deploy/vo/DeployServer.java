package com.easy.deploy.vo;

import java.util.Set;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class DeployServer {

    /**
     * 进程号
     */
    private Set<String> processNumber;

    public DeployServer() {
    }

    public DeployServer(Set<String> processNumber) {
        this.processNumber = processNumber;
    }

    public Set<String> getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(Set<String> processNumber) {
        this.processNumber = processNumber;
    }
}
