package com.example.todopractice.model.http

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.validation.Validation

class ToDoDtoTest {

    val validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun toDoDtoTest(){
        val toDoDto: ToDoDto = ToDoDto().apply {
            this.title = "sdf"
            this.description = "sdf"
            this.schedule = "2022-10-10 11:11:11"
        }

        val result = validator.validate(toDoDto)
        result.forEach {
            println(it.propertyPath.last().name)
            println(it.message)
            println(it.invalidValue)
        }
        // validator.validate()는 유효성 검사에서 오류가 발생했을 떄는 constraint violations를
        // 리턴턴하고, 오류가발생하지 않으면 empty set을 리턴한다.
        Assertions.assertEquals(true,result.isEmpty())
    }

}