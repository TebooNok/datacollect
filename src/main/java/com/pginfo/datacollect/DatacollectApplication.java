package com.pginfo.datacollect;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 继承SpringBootServletInitializer并重写configure方法使项目可以运行在tomcat容器中
 * 在tomcat运行中，DataSourceAutoConfiguration默认会实例化tomcat-jdbc，这里要去掉
 */
@MapperScan("com.pginfo.datacollect.dao")
@SpringBootApplication// (exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
@EnableSwagger2
public class DatacollectApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DatacollectApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(DatacollectApplication.class, args);
    }

    @Bean(name = "syncDataTaskPool")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(30);
        return taskScheduler;
    }
}
