# Bean Validation 및 Custom Validation
참고자료 : https://meetup.toast.com/posts/223<br>
참고자료 : https://medium.com/@SlackBeck/javabean-validation%EA%B3%BC-hibernate-validator-%EA%B7%B8%EB%A6%AC%EA%B3%A0-spring-boot-3f31aee610f5<br>
참고자료 : https://www.hanumoka.net/2019/01/06/java-20190106-java-pojo-vs-bean/
애플리케이션에서 데이터 유효성 검사 로직은 다음과 같은 문제를 가지고 있다.
- 애플리케이션 전체에 분산되어 있다.
- 코드 중복이 심하다.
- 비즈니스 로직에 섞여있어 검사 로직 추적이 어렵고 애플리케이션이 복잡해진다.
<p align="center">
<img src="https://github.com/JMsuper/SpringBoot_Kotlin_lecture/blob/main/img/validation%ED%86%B5%ED%95%A9%EC%A0%84.JPG" width=800>
출처 : Hibernate Validator 5.4.3.Final - JSR 349 Reference Implementation: Reference Guide
<p><br>
  
이를 해결하기 위해 Java에서는 Bean Validation이라는 데이터 유효성 검사 프레임워크를 제공하고 있다.<br>
Bean Validation은 위에서 말한 문제들을 해결하기 위해 다양한 제약(Contraint)을 도메인 모델(Domain Model)에<br>
어노테이션(Annotation)으로 정의할 수 있게 한다. 이 제약을 유효성 검사가 필요한 객체에 직접 정의하는 방법으로<br>
기존 유효성 검사 로직의 문제를 해결한다.
<p align="center">
<img src="https://github.com/JMsuper/SpringBoot_Kotlin_lecture/blob/main/img/validation%ED%86%B5%ED%95%A9%ED%9B%84.JPG" width=800>
출처 : Hibernate Validator 5.4.3.Final - JSR 349 Reference Implementation: Reference Guide
<p><br>
이때 Bean Validation은 "명세"이며, 실제 구현체는 Hibernate Validation이다.
  
#### Java Bean이란?
Java Bean은 데이터를 표현하기 위한 Java클래스를 만들때의 규약이다.
- 모든 클래스의 프로퍼티는 private이며 getter, setter 메소드로 제어한다.
- 인자가 없는 public 생성자가 있어야 한다.
- Serializable 인터페이스를 구현해야 한다.
  
### Bean Validation 사용법
#### 파라미터에 적용
```
@RestController
@RequestMapping("/api")
@Validated
class DeleteApiController {
    @DeleteMapping(path=["/delete-mapping"])
    fun deleteMapping(
        // String에 대해서 비어있는지 확인하려면, NotNull이 아닌 NotEmpty로 확인해야한다.
        @NotEmpty
        @RequestParam(value="name") _name : String?,

        @NotNull(message = "age값이 누락되었습니다.")
        @Min(value=20, message = "age는20보다 커야 합니다.")
        @RequestParam(value="age") _age : Int
    ):String{
        println(_name)
        println(_age)
        return _name+" "+_age
    }
}
```
기본적으로 Bean Validation은 자바 Bean의 유효성 검사를 위한 것이다. 따라서 자바 Bean이 아닌 type에 대해<br>
유효성 검사를 진행하려면 추가적으로 해당 메소드가 있는 클래스에 `@Validated` 어노테이션을 적용시켜야 한다.<br>
그리고 파라미터에 `@NotEmpty`, `@Min`과 같은 어노테이션을 적용시키면 된다. 어노테이션에서 설정값을 지정해줄 수<br>
있으며, 유효성 에러 발생시 나타날 메시지를 지정할 수 있다.

#### data class에 적용
```
data class UserRequest(

    @field:Size(min = 2, max = 8)
    @field: NotNull
    var name:String?=null,

    @field: PositiveOrZero // 0 < 숫자를 검증, 0도 포함(양수)
    var age:Int?=null,
    @field: Email // email 양식
    var email:String?=null,
    @field: NotBlank // 공백을 검증
    var address:String?=null,

    @field: Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}\$") // 정규식 검증
    var phoneNumber:String?=null, // phone_number

    var createdAt:String?=null // yyyy-MM-dd HH:mm:ss   ex) 2020-10-02 13:00:00
){
    @AssertTrue(message = "생성일자의 패턴은 yyyy-MM-dd HH:mm:ss 여야 합니다.")
    private fun isValidCreatedAt():Boolean{ // 정상 true, 비정상 false
        return try{
            LocalDateTime.parse(this.createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            true
        }catch (e:Exception){
            false
        }
    }
}
```
data class에 Validation을 적용시킬 때는 `@field` 어노테이션을 적용시킨뒤 `@field: NotNull`과 같은 형식으로 작성해야 한다.
                                
  
  
  
  
  
  

