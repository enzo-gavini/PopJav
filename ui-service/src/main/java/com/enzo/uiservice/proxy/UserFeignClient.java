package com.enzo.uiservice.proxy;

import com.enzo.uiservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "api-gateway", contextId = "userClient", path = "/api/users")
public interface UserFeignClient {

    @GetMapping("/search")
    UserDTO getUserByEmail(@RequestParam String email);
}