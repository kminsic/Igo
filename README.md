# 📁내돈내여 Project
![KakaoTalk_Photo_2022-10-21-14-36-15](https://user-images.githubusercontent.com/44489399/197121692-0bffac8f-eb68-4f17-8dda-cd8ff5e02e8b.jpeg)
> 내 돈으로 내가 다녀온 여행코스를 공유하는 플랫폼입니다. 광고에 지친 여러분, 날것의 여행 후기를 참고해보세요!

----------

### 🔎 개발환경
   - Springboot 2.7.2
   - gradle 7.5
   - Security / JWT
   - AWS MySQL
   - AWS S3
   - Tika

### 🔎 주요기능
#### 회원관리
 1. 소셜 로그인(네이버, 카카오) / 회원가입 / 로그인 / 로그아웃 / 회원탈퇴

#### 조회 기능
 1. 여행 관심사 선택 및 관심사 기반의 게시글로 추천페이지 구성
 2. 지역, 관심사, 비용별 게시글 조회  
 3. 조회순/최신순/추천순 정렬 게시글 조회(+ 무한 스크롤)
 4. 제목, 내용 기반의 게시글 검색 기능

#### 상세조회 기능
 1. 게시글 신고 및 좋아요 기능
 2. 게시글 댓글 작성, 수정, 삭제

#### 작성 기능
 1. 여행 후기 작성, 수정, 삭제       
 2. 여행 영상 스토리 기능 (모든 회원의 스토리 조회 가능, 12시간 후 삭제됨)

#### 마이페이지
 1. 프로필, 닉네임 / 관심 여행 키워드 수정
 2. 작성 게시글 / 좋아요 한 게시글 모아보기
 3. 개인 일정 작성, 수정, 삭제(일정완료 <-> 일정취소 기능)
 4. 작성한 게시글에 대한 댓글/좋아요 알림 기능

### 🔎ERD
![igo1](https://user-images.githubusercontent.com/44489399/197399324-f39dbc87-a6b9-476d-b44b-ac4876cbd2a4.png)

### 🔎서비스 아키텍처
![스크린샷 2022-10-21 오후 3 00 30](https://user-images.githubusercontent.com/44489399/197123391-901fdfba-161c-452c-8503-3705235eca9d.png)

### 💡API 설계
|기능|메서드|Url|
|-----------------|------|------------------|
|카카오 oauth2 로그인 |GET|/kakao/callback|
|네이버 oauth2 로그인 |GET|/naver/callback<br>|
|JWT 재발급 |GET| /refresh |
|회원가입 |POST| /api/member/signup |
|로그인 |POST| /api/member/login |
|회원가입 |POST| /api/member/signup |
|로그아웃	|POST|/api/member/logout|<br>
|회원탈퇴 |DELETE| /api/member/withdrawal/{id} |
|회원가입 |POST| /api/member/signup |
|로그인 후 태그 설정 |PUT|/api/member/tag|<br>
|로그인 후 추천페이지 |GET|/api/member/posts|<br>
|전체 게시글 조회|GET|/api/posts|<br>
|그룹별 게시글 조회(무한스크롤) |GET|/api/posts/group?type={type}&page=0|<br>
|관심사별 게시글 조회 |GET|/api/posts/interest?type=interest|<br>
|지역별 게시글 조회 |GET|/api/posts/region?type=region|<br>
|비용별 게시글 조회 |GET|/api/posts/cost?type=cost|<br>
|게시글 상세조회 |GET|/api/detail/{postId}|<br>
|게시글 작성	|POST|	/api/post|<br>
|게시글 수정	|PUT|/api/post/{id}|<br>
|게시글 삭제	|DELETE|	/api/post/{id}|<br>
|좋아요 기능	|POST|	/api/heart/{id}|<br>
|신고 기능	|POST|	/api/report|/{id}<br>
|스토리 조회	|GET|	/api/storys|<br>
|스토리 작성	|POST|	/api/story|<br>
|스토리 삭제 |DELETE| /api/story/{id} |
|댓글 작성	|POST|	/api/comment|<br>
|댓글 조회	|GET|	/api/comments/{id}|<br>
|댓글 수정	|PUT|	/api/comment/{id}|<br>
|댓글 삭제	|DELETE|	/api/comment/{id}|<br>
|나의 페이지 조회(태그, 닉네임, 이미지)	|GET|	/api/mypage|<br>
|나의 페이지/내가 쓴 글 불러오기	|GET|	/api/mypage/post|<br>
|나의 페이지/나의 일정 불러오기	|GET|	/api/mypost|<br>
|나의 페이지/좋아요 포스트 불러오기	|GET|	api/mypage/likepost|<br>
|프로필 수정	|PUT|	/api/mypage/profile|<br>
|나의 페이지 카테고리 수정 	|PUT|	/api/member/tag|<br>
|나의 일정 조회	|GET|	/api/mypost|<br>
|나의 일정 작성	|POST|	/api/mypost|<br>
|나의 일정 수정	|PATCH|	/api/mypost|<br>
|나의 일정 삭제	|DELETE|	/api/mypost/{id}|<br>
|나의 일정 완료	|POST|	/api/mypost/done/{id}|<br>
|나의 일정 완료취소	|POST|	/api/mypost/cancel/{id}|<br>
|게시글 검색	|GET|	/api/search/{keyword}|<br><br>
|알림 구독 |GET| /api/member/subscribe |
|전체 알림 조회 |GET| /api/member/notifications|
|알림 읽음 설정 |PATCH| /api/member/notifications/{id}|
|알림 삭제 |DELETE| /api/member/notifications/{id}|


### 💡트러블슈팅

1.**문제**<br>
   글과 이미지url를 컬럼 하나에 묶어 보낼 때 컬럼의 글자수 제한<br>

**원인**<br>
프론트에서 한 개의 Column에 text와 imgurl을 함께 담아 JSON형식으로 변환해 백엔드에 보내는 방법을 사용하기로 결정했다.<br>
사진 1개와 글은 문제없이 들어왔으나, 사진2개 이상과 글을 담아 보내는 과정에서 에러가 발생했다.<br>
에러 내용은 컬럼안에 내용들을 다 담지 못한다는 오류다.<br><br>

**해결**<br>
Column 에 추가적으로 @Column(columnDefinition = "TEXT") 입력<br>
columnDefinition으로 기본 엔티티 속성 값을 설정할 수 있기에 단순 TEXT형식으로 오고 가도록 설정<br>
Content 엔티티 안에 글 내용과 이미지를 함께 불러오는 블로그 형식의 방식이었기에 지금의 TEXT 설정을 유용하게 사용하였고, <br>
JSON 타입으로 RDB저장에 사용할 때는 TEXT대신 JSON을 입력하여 JPA의 의존성 기능을 더 활용할 수 있겠다.<br><br>

2.**문제**<br>
깃 액션을 이용한 자동 배포시 8081과 8082포트가 동시에 nohup으로 시작되는 순간<br>
ec2에 있는 인스턴스가 cpu 100%를 사용하면서 멈추는 현상<br><br>
**원인** <br>
ec2를 가동시키는 하드웨어 자체에 이슈가 있는 경우도 있다고하여 <br>
기존 인스턴스를 종료 후 새로운 인스턴스를 만들어 사용해봤지만 이슈는 같았다.<br>
인스턴스를 실행시키는 ubuntu 자체의 문제라고 생각이 되어 <br>
찾아본 결과 프리티어의 메모리는 1GB정도로 지급된다고 한다.<br>
1GB의 메모리로 8081,8082를 동시에 연다는 것이 힘들다고 생각이 됐고<br>
이 과정에서 인스턴스의 메모리 부족이라고 생각이 됐다.<br><br>
**해결**<br>
swap파일을 하나 생성해주어 hdd의 일정공간을 ram처럼 사용하게 하였다.<br>
1. 2GB의 빈 디스크를 만들어주었다 <br>
$ sudo dd if=/dev/zero of=/swapfile bs=128M count=32<br>
2. 만든 swap파일의 권한을 생성해주었다<br>
$ sudo chmod 600 /swapfile<br>
3. 스왑파일로 설정<br>
$ sudo mkswap /swapfile<br>
4. 스왑파일을 실행<br>
$ sudo swapon /swapfile<br>
5. 인스턴스 재시작시 swap파일을 다시 기동시켜줘야하는데 이 작업을 건너뛰기 위해서<br>
fstab파일에 값을 추가해 인스턴스를 재시작할때 자동으로 해당 swap을 실행시켜주게 설정.<br>
 vi /etc/fstab<br>
/root/swapfile swap swap auto 0 0<br>
후 처리 예정<br>
1.ubuntu에서 생성한 swap으로도 서버개설이 힘들다고 생각되면<br>
t2.micro대신 t2.meduim으로 변경 예정<br>

2.swap 메모리 추가<br>
사실 이 방법은 좋지 않은 방법 같다<br>
메모리를 대체할수는 있지만 100%는 힘들다<br>
30배 정도의 차이가 나는 방법이기 때문에 <br>
최대한 1번의 방법으로 생각중. <br>

