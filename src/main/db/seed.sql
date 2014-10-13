create table movie (movie_id numeric not null, movie_name varchar not null, ratings numeric);
create table song ( movie_id numeric, song_id numeric not null, song_name varchar not null, album_id numeric);
create table actor( movie_id numeric, actor_id numeric not null, actor_name varchar not null);
create table director( movie_id numeric not null, director_id numeric not null, director_name varchar not null);


create table musician (id numeric not null, name varchar not null);
create table album (id numeric not null, name varchar not null, musician_id numeric);
create table team (id numeric not null, name varchar not null, average numeric);

create table primitive(intField integer, floatField numeric, doubleField numeric, booleanField boolean, longField numeric,intObjectField integer, floatObjectField numeric, doubleObjectField numeric, booleanObjectField boolean, longObjectField numeric );

create table assistant_director (id numeric not null, name varchar not null, director_id numeric);

