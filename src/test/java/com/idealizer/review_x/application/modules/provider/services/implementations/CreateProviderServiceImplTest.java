package com.idealizer.review_x.application.modules.provider.services.implementations;

import com.idealizer.review_x.application.modules.provider.entities.PlatformType;
import com.idealizer.review_x.application.modules.provider.entities.Provider;
import com.idealizer.review_x.application.modules.provider.repositories.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class CreateProviderServiceImplTest {

    private ProviderRepository providerRepository;
    private CreateProviderServiceImpl createProviderService;

    @BeforeEach
    void setUp() {
        providerRepository = Mockito.mock(ProviderRepository.class);
        createProviderService = new CreateProviderServiceImpl(providerRepository);
    }

    @Test
    void shouldSaveProviderWhenExecuted() {

        Provider provider = new Provider();
        provider.setPlatform(PlatformType.GOOGLE);
        provider.setClientSecret("secret");
        provider.setAccessToken("access_token");
        createProviderService.execute(provider);
        verify(providerRepository, times(1)).save(provider);
    }
}
