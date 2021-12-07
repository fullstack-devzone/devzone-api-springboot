
INSERT INTO roles (id, name, created_at) VALUES
(1, 'ROLE_ADMIN', CURRENT_TIMESTAMP),
(2, 'ROLE_MODERATOR', CURRENT_TIMESTAMP),
(3, 'ROLE_USER', CURRENT_TIMESTAMP)
;

INSERT INTO users (email, password, name, bio, location, github_username, twitter_username, created_at) VALUES
('admin@gmail.com', '$2a$10$g8mojA4UcTsnuJtxMxWjyefp7P.ezB2Km99Jgiydy9/0A3dh2oVqy', 'Admin', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
('demo@gmail.com', '$2a$10$cNGCHDB..qHYEg2Fd.IRDeqQ2UdckIAOBUoQtEq/rpzcT2hwc8wka', 'Demo User', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP),
('siva@gmail.com', '$2a$10$CIXGKN9rPfV/mmBMYas.SemoT9mfVUUwUxueFpU3DcWhuNo5fexYC', 'Siva', 'Siva Bio', 'Hyderabad', 'sivaprasadreddy', 'sivalabs', CURRENT_TIMESTAMP)
;

INSERT INTO user_role (user_id, role_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 3),
(3, 2)
;

INSERT INTO user_skills (user_id, skill) VALUES
 (1, 'AWS'),
 (1, 'Azure DevOps'),
 (1, 'Python'),
 (2, 'Java'),
 (3, 'Java'),
 (3, 'AWS'),
 (3, 'GCP')
;

INSERT INTO tags(name) VALUES
('java'),
('spring'),
('spring-boot'),
('spring-cloud'),
('jpa'),
('hibernate'),
('junit'),
('devops'),
('maven'),
('gradle'),
('security')
;
