package ch.janishuber.logiclab.adapter.rest.dto;

import jakarta.validation.constraints.Pattern;

public record GuessDto(@Pattern(regexp = "\\d{4}") String guess) {}
