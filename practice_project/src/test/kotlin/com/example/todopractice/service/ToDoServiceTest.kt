package com.example.todopractice.service

import com.example.todopractice.config.AppConfig
import com.example.todopractice.model.http.ToDoDto
import com.example.todopractice.repository.ToDoRepositoryImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ToDoService::class,ToDoRepositoryImpl::class,AppConfig::class])
class ToDoServiceTest {

    @Autowired
    lateinit var toDoService: ToDoService

    @Test
    fun createTest(){
        var toDoDto = ToDoDto().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = "2022-11-11 11:11:11"
        }
        var result = toDoService.create(toDoDto)
        Assertions.assertEquals(toDoDto.javaClass,result?.javaClass)
        Assertions.assertEquals(toDoDto.title,result?.title)
    }

    @Test
    fun readTest(){
        var toDoDto = ToDoDto().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = "2022-11-11 11:11:11"
        }
        toDoService.create(toDoDto)
        var result = toDoService.read(2)
        Assertions.assertEquals(toDoDto.javaClass,result?.javaClass)
//        잘못된 index를 넣었을 때의 테스트
//        Assertions.assertNull(result)
    }

    @Test
    fun readAllTest(){
        var toDoDto = ToDoDto().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = "2022-11-11 11:11:11"
        }
        toDoService.create(toDoDto)
        toDoService.create(toDoDto)

        var result = toDoService.readAll()
        result.forEach {
            Assertions.assertEquals(toDoDto.javaClass,it.javaClass)
        }
    }

    @Test
    fun updateTest(){
        var toDoDto = ToDoDto().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = "2022-11-11 11:11:11"
        }
        toDoService.create(toDoDto)

        var newToDoDto = ToDoDto().apply {
            this.title = "new test"
            this.description = "new test"
            this.schedule = "2022-11-11 11:11:11"
        }

        var result = toDoService.update(newToDoDto)
        Assertions.assertEquals(newToDoDto.javaClass,result?.javaClass)
    }
}