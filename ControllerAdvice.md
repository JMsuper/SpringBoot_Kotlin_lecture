# ControllerAdvice
`@ControllerAdvice`는 여러 controller 클래스들에 공통으로 적용되는 ExceptionHandler, InitBinder, ModelAttribute 메소드들을
정의한다. 즉, Global하게 사용되는 특정 메소드들을 정의할 수 있다.
- `@ExceptionHandler` : `@RequestBody`를 사용하여 controller에서 object를 받을 때 Valid하지 않는다면 Exception을 발생시킨다.
  ExceptionHandler를 통해 해당 Exception을 catch하여 클라이언트에게 에러정보를 전달할 수 있다.
- `@InitBinder` : controller가 호출되었을 때 해당 메소드를 실행하기 전 수행해야할 메소드를 정의한다.
- `@ModelAttribute` : `@RequestBody`와 유사하게 클라이언트가 전송한 파라미터 혹은 json을 dto 클래스의 인스턴스로 변환시켜준다.
  이때, Requestbody와 다른 점은, Requestbody는 dto 클래스에 setter를 사용하지 않고 objectMapper를 사용하여 변환시키지만,
  ModelAttribute는 dto 클래스에 정의된 생성자나 setter를 통해 변환시킨다는 차이점이 있다.

```
@ControllerAdvice(basePackageClasses = [ToDoApiController::class])
class ToDoApiControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(e : MethodArgumentNotValidException, request: HttpServletRequest)
    : ResponseEntity<ErrorResponse>{
        val errors = mutableListOf<Error>()

        e.bindingResult.allErrors.forEach {
            val error = Error().apply {
                this.field = (it as FieldError).field
                this.field = it.defaultMessage
                this.value = it.rejectedValue
            }.apply {
                errors.add(this)
            }
        }

        val errorResponse = ErrorResponse().apply {
            this.resultCode = "FAIL"
            this.httpStatus = HttpStatus.BAD_REQUEST.value().toString()
            this.httpMethod = request.method
            this.message = ""
            this.path = request.requestURI
            this.timeStamp = LocalDateTime.now()
            this.errors = errors
        }
        return ResponseEntity.badRequest().body(errorResponse)
    }

    @InitBinder
    fun initBinder(webDataBinder: WebDataBinder){
        println("initBinder")
    }
}
```
`@ExceptionHandler(MethodArgumentNotValidException::class)`에서 해당 어노테이션을 받은 메소드는 MethodArgumentNotValidException이
발생했을 때 동작함을 의미한다.
또한, 해당 어노테이션을 적용한 메소드는 Exception, HttpServletRequest 등 다양한 파라미터를 주입받을 수 있다.
initBinder메소드는 해당 컨트롤러의 모든 메소드에서 실행되는 것이 아니라 param 혹은 path variable 혹은 requestbody를 받는 메소드에서만
실행된다. `@InitBinder("test")`와 같은 방식으로 특정 객체를 지정하여 해당하는 경우에만 실행되도록 설정할 수 있다.

## 참고자료
- InitBinder : https://goodgid.github.io/Spring-MVC-InitBinder/
- ModelAttribute : https://mangkyu.tistory.com/72
- Exceptionhandler : https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/ExceptionHandler.html
- 
- 
