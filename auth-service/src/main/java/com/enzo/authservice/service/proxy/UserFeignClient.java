package com.enzo.authservice.service.proxy;

import com.enzo.authservice.dto.UserCreateRequest;
import com.enzo.authservice.dto.UserDTO;
import com.enzo.authservice.dto.UserFullDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "persistence-service", path = "/api/users")
public interface UserFeignClient {

    @PostMapping
    public UserDTO save(@RequestBody UserCreateRequest request);

    @GetMapping
    public List<UserDTO> getAllUser();

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id);

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO);

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id);

    @GetMapping("/search")
    public UserFullDTO getUserByEmail(@RequestParam String email);

    @GetMapping("/exists/email")
    public boolean existsByEmail(@RequestParam String email);

    @GetMapping("/exists/username")
    public boolean existsByUsername(@RequestParam String username);
}
