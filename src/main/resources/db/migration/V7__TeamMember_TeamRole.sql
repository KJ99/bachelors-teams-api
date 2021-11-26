create table team_member_roles (
    team_member_id int not null,
    role_id varchar(32) not null,
    foreign key (team_member_id) references team_members(id) on delete cascade,
    foreign key (team_member_id) references team_members(id) on delete cascade,
    primary key(team_member_id, role_id)
);