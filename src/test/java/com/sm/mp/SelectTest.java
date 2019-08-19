package com.sm.mp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.sm.mp.dao.UserMapper;
import com.sm.mp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SelectTest {
    @Autowired
    private UserMapper userMapper;

    //第一部分、基本查询方法
    @Test
    public void selectById() {
        long id = 1087982257332887553L;
        User user = userMapper.selectById(id);
        System.out.println(user);
    }

    @Test
    public void selectByIds() {
        List<User> userList = userMapper.selectBatchIds(Arrays.asList(1087982257332887553L, 1088250446457389058L));
        userList.forEach(System.out::println);
    }

    // WHERE manager_id = ? AND age = ?
    @Test
    public void selectByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("age", 31);//key为数据库中的列
        map.put("manager_id", 1088248166370832385L);
        List<User> userList = userMapper.selectByMap(map);
        userList.forEach(System.out::println);
    }

    //第二部分、以条件构造器为参数的查询方法
    @Test
    public void selectByWrapper() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //QueryWrapper<User> queryWrapper = Wrappers.<User>query();

        //1、WHERE name LIKE '%雨%' AND age < 40
        //queryWrapper.like("name", "雨").lt("age", 40);

        //2、WHERE name LIKE '%雨%' AND age BETWEEN 20 AND 40 AND email IS NOT NULL
        //queryWrapper.like("name", "雨").between("age", 20, 40).isNotNull("email");

        //3、WHERE name LIKE '王%' OR age >= 25 ORDER BY age DESC , id ASC
        //queryWrapper.likeRight("name","王").or().ge("age",25).orderByDesc("age").orderByAsc("id");

        //4、创建日期为2019年2月14日并且直属上级名字为王姓
        //WHERE date_format(create_time,'%Y-%m-%d')='2019-02-14' AND manager_id IN (select id from user where name like '王%')
        /*queryWrapper.apply("date_format(create_time,'%Y-%m-%d')={0}", "2019-02-14")
                //.apply("date_format(create_time,'%Y-%m-%d')='2019-02-14' or true or true")//SQL注入风险
                .inSql("manager_id", "select id from user where name like '王%'");*/

        //5、名字为王姓并且（年龄小于40或邮箱不为空）
        //WHERE name LIKE '王%' AND ( age < 40 OR email IS NOT NULL )
        //queryWrapper.likeRight("name", "王").and(qw -> qw.lt("age", 40).or().isNotNull("email"));

        //6、名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
        //WHERE name LIKE '王%' OR ( age < 40 AND age > 20 AND email IS NOT NULL )
        //queryWrapper.likeRight("name", "王").or(qw -> qw.lt("age", 40).gt("age", 20).isNotNull("email"));

        //7、（年龄小于40或邮箱不为空）并且名字为王姓
        //WHERE ( age < 40 OR email IS NOT NULL ) AND name LIKE '王%'
        //两种写法都能够实现,推荐第2种
        //queryWrapper.and(qw -> qw.lt("age", 40).or().isNotNull("email")).likeRight("name", "王");
        //queryWrapper.nested(qw -> qw.lt("age", 40).or().isNotNull("email")).likeRight("name", "王");

        //8、年龄为30,31,34,35
        //WHERE age IN (30,31,34,35)
        //queryWrapper.in("age",Arrays.asList(30,31,34,35));

        //9、只返回满足条件的其中一条语句即可 limit 1
        //WHERE age IN (?,?,?,?) limit 1
        //queryWrapper.in("age", Arrays.asList(30, 31, 34, 35)).last("limit 1");
        //last方法只能调用一次，多次调用以最后一次为准，有sql注入风险，请谨慎使用


        List<User> list = userMapper.selectList(queryWrapper);
        System.out.println(list.size());
        list.forEach(System.out::println);
    }

    //第三部分、select 中字段不全出现的处理方法
    //1、选择：queryWrapper.select("id","name","age")...
    //2、排除：queryWrapper.select(User.class,info->!info.getColumn().equals("create_time")&&!info.getColumn().equals("manager_id"))
    //3、select也可以放到后面

    //第四部分、condition的作用
    //控制该条件是否加入到最终生成的sql语句中
    private void condition(String name, String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name)
                .like(StringUtils.isNotEmpty(email), "email", email);
        List<User> list = userMapper.selectList(queryWrapper);
        System.out.println(list.size());
        list.forEach(System.out::println);
    }

    @Test
    public void testCondition() {
        String name = "";
        String email = "x";
        //SELECT id,name,age,email,manager_id,create_time FROM user WHERE email LIKE '%x%'
        condition(name, email);
    }

    //第五部分、创建条件构造器时传入实体对象
    //默认entity中不为null的属性将作为where条件，默认使用等值比较符，and连接
    //与条件构造器的条件互不干扰，都会出现在where中
    //可以使用@TableField(condition=SqlCondition.LIKE)改变字段的默认比较符
    //@TableField(condition="%s&lt;#{%s}")

    //第六部分、条件构造器中的allEq用法
    @Test
    public void selectByWrapperAllEq() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "v1");
        params.put("manager_id", null);
        queryWrapper.allEq((k, v) -> !k.equals("name"), params, true);//name条件会被过滤掉
        //SELECT id,name,age,email,manager_id,create_time FROM user WHERE manager_id IS NULL
        List<User> list = userMapper.selectList(queryWrapper);
        System.out.println(list.size());
        list.forEach(System.out::println);
    }

    //第七部分、其他以条件构造器为参数的查询方法
    //selectMaps    适用于表字段很多，只查询几列，返回对象时绝大部分字段为空
    //              适用场景2：统计查询
    //selectObjs    只返回第一个字段的值
    //selectCount   查询总记录数 不能设置查询列，自动生成count(1)
    //selectOne     只返回一条记录，查询返回多条会抛异常

    //selectMaps的统计查询
    @Test
    public void selectByWrapperMaps2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄。并且只取年龄总和小于500的组。
        //SELECT avg(age) avg_age,min(age) min_age,max(age) max_age FROM user GROUP BY manager_id HAVING sum(age)<500
        queryWrapper.select("avg(age) avg_age", "min(age) min_age", "max(age) max_age")
                .groupBy("manager_id").having("sum(age)<{0}", 500);
        List<Map<String, Object>> list = userMapper.selectMaps(queryWrapper);
        System.out.println(list.size());
        list.forEach(System.out::println);
    }

    //第八部分：lambda条件构造器；优点：防误写
    @Test
    public void selectByLambda() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        //LambdaQueryWrapper<User> lambdaQueryWrapper2 = new LambdaQueryWrapper<User>();
        //LambdaQueryWrapper<User> lambdaQueryWrapper3 = Wrappers.<User>lambdaQuery();

        //WHERE name LIKE '%雨%' AND age < 40
        //lambdaQueryWrapper.like(User::getName,"雨").lt(User::getAge,40);

        //5、名字为王姓并且（年龄小于40或邮箱不为空）
        //WHERE name LIKE '王%' AND ( age < 40 OR email IS NOT NULL )
        lambdaQueryWrapper.likeRight(User::getName, "王")
                .and(lqw -> lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        List<User> userList = userMapper.selectList(lambdaQueryWrapper);
        System.out.println(userList.size());
        userList.forEach(System.out::println);
    }
    //LambdaQueryChainWrapper
    @Test
    public void selectByLambdaChain() {
        //WHERE name LIKE '%雨%' AND age >= 20
        new LambdaQueryChainWrapper<User>(userMapper)
                .like(User::getName, "雨")
                .ge(User::getAge, 20)
                .list()
                .forEach(System.out::println);
    }

    //第九部分：使用条件构造器的自定义SQL
    //1、注解方式
    //2、mapper文件方式,配置mybatis-plus.mapper-locations参数,编写xml文件
    @Test
    public void selectByCustom() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        lambdaQueryWrapper.likeRight(User::getName, "王")
                .and(lqw -> lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        //select * from user WHERE name LIKE '王%' AND ( age < 40 OR email IS NOT NULL )
        List<User> userList = userMapper.selectAll(lambdaQueryWrapper);
        System.out.println(userList.size());
        userList.forEach(System.out::println);
    }

    //第十部分：分页查询
    //配置分页插件 MybatisPlusConfig
    //参数isSearchCount是否查询总页数
    //selectMapsPage方法，返回结果为map
    @Test
    public void selectByPage() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Page<User> page = new Page<User>(1, 2);//isSearchCount
        queryWrapper.ge("age", 12);
        IPage<User> iPage = userMapper.selectPage(page, queryWrapper);//selectMapsPage
        System.out.println("总页数：" + iPage.getPages());
        System.out.println("总记录数：" + iPage.getTotal());
        System.out.println("当前页：" + iPage.getCurrent());
        System.out.println("每页显示条数：" + iPage.getSize());
        iPage.getRecords().forEach(System.out::println);
    }
    //LambdaQueryWrapper
    //LambdaQueryChainWrapper
    @Test
    public void selectByPageLambdaChain() {
        Page<User> page = new Page<User>(1, 2);//isSearchCount
        IPage<User> iPage = new LambdaQueryChainWrapper<User>(userMapper)
                .ge(User::getAge, 12)
                .page(page);
        System.out.println("总页数：" + iPage.getPages());
        System.out.println("总记录数：" + iPage.getTotal());
        System.out.println("当前页：" + iPage.getCurrent());
        System.out.println("每页显示条数：" + iPage.getSize());
        iPage.getRecords().forEach(System.out::println);
    }
    //自定义分页查询语句
    //1、注解方式
    //2、mapper文件方式,配置mybatis-plus.mapper-locations参数,编写xml文件
    @Test
    public void selectUserPageMy(){
        Page<User> page = new Page<User>(1, 2);//isSearchCount
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>();
        lambdaQueryWrapper.ge(User::getAge, 12);
        IPage<User> iPage = userMapper.selectUserPageMy(page,lambdaQueryWrapper);
        System.out.println("总页数：" + iPage.getPages());
        System.out.println("总记录数：" + iPage.getTotal());
        System.out.println("当前页：" + iPage.getCurrent());
        System.out.println("每页显示条数：" + iPage.getSize());
        iPage.getRecords().forEach(System.out::println);
    }
}
