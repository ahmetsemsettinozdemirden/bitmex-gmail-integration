# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bitmex_credentials (
  id                            bigint auto_increment not null,
  api_key                       varchar(255) not null,
  api_secret                    varchar(255) not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_bitmex_credentials primary key (id)
);


# --- !Downs

drop table if exists bitmex_credentials;

