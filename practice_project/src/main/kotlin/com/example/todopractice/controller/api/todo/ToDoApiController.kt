package com.example.todopractice.controller.api.todo

import com.example.todopractice.model.http.ToDoDto
import com.example.todopractice.service.ToDoService
import io.swagger.annotations.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(description = "일정관리")
@RestController
@RequestMapping("/api/todo")
class ToDoApiController(
    val toDoService: ToDoService
) {
    // C
    @ApiOperation(value = "일정생성", notes = "일정 생성 POST API")
    @ApiResponse(code = 201, message = "일정 생성 성공")
    @PostMapping(path = [""])
    fun create(@Valid @RequestBody toDoDto: ToDoDto): ResponseEntity<ToDoDto>{

        toDoService.create(toDoDto)?.let {
            return ResponseEntity.status(201).body(it)
        }?: kotlin.run {
            return  ResponseEntity.internalServerError().build()
        }
    }

    // R
    @ApiOperation(value = "일정확인", notes = "일정 확인 GET API")
    @ApiResponse(code = 200, message = "확인 성공")
    @GetMapping(path = [""])
    fun read(
        @ApiParam(name = "index test", example = "0")
        @RequestParam(required = false)
        index:Int?
    ): ResponseEntity<Any?>{
        // index o => read
        index?.let {
            return toDoService.read(index)
            ?.let {
                ResponseEntity.ok(it)
            }
            // not found
            ?: kotlin.run {
                ResponseEntity.ok().build()
            }
        }
        // index x => readAll
        ?: kotlin.run {
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION,"/api/todo/all")
                .build()
        }
    }

    @ApiOperation(value = "전체일정확인", notes = "전체 일정 확인 GET API")
    @GetMapping(path=["/all"])
    fun readAll():ResponseEntity<MutableList<ToDoDto>>{
        return ResponseEntity.ok(toDoService.readAll())
    }

    // U
    @ApiOperation(value="일정수정", notes = "일정 수정 PUT API")
    @PutMapping(path = [""])
    fun update(@Valid @RequestBody toDoDto: ToDoDto) : ResponseEntity<ToDoDto>{
        return ResponseEntity.ok(toDoService.update(toDoDto))
    }

    // D
    @ApiOperation(value = "일정삭제", notes = "일정 삭제 DELETE API")
    @DeleteMapping(path=["/{index}"])
    fun delete(@PathVariable(value = "index") index: Int):ResponseEntity<Any>{
        if(!toDoService.delete(index)){
            return ResponseEntity.status(500).build()
        }
        return ResponseEntity.ok().build()
    }
}