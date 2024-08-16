/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Properties;

import static com.complitech.complitech_test_task.constant.AppConstant.CONFIG_EXTERNAL_PATH;

@Component
@Slf4j
public class SchedulerTask {

    @Scheduled(fixedRate = 600000)
    public void checkUpdateTime() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_EXTERNAL_PATH)) {
            properties.load(input);
            shutdownAction(properties.getProperty("app.shutdown.time"));
        } catch (IOException | DateTimeParseException e) {
            log.error(e.getMessage());
        }
    }

    private void shutdownAction(String shutdownTimeString) {
        LocalDateTime shutdownTime = LocalDateTime.parse(shutdownTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime currentTime = LocalDateTime.now();

        if (currentTime.isAfter(shutdownTime)) {
            log.info("Shutting down the application because the current time exceeds the shutdown time.");
            System.exit(0);
        }
    }
}
