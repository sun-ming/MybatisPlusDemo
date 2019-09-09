package com.sm.mp.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Auther: sunming
 * @Date: 2019-9-7 16:41
 * @Description:
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        //session中获取相关填充信息
        boolean hasSetter = metaObject.hasSetter("createTime");
        if (hasSetter) {//代表有这个属性的时候，再执行填充
            System.out.println("insertFill...");
            setInsertFieldValByName("createTime", LocalDateTime.now(), metaObject);//实体类中的属性名
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //session中获取相关填充信息
        Object val = getFieldValByName("updateTime", metaObject);
        if (val == null) {//获取不到设置的值，就进行填充
            System.out.println("updateFill...");
            setUpdateFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
    }
}
