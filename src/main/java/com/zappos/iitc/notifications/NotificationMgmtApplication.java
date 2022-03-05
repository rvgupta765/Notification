package com.zappos.iitc.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
//@ComponentScan({"com.zappos.iitc.commons","com.zappos.iitc.notifications" })
//@ComponentScan({"com.zappos.iitc.notifications"})
public class NotificationMgmtApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationMgmtApplication.class, args);
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
