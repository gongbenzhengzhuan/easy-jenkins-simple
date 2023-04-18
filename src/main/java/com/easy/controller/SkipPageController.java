package com.easy.controller;

import cn.hutool.core.date.DateUtil;
import com.easy.bean.DataBranch;
import com.easy.bean.DataStructure;
import com.easy.bean.DeployRecord;
import com.easy.bean.vo.RecordVo;
import com.easy.deploy.util.LogUtil;
import com.easy.deploy.vo.DeployConnect;
import com.easy.service.DataStructureService;
import com.easy.service.QueryDataStructureService;
import com.easy.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author tanyongpeng
 * <p>页面跳转控制器</p>
 **/
@Controller
public class SkipPageController {

    @Autowired
    QueryDataStructureService queryDataStructureService;

    @GetMapping(value = {"/", "", "/index"})
    public String toMain(Model model, HttpServletRequest request) {
        DataStructure dataStructure = queryDataStructureService.getDataStructure();
        List<DeployConnect> deployConnectList = dataStructure.getDeployPathMap().get(dataStructure.getActive());
        for (DeployConnect deployConnect : deployConnectList) {
            deployConnect.setDeployState(LogUtil.getDeployState(deployConnect.getConnectId()));
        }
        model.addAttribute("deployConnectList", deployConnectList);
        model.addAttribute("branch", dataStructure.getActive());
        String ws = "ws://" + request.getServerName() + ":" + dataStructure.getEasyJenkinsPort() + "/socket/deploy";
        model.addAttribute("wsUrl", ws);
        return "index";
    }

    @GetMapping("/branch")
    public String toBranch(Model model, HttpServletRequest request) {
        DataStructure dataStructure = queryDataStructureService.getDataStructure();
        List<DataBranch> branchList = DataStructureService.initFile(dataStructure.getDataPath(), "branch", "easy-branch", DataBranch.class);
        if (branchList.size() == 0){
            branchList.add(new DataBranch().setCount(0).setCreateTime(DateUtil.now())
                    .setName("jenkins").setStatus(1));
            DataStructureService.setFile(dataStructure.getDataPath(),"branch", "easy-branch",branchList);
        }
        model.addAttribute("branchList", branchList);
        model.addAttribute("branch", dataStructure.getActive());
        return "branch";
    }

    @GetMapping("/record")
    public String toRecord(Model model, HttpServletRequest request) {
        return "record";
    }

    @GetMapping("/settings")
    public String toSettings(Model model, HttpServletRequest request) {
        DataStructure dataStructure = queryDataStructureService.getDataStructure();
        model.addAttribute("dataStructure", dataStructure);
        return "settings";
    }
}
