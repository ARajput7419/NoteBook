create table User ( email varchar(50) primary key , name varchar(50) not null , password varchar(500) , oauth2 tinyint default 0)



create table Resource( id integer primary key AUTO_INCREMENT,
                        visibility varchar(7) default "Private" ,
                        location varchar (500) default "",
                        name varchar (30) not null,
                        count_ integer default 0 ,
                        timestamp_ timestamp default CURRENT_TIMESTAMP,
                        user_id varchar(50) not null ,
                        foreign key (user_id) references User(email)


)

create table Note ( id integer primary key AUTO_INCREMENT ,
                    name varchar(100) not null ,
                     visibility varchar(7) default "Private" ,
                     timestamp_ timestamp default CURRENT_TIMESTAMP,
                     content MEDIUMTEXT ,
                     user_id varchar(50) not null ,
                     foreign key (user_id) references User(email)

)

create table Chat (
                    id integer primary key AUTO_INCREMENT ,
                    from_userid varchar(50) not null ,
                     to_userid varchar(50) not null ,
                     timestamp_ timestamp default CURRENT_TIMESTAMP,
                     message varchar(1000)  not null,
                     foreign key(from_userid) references User(email),
                     foreign key(to_userid) references User(email)

 )