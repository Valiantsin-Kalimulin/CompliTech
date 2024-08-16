/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

CREATE SCHEMA IF NOT EXISTS complitech;

CREATE TYPE IF NOT EXISTS complitech.GENDER AS ENUM ('MALE', 'FEMALE', 'NOT_SPECIFIED');

CREATE TABLE IF NOT EXISTS complitech.documents (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_login VARCHAR(50) NOT NULL,
    user_password VARCHAR(20) NOT NULL,
    user_fullname VARCHAR(256) NOT NULL,
    user_gender complitech.GENDER,
    CONSTRAINT unique_user_login UNIQUE (user_login)
);
