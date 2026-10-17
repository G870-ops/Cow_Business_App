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

    /**
     * Comprehensive AI prediction based on cows, yield, expenses, net financial result, and currency.
     */
    public static String predict(int totalCows, double milkPerCow, double monthlyExpense, double netProfit, String currency) {
        String curr = (currency != null && !currency.isEmpty()) ? currency : "$";
        if (netProfit > 0) {
            if (milkPerCow >= 15) {
                return "AI: Excellent performance! High yield (" + milkPerCow + "L/cow). Projected Net Profit: " 
                        + curr + " " + String.format("%,.2f", netProfit) + ". Highly scalable model.";
            } else if (milkPerCow >= 10) {
                return "AI: Profitable farm with Net Profit: " + curr + " " + String.format("%,.2f", netProfit) 
                        + ". Optimize feeding to boost yield.";
            } else {
                return "AI: Profitable (" + curr + " " + String.format("%,.2f", netProfit) 
                        + ") but low yield per cow. Consider superior dairy breed genetics.";
            }
        } else {
            return "AI Alert: Projected Net Loss of " + curr + " " + String.format("%,.2f", Math.abs(netProfit)) 
                    + ". Expenses exceed revenues. Optimize costs to achieve break-even.";
        }
    }
}