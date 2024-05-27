-- Active: 1716800736662@@127.0.0.1@3306@goodz

-- user 컬럼명 나열
-- user_id, username, password, birth, phone_number, profile_picture_url, account, created_at, updated_at
-- #{user_id}, #{username}, #{password}, #{birth}, #{phone_number}, #{profile_picture_url}, #{account}, #{created_at}, #{updated_at}



-- 회원 등록
INSERT INTO user (user_id, username, password, birth, phone_number, profile_picture_url, account)
VALUES ('user', '이정용', '$2a$12$TrN..KcVjciCiz.5Vj96YOBljeVTTGJ9AUKmtfbGpgc9hmC7BxQ92', '1990/01/01', '010-1234-1234', 'https://xn--pe5b27r.com/img/good.png', 'joeun@naver.com');
-- 권한 추가
INSERT INTO user_auth(user_id, auth) VALUES ('user','ROLE_USER');


-- 관리자 등록
INSERT INTO user (user_id, username, password, birth, phone_number, profile_picture_url, account)
VALUES ('admin', '김도희', '$2a$12$TrN..KcVjciCiz.5Vj96YOBljeVTTGJ9AUKmtfbGpgc9hmC7BxQ92', '1990/01/01', '010-1234-1234', 'https://xn--pe5b27r.com/img/good.png', 'joeun@naver.com');
-- 관리자 권한 등록
INSERT INTO user_auth(user_id, auth) VALUES ('admin','ROLE_USER');
INSERT INTO user_auth(user_id, auth) VALUES ('admin','ROLE_ADMIN');

