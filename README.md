# SmokingArea

## Operating Systems, Requirements, and Dependencies
* Windows 10
* Eclipse 4.30 with java-se-8u43-ri
* Oracle 11gXE (11.2)

## Quick Start
1. 이 저장소를 클론하고 `EclipseProject` 내의 자바 프로젝트를 가져온다.
2. 오라클에 아래와 같은 명령어로 테이블을 생성한다.
 ```SQL
CREATE TABLE smoking_area(
  id varchar2(20), area_nm varchar2(50), area_desc varchar2(200), 
  ctprvnnm varchar2(20), signgunm varchar2(20), emdnm varchar2(20), area_se varchar2(200),
  rdnmadr varchar2(200), lnmadr varchar2(200), inst_nm varchar2(20)
);
```
3. 코드를 실행하기 위해서는 `JSON`과 `ojdbc6`이 필요하다.
   <br>
   [JSON](https://mvnrepository.com/artifact/org.json/json/20230618) 
   [ojdb6](https://mvnrepository.com/artifact/com.oracle.database.jdbc/ojdbc6/11.2.0.4)
4. 패키지 `com.sds.apiTest.apitest`내의 `ApiExplorer`로 오라클에 값을 받아올 수 있다.<br>
   단, 코드 내 `urlBuilder` 생성 시 `Service Key`는 직접 입력해넣을 것.

