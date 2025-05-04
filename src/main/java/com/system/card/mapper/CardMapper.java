package com.system.card.mapper;

import com.system.card.card.Card;
import com.system.card.card.CardDto;
import com.system.card.card.CardService;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper {
    CardDto mapToCardDto(Card card);

    Card mapToCard(CardDto cardDto);

    List<CardDto> mapToCardDto(List<Card> cards);
    List<Card> mapToCard(List<CardDto> cardDtos);

//    @AfterMapping
//    default void afterMapToCardDto(CardDto cardDto, @Context CardService cardService);
}
