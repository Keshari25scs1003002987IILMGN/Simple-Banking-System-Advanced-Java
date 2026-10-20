package com.cyrostack.bank.model;

public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;
    public static final double OVERDRAFT_LIMIT = 10000;
    public static final double MIN_OPENING = 5000;

    public CurrentAccount(String no, String name, String phone, String pin, double opening) {
        super(no, name, phone, pin, opening);
    }

    @Override
    public double availableToWithdraw() { return balance + OVERDRAFT_LIMIT; }

    @Override
    public String getType() { return "Current"; }
}
