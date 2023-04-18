package com.easy;

import com.easy.easyrun.EasyRun;
import com.easy.enums.EasyJenkinsEnum;
import com.easy.frame.MainFrame;
import com.easy.util.PreferencesJenkinsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.File;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableAsync
@EnableScheduling
public class EasyJenkinsApplication {

    public static void main(String[] args) {
        String registryPath = PreferencesJenkinsUtil.get(EasyJenkinsEnum.EASY_JENKINS_PATH.getParam());
        String field = PreferencesJenkinsUtil.get(EasyJenkinsEnum.EASY_JENKINS_FILE_ID.getParam());
        File jenkinsFile = new File(registryPath + "\\" + field + ".jenkins");
        if (jenkinsFile.exists()) {
            SpringApplication.run(EasyJenkinsApplication.class, args);
        } else {
            MainFrame.main(args);
        }

    }

    @Component
    @Order(value = 1)
    public static class EasyJenkinsAfterRunner implements ApplicationRunner {
        @Autowired
        EasyRun easyRun;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            easyRun.run("http", "localhost");
        }
    }
}
