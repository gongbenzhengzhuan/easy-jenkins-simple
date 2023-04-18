package com.easy.controller;

import cn.hutool.core.util.StrUtil;
import com.easy.bean.DataStructure;
import com.easy.util.CustomerFileUtil;
import com.easy.bean.vo.BasicInformationVo;
import com.easy.enums.EasyJenkinsEnum;
import com.easy.service.QueryDataStructureService;
import com.easy.util.PreferencesJenkinsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 武天
 * @date 2022/12/1 18:07
 */
@RestController
@RequestMapping("/setting")
public class SettingController {

    @Autowired
    QueryDataStructureService queryDataStructureService;

    @PostMapping("/editSetting")
    public ResponseEntity<Map<String, Object>> editSetting(@RequestBody BasicInformationVo basicInfo) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("code", 200);
        objectMap.put("msg", "成功");
        if (StrUtil.isBlank(basicInfo.getHost().toString())) {
            objectMap.put("code", 500);
            objectMap.put("msg", "端口号不能为空");
            return ResponseEntity.ok(objectMap);
        }
        if (StrUtil.isBlank(basicInfo.getMavenPath())) {
            objectMap.put("code", 500);
            objectMap.put("msg", "maven路径不能为空");
            return ResponseEntity.ok(objectMap);
        }
        if (StrUtil.isBlank(basicInfo.getRegistryPath())) {
            objectMap.put("code", 500);
            objectMap.put("msg", "安装路径不能为空");
            return ResponseEntity.ok(objectMap);
        }

        //总的 .jenkins文件数据
        DataStructure dataStructure = queryDataStructureService.getDataStructure();
        String oldRPath = dataStructure.getDataPath();
        dataStructure.setEasyJenkinsPort(basicInfo.getHost())
                .setMavenPath(basicInfo.getMavenPath())
                .setDataPath(basicInfo.getRegistryPath());

        String newRPath = dataStructure.getDataPath();
        queryDataStructureService.setDataStructure(dataStructure);
        if (!oldRPath.equals(newRPath)) {
            CustomerFileUtil.copyFolder(oldRPath, newRPath);
            CustomerFileUtil.deleteDir(new File(oldRPath));
        }
        PreferencesJenkinsUtil.put(EasyJenkinsEnum.EASY_JENKINS_PATH.getParam(), dataStructure.getDataPath());
        return ResponseEntity.ok(objectMap);
    }

}
