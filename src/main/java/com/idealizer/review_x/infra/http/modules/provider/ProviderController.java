package com.idealizer.review_x.infra.http.modules.provider;

import com.idealizer.review_x.application.provider.usecases.CreateProviderUseCase;
import com.idealizer.review_x.domain.provider.entities.Provider;
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

    private final CreateProviderUseCase createProviderUseCase;

    public ProviderController (CreateProviderUseCase createProviderUseCase) {
        this.createProviderUseCase = createProviderUseCase;
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
        this.createProviderUseCase.execute(provider);
        return ResponseEntity.ok("Provider created successfully");
    }
}
