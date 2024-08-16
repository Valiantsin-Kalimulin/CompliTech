/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static com.complitech.complitech_test_task.constant.AppConstant.CONFIG_EXTERNAL_PATH;

@Configuration
@PropertySource("file:/" + CONFIG_EXTERNAL_PATH)
public class ExternalConfig {
}
