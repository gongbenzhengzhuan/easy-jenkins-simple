package com.easy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeployRecord {

    /**
     * 部署的项目类型
     */
    private String typeName;

    /**
     * 名称
     */
    private String name;

    /**
     * 部署时间
     */
    private String createTime;

    /**
     * 服务器地址
     */
    private String host;
}
