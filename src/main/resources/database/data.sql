create schema `userservice`;
create table userservice.roles (id bigint not null auto_increment, name varchar(255), primary key (id)) engine=MyISAM;
create table userservice.user_role (user_id bigint not null, role_id bigint not null, primary key (user_id, role_id)) engine=MyISAM;
create table userservice.users (id bigint not null auto_increment, created_at datetime not null, updated_at datetime not null, email varchar(255), first_name varchar(255), last_name varchar(255), password varchar(255), phone varchar(255), username varchar(255), website varchar(255), primary key (id)) engine=MyISAM;

insert into userservice.roles(name) values('ROLE_ADMIN');
insert into userservice.roles(name) values('ROLE_USER');