package com.sm.mp.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class MybatisPlusConfig {
    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<ISqlParser>();
        /*TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                //从session中获取当前用户的租户信息
                return new LongValue(1088248166370832385L);
            }

            @Override
            public String getTenantIdColumn() {
                return "manager_id";//表中字段名，代表租户字段信息
            }

            @Override
            public boolean doTableFilter(String tableName) {
                if ("role".equals(tableName)) {//查询角色表，不添加租户信息
                    return true;
                }
                return false;//不过滤，都添加租户信息
            }
        });
        sqlParserList.add(tenantSqlParser);*/

        //动态表名插件实现
        /*DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        Map<String, ITableNameHandler> tableNameHandlerMap = new HashMap<String, ITableNameHandler>();
        tableNameHandlerMap.put("user", new ITableNameHandler() {
            @Override
            public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
                return "user_2019";
            }
        });
        dynamicTableNameParser.setTableNameHandlerMap(tableNameHandlerMap);

        sqlParserList.add(dynamicTableNameParser);*/
        paginationInterceptor.setSqlParserList(sqlParserList);
        //特定sql过滤实现方法1
        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            @Override
            public boolean doFilter(MetaObject metaObject) {

                MappedStatement mappedStatement = SqlParserHelper.getMappedStatement(metaObject);
                if ("com.sm.mp.dao.User2Mapper.selectById".equals(mappedStatement.getId())) {
                    return true;    //根据ID查询时，不用添加租户信息
                }
                return false;//不过滤，都添加租户信息
            }
        });

        return paginationInterceptor;
    }

    //逻辑删除插件
    /*@Bean
    public ISqlInjector sqlInjector() {
        //从3.1.1开始，不需要配置了。
        return new LogicSqlInjector();
    }*/

    //乐观锁插件
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    //性能分析插件
    @Bean
    @Profile({"dev", "test"})
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setFormat(true);
        performanceInterceptor.setMaxTime(5000L);
        return performanceInterceptor;
    }
}
