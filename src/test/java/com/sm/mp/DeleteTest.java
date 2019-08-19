package com.sm.mp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.sm.mp.dao.UserMapper;
import com.sm.mp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteTest {
    @Autowired
    private UserMapper userMapper;

    //1、根据id删除的方法
    @Test
    public void deleteById() {
        long id = 1161099519237033986L;
        //DEBUG==>  Preparing: DELETE FROM user WHERE id=?
        //DEBUG==> Parameters: 1161099519237033986(Long)
        int rows = userMapper.deleteById(id);
        System.out.println("影响记录数为：" + rows);
    }

    //deleteBatchIds
    @Test
    public void deleteBatchIds() {
        //DEBUG==>  Preparing: DELETE FROM user WHERE id IN ( ? , ? )
        //DEBUG==> Parameters: 1162211174062874625(Long), 1162211134061682689(Long)
        int rows = userMapper.deleteBatchIds(Arrays.asList(1162211174062874625L, 1162211134061682689L));
        System.out.println("影响记录数为：" + rows);
    }

    //2、其他普通删除方法
    //deleteByMap   与selectByMap类似
    @Test
    public void deleteByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", "向北");
        columnMap.put("age", 25);
        //DEBUG==>  Preparing: DELETE FROM user WHERE name = ? AND age = ?
        //DEBUG==> Parameters: 向北(String), 25(Integer)
        int rows = userMapper.deleteByMap(columnMap);
        System.out.println("影响记录数为：" + rows);
    }

    //3、以条件构造器为参数的删除方法
    //参考查询构造器
    //lambda
    @Test
    public void deleteByWrapperLambda() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        lambdaQueryWrapper.eq(User::getAge, 27).or().gt(User::getAge, 50);
        //DEBUG==>  Preparing: DELETE FROM user WHERE age = ? OR age > ?
        //DEBUG==> Parameters: 27(Integer), 50(Integer)
        int rows = userMapper.delete(lambdaQueryWrapper);
        System.out.println("影响记录数为：" + rows);
    }

    @Test
    public void deleteByWrapperLambda2() {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = Wrappers.<User>lambdaUpdate();
        lambdaUpdateWrapper.eq(User::getName, "向北").or().gt(User::getAge, 50);
        //DEBUG==>  Preparing: DELETE FROM user WHERE name = ? OR age > ?
        //DEBUG==> Parameters: 向北(String), 50(Integer)
        int rows = userMapper.delete(lambdaUpdateWrapper);
        System.out.println("影响记录数为：" + rows);
    }
    //使用条件构造器进行删除操作，可以使用QueryWrapper，也可以使用UpdateWrapper
    //使用UpdateWrapper进行的set操作会被忽略掉，数据会删除成功
    //LambdaQueryChainWrapper无法执行删除操作，使用LambdaUpdateChainWrapper进行lambda链式编程进行删除
    @Test
    public void deleteByWrapperLambdaChain() {
        //DEBUG==>  Preparing: DELETE FROM user WHERE name = ? OR age > ?
        //DEBUG==> Parameters: 向北(String), 50(Integer)
       boolean result = new LambdaUpdateChainWrapper<User>(userMapper)
               .eq(User::getName, "向北").or().gt(User::getAge, 50)
               .remove();
        System.out.println("删除结果为：" + result);
    }
}
