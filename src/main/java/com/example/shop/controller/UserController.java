package com.example.shop.controller;

import com.example.shop.dto.user.UserDTO;
import com.example.shop.dto.user.UserRequestDTO;
import com.example.shop.dto.user.UserUpdateDTO;
import com.example.shop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/shop")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "createdAt") String sortBy) {

        log.info("GET /shop/users");

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllUser(pageable));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") UUID id) {
        log.info("GET /shop/user/{}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserById(id));
    }

    @PostMapping("/user")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        log.info("POST /shop/user");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userRequestDTO));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable("id") UUID id,
                                                  @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        log.info("PUT /shop/user/{}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUserById(id, userUpdateDTO));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") UUID id) {
        log.info("DELETE shop/user/{}", id);
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
