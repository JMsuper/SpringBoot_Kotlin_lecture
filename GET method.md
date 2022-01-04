# GET method
### 참고자료
@RestController : https://dncjf64.tistory.com/288<br>
@ResponseBody : https://cheershennah.tistory.com/179
@RequestMapping : https://mungto.tistory.com/436
data class : https://codechacha.com/ko/data-classes-in-kotlin/
### 어노테이션
- @RestController : 해당 클래스가 REST하게 동작하도록 한다.<br>
RestController는 @Controller에 @ResponseBody를 더한 것이다. @Controller는 Spring MVC에서 View에 표시될 데이터가 있는<br>
Model객체를 만들고 올바른 뷰를 선택하는 일을 담당한다. @ResponseBody는 자바 객체를 HTTP Response Body에 변환하여 담는다.<br>
RestController는 이러한 두가지 기능을 함께 수행하도록 한다. Spring에서 RESTful 웹 서비스를 보다 쉽게 개발할 수 있도록<br>
Spring 4.0에서 추가되었다.<br><br>
- @RequestMapping
서버에 uri요청이 들어왔을 때, 해당 요청을 특정 메소드에 매핑하기 위해 사용한다.<br>
`@RequestMapping("/api")` or `@ReauestMapping(value = "/api", method = RequestMethod.GET)`<br><br>
- @GetMapping<br>
Requestmapping을 사용하여 메소드를 일일이 지정하는 것은 귀찮을 일이다. GetMapping을 통해 GET method만 처리하도록<br>
지정할 수 있다. 즉, `RequestMapping(value="****", method="RequestMethod.GET")`과 동일하다.<br><br>
- @PathVariable
`/api/user/123`과 같이 "/"를 사용하여 url에 변수를 담아 전달하고 싶은 경우 사용한다.
```
@GetMapping("/user/{id}")
fun pathVariable(@PathVariable id:Int):String{
  return id
}
```
이때 "{}"에 들어가는 변수명은 @PathVariable에 해당하는 변수명과 동일해야 한다. 만약 일치하지 않는다면,<br>
`@PathVariable(value = "****") id:Int`와 같은 방식으로 매핑할 수 있다.<br>
- @RequestParam
`api/page?id=123`과 같은 쿼리 파라미터를 사용할 때 사용한다.
```
fun queryParam(@RequestParam id:Int){
  return id
}
```
@PathVariable과 마찬가지로 변수명이 일치하지 않을 때 value 속성을 사용하면 된다.<br>

#### Object, Map
@RestController 어노테이션을 적용해두면, return의 type이 object면 json type으로 리턴된다.<br>
"key"는 변수명으로 지정된다. objectMapper라는 아이를 통해서 자동으로 바뀌게 된다.<br>
만약, 많은 쿼리 파라미터를 다룰 경우 Object를 사용하면 한번에 처리할 수 있다.<br>
이를 위해 원하는 변수들을 담은 `data class`를 정의해야 한다.
```
data class UserRequest(
    var name:String?=null,
    var age:Int?=null,
    var email:String?=null,
    var address:String?=null
)
```
data class란 코틀린에서 제공하는 클래스로 데이터 보관 목적으로 만든 클래스이다.<br>
프로퍼티에 대한 toString(), hashCode(), equals(), copy() 메소드를 자동 생성해준다.
```
fun queryParamObject(userRequest: UserRequest): UserRequest{
    return userRequest
}
```

그런데, 만약 쿼리 파라미터에 "-" 하이픈이 있는 경우에는 Object 객체형태로 받을 수가 없다.<br>
왜냐하면 코틀린에서 변수명에 "-" 하이픈을 넣을 수 없기 때문이다.<br>
이 경우, @RequestParam을 사용하거나 `Map<String,Any>`를 사용하여 해결할 수 있다.
```
fun queryParamMap(@RequestParam map: Map<String, Any>): Map<String, Any>{
    var phoneNumber = map.get("phone-number")
    return map
}
```

