package com.example.cowbusiness;

public class LoanCalculator {

    /**
     * Calculates the monthly EMI (Equated Monthly Installment) using the
     * standard reducing-balance formula.
     *
     * @param loan   Principal loan amount
     * @param rate   Annual interest rate in percentage (e.g., 12 for 12%)
     * @param months Total repayment period in months
     * @return Monthly EMI amount, or 0.0 if loan/rate/months is zero
     */
    public static double calculateEMI(double loan, double rate, int months) {
        // Guard: if no loan, no rate, or zero months — no EMI
        if (loan <= 0 || rate <= 0 || months <= 0) return 0.0;

        double r = rate / 12.0 / 100.0;
        double power = Math.pow(1 + r, months);
        double denominator = power - 1;

        // Safety: avoid division by zero due to floating-point edge cases
        if (denominator == 0) return 0.0;

        return (loan * r * power) / denominator;
    }
}