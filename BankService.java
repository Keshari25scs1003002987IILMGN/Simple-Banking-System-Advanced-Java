package com.cyrostack.bank.service;

import com.cyrostack.bank.exception.*;
import com.cyrostack.bank.model.*;
import com.cyrostack.bank.util.Validator;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BankService {
    private final File dataFile;
    private Map<String, Account> accounts = new HashMap<>();
    private int nextNumber = 100001;

    public BankService(File dataFile) {
        this.dataFile = dataFile;
        load();
    }

    public Account createAccount(boolean savings, String name, String phone, String pin, double opening)
            throws BankException {
        name = Validator.name(name);
        phone = Validator.phone(phone);
        Validator.pin(pin);
        double min = savings ? SavingsAccount.MIN_OPENING : CurrentAccount.MIN_OPENING;
        if (opening < min)
            throw new InvalidInputException(String.format("Minimum opening deposit is Rs. %.2f.", min));

        String no = "CS" + nextNumber++;
        Account acc = savings ? new SavingsAccount(no, name, phone, pin, opening)
                              : new CurrentAccount(no, name, phone, pin, opening);
        accounts.put(no, acc);
        save();
        return acc;
    }

    public Account login(String accNo, String pin) throws BankException {
        Account acc = find(accNo);
        if (acc.isLocked())
            throw new AuthenticationException("Account is locked after too many wrong PINs. Contact the bank.");
        if (!acc.checkPin(pin)) {
            acc.registerFailedAttempt();
            save();
            throw new AuthenticationException(acc.isLocked()
                    ? "Account locked after 3 wrong attempts."
                    : "Wrong PIN. Attempts left: " + acc.attemptsLeft());
        }
        acc.resetFailedAttempts();
        save();
        return acc;
    }

    public Account find(String accNo) throws AccountNotFoundException {
        Account acc = accounts.get(accNo == null ? "" : accNo.trim().toUpperCase());
        if (acc == null) throw new AccountNotFoundException("Account not found.");
        return acc;
    }

    public void deposit(Account acc, double amount) throws BankException {
        acc.deposit(Validator.amount(amount), TransactionType.DEPOSIT);
        save();
    }

    public void withdraw(Account acc, double amount) throws BankException {
        acc.withdraw(Validator.amount(amount), TransactionType.WITHDRAWAL);
        save();
    }

    public void transfer(Account from, String toNo, double amount) throws BankException {
        amount = Validator.amount(amount);
        Account to = find(toNo);
        if (to == from) throw new InvalidInputException("Cannot transfer to the same account.");
        from.withdraw(amount, TransactionType.TRANSFER_OUT); // throws before anything changes
        to.deposit(amount, TransactionType.TRANSFER_IN);
        save();
    }

    public void changePin(Account acc, String oldPin, String newPin) throws BankException {
        if (!acc.checkPin(oldPin)) throw new AuthenticationException("Current PIN is incorrect.");
        acc.changePin(Validator.pin(newPin));
        save();
    }

    /** Credits one month of interest to every savings account; returns how many were credited. */
    public int applyMonthlyInterest() {
        int count = 0;
        for (Account a : accounts.values()) {
            if (a instanceof SavingsAccount) {
                double interest = ((SavingsAccount) a).monthlyInterest();
                if (interest > 0) {
                    a.deposit(interest, TransactionType.INTEREST);
                    count++;
                }
            }
        }
        save();
        return count;
    }

    private void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            out.writeObject(accounts);
            out.writeInt(nextNumber);
        } catch (IOException e) {
            System.err.println("Warning: could not save data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        if (!dataFile.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
            accounts = (Map<String, Account>) in.readObject();
            nextNumber = in.readInt();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Warning: could not load saved data, starting fresh.");
        }
    }
}
