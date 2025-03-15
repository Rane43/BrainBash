package com.prometheus.brainbash.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDto {
    private Long id;
    
    private String text;
    
    private boolean correct;
    
    private long question_id;
}
