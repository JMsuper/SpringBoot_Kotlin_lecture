# Swagger
Swagger는 API에 대한 문서를 자동으로 작성해주는 라이브러리를 말한다.<br>
또한 UI에서 직접 API를 테스트 해볼 수 있기 때문에 API 명세, 테스트등에 사용된다.<br>
swagger링크 : http://127.0.0.1:8080/swagger-ui.html (swagger를 스프링에 적용시키고 서버를 작동시키면 해당 url에서 api문서
확인가능)

### Swagger 적용
#### Dependency
Swagger를 적용하기 위해서 `springfox-swagger2`와 `springfox-swagger-ui`를 dependencies에 추가해줘야 한다.
```
dependencies{
    ...
    
    implementation("io.springfox:springfox-swagger-ui:2.9.2")
    implementation("io.springfox:springfox-swagger2:2.9.2")
    
    ...
}
```
주의할 점은 swagger와 springboot version을 맞춰줘야 한다는 것이다. 일치하지 않을 경우
`ApplicationContextException`을 마주할 수 있다. 필자의 경우 springboot는 `2.6.2`, swagger는 `2.9.2`를 사용했을 때
에러가 발생하였다. 이는 springboot 2.6버전 이후부터 설정 값이 변경되어 몇몇 라이브러리들과 출돌이 발생하는 것이다.


#### configuration
Swagger를 사용하기 위해서 `SwaggerConfig`파일을 생성해야 한다. Bean객체로 등록하여 스프링부트가 관리할 수 있도록 한다.
```
@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun docket(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
    }
}
```
- `@Configuration` : Beam객체를 springboot에 등록하기 위한 어노테이션. `@Bean`만으로도 Bean등록이 가능하지만, `@Configuration`을
  사용해야만 springboot가 해당 bean을 싱글톤으로 관리해준다.
- `@EnableSwagger2` : swagger2를 활성화시켜주는 어노테이션.
- Docket class : swagger2 설정의 핵심이 되는 Bean이다.
  - `DocumentationType.SWAGGER_2` : swagger2에 대한 메타데이터를 담은 객체를 의미한다.
  - `select()` : ApiSelectorBuilder의 인스턴스를 생성한다. 
  - `apis(RequestHandlerSelectors.any())` : api 스펙이 작성되어 있는 패키지를 지정한다. 이때 패키지는 controller가 존재하는 패키지이다.
  - `paths()` : api()로 선택되어진 API중 특정 path 조건에 맞는 API들을 다시 필터링하여 문서화한다.

#### controller
- `@Api(description="일정관리")` : controller의 이름
- `@ApiOperation(value ="일정확인", notes = "일정 확인 GET API")` : API의 이름과 API에 대한 설명
- `@ApiParam(name = "index test", example = "0")` : Api의 param에 이름과 샘플 데이터를 지정
- `@ApiResponse()` : operation의 가능한 response를 정의

#### DTO
```
    @field:ApiModelProperty(
        value = "DB INDEX",
        example = "1",
        required = false
    )
    var index: Int?=null,
```
dto에 대해서 어노테이션을 적용시켜 아래 사진과 같이 model을 확인할 수 있다.
<img src="https://github.com/JMsuper/springboot-lecture-kotlin/blob/main/img/swagger_model.JPG" width=600>





## 참고자료
- swagger : https://victorydntmd.tistory.com/341
