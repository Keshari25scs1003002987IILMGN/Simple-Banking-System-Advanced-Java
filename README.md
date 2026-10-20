# Simple Banking System (Advanced) in Java

An improved version of the console banking system with a cleaner layered structure, stronger validation, security features and data persistence.

## What's improved over the basic version
- **Layered structure:** `model`, `service`, `exception`, `util` and `ui` packages
- **Inheritance:** abstract `Account` with `SavingsAccount` and `CurrentAccount`
  - Savings: minimum balance Rs. 500, 4% yearly interest credited monthly
  - Current: overdraft up to Rs. 10,000
- **Custom exceptions:** `InsufficientFundsException`, `InvalidInputException`, `AccountNotFoundException`, `AuthenticationException`
- **Input validation:** name, 10-digit mobile number, 4-digit PIN, positive amounts, minimum opening deposits
- **Security:** PINs are salted and hashed (SHA-256), never stored in plain text; account locks after 3 wrong PINs
- **Persistence:** data is saved to `bank_data.ser` using Java serialization, so accounts survive restarts
- Transfers, mini statement with transaction IDs, and PIN change

## Project Structure
```
src/com/cyrostack/bank/
├── Main.java
├── model/      Account, SavingsAccount, CurrentAccount, Transaction, TransactionType
├── service/    BankService (all business rules + saving/loading)
├── exception/  BankException and 4 specific exceptions
├── util/       Validator, SecurityUtil
└── ui/         ConsoleApp (menus)
```

## How to Run (JDK 8+)
```bash
mkdir out
javac -d out $(find src -name "*.java")      # Windows PowerShell: javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out com.cyrostack.bank.Main
```

## Author
Your Name - Cyrostack Internship Project
