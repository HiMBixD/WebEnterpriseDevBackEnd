INSERT INTO ROLES(role_id, role_name, description) VALUES(0, 'ROLE_ADMIN', 'Can do every thing, Has role of Administrator');
INSERT INTO ROLES(role_id, role_name, description) VALUES(1, 'ROLE_MANAGER', 'Has role of Marketing Manager');
INSERT INTO ROLES(role_id, role_name, description) VALUES(2, 'ROLE_COORDINATOR', 'Has role of Marketing Coordinator');
INSERT INTO ROLES(role_id, role_name, description) VALUES(3, 'ROLE_STUDENT', 'Has role of Students');
INSERT INTO ROLES(role_id, role_name, description) VALUES(4, 'ROLE_GUEST', 'Has role of Guest');

INSERT INTO USERS(USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, PHONE, EMAIL, role_id) VALUES('acc0', '$2a$10$brfLbKVxu8EZh6Wd3g9Hg.3LfnHxlpn2aLZmpR9BQZEJwwgyrAtRm', 'duc', 'anh', '12346', 'ANH@FPT', 0);
INSERT INTO USERS(USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, PHONE, EMAIL, role_id) VALUES('acc1', '$2a$10$brfLbKVxu8EZh6Wd3g9Hg.3LfnHxlpn2aLZmpR9BQZEJwwgyrAtRm', 'duc1', 'anh1', '12346', 'ANH@FPT', 1);
INSERT INTO USERS(USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, PHONE, EMAIL, role_id) VALUES('acc2', '$2a$10$brfLbKVxu8EZh6Wd3g9Hg.3LfnHxlpn2aLZmpR9BQZEJwwgyrAtRm', 'duc2', 'anh2', '12346', 'ANH@FPT', 2);
INSERT INTO USERS(USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, PHONE, EMAIL, role_id) VALUES('acc3', '$2a$10$brfLbKVxu8EZh6Wd3g9Hg.3LfnHxlpn2aLZmpR9BQZEJwwgyrAtRm', 'duc3', 'anh3', '12346', 'ANH@FPT', 3);
INSERT INTO USERS(USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, PHONE, EMAIL, role_id) VALUES('acc4', '$2a$10$brfLbKVxu8EZh6Wd3g9Hg.3LfnHxlpn2aLZmpR9BQZEJwwgyrAtRm', 'duc3', 'anh4', '12346', 'ANH@FPT', 4);
