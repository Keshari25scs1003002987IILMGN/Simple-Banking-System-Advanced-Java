package com.cyrostack.bank.ui;

import com.cyrostack.bank.exception.BankException;
import com.cyrostack.bank.model.Account;
import com.cyrostack.bank.service.BankService;

import java.util.Scanner;

public class ConsoleApp {
    private final Scanner in = new Scanner(System.in);
    private final BankService bank;

    public ConsoleApp(BankService bank) {
        this.bank = bank;
    }

    public void run() {
        System.out.println("===== CYROSTACK BANK (Advanced) =====");
        while (true) {
            System.out.println("\n1. Open Account\n2. Login\n3. Credit Monthly Interest (admin)\n4. Exit");
            switch (readInt("Choose: ")) {
                case 1: attempt(this::openAccount); break;
                case 2: attempt(this::login); break;
                case 3:
                    System.out.println(bank.applyMonthlyInterest() + " savings account(s) credited.");
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private interface Action { void run() throws BankException; }

    private void attempt(Action a) {
        try {
            a.run();
        } catch (BankException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void openAccount() throws BankException {
        System.out.print("Account type (1 = Savings, 2 = Current): ");
        boolean savings = !"2".equals(in.nextLine().trim());
        System.out.print("Full name: ");
        String name = in.nextLine();
        System.out.print("Mobile number: ");
        String phone = in.nextLine();
        System.out.print("Set 4-digit PIN: ");
        String pin = in.nextLine();
        double opening = readDouble("Opening deposit: ");
        Account acc = bank.createAccount(savings, name, phone, pin, opening);
        System.out.println("Account opened. Your account number: " + acc.getAccountNumber());
    }

    private void login() throws BankException {
        System.out.print("Account number: ");
        String no = in.nextLine();
        System.out.print("PIN: ");
        String pin = in.nextLine();
        Account acc = bank.login(no.trim().toUpperCase(), pin);
        System.out.println("Welcome, " + acc.getHolderName() + " (" + acc.getType() + ")");
        menu(acc);
    }

    private void menu(Account acc) {
        while (true) {
            System.out.println("\n1. Balance  2. Deposit  3. Withdraw  4. Transfer");
            System.out.println("5. Mini Statement  6. Change PIN  7. Logout");
            int c = readInt("Choose: ");
            if (c == 7) { System.out.println("Logged out."); return; }
            attempt(() -> {
                switch (c) {
                    case 1:
                        System.out.printf("Balance: Rs. %,.2f | Available to withdraw: Rs. %,.2f%n",
                                acc.getBalance(), acc.availableToWithdraw());
                        break;
                    case 2:
                        bank.deposit(acc, readDouble("Amount: "));
                        System.out.println("Deposited.");
                        break;
                    case 3:
                        bank.withdraw(acc, readDouble("Amount: "));
                        System.out.println("Withdrawn.");
                        break;
                    case 4:
                        System.out.print("Recipient account number: ");
                        String to = in.nextLine();
                        bank.transfer(acc, to, readDouble("Amount: "));
                        System.out.println("Transferred.");
                        break;
                    case 5:
                        acc.getHistory().forEach(System.out::println);
                        break;
                    case 6:
                        System.out.print("Current PIN: ");
                        String oldPin = in.nextLine();
                        System.out.print("New PIN: ");
                        bank.changePin(acc, oldPin, in.nextLine());
                        System.out.println("PIN changed.");
                        break;
                    default: System.out.println("Invalid option.");
                }
            });
        }
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        try { return Integer.parseInt(in.nextLine().trim()); } catch (NumberFormatException e) { return -1; }
    }

    private double readDouble(String prompt) {
        System.out.print(prompt);
        try { return Double.parseDouble(in.nextLine().trim()); } catch (NumberFormatException e) { return Double.NaN; }
    }
}
