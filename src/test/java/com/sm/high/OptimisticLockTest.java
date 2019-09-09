package com.sm.high;

import com.sm.mp.Application;
import com.sm.mp.dao.User2Mapper;
import com.sm.mp.entity.User2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: sunming
 * @Date: 2019-9-7 15:52
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class OptimisticLockTest {
    @Autowired
    private User2Mapper user2Mapper;

    @Test
    public void update() {
        //查询出版本号
        int version = 1;

        User2 user2 = new User2();
        user2.setId(1170258295956975618L);
        user2.setAge(33);
        user2.setEmail("lmc2@baomidou.com");
        user2.setVersion(version);//实体设置版本号
        int rows = user2Mapper.updateById(user2);
        System.out.println("影响行数为:" + rows);
        //updateFill...
        //DEBUG==>  Preparing: UPDATE user2 SET update_time=?, version=?, email=?, age=? WHERE id=? AND version=? AND deleted=0
        //DEBUG==> Parameters: 2019-09-07T17:39:26.932(LocalDateTime), 2(Integer), lmc2@baomidou.com(String), 33(Integer), 1170258295956975618(Long), 1(Integer)
        //DEBUG<==    Updates: 1
        //影响行数为:1
    }
}
