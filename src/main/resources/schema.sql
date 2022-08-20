CREATE TABLE chats
(
    id          SMALLSERIAL PRIMARY KEY,
    title       VARCHAR NOT NULL,
    telegram_id BIGINT  NOT NULL UNIQUE
);

CREATE TABLE blocked_stickerpacks
(
    id      SERIAL PRIMARY KEY,
    chat_id SMALLINT NOT NULL REFERENCES chats (id) ON DELETE CASCADE,
    name    VARCHAR  NOT NULL,
    UNIQUE (chat_id, name)
);

CREATE TABLE users
(
    id          SERIAL PRIMARY KEY,
    username    VARCHAR              NOT NULL,
    telegram_id INT                  NOT NULL UNIQUE,
    gender      VARCHAR DEFAULT 'male' NOT NULL
);

CREATE TABLE media_cache
(
    id      SERIAL PRIMARY KEY,
    digest  VARCHAR NOT NULL UNIQUE,
    file_id VARCHAR NOT NULL
);

CREATE TABLE handled_media_cache
(
    id         SERIAL PRIMARY KEY,
    file_id    VARCHAR NOT NULL,
    chat_id    VARCHAR NOT NULL,
    user_id    VARCHAR NOT NULL,
    added_date timestamp
);


CREATE TABLE user_chat_statistics
(
    id             SERIAL PRIMARY KEY,
    chat_id        SMALLINT  NOT NULL REFERENCES chats (id) ON DELETE CASCADE,
    user_id        INT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    messages_count INT       NOT NULL,
    last_activity  TIMESTAMP NOT NULL,
    UNIQUE (chat_id, user_id)
);

CREATE TABLE chats_history
(
    id         SERIAL PRIMARY KEY,
    chat_id    SMALLINT NOT NULL REFERENCES chats (id) ON DELETE CASCADE,
    join_date  TIMESTAMP,
    leave_date TIMESTAMP
);

CREATE TABLE dice_poll_captcha_restrictions
(
    id                        SERIAL PRIMARY KEY,
    chat_id                   SMALLINT  NOT NULL REFERENCES chats (id) ON DELETE CASCADE,
    member_telegram_id        INT       NOT NULL,
    date_time                 TIMESTAMP NOT NULL,
    join_message_id           BIGINT    NOT NULL,
    dice_message_id           BIGINT    NOT NULL,
    poll_message_id           BIGINT    NOT NULL,
    poll_id                   VARCHAR   NOT NULL,
    correct_answer_index      SMALLINT  NOT NULL,
    can_send_messages         BOOLEAN   NOT NULL,
    can_send_media_messages   BOOLEAN   NOT NULL,
    can_send_polls            BOOLEAN   NOT NULL,
    can_send_other_messages   BOOLEAN   NOT NULL,
    can_add_web_page_previews BOOLEAN   NOT NULL,
    can_change_info           BOOLEAN   NOT NULL,
    can_invite_users          BOOLEAN   NOT NULL,
    can_pin_messages          BOOLEAN   NOT NULL,
    member_telegram_id        TYPE BIGINT,
    kicked                    BOOLEAN   NOT NULL DEFAULT FALSE,
    kick_message_id           BIGINT,
    UNIQUE (chat_id, member_telegram_id)
);

