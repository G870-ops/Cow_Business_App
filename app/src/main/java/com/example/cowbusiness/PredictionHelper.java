package com.example.cowbusiness;

public class PredictionHelper {

    /**
     * Returns an AI-style textual prediction based on daily milk yield and monthly expenses.
     *
     * @param milkPerDay Daily milk production in litres (across all cows)
     * @param expense    Monthly operational expense
     * @return A descriptive prediction string
     */
    public static String predict(double milkPerDay, double expense) {

        if (milkPerDay > 15 && expense < 30000) {
            return "AI: High yield farm. Very profitable in future.";
        } else if (milkPerDay > 10) {
            return "AI: Average growth. Improve feeding strategy.";
        } else {
            return "AI: Low productivity. Consider better cow breed.";
        }
    }
}