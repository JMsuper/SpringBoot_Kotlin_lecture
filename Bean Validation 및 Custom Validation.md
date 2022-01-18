# Bean Validation 및 Custom Validation
참고자료 : https://meetup.toast.com/posts/223<br>
참고자료 : https://medium.com/@SlackBeck/javabean-validation%EA%B3%BC-hibernate-validator-%EA%B7%B8%EB%A6%AC%EA%B3%A0-spring-boot-3f31aee610f5<br>
참고자료 : https://www.hanumoka.net/2019/01/06/java-20190106-java-pojo-vs-bean/<br><br>
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
data class에 Validation을 적용시킬 때는 `@field` 어노테이션을 적용시킨뒤 `@field: NotNull`과 같은 형식으로 작성해야 한다.<br>
왜냐하면, `()`안에 있는 프로퍼티들에 어노테이션을 적용시키는 것은 객체 생성자에 적용되기 때문이다.<br>
코틀린 코드를 바이트 코드로 변환시켜 위 내용들을 확인할 수 있다.<br><br>

##### data class 자바 코드로 변환
코틀린 코드
```
data class TestRequest(

    @Size(min = 2, max = 8)
    @NotNull
    var name:String?=null,
)                                     
```  
자바 코드  
```
   public TestRequest(@Size(min = 2,max = 8) @NotNull @Nullable String name) {
      this.name = name;
   }                                     
```  
코틀린 코드에서 `@field`를 적용시키지 않았을 때 자바코드에서 필드에 Validation이 적용되지 않고 생성자에 적용되는 것을 볼 수 있다.<br>
자바에서 필드에 Validation을 적용하려면, 생성자가 아닌
```
    @NotBlank
    private String name;                                     
```  
와 같이 필드선언위에 어노테이션을 적용해야 한다. 왜냐하면 코틀린은 `Annotation use-site target`을 지정하지 않은 경우 어노테이션의 `@Target`의 가능여부를 조사하여 우선순위가 높은 `@Target`에 적용되기 때문이다. 위와 같은 경우는 `Annotation use-site target`를
지정하지 않아, 우선순위가 가장 높은 `constructor param`에 어노테이션이 적용되게 된다.

코틀린 코드                                     
```   
data class TestRequest(
    @field: Size(min = 2, max = 8)
    @field: NotNull
    var name:String?=null,
)                                     
```                                     
자바 코드                                     
```   
@Size(
      min = 2,
      max = 8
   )
   @NotNull
   @Nullable
   private String name;                                   
```                                          
코틀린에 `@field`어노테이션을 적용했을 때 자바에서 생성자가 아닌 필드에 Validation이 적용되는 것을 확인할 수 있다.<br>
                                     
#### validation을 직접 수행하기                                     
Bean Validation에 있는 유효성 검사가 아닌, 사용자 임의의 유효성 검사를 진행하려면 다음과 같이 작성해야 한다.
```  
data class UserRequest(
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
위 코드는 `createdAt`이라는 프로퍼티가 생성일자 패턴과 일치하는지 유효성 검사하는 코드이다.<br>
이때, `@AssertTrue`는 객체가 생성될 때 매핑된 메소드를 통해 유효성 검사를 수행하도록 하는 어노테이션이다.<br>
`True`이므로 메소드가 `true`를 리턴하면 유효성 검사를 통과한 것이고 `false`를 리턴하면 실패한 것이다.<br>
`@AssertTrue` 이외에 `@AssertFalse`도 존재한다.<br><br>                                     
                                     

#### 유효성 검사 실패시 서버 Exception이 아닌 클라이언트에게 4xx와 에러메시지를 전달하기           
```
@RestController
@RequestMapping("/api")
class PutApiController{

    @PutMapping(path=["/put-mapping/object"])
    fun putMappingObject(@Valid @RequestBody userRequest: UserRequest,bindingResult: BindingResult): ResponseEntity<String> {
        if(bindingResult.hasErrors()){
            val msg = StringBuilder()
            bindingResult.allErrors.forEach{
                val field = it as FieldError
                val message = it.defaultMessage
                msg.append(field.field+" : "+message+"\n")
            }
            return ResponseEntity.badRequest().body(msg.toString())
        }

        return ResponseEntity.ok("OK")
    }
}                                     
```                                  
`@Valid`는 프로퍼티, 메소드 파라미터, 메소드 리턴값에 대해서 유효성 검사를 진행하라는 어노테이션이다.<br>
위 코드에서 `@Valid`를 붙임으로서 UserRequest에 있는 Validation이 진행된다.<br>
만약, 위 메소드에서 BindingResult를 매개변수로 받지 않는다면, 서버 Exception이 발생할 것이며, 클라이언트는 5xx 응답을 받는다.<br>
BindingResult를 매개변수로 받으면 유효성 검사 실패시 Exception을 발생시키지 않고, 메소드 바디에서 유효성 검사 결과를<br>
확인할 수 있다. BindingResult의 `hasError()` 메소드를 사용하여 검사 결과를 확인하고 그에 맞는 `ResponseEntity`를 리턴한다.<br><br>
                                     
                                     
### Custom Validation 작성법
`@AssertTrue`어노테이션을 사용하여 유효성 검사 메소드를 만들 수 있지만, 반복적으로 사용되는 경우<br>
Custom Validation을 만드는 것이 더 유용하다. <br>
Custom Validation을 작성하기 위해 어노테이션 클래스와 Validator 클래스가 필요하다.<br>
<br>
Annotation 클래스
```
@Constraint(validatedBy = [StringFormatDateTimeValidator::class])
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class StringFormatDateTime(
    val pattern: String = "yyyy-MM-dd HH:mm:ss",
    val message: String = "시간 형식이 유효하지 않습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
```
`@Constriant` : 해당 어노테이션과 Validator를 매핑한다.<br>
`@Target` : 어노테이션의 타겟팅 가능한 영역을 명시한다.<br>
`@Retension` : 어노테이션이 적용되는 시점 혹은 Scope를 명시한다.<br>
`@MustBeDocumented` : Generated Documentation에 해당 Annotation도 포함될 수 있는지를 나타낸다.
                                     
```
// ConstraintValidator<어노테이션, 입력받는 데이터 타입>
class StringFormatDateTimeValidator : ConstraintValidator<StringFormatDateTime,String>{
    private var pattern : String?=null
    override fun initialize(constraintAnnotation: StringFormatDateTime?) {
        this.pattern = constraintAnnotation?.pattern
    }
    // 정상이면 True, 비정상이면 False
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return try{
            // 파싱해서 에러가 발생하면 Exception이 발생할 것이다.
            LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern))
            true
        }catch (e: Exception){
            false
        }
    }
}  
```                                     
`isValid()`메소드는 반드시 override해야하는 메소드이다. 해당 메소드에서 유효성 검사를 진행하는 것이다.                                     
