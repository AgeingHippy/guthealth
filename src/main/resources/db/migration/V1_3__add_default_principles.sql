INSERT INTO user_meta
    (name, email, bio)
VALUES
    ('admin','',''),
    ('system','','');

INSERT INTO principle
    (username, password, user_meta_id)
VALUES
    ('admin', '$2a$10$V.BtrCJnHl7dxhHeJmm1RurYuXTPyudch3FVnPEwUTKxBwKvAdbem', (SELECT id from user_meta WHERE name = 'admin')),
    ('system', '$2a$10$W9PJKMvG1O1MM79JxE3VBOCq0m7DOZLD/ky8mWmtcQ3qziR/KR7KK', (SELECT id from user_meta WHERE name = 'system'));

INSERT INTO principle_roles
    (principle_id, role_id)
VALUES
    ((SELECT id from principle WHERE username = 'admin'), (SELECT id from role WHERE authority = 'ROLE_ADMIN')),
    ((SELECT id from principle WHERE username = 'system'), (SELECT id from role WHERE authority = 'ROLE_USER'));
