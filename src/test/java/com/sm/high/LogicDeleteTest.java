package com.sm.high;

import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
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
public class LogicDeleteTest {
    @Autowired
    private User2Mapper user2Mapper;

    @Test
    public void contextLoads() {

    }

    //逻辑删除
    @Test
    public void deleteById() {
        int rows = user2Mapper.deleteById(1094592041087729666L);
        System.out.println("影响行数为:" + rows);
        /**
         * DEBUG==>  Preparing: UPDATE user2 SET deleted=1 WHERE id=? AND deleted=0
         * DEBUG==> Parameters: 1094592041087729666(Long)
         * DEBUG<==    Updates: 1
         * 影响行数为:1
         */
    }
    //查询和修改的时候会带上删除标识,只能查询和更新逻辑未删除的数据
    //自定义sql语句不会加上删除标识字段，需要自己加上(写在sql中，或者在wrapper中构建)
    @Test
    public void selectAndUpdate() {
        new LambdaQueryChainWrapper<User2>(user2Mapper).list().forEach(System.out::println);
        //SELECT id,name,age,email,manager_id,create_time,update_time,version,deleted FROM user2 WHERE deleted=0

        /*boolean result = new LambdaUpdateChainWrapper<User2>(user2Mapper)
                .eq(User2::getId,1088248166370832385L)
                .set(User2::getAge,26)
                .update();
        System.out.println("执行结果为:"+result);*/
        //DEBUG==>  Preparing: UPDATE user2 SET age=? WHERE deleted=0 AND id = ?
        //DEBUG==> Parameters: 26(Integer), 1088248166370832385(Long)
        //DEBUG<==    Updates: 1
        //执行结果为:true
    }
}
