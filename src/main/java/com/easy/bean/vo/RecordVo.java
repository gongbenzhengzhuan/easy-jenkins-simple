package com.easy.bean.vo;

import com.easy.bean.DeployRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordVo {

    private Integer deployCount;

    private List<DeployRecord> recordList;

    private long vueCount;

    private long springbootCount;
}
