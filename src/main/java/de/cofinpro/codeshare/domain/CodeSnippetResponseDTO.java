package de.cofinpro.codeshare.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Immutable DTO object representing a code snippet Http response with the code text,
 * a formatted date time string of creation and availability restriction settings on time and views.
 */
@Value
@AllArgsConstructor
public class CodeSnippetResponseDTO {

    String code;
    String date;
    // time restriction in seconds, that this code snippet is offered to viewers by uuid (0 = no restriction)
    long time;
    // views restriction amount, how often this code snippet is offered to viewers by uuid (0 = no restriction)
    int views;
}
