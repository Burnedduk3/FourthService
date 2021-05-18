package com.fourthservice.models.pojos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class RolePojo extends AuditAttributesEntityPojo {

    private Integer id;

    private String name;


    public RolePojo(Integer id, String name, LocalDateTime createdOn) {
        super(createdOn);
        this.id = id;
        this.name = name;
    }

}
