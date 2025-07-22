package com.idealizer.review_x.application.modules.provider.repositories;

import com.idealizer.review_x.application.modules.provider.entities.PlatformType;
import com.idealizer.review_x.application.modules.provider.entities.Provider;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProviderRepository extends MongoRepository<Provider,ObjectId > {
    Provider findByPlatform(PlatformType platform);
}
