package com.system.card.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record Violation(String fieldName, String message) {

}
