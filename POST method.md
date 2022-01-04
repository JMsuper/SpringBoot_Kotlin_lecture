# POST method
- @PostMapping
POST method에 함수를 매핑한다.
- @RequestBody
POST method에서는 쿼리 파라미터를 사용하지 않고, HTTP Request body를 사용한다.<br>
따라서, `@RequestParam`이 아닌 `@RequestBody`를 사용한다.
- @JsonProperty
json은 snake case인데, 코틀린은 camel case를 사용하는 경우가 있다. 이 경우, snake case로 표현된<br>
변수명은 코틀린에서 인식되지 않는다. 이를 처리하기 위해 변수위에 `@JsonProperty`를 사용한다.
```
@JsonProperty("phone_number")
var phoneNumber:String?=null
```
objectMapper는 위 어노테이션을 활용하여 json -> object, object -> json에서<br>
serialize와 deserialize를 진행한다. request에 담긴 변수명은 어노테이션에 적힌 변수명으로 작성된다.
- @JsonNaming
만약, body에 담긴 key가 수십개라면 JsonProperty는 일일이 각 변수마다 작성해줘야 한다.<br>
이러한 수고스러움을 덜기 위해 클래스위에 @JsonNaming을 사용한다.
```
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class UserRequest(
    var name:String?=null,
    var age:Int?=null,
    var email:String?=null,
    var address:String?=null,
    var phoneNumber:String?=null // phone_number
)
```
- 코틀린 더블콜론(::)
더블콜론(::)은 함수 혹은 클래스를 parameter로 전달할 때 사용한다.
```
fun testFunc():String{
    return "testFunc"
}

fun wrapperFunc(hanlder ()->String){
    var a = handler()
    println(a)
}

fun main(args* Array<String>){
    wrapperFunc(testFunc)
}
```
