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
    (1, 'uid-1', 1);