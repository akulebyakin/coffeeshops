package com.kulebiakin.coffeeshops.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HtmlRatingUtilsTest {

    private final HtmlRatingUtils htmlRatingUtils = new HtmlRatingUtils();

    @Test
    void generateStars_withFullRating() {
        String result = htmlRatingUtils.generateStars(50);
        assertEquals("<span class='fa fa-star checked'></span><span class='fa fa-star checked'></span><span class='fa fa-star checked'></span><span class='fa fa-star checked'></span><span class='fa fa-star checked'></span>", result);
    }

    @Test
    void generateStars_withHalfRating() {
        String result = htmlRatingUtils.generateStars(45);
        assertEquals("<span class='fa fa-star checked'></span><span class='fa fa-star checked'></span><span class='fa fa-star checked'></span><span class='fa fa-star checked'></span><span class='fa fa-star-half-o checked'></span>", result);
    }

    @Test
    void generateStars_withNoRating() {
        String result = htmlRatingUtils.generateStars(0);
        assertEquals("<span class='fa fa-star'></span><span class='fa fa-star'></span><span class='fa fa-star'></span><span class='fa fa-star'></span><span class='fa fa-star'></span>", result);
    }

    @Test
    void generateStars_withPartialRating() {
        String result = htmlRatingUtils.generateStars(23);
        assertEquals("<span class='fa fa-star checked'></span><span class='fa fa-star checked'></span><span class='fa fa-star'></span><span class='fa fa-star'></span><span class='fa fa-star'></span>", result);
    }

    @Test
    void formatRating_withFullRating() {
        String result = htmlRatingUtils.formatRating(50);
        assertEquals("5.0 / 5", result);
    }

    @Test
    void formatRating_withHalfRating() {
        String result = htmlRatingUtils.formatRating(45);
        assertEquals("4.5 / 5", result);
    }

    @Test
    void formatRating_withNoRating() {
        String result = htmlRatingUtils.formatRating(0);
        assertEquals("0.0 / 5", result);
    }

    @Test
    void formatRating_withPartialRating() {
        String result = htmlRatingUtils.formatRating(23);
        assertEquals("2.3 / 5", result);
    }
}