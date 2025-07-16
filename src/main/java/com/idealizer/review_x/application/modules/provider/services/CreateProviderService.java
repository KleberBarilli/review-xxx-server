package com.idealizer.review_x.application.modules.provider.services;

import com.idealizer.review_x.application.modules.provider.entities.Provider;
import com.idealizer.review_x.application.modules.provider.repositories.ProviderRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateProviderService {
    private ProviderRepository providerRepository;

    public CreateProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public void execute(Provider provider) {

        this.providerRepository.save(provider);

    }
}
