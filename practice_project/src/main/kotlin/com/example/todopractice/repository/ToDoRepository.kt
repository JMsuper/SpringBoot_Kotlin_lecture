package com.example.todopractice.repository

import com.example.todopractice.database.ToDo

// repository가 하는 일
// Entity에 의해 생성된 DB에 접근하는 메소드들을 사용하기 위한 인터페이스이다.
// DB에 어떤 값을 생성할 것인지, 조회할 것인지 CRUD기능을 정의하는 계층이다.
interface ToDoRepository {

    fun save(toDo: ToDo):ToDo?
    fun saveAll(todoList: MutableList<ToDo>): Boolean
    fun delete(index: Int): Boolean
    fun findOne(index: Int): ToDo?
    fun findAll():MutableList<ToDo>
}