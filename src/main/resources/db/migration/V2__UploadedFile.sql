create table uploaded_files (
    id int not null primary key auto_increment,
    file_name varchar(255) not null,
    original_file_name varchar(255) not null,
    media_type varchar(255) not null,
    created_at datetime default current_timestamp,
    updated_at datetime default null
);