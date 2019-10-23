use sampledb;
-- usertbl 생성
drop table if exists usertbl;
create table usertbl(
	userno varchar(8) not null primary key,
    username varchar(5) not null,
    userpassword varchar(20) not null,
    usermail varchar(30) not null unique,
    usertel varchar(13) not null unique,
    userimgpath varchar(1024),
    userdept varchar(10) not null,
    userposition varchar(5) default '사원',
    userStatusMsg varchar(60),
    adminAvailable int default 0
  --  foreign key (userdept) references depttbl(deptname)
);

drop table if exists depttbl;
create table depttbl(
	deptno varchar(2) not null primary key,
    deptname varchar(4) not null
);

drop table if exists boardtbl;
create table boardtbl(
	boardno int auto_increment primary key,
    boardheader varchar(10) not null,
    boardtitle varchar(32) not null,
    boardcontent varchar(4096),
    boardpassword varchar(16) not null,
    boarduserno varchar(8) not null,
    boarddate datetime not null,
    boardfile varchar(512),
    boardavailable int not null default 1
-- ,    foreign key (userno) references usertbl(userno)
);