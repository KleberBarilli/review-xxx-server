package com.idealizer.review_x.application.modules.provider.controllers;

import com.idealizer.review_x.application.modules.provider.entities.Provider;
import com.idealizer.review_x.application.modules.provider.services.CreateProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController()
@RequestMapping("/api/providers")
public class ProviderController {

    @Value("${PROVIDER_SECRET_KEY}")
    private String providerSecretKey;

    private final CreateProviderService createProviderService;

    public ProviderController (CreateProviderService createProviderService) {
        this.createProviderService = createProviderService;
    }

    @Operation(summary = "Create provider",
            parameters = {
                    @Parameter(name = "key", in = ParameterIn.HEADER, required = true, example = "your-token")
            })
    @PostMapping()
    public ResponseEntity<String> createProvider(@RequestBody Provider provider,  @RequestHeader("key") String authorizationHeader) {

        Logger.getLogger(ProviderController.class.getName()).info("Creating provider: " + provider.getPlatform());
        if (!providerSecretKey.equals(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        this.createProviderService.execute(provider);
        return ResponseEntity.ok("Provider created successfully");
    }
}
