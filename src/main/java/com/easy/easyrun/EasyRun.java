package com.easy.easyrun;

import com.easy.bean.DataStructure;
import com.easy.service.DataStructureService;
import com.easy.service.QueryDataStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@Component
public class EasyRun {

    @Autowired
    QueryDataStructureService queryDataStructureService;

    public void run(String head, String localhost) {
        DataStructure dataStructure = queryDataStructureService.getDataStructure();
        try {
            Runtime.getRuntime().exec("cmd   /c   start " + head + "://" + localhost + ":" + dataStructure.getEasyJenkinsPort() + " ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
