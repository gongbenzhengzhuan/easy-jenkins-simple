package com.easy.enums;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public enum EasyJenkinsEnum {

    EASY_JENKINS_PATH(1, "easy_jenkins_path"),
    EASY_JENKINS_FILE_ID(2, "easy_jenkins_file_id"),
    EASY_JENKINS(3, "easy-jenkins"),
    SUCCESSFULLY_DEPLOYED(4, "Successfully deployed");

    private int code;

    private String param;

    EasyJenkinsEnum(int code, String param) {
        this.code = code;
        this.param = param;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

}
