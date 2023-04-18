package com.easy.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.easy.bean.DataStructure;
import com.easy.deploy.vo.DeployConnect;
import com.easy.enums.EasyJenkinsEnum;
import com.easy.util.PreferencesJenkinsUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@Service
public class QueryDataStructureService {

    public DataStructure getDataStructure() {
        try {
            String path = PreferencesJenkinsUtil.get(EasyJenkinsEnum.EASY_JENKINS_PATH.getParam());
            String fileId = PreferencesJenkinsUtil.get(EasyJenkinsEnum.EASY_JENKINS_FILE_ID.getParam());
            if (StrUtil.isNotBlank(path) && StrUtil.isNotBlank(fileId)) {
                String json = FileUtil.readUtf8String(path + "\\" + fileId + ".jenkins");
                return JSONUtil.toBean(json, DataStructure.class);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void setDataStructure(DataStructure dataStructure) {
        String path = PreferencesJenkinsUtil.get(EasyJenkinsEnum.EASY_JENKINS_PATH.getParam());
        String fileId = PreferencesJenkinsUtil.get(EasyJenkinsEnum.EASY_JENKINS_FILE_ID.getParam());
        if (StrUtil.isNotBlank(path) && StrUtil.isNotBlank(fileId)) {
            String jsonFormatString = JSON.toJSONString(dataStructure, SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat);
            FileUtil.writeUtf8String(jsonFormatString, path + "\\" + fileId + ".jenkins");
        }
    }

    public List<DeployConnect> getConnectList() {
        DataStructure dataStructure = getDataStructure();
        return dataStructure.getDeployPathMap().get(dataStructure.getActive());
    }

    public DeployConnect getByHost(List<DeployConnect> deployConnects, String host) {
        for (DeployConnect deployConnect : deployConnects) {
            if (deployConnect.getHost().equals(host)) {
                return deployConnect;
            }
        }
        return null;
    }

    public DeployConnect getById(String connectId) {
        List<DeployConnect> connectList = getConnectList();
        for (DeployConnect deployConnect : connectList) {
            if (deployConnect.getConnectId().equals(connectId)) {
                return deployConnect;
            }
        }
        return null;
    }
}
