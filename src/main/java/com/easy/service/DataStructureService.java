package com.easy.service;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.easy.bean.DataBranch;
import com.easy.bean.DataStructure;
import com.easy.bean.DeployRecord;
import com.easy.deploy.vo.DeployConnect;
import com.easy.deploy.util.LogUtil;
import com.easy.enums.EasyJenkinsEnum;
import com.easy.util.DESUtil;
import com.easy.util.PreferencesJenkinsUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class DataStructureService {


    private static String[] fileName(String localPath) {
        String fileId = DESUtil.encryption(String.valueOf(System.currentTimeMillis()));
        return new String[]{localPath, fileId};
    }


    public static String[] touchJSON(String localPath) {
        String[] fileNames = fileName(localPath);
        File file = new File(fileNames[0] + "\\" + fileNames[1] + ".jenkins");
        if (!file.isFile()) {
            FileUtil.writeUtf8String("", file);
        }
        return fileNames;
    }

    /**
     * 初始化记录
     * 即
     * 可以获取记录
     */
    public static <T> T initFile(String localPath, String dirName, String fileName, Class<?> cls) {
        String path = localPath + "\\" + dirName + "\\" + fileName + ".jenkins";
        File file = new File(path);
        if (!file.isFile()) {
            FileUtil.writeUtf8String("", file);
            return (T) new ArrayList<T>();
        } else {
            try {
                String json = FileUtil.readUtf8String(path);
                JSONArray jsonArray = new JSONArray(json);
                return (T) JSONUtil.toList(jsonArray, cls);
            } catch (Exception e) {
                return (T) new ArrayList<T>();
            }
        }
    }

    /**
     * 将新的集合塞到文件中
     *
     * @param localPath
     * @param fileName
     * @param objectList 新的集合
     */
    public static void setFile(String localPath, String dirName, String fileName, Object objectList) {
        String path = localPath + "\\" + dirName + "\\" + fileName + ".jenkins";
        File file = new File(path);
        if (file.isFile()) {
            FileUtil.writeUtf8String(JSON.toJSONString(objectList, SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat), file);
        } else {
            LogUtil.info("当前文件不存在....文件路径为:" + path);
        }
    }


    /**
     * 程序第一次启动初始化方法
     *
     * @param localPath
     * @param mavenPath
     * @param port
     */
    public static void initJSON(String localPath, String mavenPath, int port) {
        String[] touchJSON = touchJSON(localPath);
        DataStructure dataStructure = new DataStructure();
        dataStructure.setFileId(touchJSON[1]);
        dataStructure.setDataPath(touchJSON[0]);
        dataStructure.setEasyJenkinsPort(port);
        dataStructure.setMavenPath(mavenPath);
        Map<String, List<DeployConnect>> dataStructureDeployPathMap = dataStructure.getDeployPathMap();
        List<DeployConnect> deployConnectList = new ArrayList<>();
        dataStructureDeployPathMap.put("jenkins", deployConnectList);
        dataStructure.setActive("jenkins");
        String jsonFormatString = JSON.toJSONString(dataStructure, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
        FileUtil.writeUtf8String(jsonFormatString, touchJSON[0] + "\\" + touchJSON[1] + ".jenkins");
        PreferencesJenkinsUtil.put(EasyJenkinsEnum.EASY_JENKINS_PATH.getParam(), touchJSON[0]);
        PreferencesJenkinsUtil.put(EasyJenkinsEnum.EASY_JENKINS_FILE_ID.getParam(), touchJSON[1]);
        DataStructureService.initFile(dataStructure.getDataPath(), "branch", dataStructure.getActive(), DataBranch.class);
    }

}
