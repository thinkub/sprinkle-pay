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

## 고민거리들
* TOKEN 생성시 Random 문자열에 대한 별도 중복 체크로직을 만들지 않음
* 뿌리기 API 호출 시 미리 금액을 분배해두는 로직으로 가져가기 API 동시 호출 시 DB Lock에 대한 이슈가 있음
  * Redis를 사용하여 분배로직을 구현해야 한다고 생각.
* 뿌리기 API 호출 시 미리 금액을 분배하는 로직이 있기 때문에 받는 사용자가 많을 수록 API가 느린 이슈
  * 가져가기 API 호출 시 금액을 분배하는 방법이 더 좋은 구조라고 생각.
  * JPA 대량 Insert 를 해소해야 하는 고민이 필요
* 개인 적으로 JPA n+1 문제를 해결하기 위해 query dsl 사용하는 걸 선호하나 해당 과제에서는 사용하지 않음
 




