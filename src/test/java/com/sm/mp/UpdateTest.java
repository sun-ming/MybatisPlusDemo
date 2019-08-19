package com.sm.mp;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.sm.mp.dao.UserMapper;
import com.sm.mp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateTest {
    @Autowired
    private UserMapper userMapper;

    //1、根据id更新
    @Test
    public void updateById() {
        User user = new User();
        user.setId(1161099519237033986L);
        user.setAge(26);
        user.setEmail("xb2@baomidou.com");
        //DEBUG==>  Preparing: UPDATE user SET age=?, email=? WHERE id=?
        //DEBUG==> Parameters: 26(Integer), xb2@baomidou.com(String), 1161099519237033986(Long)
        int rows = userMapper.updateById(user);
        System.out.println("影响记录数为：" + rows);
    }
    //实体中变量出现在set中，updateWrapper条件出现在where中，
    //updateWrapper中传入实体，实体的变量也会出现在where中，与wrapper条件互不干扰
    @Test
    public void updateByWrapper() {
        User user = new User();
        user.setAge(29);
        user.setEmail("lyw2@baomidou.com");
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = Wrappers.<User>lambdaUpdate();
        lambdaUpdateWrapper.eq(User::getName, "李艺伟").eq(User::getAge, 28);
        //DEBUG==>  Preparing: UPDATE user SET age=?, email=? WHERE name = ? AND age = ?
        //DEBUG==> Parameters: 29(Integer), lyw2@baomidou.com(String), 李艺伟(String), 28(Integer)
        int rows = userMapper.update(user, lambdaUpdateWrapper);
        System.out.println("影响记录数为：" + rows);
    }
    @Test
    public void updateByWrapper2() {
        User setUser = new User();
        setUser.setAge(30);
        setUser.setEmail("lyw3@baomidou.com");

        User whereUser = new User();
        whereUser.setName("李艺伟");

        LambdaUpdateWrapper<User> lambdaUpdateWrapper = Wrappers.<User>lambdaUpdate(whereUser);
        lambdaUpdateWrapper.eq(User::getName, "李艺伟").eq(User::getAge, 29);
        //DEBUG==>  Preparing: UPDATE user SET age=?, email=? WHERE name=? AND name = ? AND age = ?
        //DEBUG==> Parameters: 30(Integer), lyw3@baomidou.com(String), 李艺伟(String), 李艺伟(String), 29(Integer)
        int rows = userMapper.update(setUser, lambdaUpdateWrapper);
        System.out.println("影响记录数为：" + rows);
    }

    //2、updateWrapper的set用法，适用于修改少量字段时使用
    @Test
    public void updateByWrapper3() {
        UpdateWrapper<User> updateWrapper = Wrappers.<User>update();
        //DEBUG==>  Preparing: UPDATE user SET age=? WHERE name = ? AND age = ?
        //DEBUG==> Parameters: 31(Integer), 李艺伟(String), 30(Integer)
        updateWrapper.eq("name", "李艺伟").eq("age", 30).set("age", 31);
        int rows = userMapper.update(null, updateWrapper);
        System.out.println("影响记录数为：" + rows);
    }

    //lambda用法
    @Test
    public void updateByWrapperLambda() {
        //LambdaUpdateWrapper<User> lambdaUpdateWrapper1 = new UpdateWrapper<User>().lambda();
        //LambdaUpdateWrapper<User> lambdaUpdateWrapper2 = new LambdaUpdateWrapper<User>();
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = Wrappers.<User>lambdaUpdate();
        lambdaUpdateWrapper.eq(User::getName, "李艺伟").eq(User::getAge, 31).set(User::getAge, 32);
        //DEBUG==>  Preparing: UPDATE user SET age=? WHERE name = ? AND age = ?
        //DEBUG==> Parameters: 32(Integer), 李艺伟(String), 31(Integer)
        int rows = userMapper.update(null, lambdaUpdateWrapper);
        System.out.println("影响记录数为：" + rows);
    }
    @Test
    public void updateByWrapperLambdaChain() {
        //DEBUG==>  Preparing: UPDATE user SET age=? WHERE name = ? AND age = ?
        //DEBUG==> Parameters: 33(Integer), 李艺伟(String), 32(Integer)
        boolean result = new LambdaUpdateChainWrapper<User>(userMapper)
                .eq(User::getName, "李艺伟")
                .eq(User::getAge, 32).set(User::getAge, 33).update();
        System.out.println("更新结果为：" + result);
    }

}
