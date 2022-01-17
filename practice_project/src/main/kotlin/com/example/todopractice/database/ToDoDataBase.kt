package com.example.todopractice.database

data class ToDoDataBase(
    var index: Int = 0,
    var toDoList: MutableList<ToDo> = mutableListOf(),
){

    fun init(){
        this.index = 0
        this.toDoList = mutableListOf()
        println("[DEBUG] toDo Database init")
    }
}