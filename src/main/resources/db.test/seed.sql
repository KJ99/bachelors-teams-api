insert into uploaded_files (id, file_name, original_file_name, media_type) values
    (1, 'filename.jpg', 'or-filename.jpg', 'image/jpg'),
    (2, 'filename.jpg', 'or-filename.jpg', 'image/jpg'),
    (3, 'filename.jpg', 'or-filename.jpg', 'image/jpg');

insert into teams (id, name, picture_id, settings_theme) values
    (1, 'team-1', 1, 'DEFAULT'),
    (2, 'team-2', 2, 'DEFAULT'),
    (3, 'team-3', 3, 'DEFAULT');

insert into team_invitations (id, token, code, team_id, expires_at) values
    (1, 'valid-token-1', '0123456', 1, '3000-01-01 23:59:59'),
    (2, 'valid-token-2', '09867541', 2, '3000-01-01 23:59:59'),
    (3, 'expired-token-1', '07821', 1, '1000-01-01 23:59:59');

 insert into team_members (id, user_id, team_id) values
    (1, 'uid-1', 1),
    (2, 'uid-11', 1),
    (3, 'uid-100', 1);

 insert into team_member_roles (team_member_id, role_id) values
    (1, 'OWNER'),
    (2, 'TEAM_MEMBER'),
    (3, 'TEAM_MEMBER');

 insert into cache_items (item_key, tag, item_value, expires_at) values
    ('key-1', 'USER_PROFILE', '{"first_name": "Francis"}', '3000-12-12 22:22:22'),
    ('key-1', 'USER_PROFILE', '{"first_name": "John"}', '1000-12-12 22:22:22'),
    ('key-2', 'USER_PROFILE', '{"first_name": "Ronald"}', '1000-12-12 22:22:22'),
    ('uid-100', 'USER_PROFILE', '{"first_name": "John", "last_name": "Doe", "id": "uid-100"}', '3000-12-12 22:22:22');