# Controller Advice 및 ExceptionHandler
## Controller Advice
Controller Advice는 controller에 대해 전역적으로 exception을 처리하는 클래스를 말한다.<br>
`@RestControllerAdvice`어노테이션을 적용해야 exception발생시 해당 클래스가 동작한다.<br>
exception을 처리하는 메소드에는 `@ExceptionHandler`어노테이션을 적용한다.<br>
```
@RestControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler(value = [RuntimeException::class])
    fun exception(e : RuntimeException): String{
        return "Server Error"
    }

    @ExceptionHandler(value = [IndexOutOfBoundsException::class])
    fun indexOutOfBoundsException(e : IndexOutOfBoundsException): ResponseEntity<String>{
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Index Error")
    }
}
```
`@ExceptionHandler`의 파라미터로 처리할 Exception class가 들어간다. exception handler에서는 에러처리에 대한<br>
메시지를 클라이언트에게 전송할 수 있다.

## ExceptionHandler
```
    @ExceptionHandler(value=[ConstraintViolationException::class])
    fun constraintViolationException(e : ConstraintViolationException,
    request : HttpServletRequest):ResponseEntity<ErrorResponse>{
        val errors = mutableListOf<Error>()

        e.constraintViolations.forEach{
            val error = Error().apply {
                this.field = it.propertyPath.last().name
                this.message = it.message
                this.value = it.invalidValue
            }
            errors.add(error)
        }
        val errorResponse = ErrorResponse().apply{
            this.resultCode = "FAIL"
            this.httpStatus = HttpStatus.BAD_REQUEST.value().toString()
            this.httpMethod = request.method
            this.message = "요청에 에러가 발생하였습니다."
            this.path = request.requestURI.toString()
            this.timestamp = LocalDateTime.now()
            this.errors = errors
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }
```
