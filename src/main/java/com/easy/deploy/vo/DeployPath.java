package com.easy.deploy.vo;

import java.util.*;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class DeployPath {

    private Set<String> dirList = new HashSet<>();

    private Set<String> dirServerList = new HashSet<>();

    private List<String> fileList = new ArrayList<>();

    private Map<String, String> uploadMap = new HashMap<>();

    private String rootPath;

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Map<String, String> getUploadMap() {
        return uploadMap;
    }

    public void setUploadMap(Map<String, String> uploadMap) {
        this.uploadMap = uploadMap;
    }

    public Set<String> getDirServerList() {
        return dirServerList;
    }

    public void setDirServerList(Set<String> dirServerList) {
        this.dirServerList = dirServerList;
    }

    public Set<String> getDirList() {
        return dirList;
    }

    public void setDirList(Set<String> dirList) {
        this.dirList = dirList;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }
}
