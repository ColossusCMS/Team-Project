use snsproject;
-- usertbl 생성
drop table if exists usertbl;
create table usertbl(
	userno varchar(8) not null primary key,
    username varchar(10) not null,
    userpassword varchar(20) not null,
    usermail varchar(30) not null unique,
    usertel varchar(13) not null unique,
    userimgpath varchar(1024),
    userdept varchar(10) not null,
    userposition varchar(5) default '사원',
    userStatusMsg varchar(60),
    userLoginStatus int default 0,
    adminAvailable int default 0
  --  foreign key (userdept) references depttbl(deptname)
);
select * from usertbl;
update usertbl set userloginstatus = 0;
update usertbl set userimgpath = 'uploadedfiles/images/default.jpg';

drop table if exists depttbl;
create table depttbl(
	deptno varchar(2) not null primary key,
    deptname varchar(4) not null
);
insert into depttbl values ('10', '개발'), ('20', '기획'), ('30', '경영'), ('40', '인사'), ('50', '영업'), ('60', '디자인');

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

create table scheduletbl(
	schno int auto_increment primary key not null,
    schuserno varchar(8) not null,
    schtitle varchar(32) not null,
    schcontent varchar(256) not null,
    schentrydate date not null,
    schgroup int default 1
);
select * from scheduletbl;
select * from scheduletbl where schuserno = 0000 and schgroup in ((select d.deptno from depttbl d inner join usertbl u on u.userdept = d.deptname where u.userno = 9999), 0) and schentrydate = '2019-11-11';
select schno, schtitle, schcontent from scheduletbl where schuserno = 9999 and schentrydate = date(now()) order by schentrydate desc;

create table dayofftbl(
	dono int auto_increment primary key not null,
    douserno varchar(8) not null,
    dotitle varchar(128) not null,
    dostart date not null,
    doend date not null,
    docontent varchar(128) not null
);

select * from dayofftbl;

create table noticetbl(
	noticeno int auto_increment not null primary key,
    noticeclass varchar(10) not null,
    noticetitle varchar(30) not null,
    noticecontent varchar(50) not null,
    noticeavailable int not null default 1
);
insert into noticetbl values(null, '공지', '연말 행사 안내', '연말을 맞이하여 연말 행사를 진행할 예정입니다.\n참석 여부나 자세한 내용은 관리자에게 문의해주세요.', default);