ALTER TABLE email_content
    ADD CONSTRAINT uk_email_content_ses_message_id UNIQUE (ses_message_id);