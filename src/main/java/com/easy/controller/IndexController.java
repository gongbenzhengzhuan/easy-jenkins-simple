package com.easy.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.easy.bean.DataBranch;
import com.easy.bean.DataStructure;
import com.easy.deploy.dir.DeployDirector;
import com.easy.deploy.util.LogUtil;
import com.easy.deploy.vo.DeployConnect;
import com.easy.service.DataStructureService;
import com.easy.service.QueryDataStructureService;
import com.easy.util.DESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    DeployDirector deployDirector;

    @Autowired
    QueryDataStructureService queryDataStructureService;

    @GetMapping("/deploy/{connectId}")
    public ResponseEntity<Map<String, Object>> deploy(@PathVariable String connectId) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("code", 200);
        objectMap.put("msg", "成功");
        if (LogUtil.isStart()) {
            objectMap.put("code", 500);
            objectMap.put("msg", "正在有程序部署,请稍等");
        } else {
            DeployConnect deployConnect = queryDataStructureService.getById(connectId);
            deployDirector.start(deployConnect.getTypeName(), deployConnect);
        }
        return ResponseEntity.ok(objectMap);
    }

    @PostMapping("/batch/deploy")
    public ResponseEntity<Map<String, Object>> batchDeploy(@RequestBody JSONArray jsonArray) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("code", 200);
        objectMap.put("msg", "成功");
        if (LogUtil.isStart()) {
            objectMap.put("code", 500);
            objectMap.put("msg", "正在有程序部署,请稍等");
        } else {
            deployDirector.bach(jsonArray);
        }
        return ResponseEntity.ok(objectMap);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> save(@RequestBody DeployConnect deployConnect) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("code", 200);
        objectMap.put("msg", "成功");
        deployConnect.setCreateTime(DateUtil.now());
        if (StrUtil.isBlank(deployConnect.getHost())) {
            objectMap.put("code", 500);
            objectMap.put("msg", "服务器地址不能为空");
            return ResponseEntity.ok(objectMap);
        }
        deployConnect.setPassword(DESUtil.encryption(deployConnect.getPassword()));
        DataStructure dataStructure = queryDataStructureService.getDataStructure();
        deployConnect.setMavenPath(dataStructure.getMavenPath());
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        deployConnect.setConnectId(snowflake.nextIdStr());
        List<DeployConnect> deployConnects = dataStructure.getDeployPathMap().get(dataStructure.getActive());
//        DeployConnect connect = queryDataStructureService.getByHost(deployConnects, deployConnect.getHost());
//        if (connect != null) {
//            objectMap.put("code", 500);
//            objectMap.put("msg", "存在重复的服务器地址");
//            return ResponseEntity.ok(objectMap);
//        }

        deployConnects.add(deployConnect);
        queryDataStructureService.setDataStructure(dataStructure);
        List<DataBranch> branchList = DataStructureService.initFile(dataStructure.getDataPath(), "branch", "easy-branch", DataBranch.class);
        List<String> branchNames = branchList.stream().filter(i -> i.getName() != null).map(DataBranch::getName).collect(Collectors.toList());
        if (branchNames.contains(dataStructure.getActive())) {
            branchList.forEach(i -> {
                if (i.getName().equals(dataStructure.getActive())) {
                    i.setStatus(1);
                    i.setCount(i.getCount() + 1);
                }
            });
        } else {
            DataBranch dataBranch = new DataBranch().setCreateTime(DateUtil.now())
                    .setCount(1).setName(dataStructure.getActive()).setStatus(1);
            branchList.add(dataBranch);
        }
        DataStructureService.setFile(dataStructure.getDataPath(), "branch", "easy-branch", branchList);
        return ResponseEntity.ok(objectMap);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestBody DeployConnect deployConnect) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("code", 200);
        objectMap.put("msg", "成功");

        if (LogUtil.getDeployState(deployConnect.getConnectId())) {
            objectMap.put("code", 500);
            objectMap.put("msg", "当前服务正在部署，请稍等");
            return ResponseEntity.ok(objectMap);
        }

        if (StrUtil.isBlank(deployConnect.getHost())) {
            objectMap.put("code", 500);
            objectMap.put("msg", "服务器地址不能为空");
            return ResponseEntity.ok(objectMap);
        }
        DataStructure dataStructure = queryDataStructureService.getDataStructure();
        List<DeployConnect> deployConnects = dataStructure.getDeployPathMap().get(dataStructure.getActive());
        DeployConnect oldConnect = null;
        for (DeployConnect connect : deployConnects) {
            if (connect.getConnectId().equals(deployConnect.getConnectId())) {
                oldConnect = connect;
                break;
            }
        }
        deployConnects.removeIf(d -> d.getConnectId().equals(deployConnect.getConnectId()));
      //  DeployConnect dep = queryDataStructureService.getByHost(deployConnects, deployConnect.getHost());
//        if (dep != null) {
//            objectMap.put("code", 500);
//            objectMap.put("msg", "存在重复的服务器地址");
//        } else {
//
//        }
        assert oldConnect != null;
        if (!oldConnect.getPassword().equals(deployConnect.getPassword())) {
            deployConnect.setPassword(DESUtil.encryption(deployConnect.getPassword()));
        }
        deployConnects.add(deployConnect);
        queryDataStructureService.setDataStructure(dataStructure);
        return ResponseEntity.ok(objectMap);
    }

    @GetMapping("/info/{connectId}")
    public ResponseEntity<DeployConnect> info(@PathVariable String connectId) {
        DeployConnect deployConnect = queryDataStructureService.getById(connectId);
        return ResponseEntity.ok(deployConnect);
    }

    @GetMapping("/delete/{connectId}/{currentBranch}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable String connectId, @PathVariable String currentBranch) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("code", 200);
        objectMap.put("msg", "成功");
        if (LogUtil.getDeployState(connectId)) {
            objectMap.put("code", 500);
            objectMap.put("msg", "当前服务正在部署，请稍等");
            return ResponseEntity.ok(objectMap);
        }
        DataStructure dataStructure = queryDataStructureService.getDataStructure();
        List<DeployConnect> deployConnectList = dataStructure.getDeployPathMap().get(dataStructure.getActive());
        boolean anIf = deployConnectList.removeIf(s -> s.getConnectId().equals(connectId));
        queryDataStructureService.setDataStructure(dataStructure);

        List<DataBranch> branchList = DataStructureService.initFile(dataStructure.getDataPath(), "branch", "easy-branch", DataBranch.class);
        branchList.forEach(i -> {
            if (i.getName().equals(currentBranch)) {
                i.setCount(i.getCount() - 1);
            }
        });
        DataStructureService.setFile(dataStructure.getDataPath(), "branch", dataStructure.getActive(), branchList);
        return ResponseEntity.ok(objectMap);
    }

}
