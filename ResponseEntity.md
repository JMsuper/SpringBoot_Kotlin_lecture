# ResponseEntity
#### 참고자료
- let, run : https://medium.com/@limgyumin/%EC%BD%94%ED%8B%80%EB%A6%B0-%EC%9D%98-apply-with-let-also-run-%EC%9D%80-%EC%96%B8%EC%A0%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94%EA%B0%80-4a517292df29<br>
- https://blog.yena.io/studynote/2020/04/15/Kotlin-Scope-Functions.html
- https://kotlinlang.org/docs/scope-functions.html#let
- 코틀린 람다 표현식 : https://tourspace.tistory.com/110
- Elvis operator : https://tourspace.tistory.com/114
### let
let의 사용 규칙은 다음과 같다.
- 지정된 값이 null이 아닌 경우에 코드를 실행해야 하는 경우
- Nullable 객체를 다른 Nullable 객체로 변환하는 경우
- 단일 지역 변수의 범위를 제한하는 경우
```
fun main(args:Array<String>) {
    val person = Person()
    val resultIt = person.let {
        it.name = "name"
        it.age = 10
        it
    }
    println(resultIt.name + resultIt.age)
}

class Person(){
    var name:String = ""
    var age:Int = 0
    constructor(_name:String, _age:Int) : this() {
        this.name = _name
        this.age = _age
    }
}
```
let함수는 {} 마지막 라인에 있는 값을 리턴한다. 만약 해당 부분이 존재하지 않으면 반환값이 없는 것이다.

### run
run의 사용 규칙은 다음과 같다.
- 어떤 값을 계산할 필요가 있거나 여러개의 지역 변수의 범위를 제한할 경우
- run is useful when your lambda contains both the object initialization and the computation of the return value.
- 매개 변수로 전달된 명시적 수신 객체를 암시적 수신 객체로 변환할 경우
```
val service = MultiportService("https://example.kotlinlang.org", 80)

val result = service.run {
    port = 8080
    query(prepareRequest() + " to port $port")
}

// the same code written with let() function:
val letResult = service.let {
    it.port = 8080
    it.query(it.prepareRequest() + " to port ${it.port}")
}
```

### 코틀린 람다 표현식(Lambda Expression)
EX)
```
fun main(args: Array){
    val sum = {x: Int, y: Int -> println("Computing the sum of $x and $y...")
        x + y
    }
    println(sum(1,2))
}
```
- 중괄호로 감싼다. {...}
- 매개변수와 본문은 "->"로 구분한다.
- 매개변수는 ()로 감싸지 않는다.
- 매개변수의 타입을 생략할 수 있다.
- 변수에 람다식을 담는 경우에는 인자의 타입을 생략할 수 없다.

###  Elvis Operator
- ?. : 좌항이 Not null이면 우항의 코드를 리턴한다.
- ?: : 좌항이 null이면 default로 우항의 코드를 리턴한다.
```
fun main(args:Array<String>){
    var s :String?=null
    s?.let{
        println("s is not null")
    }?: run {
        println("s is null")
    }
}
// "s is null"
```

## ResponseEntity
ResponseEntity 클래스는 HttpEntity 클래스를 상속하는 클래스로, Http 응답을 수행할 때 클라이언트에게 전달되는<br>
객체이다. ResponseEntity를 통해 HttpStatus, HttpHeaders, HttpBody를 작성할 수 있다.<br>
<br>
아래 코드는 age에 대한 null check를 진행한 후, not null일 경우 let{}을 수행하고,<br>
null 일 경우 kotlin.run{}을 수행하는 코드이다.
```
    @GetMapping("")
    fun getMapping(@RequestParam(required = false) age : Int?): ResponseEntity<String> {
        return age?.let{
            if(it < 20){
                return ResponseEntity.status(400).body("age 값은 20보다 커야 합니다.")
            }
            ResponseEntity.ok("OK")
        }?: kotlin.run{
            ResponseEntity.status(400).body("age값이 누락되었습니다.")
        }
    }
```

- ResponseEntity.status() : status()는 BodyBuilder의 인스턴스를 리턴한다. BodyBuilder클래스는 HttpBody를 작성하도록<br>
  하는 클래스이며, body() 메소드를 가지고 있다. body()메소드는 ResponseEntity를 리턴하며 최종적으로 Http 응답 객체가<br>
  완성된다.
- body() : body의 매개변수는 `@Nullable T body`이다. 따라서 객체가 들어갈 수도 있다. 해당 객체는 objectMapper에 의해<br>
  클라이언트에게 전송시 object type으로 변환된다.


## HTML파일을 리턴하는 방법
Http 요청에 대해 HTML파일을 응답하려면 `@Controller`어노테이션을 사용해야 한다.<br>
이 어노테이션은 특정한 HTML페이지를 응답할 때 사용하는 Controller이다. 매핑되있는 함수에서 HTML파일명을 return하면,<br>
resources폴더 하위에 static폴더에서 해당 파일을 찾게된다.<br>
그런데 만약, 매핑된 메소드에 `@ResponseBody`어노테이션을 추가하면 HTML파일이 아닌 return value 자체를 응답하게 된다.
```
@Controller
class PageController {
    // http://localhost:8080/main
    @GetMapping("/main")
    fun main(): String {
        return "main.html"
    }

    @ResponseBody
    @GetMapping("/test")
    fun reponse(): UserRequest {
        return "main.html"
    }
}
```
