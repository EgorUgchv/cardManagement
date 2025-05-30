package com.system.card.exception;

public class CardAlreadyBlockedException extends  RuntimeException{
    public CardAlreadyBlockedException(String message) {
        super(message);
    }
}

