package com.sm.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * id字段默认为‘id’,MP默认使用基于雪花算法的自增id
 * 表明和字段默认与数据库对应，默认下划线转驼峰
 *
 * 可使用注解TableName,TableId,TableField
 *
 * 排除非表字段的三种方式、transient、static、@TableField(exist = false)
 */
@Data
//@TableName("sys_user")
@EqualsAndHashCode(callSuper = false)
public class User extends Model<User> {

    /*@TableId
    private Long userId;*/
    /*@TableField("name")
    private String realName;*/

    private Long id;

    private String name;

    private Integer age;

    private String email;
    //直属上级
    private Long managerId;

    private LocalDateTime createTime;

}
