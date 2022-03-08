package com.wenxue.uzi;


import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yl
 */
@SpringBootApplication
@EnableApolloConfig(value = {"common.bootstrap","application","dbConnection.yml","common.mybatis-plus"})
@MapperScan({"com.wenxue.uzi.mapper"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
