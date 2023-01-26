INSERT IGNORE INTO users
(id,
 enabled,
 name,
 password,
 username)
VALUES (1,
        1,
        "DDDD",
        "$2a$10$74VqfzUpHOQLLJs9mIwjY.2bgewt0.Y2tfcCz6R5eUiXqf.xVaony",
        "admin@gmail.com");

INSERT IGNORE INTO roles
(id,
 name)
VALUES (1,
        "ADMIN");

INSERT IGNORE INTO users_roles
(user_id,
 role_id)
VALUES (1,
        1);