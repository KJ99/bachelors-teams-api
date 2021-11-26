create table teams (
    id int not null primary key auto_increment,
    name varchar(255) not null,
    picture_id int default null,
    created_at datetime not null default current_timestamp,
    updated_at datetime default null,
    settings_theme varchar(32) not null,

    foreign key (picture_id)
        references uploaded_files(id)
        on delete cascade
);