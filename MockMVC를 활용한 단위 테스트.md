# MockMVC를 활용한 단위 테스트
참고링크 : http://clipsoft.co.kr/wp/blog/tddtest-driven-development-%EB%B0%A9%EB%B2%95%EB%A1%A0/<br>
참고링크 : https://velog.io/@woo00oo/SpringBoot-Test-2<br>
참고링크 : https://www.youtube.com/watch?v=SFVWo0Z5Ppo
## TDD(Test driven development)
<img src="https://github.com/JMsuper/springboot-lecture-kotlin/blob/main/img/TDD.JPG" width=600><br>
TDD는 테스트케이스를 작성 한 후 실제 코드를 개발하여 리펙토링하는 절차를 따른다.<br>
TDD는 작가가 책을 쓰는 과정과 유사하다. 책을 쓸 때는 목차를 처음 구성한다. 이후 각 목차에 맞는 내용을 구상하여<br>
초안을 작성하고 고쳐쓰기를 반복한다. 이 과정을 TDD에 비유하면 목차구성은 "테스트코드 작성", 초안작성은 "코드개발",<br>
고쳐쓰기는 "코드수정"에 해당한다. 반복적인 검토와 고쳐쓰기를 통해서 좋은 글이 완성되는 것처럼 소프트웨어도 반복적인<br>
테스트와 수정을 통해 고품질의 소프트웨어를 만들 수 있다.<br>
<br>
테스트는 단위테스트와 통합테스트로 나뉜다. 단위테스트는 하나의 모듈을 기준으로 독립적으로 진행되는 가장 작은단위의 테스트이며,<br>
통합테스트는 모듈을 통합하는 과정에서 모듈 간의 호환성을 확인하기 위해 수행되는 테스트이다. 통합테스트를 진행하기 위해서는<br>
다른 컴포넌트들과 연결해야하며, 크기가 커질수록 비용또한 커진다. 그래서 TDD에서는 단위테스트를 수행한다.<br>

## MockMVC를 활용한 단위 테스트
테스트를 위한 클래스는 실제 코드와 일치하는 디렉토리 구조를 갖도록 한다.<br>
예를 들어서 실제 코드가`/kotlin/com.example/mvc/controller/exception`에 있다면,<br>
테스트 코드 또한 `/test/kotlin/com.example/mvc/controller/exception`에 위치하도록 한다.

```
@WebMvcTest
@AutoConfigureMockMvc
class ExceptionApiControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun helloTest(){
```

테스트 클래스에는 `@WebMvcTest`,`@AutoConfigureMockMvc`,`@Autowired`,`@Test` 어노테이션을 사용한다.
###### `@WebMvcTest`
- ()에 작성된 클래스만 실제로 로드하여 테스트를 진행
- 매개변수를 지정해주지 않으면 `@Controller`,`@RestController`,`@RestControllerAdvice`등 컨트롤러와 연관된 Bean이 모두 로드됨
- 웹에서 테스트하기 힘든 컨트롤러를 테스트하는 데 적합하다
- 웹상에서의 요청과 응답에 대해 테스트 가능하다
- `@SpringBootTest` 어노테이션보다 가볍게 테스트할 수 있다
- 전체 자동 구성을 사용하지 않고, MVC 테스트와 관련된 구성만 적용한다.

`@AutoConfigureMockMvc`없이 `@WebMvcTest`만 어노테이트해도 mockMvc가 생성되어 테스트는 정상작동한다.<br>
mockMvc를 생성하는 것은 `@WebMvcTest`이다. `@AutoConfigureMockMvc`를 추가로 어노테이트하는 이유는,<br>
mockMvc에 대해 더 세세한 컨트롤을 할 수 있기 때문이다.<br>

###### `@AutoConfigureMockMvc`
- spring.test.mockmvc의 설정을 로드하면서 MockMvc의 의존성을 자동으로 주입한다.
- MockMvc 클래스는 REST API 테스트를 할 수 있는 클래스
###### `@Autowired`
- Controller의 API를 테스트하는 용도인 MockMvc 객체를 주입 받음
- perform() 메소드를 활용하여 컨트롤러의 동작을 확인할 수 있음
- andExpect(), andDo(), andReturn()등의 메소드를 같이 활용함
###### `@Test`
- 어노테이트된 메소드가 테스트 메소드임을 나타낸다
- 해당 메소드는 private, static이면 안되며, value를 리턴해서는 안된다.

```
    @Test
    fun helloTest(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/exception/hello")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andExpect(
            MockMvcResultMatchers.content().string("hello")
        ).andDo(MockMvcResultHandlers.print())
    }
```
perform()메소드는 테스트할 api를 지정한다. andExpect()는 기대하는 api의 결과를 확인하는 메소드이며, andDo()는 테스트가 종료된 후<br>
불려지는 메소드이다. andDo()에서 테스트 결과를 확인할 수 있다.

```
    @Test
    fun getTest(){
        val queryParams = LinkedMultiValueMap<String,String>()
        queryParams.add("name","steve")
        queryParams.add("age","20")

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/exception").queryParams(queryParams)
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andExpect(
            MockMvcResultMatchers.content().string("steve 20")
        ).andDo(
            MockMvcResultHandlers.print()
        )
    }
```
테스트에 파라미터값을 넣고 싶을 때는 `MultiValueMap<>` 데이터 셋을 만들어야 한다. queryParams()메소드는 해당 타입을 매개변수로 받는다. 
```
    @Test
    fun getFailTest(){
        val queryParams = LinkedMultiValueMap<String,String>()
        queryParams.add("name","steve")
        queryParams.add("age","9")

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/exception").queryParams(queryParams)
        ).andExpect(
            MockMvcResultMatchers.status().isBadRequest
        ).andExpect(
            MockMvcResultMatchers.content().contentType("application/json")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("\$.result_code").value("FAIL")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("\$.errors[0].field").value("age")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("\$.errors[0].value").value("9")
        ).andDo(
            MockMvcResultHandlers.print()
        )
    }
```
테스트 메소드는 성공할 경우뿐만 아니라, 오류가 발생하는 경우를 다루는 메소드도 정의해야 한다. 오류가 발생했을 때<br>
적절한 응답값을 리턴하는지 확인해야 되기 때문이다.
```
    @Test
    fun postTest(){

        val userRequest = UserRequest().apply {
            this.name = "steve"
            this.age = 10
            this.phoneNumber = "010-1242-1234"
            this.address = "sdf"
            this.email = "123@123"
            this.createdAt = "2020-12-12 12:12:12"
        }

        val json = jacksonObjectMapper().writeValueAsString(userRequest)
        println(json)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/exception")
                .content(json)
                .contentType("application/json")
                .accept("application/json")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andExpect(
            MockMvcResultMatchers.jsonPath("\$.name").value("steve")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("\$.email").value("123@123")
        ).andDo(MockMvcResultHandlers.print())
    }
```
body에 json을 담아 넘겨주고 싶을 때는 Jackson의 ObjectMapper가 가지고 있는 `writeValueAsString()`메소드를 사용한다.<br>
해당 메소드는 객체를 json형태로 변환한뒤 이를 String type으로 변환해준다.
