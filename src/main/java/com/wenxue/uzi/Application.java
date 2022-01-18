package com.wenxue.uzi;


import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yl
 */
@SpringBootApplication
@EnableApolloConfig(value = {"common.bootstrap","application","dbConnection.yml","common.mybatis-plus"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
