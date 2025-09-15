-- MariaDB / InnoDB / UTF8MB4
SET NAMES utf8mb4;

-- =========================================================
-- TOUR
-- =========================================================
CREATE TABLE tour (
                      tour_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
                      language          VARCHAR(20)      NOT NULL,
                      road_address      VARCHAR(255),
                      basic_address     VARCHAR(255),
                      area_code         VARCHAR(50),
                      sigun_gu_code     VARCHAR(50),
                      content_id        VARCHAR(100)     NOT NULL,
                      content_type_id   VARCHAR(30)      NOT NULL,
                      event_start_date  DATE,
                      event_end_date    DATE,
                      main_image_url    VARCHAR(512),
                      map_x             VARCHAR(100),
                      map_y             VARCHAR(100),
                      m_level           VARCHAR(50),
                      modified_time     DATETIME,
                      telephone         VARCHAR(1000),
                      title             VARCHAR(255),
                      CONSTRAINT uk_tour_content_id UNIQUE (content_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_tour_lang ON tour (language);
CREATE INDEX idx_tour_region ON tour (area_code, sigun_gu_code);
CREATE INDEX idx_tour_content_type ON tour (content_type_id);
CREATE INDEX idx_tour_modified_time ON tour (modified_time);
CREATE INDEX idx_tour_event_range ON tour (event_start_date, event_end_date);

-- =========================================================
-- TOUR DETAIL (1:1, PK != FK)
-- =========================================================
CREATE TABLE tour_detail (
                             tour_detail_id BIGINT NOT NULL AUTO_INCREMENT,
                             tour_id BIGINT NOT NULL,

                             age_limit VARCHAR(255),
                             booking_place VARCHAR(255),
                             discount_info_festival VARCHAR(255),
                             event_homepage VARCHAR(1500),
                             event_place VARCHAR(255),

                             place_info VARCHAR(1000),
                             play_time VARCHAR(500),
                             program VARCHAR(1000),

                             spend_time_festival VARCHAR(255),
                             sponsor VARCHAR(255),
                             sponsor_telephone VARCHAR(1000),
                             sub_event VARCHAR(255),
                             use_time_festival VARCHAR(500),

                             PRIMARY KEY (tour_detail_id),
                             UNIQUE KEY uk_tour_detail_tour_id (tour_id),
                             CONSTRAINT fk_tour_detail_tour FOREIGN KEY (tour_id)
                                 REFERENCES tour (tour_id)
                                 ON UPDATE CASCADE
                                 ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- TOUR IMAGE (N:1 Tour)
-- =========================================================
CREATE TABLE tour_image (
                            tour_image_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
                            tour_id           BIGINT       NOT NULL,
                            origin_image_url  VARCHAR(512),
                            small_image_url   VARCHAR(512),
                            image_name        VARCHAR(255),
                            serial_number     VARCHAR(100),
                            CONSTRAINT fk_tour_image_tour
                                FOREIGN KEY (tour_id) REFERENCES tour(tour_id)
                                    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_tour_image_tour ON tour_image (tour_id);

-- =========================================================
-- TOUR REPEAT (N:1 Tour)
-- =========================================================
CREATE TABLE tour_repeat (
                             tour_repeat_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
                             tour_id           BIGINT       NOT NULL,
                             serial_number     VARCHAR(100),
                             info_name         VARCHAR(255),
                             info_text         TEXT,
                             CONSTRAINT fk_tour_repeat_tour
                                 FOREIGN KEY (tour_id) REFERENCES tour(tour_id)
                                     ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_tour_repeat_tour ON tour_repeat (tour_id);

-- =========================================================
-- TOUR KEYWORDS (ElementCollection)
-- =========================================================
CREATE TABLE tour_keywords (
                               tour_id   BIGINT        NOT NULL,
                               keyword   VARCHAR(255)  NOT NULL,
                               CONSTRAINT pk_tour_keywords PRIMARY KEY (tour_id, keyword),
                               CONSTRAINT fk_tour_keywords_tour
                                   FOREIGN KEY (tour_id) REFERENCES tour(tour_id)
                                       ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- MEMBER (Embedded: PlatformInfo, ActivityInfo, Subscriptions)
-- =========================================================
CREATE TABLE member (
                        member_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- PlatformInfo
                        platform_type    VARCHAR(20),
                        platform_pk      VARCHAR(255),
                        platform_email   VARCHAR(255),
    -- ActivityInfo
                        sign_up_date     DATETIME,
                        last_sign_in_date DATE,
    -- 기타
                        email_subscribe  TINYINT(1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_member_platform ON member (platform_type, platform_pk);
-- 필요 시 유니크 활성화(운영 정책에 따라):
-- ALTER TABLE member ADD CONSTRAINT uk_member_platform UNIQUE (platform_type, platform_pk);
-- ALTER TABLE member ADD CONSTRAINT uk_member_email UNIQUE (platform_email);

-- =========================================================
-- MEMBER_SUBSCRIPTION (ElementCollection)
-- =========================================================
CREATE TABLE member_subscription (
                                     member_id      BIGINT       NOT NULL,
                                     area_code      VARCHAR(50)  NOT NULL,
                                     sigun_gu_code  VARCHAR(50)  NOT NULL,
                                     subscribe_date DATETIME,
                                     CONSTRAINT pk_member_subscription PRIMARY KEY (member_id, area_code, sigun_gu_code),
                                     CONSTRAINT fk_member_subscription_member
                                         FOREIGN KEY (member_id) REFERENCES member(member_id)
                                             ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_region ON member_subscription (area_code, sigun_gu_code);

-- =========================================================
-- EMAIL (N:1 Member, N:1 Tour)
-- =========================================================
CREATE TABLE email (
                       email_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
                       member_id       BIGINT,
                       tour_id         BIGINT,
                       send_time       DATETIME,
                       title           VARCHAR(255),
                       body            MEDIUMTEXT,
                       ses_message_id  VARCHAR(255),
                       CONSTRAINT uk_email_message_id UNIQUE (ses_message_id),
                       CONSTRAINT fk_email_member
                           FOREIGN KEY (member_id) REFERENCES member(member_id)
                               ON UPDATE CASCADE ON DELETE SET NULL,
                       CONSTRAINT fk_email_tour
                           FOREIGN KEY (tour_id) REFERENCES tour(tour_id)
                               ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_email_member ON email (member_id);
CREATE INDEX idx_email_tour ON email (tour_id);
CREATE INDEX idx_email_send_time ON email (send_time);
