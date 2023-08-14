package com.aoz.aozmeeting;



import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@ComponentScan({"im.youdu.sdk","com.aoz"})

//配置扫描拦截器
 @ServletComponentScan  //临时关闭拦截器
public class AozMeetingApplication {
    public static void main(String[] args){
        SpringApplication.run(AozMeetingApplication.class, args);
        log.info("系统用户端登陆地址："+"http://localhost:8080/");
    }
}
