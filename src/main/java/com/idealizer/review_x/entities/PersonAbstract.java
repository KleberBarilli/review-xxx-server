package com.idealizer.review_x.entities;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public abstract class PersonAbstract {
    private String name;
    @Column(name = "birth_date")
    private Date birthDate;
    @Column(name = "avatar_url")
    private String avatarUrl;
    private String description;

}
