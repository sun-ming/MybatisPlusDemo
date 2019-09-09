package com.sm.high;

import com.sm.mp.Application;
import com.sm.mp.dao.User2Mapper;
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
public class InjectTest {
    //@Autowired
    //private UserMapper userMapper;
    @Autowired
    private User2Mapper user2Mapper;

    /*@Test
    public void deletAll() {
        int rows = userMapper.deleteAll();
        System.out.println("影响行数为:" + rows);
    }*/


    @Test
    public void deletAll2() {
        int rows = user2Mapper.deleteAll();
        System.out.println("影响行数为:" + rows);
    }


}
