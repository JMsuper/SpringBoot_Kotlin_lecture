# PUT method
### 코틀린 apply 함수
참고자료 : https://medium.com/@limgyumin/%EC%BD%94%ED%8B%80%EB%A6%B0-%EC%9D%98-apply-with-let-also-run-%EC%9D%80-%EC%96%B8%EC%A0%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94%EA%B0%80-4a517292df29<br>
`fun <T> T.apply(block: T.() -> Unit: T{block(); return this}`<br>
EX) `val t: T = T().apply{this.foo()`<br>
수신 객체 람다 내부에서 수신 객체의 함수를 사용하지 않고 수신 객체 자신을 다시 반환하려는 경우에 apply를 사용한다.<br>
수식 객체의 property만을 사용하는 대표적인 경우가 객체의 초기화이며, 이곳에 apply를 사용한다.
#### apply 사용하는 경우
```
var person = Person().apply{
    name = "james"
    age = 18
}
```
#### apply 사용하지 않는 경우
```
val person = Person()
person.name = "james"
person.age = 18
```
apply는 수신 객체를 그대로 반환하기 때문에 chaining을 사용하여 연속해서 객체를 초기화 할 때 사용하면 유용하다.

#### PUT method 예제
apply 함수를 적용하여 Response객체 초기화
```
    @PutMapping(path=["/put-mapping/object"])
    fun putMappingObject(@RequestBody userRequest: UserRequest): UserResponse {

        // apply는 반환된 값이 아닌, 처음 자신의 값을 계속 넘겨준다.
        // 0. UserResponse
        return UserResponse().apply{

            // 1. result
            this.result = Result().apply{
                this.resultCode = "OK"
                this.resultMessage = "성공"
            }
        }.apply{
            // 2. decription
            this.description = "~~~~~~~~"
        }.apply {
            // 3. user mutable list
            val userList = mutableListOf<UserRequest>()
            userList.add(userRequest)
            userList.add(UserRequest().apply{
                this.name = "a"
                this.age = 10
                this.email = "a@gmail.com"
                this.address = "a address"
                this.phoneNumber = "01046248531"
            })
            userList.add(UserRequest().apply{
                this.name = "b"
                this.age = 20
                this.email = "b@gmail.com"
                this.address = "b address"
                this.phoneNumber = "01046248531"
            })
            this.userRequest = userList
        }
    }
```
UserResponse class
```
package com.example.mvc.model.http

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

data class UserResponse(
    var result:Result?=null,
    var description:String?=null,

    @JsonProperty("user")
    var userRequest: MutableList<UserRequest>?=null
)
// List<> : 읽기 전용 리스트
// MutableList<> : 수정할 수 있는 리스트

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class Result(
    var resultCode:String?=null,
    var resultMessage:String?=null
)
```
코틀린에서 var은 수정가능한 변수에 사용되고, val은 수정을 금지할 변수에 사용된다.<br>
다만, val의 경우 객체의 reference만 수정되지 않을 뿐, 주소가 가리키는 메모리의 값은 수정될 수 있다.

# DELETE method
DELETE method에서는 query param과 pathVariable을 사용한다.

