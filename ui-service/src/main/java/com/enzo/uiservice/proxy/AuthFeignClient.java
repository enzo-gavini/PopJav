package com.enzo.uiservice.proxy;

import com.enzo.uiservice.dto.AuthResponseDTO;
import com.enzo.uiservice.dto.LoginRequestDTO;
import com.enzo.uiservice.dto.RegisterRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "api-gateway", contextId = "authClient", path = "/auth")
public interface AuthFeignClient {

    @PostMapping("/register")
    public AuthResponseDTO register(@RequestBody RegisterRequestDTO request);

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request);

}
