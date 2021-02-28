<h3> 리포지토리를 가짜 구현으로 사용</h3>

* 동일 ID를 가진 회원이 존재할 경우 에러 발생하는 테스트 작성 아래 테스트 골격을 사용<br>

```java 
asssertThrows(Duplication.class, () -> 
    {userRegister.register("ID","pw","email"")});

```

* 이메일 발송 여부를 확인하기 위해 스파이를 사용
    * 이메일 발송 여부와 발송을 요청할 때 사용한 이메일 주소를 제공할 수 있어야 한다.
    