ALTER TABLE comment
    ADD FOREIGN KEY (post_id)
        REFERENCES post (id)
        ON DELETE CASCADE;