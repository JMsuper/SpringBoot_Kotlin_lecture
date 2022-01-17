package com.example.todopractice.repository

import com.example.todopractice.database.ToDo
import com.example.todopractice.database.ToDoDataBase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.lang.Exception
import java.time.LocalDateTime

@Repository
class ToDoRepositoryImpl: ToDoRepository  {

    @Autowired
    lateinit var toDoDataBase: ToDoDataBase

    override fun save(toDo: ToDo): ToDo? {
        // index O => update
        return toDo.index?.let {
            return findOne(it)?.apply {
                this.title = toDo.title
                this.description = toDo.description
                this.schedule = toDo.schedule
                this.updatedAt = LocalDateTime.now()
            }
        }
        // index X => create
        ?: kotlin.run {
            toDo.apply {
                this.index = ++toDoDataBase.index
                this.createdAt = LocalDateTime.now()
                this.updatedAt = LocalDateTime.now()
            }.run {
                toDoDataBase.toDoList.add(toDo)
                this
            }
        }
    }

    override fun saveAll(todoList: MutableList<ToDo>): Boolean {
        return try{
            todoList.forEach {
                save(it)
            }
            true
        }catch (e:Exception){
            println(e.stackTrace)
            false
        }
    }

    override fun delete(index: Int): Boolean {
        return findOne(index)?.let {
            toDoDataBase.toDoList.remove(it)
            true
        }?: kotlin.run {
            false
        }
    }

    override fun findOne(index: Int): ToDo? {
        return try{
             toDoDataBase.toDoList.first{
                it.index == index
            }
        }catch (e:Exception){
            null
        }
    }

    override fun findAll(): MutableList<ToDo> {
        return toDoDataBase.toDoList
    }


}