# Board-Service
공지사항을 등록/조회/수정/삭제할 수 있는 웹 어플리케이션

### Prerequisites
- Java 11

### 실행하기
* application.yml에 업로드할 파일을 저장할 절대 경로 지정하고 실행하기
``` yml
file:
  dir: '파일 업로드할 디렉토리 절대경로'
```

### Dependencies
Dependence         |Version
-------------------|-------
spring-boot       |2.6.2.RELEASE
spring-boot-starter-data-jpa |
spring-boot-starter-web |
spring-boot-starter-validation |
com.github.gavlyukovskiy:p6spy-spring-boot-starter |1.6.2
com.h2database:h2 |
org.projectlombok:lombok |
----

## API 리스트
* 공지사항 등록 
  * POST /api/v1/board
  * 등록 정보 : 제목, 내용, 공지 시작일시, 공지 종료일시, 여러 첨부파일
* 공지사항 리스트 조회 with Paging
  * GET /api/v1/board[?page={}&size={}&sort={}]
* 공지사항 조회 with ID
  * GET /api/v1/board/{id}
* 공지사항 수정
  * PUT /api/v1/board/{id}  
* 공지사항 삭제    
  * DELETE /api/v1/board/{id}  
