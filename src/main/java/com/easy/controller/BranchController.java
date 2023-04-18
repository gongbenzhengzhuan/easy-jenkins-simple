package com.easy.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.easy.bean.DataBranch;
import com.easy.bean.DataStructure;
import com.easy.bean.DeployRecord;
import com.easy.deploy.vo.DeployConnect;
import com.easy.service.DataStructureService;
import com.easy.service.QueryDataStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;

/**
 * @author 武天
 * @date 2022/12/2 14:58
 */
@RestController
@RequestMapping("/branch")
public class BranchController {

    @Autowired
    QueryDataStructureService dataStructureService;

    @PostMapping("/saveBranch")
    public ResponseEntity<Map<String,Object>> save(@RequestBody Map<String,String> map){
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("code",200);
        objectMap.put("msg","成功");
        if (StrUtil.isBlank(map.get("branchName"))){
            objectMap.put("code",500);
            objectMap.put("msg","分支名不能为空");
            return ResponseEntity.ok(objectMap);
        }
        DataStructure dataStructure = dataStructureService.getDataStructure();
        if(dataStructure.getDeployPathMap().get(map.get("branchName"))!=null){
            objectMap.put("code",500);
            objectMap.put("msg","该分支名已存在,不允许存在相同的分支名");
            return ResponseEntity.ok(objectMap);
        }
        List<DataBranch> branchList = DataStructureService.initFile(dataStructure.getDataPath(), "branch", "easy-branch", DataBranch.class);
        branchList.add(new DataBranch().setCount(0).setCreateTime(DateUtil.now())
                .setName(map.get("branchName")).setStatus(0));
        DataStructureService.setFile(dataStructure.getDataPath(),"branch","easy-branch", branchList);
        //添加分支时添加分支部署记录文件
        DataStructureService.initFile(dataStructure.getDataPath(),"record",map.get("branchName"),DeployRecord.class);
        return ResponseEntity.ok(objectMap);
    }

    @GetMapping("/editBranch/{oldBranchName}/{newBranchName}")
    public ResponseEntity<Map<String,Object>> info(@PathVariable String oldBranchName,@PathVariable String newBranchName){
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("code",200);
        objectMap.put("msg","成功");
        DataStructure dataStructure = dataStructureService.getDataStructure();
        List<DataBranch> branchList = DataStructureService.initFile(dataStructure.getDataPath(), "branch", "easy-branch", DataBranch.class);
        long count = branchList.stream().filter(i->i.getName().equals(newBranchName)).count();
        if(count==0){
            for (DataBranch branch : branchList) {
                if(branch.getName().equals(oldBranchName)){
                    branch.setName(newBranchName);
                }
            }
        }else{
            objectMap.put("code",500);
            objectMap.put("msg","该分支名已存在,不允许存在相同的分支名");
            return ResponseEntity.ok(objectMap);
        }
        Map<String,List<DeployConnect>> delopMap = dataStructure.getDeployPathMap();
        List<DeployConnect> deployConnects = delopMap.get(oldBranchName);
        delopMap.remove(oldBranchName);
        delopMap.put(newBranchName,deployConnects);
        if (dataStructure.getActive().equals(oldBranchName)){
            dataStructure.setDeployPathMap(delopMap).setActive(newBranchName);
        }
        DataStructureService.setFile(dataStructure.getDataPath(),"branch","easy-branch", branchList);
        dataStructureService.setDataStructure(dataStructure);
        //修改分支时修改分支部署记录文件名
        List<DeployRecord> deployRecords = DataStructureService.initFile(dataStructure.getDataPath(),"record",oldBranchName,DeployRecord.class);
        DataStructureService.initFile(dataStructure.getDataPath(),"record",newBranchName,DeployRecord.class);
        DataStructureService.setFile(dataStructure.getDataPath(),"record", newBranchName,deployRecords);
        //删除改名前的部署记录文件
        new File(dataStructure.getDataPath()+"\\record\\"+oldBranchName+".jenkins").delete();
        return ResponseEntity.ok(objectMap);
    }

    @GetMapping("/switchPrimaryBranch/{branchName}")
    public ResponseEntity<Map<String,Object>> switchPrimaryBranch(@PathVariable String branchName){
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("code",200);
        objectMap.put("msg","成功");
        DataStructure dataStructure = dataStructureService.getDataStructure();
        List<DataBranch> branchList = DataStructureService.initFile(dataStructure.getDataPath(), "branch", "easy-branch", DataBranch.class);
        List<DeployConnect> deployConnects = dataStructure.getDeployPathMap().get(branchName);
        branchList.forEach(i->i.setStatus(0));
        branchList.forEach(i->{
            if(i.getName().equals(branchName)){
                i.setStatus(1).setCount(deployConnects == null?0:deployConnects.size());
            }
        });
        DataStructureService.setFile(dataStructure.getDataPath(),"branch","easy-branch", branchList);
        Map<String,List<DeployConnect>> deloyMap = dataStructure.getDeployPathMap();
        deloyMap.computeIfAbsent(branchName, k -> new ArrayList<>());
        dataStructure.setActive(branchName).setDeployPathMap(deloyMap);
        dataStructureService.setDataStructure(dataStructure);
        return ResponseEntity.ok(objectMap);
    }

    @GetMapping("/deleteBranch/{branchName}")
    public ResponseEntity<Map<String,Object>> deleteBranch(@PathVariable String branchName){
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("code",200);
        objectMap.put("msg","成功");
        DataStructure dataStructure = dataStructureService.getDataStructure();
        List<DataBranch> branchList = DataStructureService.initFile(dataStructure.getDataPath(), "branch", "easy-branch", DataBranch.class);
        if(branchList.size()==1){
            objectMap.put("code",501);
            objectMap.put("msg","删除失败,当前系统必须保留至少一个分支");
        } else if(branchList.size()>1&&dataStructure.getActive().equals(branchName)){
            objectMap.put("code",501);
            objectMap.put("msg","删除失败,当前分支正在使用中,请切换其他分支后再删除该分支");
        }else{
            branchList.removeIf(i->i.getName().equals(branchName));
            if(dataStructure.getActive().equals(branchName)){
                Map<String,List<DeployConnect>> map = dataStructure.getDeployPathMap();
                map.remove(branchName);
                dataStructure.setDeployPathMap(map);
            }
            DataStructureService.setFile(dataStructure.getDataPath(),"branch","easy-branch", branchList);
            new File(dataStructure.getDataPath()+"\\record\\"+branchName+".jenkins").delete();
        }
        dataStructureService.setDataStructure(dataStructure);
        return ResponseEntity.ok(objectMap);
    }

}
