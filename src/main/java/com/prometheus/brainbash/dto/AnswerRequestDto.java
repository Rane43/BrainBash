package com.prometheus.brainbash.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerRequestDto {
	private String text;
	private boolean correct;
}
