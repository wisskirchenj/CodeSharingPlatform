package de.cofinpro.codeshare.domain;

/**
 * Immutable DTO object representing a code snippet Http response with the code text,
 * a formatted date time string of creation and availability restriction settings on time and views.
 *
 * @param time  time restriction in seconds, that this code snippet is offered to viewers by uuid (0 = no restriction)
 * @param views views restriction amount, how often this code snippet is offered to viewers by uuid (0 = no restriction)
 */

public record CodeSnippetResponseDTO(String code, String date, long time, int views) {

}
