create table team_members (
    id int not null primary key auto_increment,
    user_id varchar(255) not null,
    team_id int not null,
    created_at datetime not null default current_timestamp,
    updated_at datetime default null,
    foreign key (team_id)
        references teams(id)
        on delete cascade
);