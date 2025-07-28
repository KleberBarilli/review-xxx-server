package com.idealizer.review_x.application.provider.usecases;

import com.idealizer.review_x.domain.provider.entities.Provider;
import com.idealizer.review_x.domain.provider.repositories.ProviderRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateProviderUseCase {

    private final ProviderRepository providerRepository;

    public CreateProviderUseCase(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public void execute(Provider provider) {

        this.providerRepository.save(provider);

    }
}
