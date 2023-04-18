package com.easy.bean.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 武天
 * @date 2022/12/1 15:53
 */
@Data
@Accessors(chain = true)
public class BasicInformationVo {

    private Integer host;

    private String mavenPath;

    private String registryPath;
}
