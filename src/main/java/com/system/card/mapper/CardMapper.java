package com.system.card.mapper;

import com.system.card.card.Card;
import com.system.card.card.CardDto;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper {
    CardDto mapToCardDto(Card card);

    //@Mapping(target = "cardNumber", ignore = true)
    Card mapToCard(CardDto cardDto, @Context CardService cardService);


    @AfterMapping
    default void afterMapToCardDto(CardDto cardDto, @Context CardService cardService);
}
