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

        // 1. Initial Investment (Capital Expenditure)
        double initialInvestment = totalCows * costPerCow;

        // 2. Revenue Calculation
        double dailyMilkProduction = totalCows * milkPerCowPerDay;
        double yearlyRevenue = dailyMilkProduction * milkPrice * 365;
        double totalRevenue = yearlyRevenue * years;

        // 3. Operating Expense Calculation
        double totalOperatingExpense = monthlyExpense * 12 * years;

        // 4. Loan Interest Calculation (Simple approximation for business logic)
        // If the user took a loan, the interest paid over 'n' years is an expense.
        double totalInterest = 0;
        if (loanAmount > 0 && annualRate > 0) {
            // Total interest paid over the duration
            double monthlyRate = annualRate / 12 / 100;
            int totalMonths = years * 12;
            double emi = (loanAmount * monthlyRate * Math.pow(1 + monthlyRate, totalMonths))
                    / (Math.pow(1 + monthlyRate, totalMonths) - 1);
            totalInterest = (emi * totalMonths) - loanAmount;
        }

        // 5. Final Net Profit
        // Formula: Total Revenue - (Initial Investment + Operating Costs + Loan Interest)
        return totalRevenue - (initialInvestment + totalOperatingExpense + totalInterest);
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
}