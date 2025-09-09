package com.idealizer.review_x.application.provider.usecases;

import com.idealizer.review_x.domain.core.provider.entities.PlatformType;
import com.idealizer.review_x.domain.core.provider.entities.Provider;
import com.idealizer.review_x.domain.core.provider.repositories.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class CreateProviderUseCaseTest {

    private ProviderRepository providerRepository;
    private CreateProviderUseCase createProviderUseCase;

    @BeforeEach
    void setUp() {
        providerRepository = Mockito.mock(ProviderRepository.class);
        createProviderUseCase = new CreateProviderUseCase(providerRepository);
    }

    @Test
    void shouldSaveProviderWhenExecuted() {

        Provider provider = new Provider();
        provider.setPlatform(PlatformType.GOOGLE);
        provider.setClientSecret("secret");
        provider.setAccessToken("access_token");
        createProviderUseCase.execute(provider);
        verify(providerRepository, times(1)).save(provider);
    }
}
