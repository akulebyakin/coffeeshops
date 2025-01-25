package com.kulebiakin.coffeeshops.util;

import org.springframework.stereotype.Component;

@Component
public class HtmlRatingUtils {
    private final static int MAX_STARS = 5;

    public String generateStars(int rating) {
        StringBuilder stars = new StringBuilder();
        int fullStars = rating / 10; // Полные звёзды
        int halfStar = (rating % 10 >= MAX_STARS) ? 1 : 0; // Полузвезда
        int emptyStars = MAX_STARS - fullStars - halfStar; // Пустые звёзды

        // Add full stars
        stars.append("<span class='fa fa-star checked'></span>".repeat(Math.max(0, fullStars)));
        // Add half-star if needed
        if (halfStar == 1) {
            stars.append("<span class='fa fa-star-half-o checked'></span>");
        }
        // Add empty stars
        stars.append("<span class='fa fa-star'></span>".repeat(Math.max(0, emptyStars)));
        return stars.toString();
    }

    public String formatRating(int rating) {
        double roundedRating = Math.round(rating / 10.0 * 10.0) / 10.0;
        return String.format("%.1f / %d", roundedRating, MAX_STARS);
    }
}

