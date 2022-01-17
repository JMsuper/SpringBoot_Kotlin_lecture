package com.example.todopractice.controller.api.todo

import com.example.todopractice.database.ToDo
import com.example.todopractice.database.ToDoDataBase
import com.example.todopractice.model.http.ToDoDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

//https://okky.kr/article/1061021
//WebMvcTest는 빠르게 테스트 하기위해 repository에 관련된 bean은 가져오지 않는다.
//따라서, WebMvcTest가 아닌 SpringBootTest를 사용해야 한다.
//SpringBootTest는 모든 bean을 가져오기 때문에 통합 테스트를 진행할 때 사용된다.

@SpringBootTest
@AutoConfigureMockMvc
class ToDoApiControllerTest {

    @Autowired
    lateinit var mockMvc:MockMvc

    @Autowired
    lateinit var toDoDataBase: ToDoDataBase

    @BeforeEach
    fun setup(){
        toDoDataBase.init()
    }

    @Test
    fun createTest(){
        var toDoDto = ToDoDto().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = "2022-11-11 11:11:11"
        }
        var objectMapper:ObjectMapper = jacksonObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        val json = objectMapper.writeValueAsString(toDoDto)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/todo")
                .content(json)
                .contentType("application/json")
                .accept("application/json")
        ).andExpect(
            MockMvcResultMatchers.status().`is`(201)
        ).andExpect(
            MockMvcResultMatchers.content().contentType("application/json")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("\$.title").value("test")
        ).andDo(
            MockMvcResultHandlers.print()
        )
    }

    @Test
    fun readTest(){
        var toDoDto = ToDoDto().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = "2022-11-11 11:11:11"
        }
        var objectMapper:ObjectMapper = jacksonObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        val json = objectMapper.writeValueAsString(toDoDto)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/todo")
                .content(json)
                .contentType("application/json")
                .accept("application/json"))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/todo")
                .param("index","1")
        ).andExpect(
            MockMvcResultMatchers.status().`is`(200)
        ).andExpect(
            MockMvcResultMatchers.content().contentType("application/json")
        ).andDo(
            MockMvcResultHandlers.print()
        )
    }

    @Test
    fun readNotFoundTest(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/todo")
                .param("index","1")
        ).andExpect(
            MockMvcResultMatchers.status().`is`(200)
        ).andExpect(
            MockMvcResultMatchers.content().string("")
        ).andDo(
            MockMvcResultHandlers.print()
        )
    }

    @Test
    fun readRedirectTest(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/todo")
        ).andExpect(
            MockMvcResultMatchers.status().`is`(301)
        ).andDo(
            MockMvcResultHandlers.print()
        )
    }

    @Test
    fun readAllTest(){
        var toDoDto = ToDoDto().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = "2022-11-11 11:11:11"
        }
        var objectMapper:ObjectMapper = jacksonObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        val json = objectMapper.writeValueAsString(toDoDto)
        for(i:Int in 1..3){
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/todo")
                    .content(json)
                    .contentType("application/json")
                    .accept("application/json"))
        }

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/todo")
                .param("index","1")
        ).andExpect(
            MockMvcResultMatchers.status().`is`(200)
        ).andExpect(
            MockMvcResultMatchers.content().contentType("application/json")
        ).andDo(
            MockMvcResultHandlers.print()
        )
    }

    // 정상적으로 update된 경우
    @Test
    fun updateTest(){
        var toDoDto = ToDoDto().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = "2022-11-11 11:11:11"
        }

        var objectMapper:ObjectMapper = jacksonObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        val json = objectMapper.writeValueAsString(toDoDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/todo")
                .content(json)
                .contentType("application/json")
                .accept("application/json")
        )

        var newToDoDto = ToDoDto().apply {
            this.index = 1
            this.title = "new test"
            this.description = "new test"
            this.schedule = "2022-11-11 11:11:11"
        }

        val newJson = objectMapper.writeValueAsString(newToDoDto)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/todo")
                .content(newJson)
                .contentType("application/json")
                .accept("application/json")
        ).andExpect(
            MockMvcResultMatchers.jsonPath("\$.index").value(1)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("\$.title").value("new test")
        ).andExpect(
            MockMvcResultMatchers.status().`is`(200)
        ).andDo(
            MockMvcResultHandlers.print()
        )
    }

    @Test
    fun updateWrongIndexTest(){
        var toDoDto = ToDoDto().apply {
            this.index = 2
            this.title = "new test"
            this.description = "new test"
            this.schedule = "2022-11-11 11:11:11"
        }

        var objectMapper:ObjectMapper = jacksonObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        val json = objectMapper.writeValueAsString(toDoDto)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/todo")
                .content(json)
                .contentType("application/json")
                .accept("application/json")
        ).andExpect(
            MockMvcResultMatchers.status().`is`(200)
        ).andDo(
            MockMvcResultHandlers.print()
        )
    }

    @Test
    fun updateValidationErrorTest(){
        var toDoDto = ToDoDto().apply {
            this.index = 1
            this.title = "new test"
            this.description = "new test"
            this.schedule = "202-11-11 11:11:11"
        }

        var objectMapper:ObjectMapper = jacksonObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        val json = objectMapper.writeValueAsString(toDoDto)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/todo")
                .content(json)
                .contentType("application/json")
                .accept("application/json")
        ).andExpect(
            MockMvcResultMatchers.status().`is`(400)
        ).andDo(
            MockMvcResultHandlers.print()
        )
    }

    @Test
    fun deleteTest(){
        var toDoDto = ToDoDto().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = "2022-11-11 11:11:11"
        }

        var objectMapper:ObjectMapper = jacksonObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        val json = objectMapper.writeValueAsString(toDoDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/todo")
                .content(json)
                .contentType("application/json")
                .accept("application/json")
        )

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/todo/1")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andDo(
            MockMvcResultHandlers.print()
        )
    }

    @Test
    fun deleteErrorTest(){
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/todo/1")
        ).andExpect(
            MockMvcResultMatchers.status().`is`(500)
        ).andDo(
            MockMvcResultHandlers.print()
        )
    }
}







