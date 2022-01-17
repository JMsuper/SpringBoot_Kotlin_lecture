package com.example.todopractice.service

import com.example.todopractice.database.ToDo
import com.example.todopractice.database.convertTodo
import com.example.todopractice.model.http.ToDoDto
import com.example.todopractice.model.http.convertToDoDto
import com.example.todopractice.repository.ToDoRepositoryImpl
import org.springframework.stereotype.Service

@Service
class ToDoService(
    val toDoRepositoryImpl: ToDoRepositoryImpl
){
    // C
    fun create(toDoDto: ToDoDto):ToDoDto?{
        return toDoDto.let {
            ToDo().convertTodo(it)
        }.let {
            toDoRepositoryImpl.save(it)
        }?.let {
            ToDoDto().convertToDoDto(it)
        }
    }
    // R
    fun read(index:Int):ToDoDto?{
        return toDoRepositoryImpl.findOne(index)?.let {
            ToDoDto().convertToDoDto(it)
        }
    }

    fun readAll(): MutableList<ToDoDto>{
        return toDoRepositoryImpl.findAll().map {
            ToDoDto().convertToDoDto(it)
        }.toMutableList()
    }
    // U
    fun update(toDoDto: ToDoDto):ToDoDto?{
        return toDoDto.let {
            ToDo().convertTodo(it)
        }.let {
            toDoRepositoryImpl.save(it)
        }?.let {
            ToDoDto().convertToDoDto(it)
        }
    }

    // D
    fun delete(index: Int):Boolean{
        return toDoRepositoryImpl.delete(index)
    }
}