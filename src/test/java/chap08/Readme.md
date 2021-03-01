<h3> 테스트 가능한 설계 </h3>

테스트 하기 어려운 코드
- 하드코딩된 경로의 문제
    - 운영체제에 따라 경로가 다름
    - 테스트를 진행하기 위해 해당 위치에 파일이 있어야함
```java
Path path = Paths.get("D:\data\pay\cp0001.csv");
List<PayInfo> payInfos =  Files.line(path);
//파일 불러와서 처리하는 로직
```
---

- 의존 객체를 직접 생성
    - 이 테스트를 위해서는 PayInfoDao가 올바르게 동작하는데 필요한 모든 환경을 구성해야함 ex)DB연결
```java
private PayInfoDao payInfoDao = new PayInfoDao();

public void sync() throws IOException{
    payInfos.forEach(pi -> payInfoDao.insert(pi));
}
```
---

- 정적매서드 사용
    - AuthUtil 클래스가 인증 서버와 통신하는 경우, 인증서버 필요
    - AuthUtil 클래스가 통신할 인증 서버 정보를 시스템 프로퍼티에서 가져온다면 시스템 프로퍼티도 테스트 환경에 맞게 설정해줘야 함
    - 다양한 상황을 테스트 하려면 인증 서버에 저장되어 있는 유효한 아이디와 암호도 필요
```java
public LoginResult login(String id, String pw){
    int resp =0;
    boolean authorized = AuthUtil.authorize(authKey);
    if(authorized){
        resp = AuthUtil.authenticate(id,pw);    
    }else{
        resp = -1;
    }
    //resp 값에 따른 로직
}
```

- 실행 시점에 따라 달라지는 결과
    - LocalDate.now()에 의해 오늘은 성공하지만 내일은 실패할 수도 있음
```java
public int calculatePoint(User u){
    Subscription s = subsccriptionDao.selectByUser(u.getId());
    if(s == null) throw new NoSubscriptionException();
    Product p = productDao.selectById(s.getProductId());
    LocalDate now = LocalDate.now() //
    int point = 0;
    if(s.isFinished(now)){
        point += p.getDefaultPoint();    
    }else{
        point += p.getDefaultPoint() + 10;      
    }
}
```

- 역할이 섞여 있는 코드
    - 위 코드에서 포인트 계산 로직만 테스트하고 싶은데, Subscription, ProductDao에 대한 대역을 구성해야함
    - 포인트 계산 자체는 point += p.getDefaultPoint(), point += p.getDefaultPoint() + 10; 만 필요함
  
- 이외에
    - 메서드 중간에 소켓 통신 코드가 포함되어 있다.
    - 콘솔에 입력을 받거나 결과를 콘솔에 출력한다.
    - 테스트 대상이 사용하는 의존 대상 클래스나 메서드가 final이다. 이경우 대역으로 대체가 어렵다.
    - 테스트 대상의 소스를 소유하고 있지 않아 수정이 어렵다.
  

<h3> 테스트 가능한 설계</h3>

- 하드 코딩된 상수를 생성자나 메서드 파라미터로 받기
    - 하드 코딩된 경로가 테스트 어려운 이유는 테스트 환경에 따라 경로를 다르게 줄 수 있는 수단이 없기 때문
    - 해당 상수를 교체할 수 있는 기능을 추가해서 해결해야 한다.
    - 또 다른 방법으로는 메서드를 실행할 때 인자로 전달
  
```java
// 상수를 교체할 수 있는 set메서드
private String filePath = "D:\data\pay\cp0001.csv";

public void setFilePath(String filePath){
    this.filePath = filePath;    
}

// 또는 메서드 인자로 전달
public void sync(String filePath) throws IOException{
    Path path = Paths.get(filePath);    
}
```
테스트 코드에서 데이터를 읽을 때 사용하는 파일은 소스 코드 리포지토리에 함께 등록해야 한다.<br>
메이븐 프로젝트를 사용하면 src/test/file 폴더나 src/test/resources폴더가 테스트 용도 파일을 저장하기에 적당한 위치이다.

- 의존 대상을 주입받기
    - 생성자나 세터를 주입 수단으로 이용하기
    - 생성자나 세터를 통해 의존 대상을 교체할 수 있게 되면 실제 구현 대신에 대역을 사용할 수 있다.
  
```java
public class PaySync {
  private PayInfoDao payInfoDao;

  public paySync(PayInfoDao payInfoDao) {
    this.payInfoDao = payInfoDao
  }
  ...
  
  //또는
  public void setPayInfoDao(PayInfoDao payInfoDao){
      this.payInfoDao = payInfoDao;
  }
}
```

- 테스트하고 싶은 코드를 분리하기
    - 내가 테스트하려고 하는 코드인 부분만 떼어내서 다시 메서드로 만들고 그 메서드를 테스트
```java
public class PointRule{
    
  public int calculate(Subscription s, Product p, LocalDate now){
    int point = 0;
    if(s.isFinished(now)){
      point += p.getDefaultPoint();
    }else{
      point += p.getDefaultPoint() + 10;
    }
    if(s.getGrade() == GOLD){
        point += 100;
    }
    return point;
  }
}

```

- 시간이나 임의 값 생성 기능 분리하기
    - 시간 구하는 기능을 분리하고, 분리한 대상을 주입할 수 있게 변경하여, 상황을 쉽게 제어하도록 변경
    - Test코드에서는 <code>given(mockTimes.today()).willReturn(LocalDate.of(2019,1,1))</code>식으로 사용
      

```java
import java.time.format.DateTimeFormatter;

//기존
public class DailyBatchLoader {
  public int load() {
    LocalDate date = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    ...

  }
}
// 변경 
public class DailyBatchLoader {
  private Times times = new Times();

  public void setTimes(Times times) {
    this.times = times;
  }

  public int load() {
    LocalDate date = times.today();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    ...

  }
}
```

- 외부 라이브러리는 직접 사용하지 말고 감싸서 사용
    - AuthUtil.authenticate() 메서드는 정적 메서드이기 때문에 대역으로 대체하기 어려움
    - 외부 라이브러리를 직접 사용하지 말고 외부 라이브러리와 연동하기 위한 타입을 따로 만들자.

```java
// 아래와 같이 분리
public class AuthService {
  private String authKey = "someKey";

  public int authenticate(String id, String pw) {
    boolean authorized = AuthUtil.authorize(authKey);
    if (authorized) {
      return AuthUtil.authenticate(id, pw);
    } else {
      return -1;
    }
  }
}

public class LoginService {
  private AuthService authService = new AuthService();

  //set으로 주입받을 수 있게 만들고
  public void setAuthService(AuthService authService) {
    this.authService = authService;
  }
  
  public LoginResult login(String id, String pw){
      int resp = authService.authenticate(id,pw); // 이렇게 사용 필요에 따라 대역을 만들어 테스트도 가능해짐 
      if(resp == -1) return LoginResult.badAuthKey();
      ...
  }
}

```




