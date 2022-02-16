package com.example.springbootv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.sql.DataSource;

@SpringBootApplication
        //(exclude = {DataSourceAutoConfiguration.class})
//这里是因为暂时引入了数据库的依赖，但是没有配置数据库的url，于是先将DataSource的配置给关了。
public class SpringbootV2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootV2Application.class, args);
    }

}
