-- 기존 ON UPDATE CASCADE ON DELETE SET NULL 설정 제거
ALTER TABLE email
    DROP FOREIGN KEY fk_email_member;

-- 기존 ON UPDATE CASCADE ON DELETE SET NULL 설정 제거
ALTER TABLE email
    ADD CONSTRAINT fk_email_member
        FOREIGN KEY (member_id) REFERENCES member(member_id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

-- =========== 실제 마이그레이션을 위한 SQL 구문 ========
--     1. email_content 테이블 생성한다.
--     2. email 테이블에서 title, body, ses_message_id 를 distinct 로 조회 후 email_content 에 저장
--             ( 현재 ses_message_id는 uuid로 유니크하지만 이 값은 여러 행에 중복으로 저장되어 있을 수 있다)
--     3. email_content 테이블에 email_content_id 칼럼을 추가한다 ( 외래키 ) 그리고 ses_message_id를 이용해서 알맞은 email_content_id를 업데이트한다
--     4. 연결이 잘 되었다면, email 테이블에서 title, body, ses_message_id 를 모두 제거한다.

-- 1)
CREATE TABLE email_content (
                               email_content_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
                               title   VARCHAR(255),
                               body    MEDIUMTEXT,
                               ses_message_id  VARCHAR(36),
                               CONSTRAINT uk_email_content_message_id UNIQUE (ses_message_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2)
INSERT INTO email_content (title, body, ses_message_id)
SELECT
    distinct
    e.title,
    e.body,
    e.ses_message_id
FROM email e
GROUP BY e.ses_message_id;

-- 3)
ALTER TABLE email
    ADD COLUMN email_content_id BIGINT NULL AFTER send_time;
CREATE INDEX idx_email_email_content ON email (email_content_id);

UPDATE email e
    JOIN email_content ec
ON ec.ses_message_id = e.ses_message_id
    SET e.email_content_id = ec.email_content_id
WHERE e.email_content_id IS NULL;

ALTER TABLE email
    ADD CONSTRAINT fk_email_content
        FOREIGN KEY (email_content_id) REFERENCES email_content(email_content_id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

ALTER TABLE email
    MODIFY email_content_id BIGINT NOT NULL;

-- 4)
ALTER TABLE email
DROP COLUMN title,
  DROP COLUMN body,
  DROP COLUMN ses_message_id;