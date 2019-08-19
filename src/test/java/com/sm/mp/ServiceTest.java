package com.sm.mp;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sm.mp.entity.User;
import com.sm.mp.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void getOne() {
        //返回多条，抛出异常
        //User one = userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge, 25));
        //返回多条，不抛出异常，给出警告，返回第一个
        User one = userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge, 25), false);
        System.out.println(one);
    }
    //MP为什么把批量操作封装到Service层呢？
    //1、SQL长度有限制，海量数据量单条SQL无法执行，就算可执行也容易引起内存泄露、JDBC连接超时等问题
    //2、不同数据库对单条SQL批量语法不一样，不利于通用

    //每次插入的时候都会做判断，是否设置主键
    //如果没设置主键，执行插入操作
    //如果设置了主键，根据主键先执行查询，有记录存在执行更新操作,无记录存在执行插入操作
    @Test
    public void saveOrUpdateBatch() {
        User user1 = new User();
        user1.setName("徐丽3");
        user1.setAge(25);
        User user2 = new User();
        user2.setId(1163288805784965122L);
        user2.setName("徐力2");
        user2.setAge(25);
        List<User> userList = Arrays.asList(user1, user2);
        //boolean result = userService.saveBatch(userList);
        boolean result = userService.saveOrUpdateBatch(userList);
        System.out.println("执行结果为:" + result);
    }

    @Test
    public void selectChain() {
        userService.lambdaQuery()
                .gt(User::getAge, 25).like(User::getName, "雨")
                .list()
                .forEach(System.out::println);
    }

    @Test
    public void updateChain() {
        boolean result = userService.lambdaUpdate()
                .eq(User::getAge, 25).likeRight(User::getName, "徐")
                .set(User::getAge, 26)
                .update();
        System.out.println("执行结果为:" + result);
    }

    @Test
    public void deleteChain() {
        boolean result = userService.lambdaUpdate()
                .eq(User::getAge, 26).likeRight(User::getName, "徐丽")
                .remove();
        System.out.println("删除结果为:" + result);
    }
}
