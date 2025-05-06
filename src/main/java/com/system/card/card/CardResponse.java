package com.system.card.card;

public class CardResponse {
    public static CardDto fromCard(Card card, String userEmail) {
        CardDto cardDto = new CardDto();
        String cardNumber = card.getEncryptedCardNumber();
        if (cardNumber != null && cardNumber.length() > 4) {
            String maskedCardNumber = "*".repeat(cardNumber.length() - 4) + cardNumber.substring(cardNumber.length() - 4);
            cardDto.setCardNumber(maskedCardNumber);
        } else {
            cardDto.setCardNumber(cardNumber);
        }
        cardDto.setCardHolderFullName(card.getCardHolderFullName());
        cardDto.setExpiryDate(card.getExpiryDate());
        cardDto.setBalance(card.getBalance());
        cardDto.setCardHolderEmail(userEmail);
        return cardDto;
    }
}
