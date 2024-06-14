-- Active: 1716800736662@@127.0.0.1@3306@goodz

-- user 컬럼명 나열
-- user_id, no, username, password, birth, phone_number, created_at, updated_at
-- #{user_id}, #{no}, #{username}, #{password}, #{birth}, #{phone_number}, #{created_at}, #{updated_at}



-- 회원 등록
INSERT INTO user (user_id, username, nickname, password, birth, phone_number)
VALUES ('user', '이정용', 'wjddydWkd','$2a$12$TrN..KcVjciCiz.5Vj96YOBljeVTTGJ9AUKmtfbGpgc9hmC7BxQ92', '000325000325', '01012341234');
-- 권한 추가
INSERT INTO user_auth(user_id, auth) VALUES ('user','ROLE_USER');


-- 관리자1 등록 / 권한등록
INSERT INTO user (user_id, username, nickname, password, birth, phone_number)
VALUES ('admin', '김도희', 'ehgmlWkd', '$2a$12$TrN..KcVjciCiz.5Vj96YOBljeVTTGJ9AUKmtfbGpgc9hmC7BxQ92', '9708241234567', '01012341234');
INSERT INTO user_auth(user_id, auth) VALUES ('admin','ROLE_USER');
INSERT INTO user_auth(user_id, auth) VALUES ('admin','ROLE_ADMIN');

-- 관리자2 등록 / 권한등록
INSERT INTO user (user_id, username, nickname, password, birth, phone_number)
VALUES ('admin2', '정용', 'admin21', '$2a$12$TrN..KcVjciCiz.5Vj96YOBljeVTTGJ9AUKmtfbGpgc9hmC7BxQ92', '0003251231231', '01012341234');
INSERT INTO user_auth(user_id, auth) VALUES ('admin2','ROLE_USER');
INSERT INTO user_auth(user_id, auth) VALUES ('admin2','ROLE_ADMIN');

