# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table admin (
  id                            bigint auto_increment not null,
  username                      varchar(255) not null,
  password                      varchar(255) not null,
  token                         varchar(255) not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_admin primary key (id)
);

create table bitmex_credentials (
  id                            bigint auto_increment not null,
  api_key                       varchar(255) not null,
  api_secret                    varchar(255) not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_bitmex_credentials primary key (id)
);

create table gmail_credentials (
  id                            bigint auto_increment not null,
  email                         varchar(255) not null,
  credentials                   varchar(255) not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_gmail_credentials primary key (id)
);


# --- !Downs

drop table if exists admin;

drop table if exists bitmex_credentials;

drop table if exists gmail_credentials;

