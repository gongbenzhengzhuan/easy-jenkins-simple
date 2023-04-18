package com.easy.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 数据分支
 *
 * @author 武天
 * @date 2022/12/2 13:56
 */
@Data
@Accessors(chain = true)
public class DataBranch {

    /**
     * 名字
     */
    private String name;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 分支状态  0:未启用 1启用(当前分支为部署分支)
     */
    private Integer status;

    /**
     * 分支数量
     */
    private Integer count;
}
