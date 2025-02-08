INSERT INTO member (nickname, image, email, phone_number, social_type, social_uuid, total_group, total_vote)
VALUES ('john_doe', null, 'john.doe@example.com', '010-1234-5678', 'GOOGLE', 'uuid-1234', 1, 0);

INSERT INTO member (nickname, image, email, phone_number, social_type, social_uuid, total_group, total_vote)
VALUES ('alice_wonder', null, 'alice.wonder@example.com', '010-9876-5432', 'KAKAO', 'uuid-5678', 1, 0);

INSERT INTO member (nickname, image, email, phone_number, social_type, social_uuid, total_group, total_vote)
VALUES ('bob_builder', null, 'bob.builder@example.com', '010-4567-7890', 'NAVER', 'uuid-9012', 1, 0);

INSERT INTO member (nickname, image, email, phone_number, social_type, social_uuid, total_group, total_vote)
VALUES ('charlie_brown', null, 'charlie.brown@example.com', '010-1111-2222', 'NAVER', 'uuid-3456', 1, 0);

INSERT INTO member (nickname, image, email, phone_number, social_type, social_uuid, total_group, total_vote)
VALUES ('diana_prince', null, 'diana.prince@example.com', '010-3333-4444', 'KAKAO', 'uuid-7890', 0, 0);


INSERT INTO user_group (name, image, description, total_member_count, invite_code)
VALUES ('Developers Hub', null, 'A community for software developers.', 1, 'INV123');

INSERT INTO user_group (name, image, description, total_member_count, invite_code)
VALUES ('Fitness Enthusiasts', null, 'A group dedicated to fitness and health.', 3, 'FIT456');

INSERT INTO group_member (status, member_id, user_group_id)
VALUES ('OWNER', 1, 1);

INSERT INTO group_member (status, member_id, user_group_id)
VALUES ('WAITING', 4, 1);

INSERT INTO group_member (status, member_id, user_group_id)
VALUES ('WAITING', 1, 2);

INSERT INTO group_member (status, member_id, user_group_id)
VALUES ('OWNER', 2, 2);

INSERT INTO group_member (status, member_id, user_group_id)
VALUES ('MEMBER', 3, 2);

INSERT INTO group_member (status, member_id, user_group_id)
VALUES ('WAITING', 4, 2);




