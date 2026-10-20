package com.cyrostack.bank.model;

public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    public static final double MIN_BALANCE = 500;
    public static final double MIN_OPENING = 1000;
    public static final double ANNUAL_RATE = 0.04;

    public SavingsAccount(String no, String name, String phone, String pin, double opening) {
        super(no, name, phone, pin, opening);
    }

    @Override
    public double availableToWithdraw() { return Math.max(0, balance - MIN_BALANCE); }

    @Override
    public String getType() { return "Savings"; }

    public double monthlyInterest() {
        return Math.round(balance * ANNUAL_RATE / 12 * 100.0) / 100.0;
    }
}
