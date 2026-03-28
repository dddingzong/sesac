# SeSac

고립·은둔 청년의 회복과 사회적 재연결을 돕기 위한 Spring Boot 기반 웹 서비스입니다.  
심리적 회복을 직접 진단하거나 치료하는 서비스가 아니라, 작은 행동 기록, 맞춤형 정보, 커뮤니티 참여를 통해 일상 복귀를 돕는 회복형 플랫폼을 목표로 합니다.

## 서비스 개요

SeSac은 다음 4가지 흐름을 하나의 서비스 안에서 연결합니다.

- 성장형 메인 대시보드
  사용자의 경험치와 성장 단계를 시각적으로 보여줍니다.
- 일일 미션
  작은 생활 습관, 외부 활동, 만남 기반 미션을 통해 점진적인 행동 변화를 유도합니다.
- 맞춤형 정보
  복지 정보와 취업 정보를 관심사에 맞게 필터링해 제공합니다.
- 커뮤니티
  부담이 낮은 소규모 모임 게시판으로 사회적 연결을 다시 시작할 수 있게 합니다.

## 핵심 기능

### 1. 로그인 / 회원가입

- 로그인 ID와 비밀번호 기반 인증
- 관심 정보 유형 선택
  `복지`, `취업`, `모두`
- 세션 기반 로그인 상태 유지

### 2. 메인 대시보드

- 현재 성장 단계 표시
- 일일 미션 2개 제공
- 미션 완료 여부 기록
- 추천 정보와 최근 모임 미리보기 제공

### 3. 맞춤형 정보 페이지

- 관심 정보 기반 기본 추천
- 역할별 필터링
  `전체`, `복지`, `취업`
- 제목 검색
- 크롤링 기반 정보 갱신

### 4. 커뮤니티 게시판

- 모임 생성
- 게시글 목록 조회 및 검색
- 모임 참가 / 나가기
- 정원 마감 시 추가 참가 제한
- 작성자만 게시글 삭제 가능

### 5. 마이페이지

- 비밀번호 변경
- 관심 정보 설정 변경
- 현재 참여 중인 모임 확인

## 현재 아키텍처

현재 프로젝트는 전형적인 MVC 구조에서 한 단계 정리된 형태입니다.

- `controller`
  HTTP 요청 진입점, 세션 확인, 뷰 반환
- `application`
  화면 단위 유스케이스 조립, 페이지용 View 모델 생성
- `domain`
  엔티티와 enum, converter
- `service`
  영속성/업무 처리 로직
- `repository`
  JPA Repository
- `templates`, `static`
  Thymeleaf 기반 프론트엔드

구조적으로는 `controller -> application -> service/repository -> domain` 흐름을 따릅니다.

## 기술 스택

- Language: Java 17
- Framework: Spring Boot 3.2
- View: Thymeleaf
- Persistence: Spring Data JPA
- Database: H2 in-memory
- Build: Gradle
- Crawling: Selenium

## 로컬 실행

### 요구 사항

- Java 17
- Chrome 브라우저
- Gradle Wrapper 사용 가능 환경

### 실행 방법

```bash
sh gradlew bootRun
```

앱 실행 후 접속:

- 메인 진입: `http://localhost:8080/`
- H2 콘솔: `http://localhost:8080/h2-console`

### 기본 데이터

애플리케이션 시작 시 [data.sql](/Users/chungjongin/Desktop/forProject/sesac/src/main/resources/data.sql) 이 로드됩니다.

기본 계정 예시:

- `chung / 1234`
- `ddding / 1234`
- `water / 1234`
- `sangjun / 1234`

### 현재 로컬 DB 설정

[application.properties](/Users/chungjongin/Desktop/forProject/sesac/src/main/resources/application.properties)

```properties
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create
spring.sql.init.mode=always
```

즉, 현재 저장소 기준 실행 환경은 MySQL이 아니라 H2 메모리 DB입니다.  
애플리케이션 재시작 시 데이터는 매번 초기화됩니다.

## 테스트

```bash
sh gradlew test
```

현재는 기본 컨텍스트 로드 테스트와 회원가입-회원정보 매핑 검증 테스트가 포함되어 있습니다.

## 크롤링 동작 방식

정보 페이지의 새로고침 기능은 Selenium 기반 크롤링으로 동작합니다.

- 기본적으로 headless Chrome 실행
- `webdriver.chrome.driver` 시스템 프로퍼티 또는 `CHROME_DRIVER_PATH` 환경 변수를 우선 사용
- 크롤링 실패 시 기존 정보 데이터를 삭제하지 않고 유지

예시:

```bash
export CHROME_DRIVER_PATH=/path/to/chromedriver
sh gradlew bootRun
```

참고:

- 사이트 구조가 바뀌면 XPath 기반 크롤링이 쉽게 깨질 수 있습니다.
- 서버 환경에서 Chrome 실행 환경이 없으면 정보 갱신 기능은 실패합니다.

## 주요 엔드포인트

- `GET /`
- `GET /login`
- `POST /login/signup`
- `POST /login/signin`
- `GET /main`
- `POST /mission/complete`
- `GET /information/{page}`
- `GET /information/getCareAndJobInformation`
- `GET /board/{page}`
- `GET /board/content/{id}`
- `GET /board/create`
- `POST /board/create`
- `GET /mypage`
- `POST /mypage/update`

## 최근 정리된 내용

- application 계층 분리
- 공통 세션 사용자 조회 로직 정리
- enum 기반 역할/미션 단계 모델링
- 프론트엔드 공통 레이아웃 및 공통 스타일 정리
- `Member` 와 `MemberInfo` shared PK 매핑으로 사용자-프로필 정합성 개선
- 회원가입 submit 흐름 정리
  Enter 제출 포함
- 정보 새로고침 실패 시 기존 데이터 보존

## 현재 알려진 한계

- `board/deadline` 은 실제 상태 전이보다 안내 메시지 성격이 강합니다.
- 모임 마감 후 3시간 뒤 정산은 메모리 스케줄러 기반이라 서버 재시작 시 유실될 수 있습니다.
- 크롤링은 외부 사이트 구조와 Chrome 실행 환경에 강하게 의존합니다.
- 테스트 커버리지는 아직 낮습니다.

## 디렉터리 요약

```text
src/main/java/project/sesac
├── application
├── controller
├── domain
├── repository
└── service

src/main/resources
├── static
├── templates
├── application.properties
└── data.sql
```

## 다음 개선 우선순위

1. 게시판 마감 로직을 실제 상태 기반으로 변경
2. 3시간 후 정산을 메모리 스케줄러가 아닌 영속적 작업으로 전환
3. 크롤링 로직을 별도 인프라 계층으로 분리
4. 핵심 유스케이스 테스트 확대
