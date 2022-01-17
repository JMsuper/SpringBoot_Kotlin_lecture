package com.example.todopractice.repository

import com.example.todopractice.config.AppConfig
import com.example.todopractice.database.ToDo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

// @ExtendWith(SpringExtension::class)
// 위 어노테이션을 사용하지 않아도 된다. 왜냐하면, @SpringBootTest에 해당 어노테이션이
// 적용되있기 때문이다.
@SpringBootTest(classes = [ToDoRepositoryImpl::class,AppConfig::class])
class ToDoRepositoryTest {

    @Autowired
    lateinit var toDoRepositoryImpl: ToDoRepositoryImpl

    @BeforeEach
    fun before(){
        toDoRepositoryImpl.toDoDataBase.init()
    }

    @Test
    fun saveTest(){
        var toDo = ToDo().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = LocalDateTime.now()
        }
        var result = toDoRepositoryImpl.save(toDo)

        Assertions.assertNotNull(result)
        Assertions.assertEquals("test",result?.title)
        Assertions.assertEquals("test",result?.description)
        Assertions.assertEquals(1,result?.index)
    }

    @Test
    fun updateTest(){
        var toDo = ToDo().apply {
            this.title = "test"
            this.description = "test"
            this.index = 1
            this.schedule = LocalDateTime.now()
        }
        toDoRepositoryImpl.save(toDo)

        var newToDo = ToDo().apply {
            this.title = "new test"
            this.description = "new test"
            this.schedule = LocalDateTime.now()
        }
        var result = toDoRepositoryImpl.save(newToDo)

        Assertions.assertNotNull(result)
        Assertions.assertEquals("new test",result?.title)
        Assertions.assertEquals("new test",result?.description)
        Assertions.assertEquals(1,result?.index)
    }

    @Test
    fun updateErrorTest(){
        var toDo = ToDo().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = LocalDateTime.now()
        }
        toDoRepositoryImpl.save(toDo)

        var newToDo = ToDo().apply {
            this.index = 2
            this.title = "new test"
            this.description = "new test"
            this.schedule = LocalDateTime.now()
        }
        var result = toDoRepositoryImpl.save(newToDo)

        Assertions.assertNull(result)
    }

    @Test
    fun saveAllTest(){
        var toDoList = mutableListOf<ToDo>().apply {
            var toDo = ToDo().apply {
                this.title = "test"
                this.description = "test"
                this.schedule = LocalDateTime.now()
            }
            this.add(toDo)
            var toDo2 = ToDo().apply {
                this.title = "test"
                this.description = "test"
                this.schedule = LocalDateTime.now()
            }
            this.add(toDo2)
        }
        var result = toDoRepositoryImpl.saveAll(toDoList)
        Assertions.assertEquals(true,result)
    }



    @Test
    fun findOneTest(){
        var toDo = ToDo().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = LocalDateTime.now()
        }
        toDoRepositoryImpl.save(toDo)

        var result = toDoRepositoryImpl.findOne(1)
        Assertions.assertNotNull(result)

        var result2 = toDoRepositoryImpl.findOne(2)
        Assertions.assertNull(result2)
    }

    @Test
    fun deleteTest(){
        var toDo = ToDo().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = LocalDateTime.now()
        }
        toDoRepositoryImpl.save(toDo)

        var result = toDoRepositoryImpl.delete(1)
        Assertions.assertEquals(true,result)
    }

    @Test
    fun deleteErrorTest(){
        var toDo = ToDo().apply {
            this.title = "test"
            this.description = "test"
            this.schedule = LocalDateTime.now()
        }
        toDoRepositoryImpl.save(toDo)

        var result = toDoRepositoryImpl.delete(2)
        Assertions.assertEquals(false,result)
    }

    @Test
    fun findAllTest(){
        var toDoList = mutableListOf<ToDo>().apply {
            var toDo = ToDo().apply {
                this.title = "test"
                this.description = "test"
                this.schedule = LocalDateTime.now()
            }
            this.add(toDo)
            var toDo2 = ToDo().apply {
                this.title = "test"
                this.description = "test"
                this.schedule = LocalDateTime.now()
            }
            this.add(toDo2)
        }
        toDoRepositoryImpl.saveAll(toDoList)

        var result = toDoRepositoryImpl.findAll()
        Assertions.assertEquals(2,result.size)
    }
}