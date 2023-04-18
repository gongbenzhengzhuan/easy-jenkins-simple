package com.easy.deploy.vo;

import lombok.Data;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@Data
public class DeployConnect {

    /**
     * 连接id
     */
    private String connectId;

    /**
     * 执行的命令
     */
    private String exec;

    /**
     * 是否正在部署
     * true= 是  false = 否
     */
    private boolean deployState;

    /**
     * 部署的项目类型
     */
    private String typeName;

    /**
     * 名称
     */
    private String name;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 服务器地址
     */
    private String host;
    /**
     * 服务器端口号
     */
    private int port;
    /**
     * 服务器账号
     */
    private String username;
    /**
     * 服务器密码
     */
    private String password;
    /**
     * 需要上传的jar名称
     */
    private String jarName;
    /**
     * 服务器地址
     */
    private String serverPath;
    /**
     * 本地地址
     */
    private String localPath;

    /**
     * vue路径的根目录
     */
    private String vueRootLocalPath;

    /**
     * 部署项目的端口号
     */
    private int projectPort;

    /**
     * maven路径地址
     * 如：C:\apache-maven-3.6.3
     */
    private String mavenPath;

    /**
     * 项目pom.xml路径地址
     */
    private String pomXmlPath;


    public DeployConnect() {
    }

    public DeployConnect(String typeName, String host, int port, String username, String password, String serverPath, String localPath, String vueRootLocalPath) {
        this.typeName = typeName;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.serverPath = serverPath;
        this.localPath = localPath;
        this.vueRootLocalPath = vueRootLocalPath;
    }

    public DeployConnect(String typeName, String host, int port, String username, String password, String jarName, String serverPath, String localPath, int projectPort, String mavenPath, String pomXmlPath) {
        this.typeName = typeName;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.jarName = jarName;
        this.serverPath = serverPath;
        this.localPath = localPath;
        this.projectPort = projectPort;
        this.mavenPath = mavenPath;
        this.pomXmlPath = pomXmlPath;
    }
}
