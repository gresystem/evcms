## JDK 설치
해당 프로젝트는 java8로 이루어져있다고 pom.xml 파일에 명시되어있다.
따라서 자바 개발을 위한 키트를 한번에 다운받아서 적용시켜야한다.
mac 사용자는
https://www.oracle.com/java/technologies/downloads/#jdk18-mac 링크로 들어가서 java8버전 mac os 를 다운받으면된다.

## 확인하기
터미널에을 치면 다운로드받은 버전 (1.8.0_341) 이 나올것이다
```bash
java -version #java version "1.8.0_341 ...etc"
```
다운받은 자바 위치는 아래 커맨드로 가능하다
```bash
which java #/usr/bin/java
```


## 적용
프로젝트 설정으로 들어가 SDK 설정을 다운받은 java설정으로 변경한다.


## log 파일 위치 변경
1. gre-sys-evcms/src/main/java/kr/co/watchpoint/evcms/logs에  logs 폴더 생성
2. gre-sys-evcms/src/main/resources/logback-spring.xml 해당 위치 xml 파일의 3번 라인의 property 값을 log폴더의 절대경로 수정


## 실행하기
1. 빌드
2. 실행 (Application.java)
3. logs 폴더내 logback.log를 이용하여 실행확인


성공하는 경우
```
   [2022-08-16 14:06:14:574] INFO  kr.co.watchpoint.evcms.Application - Starting Application using Java 1.8.0_341 on igyubin-ui-MacBookPro.local with PID 33285 (/Users/gyubin/project/gre-sys-evcms/target/classes started by gyubin in /Users/gyubin/project/gre-sys-evcms)
   [2022-08-16 14:06:14:579] INFO  kr.co.watchpoint.evcms.Application - No active profile set, falling back to default profiles: default
   [2022-08-16 14:06:15:1862] INFO  o.s.b.w.e.tomcat.TomcatWebServer - Tomcat initialized with port(s): 8080 (http)
   [2022-08-16 14:06:15:1869] INFO  o.a.coyote.http11.Http11NioProtocol - Initializing ProtocolHandler ["http-nio-8080"]
   [2022-08-16 14:06:15:1869] INFO  o.a.catalina.core.StandardService - Starting service [Tomcat]
   [2022-08-16 14:06:15:1870] INFO  o.a.catalina.core.StandardEngine - Starting Servlet engine: [Apache Tomcat/9.0.44]
   [2022-08-16 14:06:15:1922] INFO  o.a.c.c.C.[Tomcat].[localhost].[/] - Initializing Spring embedded WebApplicationContext
   [2022-08-16 14:06:15:1922] INFO  o.s.b.w.s.c.ServletWebServerApplicationContext - Root WebApplicationContext: initialization completed in 1288 ms
   [2022-08-16 14:06:16:2553] INFO  o.s.s.c.ThreadPoolTaskExecutor - Initializing ExecutorService 'applicationTaskExecutor'
   [2022-08-16 14:06:16:2920] INFO  o.a.coyote.http11.Http11NioProtocol - Starting ProtocolHandler ["http-nio-8080"]
   [2022-08-16 14:06:16:2935] INFO  o.s.b.w.e.tomcat.TomcatWebServer - Tomcat started on port(s): 8080 (http) with context path ''
   [2022-08-16 14:06:16:2955] INFO  kr.co.watchpoint.evcms.Application - Started Application in 2.805 seconds (JVM running for 3.276)
```
