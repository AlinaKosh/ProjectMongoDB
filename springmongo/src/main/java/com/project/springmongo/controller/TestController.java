package com.project.springmongo.controller;

import com.project.springmongo.model.TodoDTO;
import com.project.springmongo.model.User;
import com.project.springmongo.repository.UserRepository;
import com.project.springmongo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    private final TodoService todoService;
    private final UserRepository userRepository;

    public TestController(TodoService todoService, UserRepository userRepository) {
        this.todoService = todoService;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/allTodo")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MODERATOR')")
    public List<TodoDTO> userAccess() {
        return new ArrayList<>(todoService.findAll());
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MODERATOR')")
    public Optional<TodoDTO> findById(@PathVariable("id") String id){
        return todoService.findById(id);
    }
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todo){
        todoService.save(todo);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> updateTodo(@PathVariable("id") String id, @RequestBody TodoDTO todo){
        todoService.update(id, todo);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") String id){
        todoService.delete(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    //МЕТОДЫ ДЛЯ ПРОВЕРКИ АГРЕГАТНЫХ ФУНКЦИЙ
    @GetMapping("/startWithS")
    @PreAuthorize("hasRole('ADMIN')")
    public String usernameStartsWithS(){
        return "Имена начинающиеся с буквы S" + userRepository.findUsersNameStartsWithS().toString();
    }

    @GetMapping("/unique")
    @PreAuthorize("hasRole('ADMIN')")
    public String uniqueUserNames(){
        return "Уникальное имя" + userRepository.findAllUniqueUserNames().toString();
    }
    @GetMapping("/usersWithNameLana")
    @PreAuthorize("hasRole('ADMIN')")
    public String usersWithNameLana(){
        return "Все Ланы в базе данных" + userRepository.findUsersWithNameLana().toString();
    }
}