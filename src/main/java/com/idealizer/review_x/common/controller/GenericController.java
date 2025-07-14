package com.idealizer.review_x.common.controller;

import org.bson.types.ObjectId;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public interface GenericController {

    default URI getLocationHeader(ObjectId id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
