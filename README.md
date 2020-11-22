# 페이 뿌리기 API

## Feature
* Java 11
* Sprint boot 2.4.0
* JPA, h2
* gradle
* spring restdocs

## Getting Started
* build
```cmd
./gradlew clean bootJar
```
* run
```cmd
java -jar build/libs/sprinkle-pay-*.jar
```

## API Document
```
http://localhost:8081/docs/index.html
```

## 문제 해결 전략 & 고민거리들
* TOKEN 생성시 Random 문자열에 대한 별도 중복 체크로직을 만들지 않음
* 가져가기 API 호출 시 `비관적인 락(PESSIMISTIC_READ)` 을 걸어 동시에 가져가기 API호출 시 데이터에 문제가 발생하지 않도록 설계
  * 다량의 트래픽에 대비해 Redis를 사용한는게 더 적합하다고 판단됨.
* 개인 적으로 JPA n+1 문제를 해결하기 위해 query dsl 사용하는 걸 선호하나 해당 과제에서는 사용하지 않음
 




