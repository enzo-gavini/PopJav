package com.enzo.authservice.service.proxy;

import com.enzo.authservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "persistence-service", path = "/api/users")
public interface UserFeignClient {

    @PostMapping
    public UserDTO save(@RequestBody UserDTO userDTO);

    @GetMapping
    public List<UserDTO> getAllUser();

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id);

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO);

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id);

    @GetMapping("/search")
    public UserDTO getUserByEmail(@RequestParam String email);

    @GetMapping("/exists/email")
    public boolean existsByEmail(@RequestParam String email);

    @GetMapping("/exists/username")
    public boolean existsByUsername(@RequestParam String username);
}
