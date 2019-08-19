package com.sm.mp;

import com.sm.mp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ARTest {

    @Test
    public void insert() {
        User user = new User();
        user.setName("刘花");
        user.setAge(29);
        user.setEmail("lh@baomidou.com");
        user.setManagerId(1088248166370832385L);
        user.setCreateTime(LocalDateTime.now());
        boolean result = user.insert();
        System.out.println("插入结果为：" + result);
        System.out.println(user.getId());//插入成功，回填ID
    }
    @Test
    public void selectAll() {
        User user = new User();
        user.selectAll().forEach(System.out::println);
    }
    @Test
    public void selectById() {
        User user = new User();
        user.setId(1162278182112497665L);
        User resultUser = user.selectById();
        //User resultUser = user.selectById(1162278182112497665L);
        System.out.println(resultUser);
        System.out.println(resultUser==user);//返回新对象
    }

    @Test
    public void updateById() {
        User user = new User();
        user.setId(1162278182112497665L);
        user.setEmail("lh2@baomidou.com");
        boolean result = user.updateById();
        System.out.println("更新结果为：" + result);
    }

    @Test
    public void deleteById() {
        User user = new User();
        user.setId(1162278182112497665L);
        boolean result = user.deleteById();
        System.out.println("删除结果为：" + result);
    }

    //如果不设置ID就是insert语句，如果设置ID就会先查询，存在就是update语句，不存在就是insert语句
    @Test
    public void insertOrUpdate() {
        User user = new User();
        user.setName("张强");
        user.setAge(25);
        user.setEmail("zq@baomidou.com");
        user.setManagerId(1088248166370832385L);
        user.setCreateTime(LocalDateTime.now());
        boolean result = user.insertOrUpdate();
        System.out.println("操作结果为：" + result);
    }
}
