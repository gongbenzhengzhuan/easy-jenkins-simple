package com.easy.bean;

import com.easy.deploy.vo.DeployConnect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DataStructure {

    /**
     * 当前文件id
     */
    private String fileId;

    /**
     * 数据存放的位置
     */
    private String dataPath;

    /**
     * 项目端口号
     */
    private int easyJenkinsPort;


    /**
     * maven的总路径
     */
    private String mavenPath;

    /**
     * 选择的分支
     */
    private String active;


    /**
     * @{param} String 数据分支名称
     * @{param} List<DeployPath> 连接的数据源
     * 每个分支可以有多个数据源
     */
    private Map<String, List<DeployConnect>> deployPathMap = new HashMap<>();

}
