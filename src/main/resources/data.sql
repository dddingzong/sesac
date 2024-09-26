INSERT INTO Member (main_id,login_id,login_password) VALUES (11,'chung','1234');
INSERT INTO Member (main_id,login_id,login_password) VALUES (12,'ddding','1234');
INSERT INTO Member (main_id,login_id,login_password) VALUES (13,'yoon','0101');

INSERT INTO Member_info (main_id, name, exp, point, choose_role) values (11,'정종인',20,5,0);
INSERT INTO Member_info (main_id, name, exp, point, choose_role) values (12,'정종인',250,15,1);
INSERT INTO Member_info (main_id, name, exp, point, choose_role) values (13,'윤신헤',1600,25,2);

insert into mission (id, content, content_role) values (1,'방 청소하기',0);
insert into mission (id, content, content_role) values (2,'밥 세끼 챙겨먹기',0);
insert into mission (id, content, content_role) values (3,'책 한시간 읽기',0);
insert into mission (id, content, content_role) values (4,'설거지 하기',0);
insert into mission (id, content, content_role) values (5,'산책하기',1);
insert into mission (id, content, content_role) values (6,'영화보기',1);
insert into mission (id, content, content_role) values (7,'카페에서 음료수 사오기',1);
insert into mission (id, content, content_role) values (8,'운동하기',1);
insert into mission (id, content, content_role) values (9,'지인 만나기',2);
insert into mission (id, content, content_role) values (10,'지인과 통화하기',2);
insert into mission (id, content, content_role) values (11,'지인과 같이 운동하기',2);
insert into mission (id, content, content_role) values (12,'지인과 카페가기',2);


insert into default_mission (id, mission1, mission2) values (1,'defualt_mission1','default_mission2');
insert into outside_mission (id, mission1, mission2) values (1,'outside_mission1','outside_mission2');
insert into meet_mission (id, mission1, mission2) values (1,'meet_mission1','meet_mission2');