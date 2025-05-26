package com.system.card.card;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.system.card.config.ProtectDataSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@JsonSerialize(using = ProtectDataSerializer.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaskData {

}
