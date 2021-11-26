create table team_invitations (
    id int not null primary key auto_increment,
    token varchar(255) not null unique,
    code varchar(32) not null unique,
    team_id int not null,
    created_at datetime not null default current_timestamp,
    updated_at datetime default null,
    expires_at datetime not null,
    foreign key (team_id)
        references teams(id)
        on delete cascade
);