insert into Member (main_id,login_id,login_password) VALUES (11,'chung','1234');

insert into Member_info (main_id, name, exp, point, point_role, choose_role) values (11,'정종인',20,5,0,0);

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

INSERT INTO information (id, title, url, info_role) VALUES (200,'테스트용1','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (201,'테스트용2','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (202,'테스트용3','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (203,'테스트용4','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (204,'테스트용5','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (205,'테스트용6','/',1);
INSERT INTO information (id, title, url, info_role) VALUES (206,'테스트용7','/',1);
INSERT INTO information (id, title, url, info_role) VALUES (207,'테스트용8','/',1);
INSERT INTO information (id, title, url, info_role) VALUES (208,'테스트용9','/',1);
INSERT INTO information (id, title, url, info_role) VALUES (209,'테스트용10','/',1);
INSERT INTO information (id, title, url, info_role) VALUES (210,'테스트용11','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (211,'테스트용12','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (212,'테스트용13','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (213,'테스트용14','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (214,'테스트용15','/',0);

INSERT INTO board (board_id, title, author, content, total, agent, agent_full) VALUES (100,'책읽을사람','chung','롤 5인큐 구해요',5,1,'false');

INSERT INTO agent (id, board_id, agent) VALUES (200,100,'chung');