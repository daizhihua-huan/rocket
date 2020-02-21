package com.huanyuenwei.rockt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication(scanBasePackages = "com")
public class RocktApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocktApplication.class, args);
    }

}
