package com.sm.mp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: sunming
 * @Date: 2019-8-13 11:25
 * @Description: 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
 */
public class CodeGenerator {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        //1、数据源 dataSourceConfig 配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        /*dataSourceConfig.setDbQuery(new MySqlQuery());
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setSchemaName(null);
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert());*/
        dataSourceConfig.setUrl("jdbc:mysql://172.30.154.61:3306/platform_gdgj?useSSL=false&serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf8&characterSetResults=utf8&allowMultiQueries=true");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver"); //设置数据库驱动,因为我的数据库版本是mysql5.7,所以使用该驱动
        //dsc.setDriverName("com.mysql.jdbc.Driver"); //mysql5.6以下的驱动
        dataSourceConfig.setUsername("root"); //数据库名称
        dataSourceConfig.setPassword("rzxNS_123"); //数据库密码
        autoGenerator.setDataSource(dataSourceConfig);

        //2、数据库表配置；策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setSkipView(true);
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");
        strategyConfig.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
        strategyConfig.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
        strategyConfig.setInclude("rpt_url_abnormal_website");
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setRestControllerStyle(true);
        strategyConfig.setEntityTableFieldAnnotationEnable(false);
        autoGenerator.setStrategy(strategyConfig);

        //3、包名配置
        String packageName = "com.sm";
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName("mp");
        packageConfig.setMapper("dao");
        packageConfig.setParent(packageName);
        autoGenerator.setPackageInfo(packageConfig);


        //4、模板配置
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        autoGenerator.setTemplate(templateConfig);

        //5、全局策略 globalConfig 配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        //String projectPath = "E:/workCode/IdeaProjects/MybatisPlusDemo"; //windows下生成文件的位置
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setFileOverride(false);
        globalConfig.setAuthor("sunming"); //生成文件的作者名称
        globalConfig.setOpen(false);
        globalConfig.setEnableCache(false);// XML 二级缓存
        globalConfig.setSwagger2(true);
        globalConfig.setActiveRecord(false);
        globalConfig.setBaseResultMap(false);// XML ResultMap
        globalConfig.setBaseColumnList(false);// XML columList
        globalConfig.setIdType(IdType.AUTO);
        autoGenerator.setGlobalConfig(globalConfig);

        //6、注入 injectionConfig 配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();
        fileOutConfigList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/src/main/resources/mapper/" + packageConfig.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        injectionConfig.setFileOutConfigList(fileOutConfigList);
        autoGenerator.setCfg(injectionConfig);


        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.execute();
    }
}
