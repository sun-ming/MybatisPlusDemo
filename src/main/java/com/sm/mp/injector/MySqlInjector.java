package com.sm.mp.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.additional.InsertBatchSomeColumn;
import com.sm.mp.method.DeleteAllMethod;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: sunming
 * @Date: 2019-9-7 21:50
 * @Description:
 */
@Component
public class MySqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList =  super.getMethodList(mapperClass);
        methodList.add(new DeleteAllMethod());
        //选装件
        methodList.add(new InsertBatchSomeColumn(t->!t.isLogicDelete()&&!t.getColumn().equals("age")));
        return methodList;
    }
}
