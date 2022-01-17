package com.example.todopractice.model.http

import com.example.todopractice.annotation.StringFormatDateTime
import com.example.todopractice.database.ToDo
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.validation.constraints.NotBlank


data class ToDoDto(
    @field:ApiModelProperty(
        value = "DB INDEX",
        example = "1",
        required = false
    )
    var index: Int?=null,
    @field:ApiModelProperty(
        value = "일정명",
        example = "일정관리",
        required = true
    )
    @field:NotBlank
    var title: String?=null,
    @field:ApiModelProperty(
        value = "일정설명",
        example = "13시 ~~",
        required = false
    )
    var description: String?=null,
    @field:ApiModelProperty(
        value = "시간",
        example = "2020-11-11 11:11:11",
        required = true
    )
    @field:NotBlank
    @field:StringFormatDateTime
    var schedule: String?=null,
    @field:ApiModelProperty(
        value = "생성일자",
        required = false
    )
    var createdAt : LocalDateTime?=null,
    @field:ApiModelProperty(
        value = "수정일자",
        required = false
    )
    var updatedAt : LocalDateTime?=null
)

fun ToDoDto.convertToDoDto(toDo: ToDo):ToDoDto{
    return ToDoDto().apply {
        this.index = toDo.index
        this.title = toDo.title
        this.description = toDo.description
        this.schedule = toDo.schedule?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        this.createdAt = toDo.createdAt
        this.updatedAt = toDo.updatedAt
    }
}

