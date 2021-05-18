package com.fourthservice.models.pojos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class FullTaskResultPojo {

    private List<TaskPojo> finished;

    private List<TaskPojo> unfinished;
}
