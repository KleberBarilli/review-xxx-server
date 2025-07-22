package com.idealizer.review_x.application.modules.provider.services.implementations;

import com.idealizer.review_x.application.modules.provider.entities.Provider;
import com.idealizer.review_x.application.modules.provider.repositories.ProviderRepository;
import com.idealizer.review_x.application.modules.provider.services.CreateProviderService;
import org.springframework.stereotype.Service;

@Service
public class CreateProviderServiceImpl implements CreateProviderService {
    private ProviderRepository providerRepository;

    public CreateProviderServiceImpl(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Override
    public void execute(Provider provider) {

        this.providerRepository.save(provider);

    }
}