package com.easy.controller;

import com.easy.bean.DataStructure;
import com.easy.bean.DeployRecord;
import com.easy.bean.vo.RecordVo;
import com.easy.service.DataStructureService;
import com.easy.service.QueryDataStructureService;
import com.easy.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    QueryDataStructureService queryDataStructureService;

    @GetMapping("/list")
    public ResponseEntity<Object> list(@RequestParam Integer pageIndex) {
        DataStructure dataStructure = queryDataStructureService.getDataStructure();
        List<DeployRecord> deployRecordList = DataStructureService.initFile(dataStructure.getDataPath(), "record", dataStructure.getActive(), DeployRecord.class);
        deployRecordList.sort((m1, m2) -> m2.getCreateTime().compareTo(m1.getCreateTime()));
        PageUtil<DeployRecord> pageUtil = new PageUtil<>();
        List<DeployRecord> pageList = pageUtil.page(deployRecordList, 20, pageIndex);
        return ResponseEntity.ok(new RecordVo(deployRecordList.size(), pageList, count(deployRecordList, "vue"), count(deployRecordList, "springboot")));
    }

    public long count(List<DeployRecord> deployRecords, String name) {
        return deployRecords.stream().filter(s -> s.getTypeName().equals(name)).count();
    }
}
