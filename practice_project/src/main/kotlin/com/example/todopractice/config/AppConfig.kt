package com.example.todopractice.config

import com.example.todopractice.database.ToDo
import com.example.todopractice.database.ToDoDataBase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// 빈 설정 메타정보를 담고있는 코드
// 애플리케이션이 구동될 때의 빈들을 먼저 설정하는 어노테이션
@Configuration
class AppConfig {

    // initMethod는 Bean이 생성될 때 호출되는 메소드를 지정하는 것이다.
    // initMethod의 value는 함수명으로 다른 곳에서 해당함수를 호출할 수 있다.
    @Bean(initMethod = "init")
    fun toDoDataBase(): ToDoDataBase{
        return ToDoDataBase()
    }
}