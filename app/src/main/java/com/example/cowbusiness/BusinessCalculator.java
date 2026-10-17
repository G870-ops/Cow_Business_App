package com.example.cowbusiness;

public class BusinessCalculator {

    /**
     * Calculates the net profit/loss after taking investment, operating costs,
     * and loan interest into account.
     */
    public static double calculateProfit(int totalCows, double costPerCow,
                                         double milkPerCowPerDay, double milkPrice,
                                         double monthlyExpense, int years,
                                         double loanAmount, double annualRate) {

        int validYears = Math.max(1, years);

        // 1. Initial Investment (Capital Expenditure)
        double initialInvestment = totalCows * costPerCow;

        // 2. Revenue Calculation
        double dailyMilkProduction = totalCows * milkPerCowPerDay;
        double yearlyRevenue = dailyMilkProduction * milkPrice * 365;
        double totalRevenue = yearlyRevenue * validYears;

        // 3. Operating Expense Calculation
        double totalOperatingExpense = monthlyExpense * 12 * validYears;

        // 4. Loan Interest Calculation
        // If the user took a loan, the interest paid over 'n' years is an expense.
        double totalInterest = calculateTotalInterest(loanAmount, annualRate, validYears);

        // 5. Final Net Profit
        // Formula: Total Revenue - (Initial Investment + Operating Costs + Loan Interest)
        return totalRevenue - (initialInvestment + totalOperatingExpense + totalInterest);
    }

    /**
     * Calculates total loan interest paid over the duration safely without division by zero.
     */
    public static double calculateTotalInterest(double loanAmount, double annualRate, int years) {
        if (loanAmount <= 0 || annualRate <= 0 || years <= 0) return 0.0;
        int totalMonths = years * 12;
        double emi = LoanCalculator.calculateEMI(loanAmount, annualRate, totalMonths);
        double totalPaid = emi * totalMonths;
        return Math.max(0.0, totalPaid - loanAmount);
    }

    /**
     * Calculates annual average net profit.
     */
    public static double calculateAnnualProfit(int totalCows, double costPerCow,
                                               double milkPerCowPerDay, double milkPrice,
                                               double monthlyExpense, int years,
                                               double loanAmount, double annualRate) {
        int validYears = Math.max(1, years);
        return calculateProfit(totalCows, costPerCow, milkPerCowPerDay, milkPrice,
                monthlyExpense, validYears, loanAmount, annualRate) / validYears;
    }

    /**
     * Provides a business health status based on the calculated net profit.
     */
    public static String getSuggestion(double profit) {
        if (profit > 1000000) {
            return "🚀 High Growth: This is a highly scalable business model!";
        } else if (profit > 500000) {
            return "✅ Excellent: Great returns on your investment.";
        } else if (profit > 100000) {
            return "📈 Stable: Moderate profit. Look into reducing feed costs.";
        } else if (profit > 0) {
            return "⚠️ Low Margin: Breaking even. Consider increasing milk production.";
        } else {
            return "❌ Loss Alert: Expenses exceed revenue. Re-evaluate your plan.";
        }
    }

    /**
     * Provides a business health status formatted with the selected currency symbol.
     */
    public static String getSuggestion(double profit, String currency) {
        String curr = (currency != null && !currency.isEmpty()) ? currency : "$";
        if (profit > 1000000) {
            return "🚀 High Growth: Projected return of " + curr + " " + String.format("%,.2f", profit) + "! Highly scalable business.";
        } else if (profit > 500000) {
            return "✅ Excellent: Strong returns of " + curr + " " + String.format("%,.2f", profit) + " on your dairy investment.";
        } else if (profit > 100000) {
            return "📈 Stable: Moderate profit of " + curr + " " + String.format("%,.2f", profit) + ". Focus on reducing feed costs.";
        } else if (profit > 0) {
            return "⚠️ Low Margin: Breaking even (" + curr + " " + String.format("%,.2f", profit) + "). Consider increasing milk production.";
        } else {
            return "❌ Loss Alert: Projected Net Loss of " + curr + " " + String.format("%,.2f", Math.abs(profit)) + ". Re-evaluate costs.";
        }
    }
}