CREATE TABLE USERS
(
  ID BIGINT AUTO_INCREMENT PRIMARY KEY,
  LOGIN VARCHAR(255) NOT NULL,
  PASSWORD VARCHAR(255) NOT NULL,
  REGISTRATION_DATE_TIME DATETIME(6) NULL,
  LAST_UPDATE_DATE_TIME DATETIME(6) NULL,
  IS_USING_COOKIE BIT NOT NULL,
  ROLE_ID BIGINT NULL,

  CONSTRAINT UK_LOGIN UNIQUE (LOGIN),
  CONSTRAINT FK_ROLE_ID FOREIGN KEY (ROLE_ID) REFERENCES ROLES (ID)
);
