# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table blog (
  id                        bigint not null,
  title                     varchar(255),
  author_id                 bigint,
  content                   varchar(255),
  constraint pk_blog primary key (id))
;

create table user (
  id                        bigint not null,
  email                     varchar(255),
  password                  varchar(255),
  user_type                 varchar(1),
  constraint ck_user_user_type check (user_type in ('B','R')),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

create sequence blog_seq;

create sequence user_seq;

alter table blog add constraint fk_blog_author_1 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_blog_author_1 on blog (author_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists blog;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists blog_seq;

drop sequence if exists user_seq;

