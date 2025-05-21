insert into Member (main_id,login_id,login_password) VALUES (11,'chung','1234');
insert into Member (main_id,login_id,login_password) VALUES (12,'ddding','1234');
insert into Member (main_id,login_id,login_password) VALUES (13,'water','1234');
insert into Member (main_id,login_id,login_password) VALUES (14,'sangjun','1234');

insert into Member_info (main_id, name, exp, point, point_role, choose_role) values (11,'정종인',20,5,0,0);
insert into Member_info (main_id, name, exp, point, point_role, choose_role) values (12,'정종인',20,5,0,0);
insert into Member_info (main_id, name, exp, point, point_role, choose_role) values (13,'박건우',20,5,0,0);
insert into Member_info (main_id, name, exp, point, point_role, choose_role) values (14,'강상준',20,5,0,0);

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

INSERT INTO information (id, title, url, info_role) VALUES (200,'김재옥 기자 "고립·은둔 청년 50만명 시대… 사회 연결망 구축 시급"','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (201,'고립·은둔 청년 증가, 아산시 실태조사로 해법 찾는다','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (202,'경남 고립·은둔 청년 3만3천200여명 추정…도, 실태조사','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (203,'인천청년미래센터, 고립은둔청년 지킴이 양성 부모 교육 첫 운영','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (204,'인천청년미래센터, ‘고립은둔청년 지킴이 양성 부모 교육’ 운영','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (205,'"은둔고립 정책, 대부분 청년 일자리 지원…청소년 정책 필요"','/',1);
INSERT INTO information (id, title, url, info_role) VALUES (206,'경남 고립·은둔 청소년 3만3000명…맞춤형 지원 나선다','/',1);
INSERT INTO information (id, title, url, info_role) VALUES (207,'김재희 "청소년복지지원법에 은둔고립 청소년 개념·지원사항 추가해야"','/',1);
INSERT INTO information (id, title, url, info_role) VALUES (208,'[뉴스+] "사회적 관계를 단절한 사람"···고립·은둔 청년, 사회에 나오게 하려면? ::::: 기사','/',1);
INSERT INTO information (id, title, url, info_role) VALUES (209,'경남도, 고립·은둔 청년 지원 확대...창원·통영·양산','/',1);
INSERT INTO information (id, title, url, info_role) VALUES (210,'테스트용11','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (211,'테스트용12','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (212,'테스트용13','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (213,'테스트용14','/',0);
INSERT INTO information (id, title, url, info_role) VALUES (214,'테스트용15','/',0);

INSERT INTO board (board_id, title, author, content, total, agent, agent_full) VALUES (100,'책읽을사람','chung','롤 5인큐 구해요',5,1,'false');
INSERT INTO board (board_id, title, author, content, total, agent, agent_full) VALUES (101,'선릉역 볼사람 구해요','water','롤 5인큐 구해요',3,2,'false');
INSERT INTO board (board_id, title, author, content, total, agent, agent_full) VALUES (102,'같이 게임해요','sangjun','롤 5인큐 구해요',5,3,'false');



INSERT INTO agent (id, board_id, agent) VALUES (200,100,'chung');
INSERT INTO agent (id, board_id, agent) VALUES (201,101,'water');
INSERT INTO agent (id, board_id, agent) VALUES (202,102,'sangjun');