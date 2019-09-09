package com.sm.mp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class User2 {

    private Long id;

    private String name;

    private Integer age;

    private String email;
    //直属上级
    private Long managerId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    @Version
    private Integer version;

    @TableLogic
    @TableField(select = false) //查询时，排除逻辑删除字段
    private Integer deleted;

}
