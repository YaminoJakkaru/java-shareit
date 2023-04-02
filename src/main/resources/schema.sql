drop schema public cascade;
create schema public;
CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name  VARCHAR(252)                            NOT NULL,
    email VARCHAR(320)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR(512)                            NOT NULL,
    requestor_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requestor FOREIGN KEY (requestor_id) REFERENCES users
);

CREATE TABLE IF NOT EXISTS item
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_name         VARCHAR(252)                            NOT NULL,
    description  VARCHAR(512)                            NOT NULL,
    is_available boolean                                 NOT NULL,
    owner_id     BIGINT                                  NOT NULL,
    request_id   BIGINT,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users,
    CONSTRAINT fk_request FOREIGN KEY (request_id) REFERENCES requests
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    item_id    BIGINT                                  NOT NULL,
    booker_id  BIGINT                                  NOT NULL,
    status     VARCHAR(20)                             NOT NULL,
    CONSTRAINT pk_bookings PRIMARY KEY (id),
    CONSTRAINT fk_booking_item FOREIGN KEY (item_id) REFERENCES item,
    CONSTRAINT fk_booker FOREIGN KEY (booker_id) REFERENCES users
);

CREATE TABLE IF NOT EXISTS comments
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    note         VARCHAR(512) NOT NULL,
    item_id      BIGINT                                  NOT NULL,
    author_id    BIGINT                                  NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comment_item FOREIGN KEY (item_id) REFERENCES item,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users
)

