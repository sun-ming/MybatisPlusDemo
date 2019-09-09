package com.sm.high;

import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
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
public class AutoFillTest {
    @Autowired
    private User2Mapper user2Mapper;

    @Test
    public void contextLoads() {

    }

    @Test
    public void insert() {
        User2 user2 = new User2();
        user2.setName("刘明超");
        user2.setAge(31);
        user2.setEmail("lmc@baomidou.com");
        user2.setManagerId(1088248166370832385L);
        int rows = user2Mapper.insert(user2);
        System.out.println("影响行数为:" + rows);
        /**
         * insertFill...
         * DEBUG==>  Preparing: INSERT INTO user2 ( id, name, age, email, manager_id, create_time ) VALUES ( ?, ?, ?, ?, ?, ? )
         * DEBUG==> Parameters: 1170258295956975618(Long), 刘明超(String), 31(Integer), lmc@baomidou.com(String), 1088248166370832385(Long), 2019-09-07T16:51:34.768(LocalDateTime)
         * DEBUG<==    Updates: 1
         * 影响行数为:1
         */
    }

    @Test
    public void update() {
        boolean result = new LambdaUpdateChainWrapper<User2>(user2Mapper)
                .eq(User2::getId, 1170258295956975618L)
                .set(User2::getAge, 32)
                .update();
        System.out.println("执行结果为:" + result);
        //DEBUG==>  Preparing: UPDATE user2 SET age=? WHERE deleted=0 AND id = ?
        //DEBUG==> Parameters: 32(Integer), 1170258295956975618(Long)
        //DEBUG<==    Updates: 1
        //执行结果为:true
        //自动填充不生效

        User2 user2 = new User2();
        user2.setId(1170258295956975618L);
        user2.setAge(33);
        int rows = user2Mapper.updateById(user2);
        System.out.println("影响行数为:"+rows);
        //updateFill...
        //DEBUG==>  Preparing: UPDATE user2 SET age=?, update_time=? WHERE id=? AND deleted=0
        //DEBUG==> Parameters: 32(Integer), 2019-09-07T16:59:41.120(LocalDateTime), 1170258295956975618(Long)
        //DEBUG<==    Updates: 1
        //影响行数为:1

        //自动填充对updateById生效，对update方法不生效
        //自动填充对实体操作生效，对Wrapper操作不生效
    }
}
