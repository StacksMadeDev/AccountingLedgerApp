import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class FinancialTracker {
    // The name of the CSV file where transaction data is stored
    private static final String FILENAME = "transactions.csv";

    public static void main(String[] args) {
        // Create a new Scanner object to read input from the user
        Scanner scanner = new Scanner(System.in);
        // Declare a HashMap to hold transaction details

        HashMap<String, String> transaction;
        // Create a new TransactionManager object to manage transactions
        TransactionManager transactionManager = new TransactionManager();

        // Main program loop
        while (true) {
            System.out.println("Financial Tracker\n");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit\n");

            // Prompt the user to select an option
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().toUpperCase();

            // Handle the user's choice
            switch (choice) {
                case "D": // Add a deposit
                    transaction = readTransactionFromUser("Deposit");  // Prompt the user to enter deposit details
                    Transaction deposit = createTransaction(transaction);   // Create a new Transaction object from the user's input
                    transactionManager.addTransaction(deposit);              // Add the transaction to the TransactionManager
                    addTransactionToCSV(deposit);                             // Add the transaction to the CSV file
                    System.out.println("\nDeposit saved.\n");
                    break;
                case "P":   // Make a payment
                    transaction = readTransactionFromUser("Payment");
                    Transaction payment = createTransaction(transaction);
                    transactionManager.addTransaction(payment);
                    addTransactionToCSV(payment);
                    System.out.println("\nPayment saved.\n");
                    break;
                case "L":  // Display the ledger
                    transactionManager.displayLedger();
                    break;
                case "X": // Exit the program
                    System.out.println("\nExiting program.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.\n");
                    break;
            }
        }
    }

    // Prompt the user to enter transaction details and return a HashMap of the input
    private static HashMap<String, String> readTransactionFromUser(String type) {
        // Create a new Scanner object to read input from the user
        Scanner scanner = new Scanner(System.in);
        // Declare a HashMap to hold transaction details
        HashMap<String, String> transaction = new HashMap<>();

        System.out.println("\nEnter " + type + " details:");
        System.out.print("Date (YYYY-MM-DD): ");
        transaction.put("date", scanner.nextLine());

        // Prompt the user to enter transaction details
        System.out.print("Time (HH:MM:SS): ");
        transaction.put("time", scanner.nextLine());

        System.out.print("Description: ");
        transaction.put("description", scanner.nextLine());

        System.out.print("Vendor: ");
        transaction.put("vendor", scanner.nextLine());

        System.out.print("Amount: ");
        transaction.put("amount", scanner.nextLine());

        return transaction;
    }

    private static Transaction createTransaction(HashMap<String, String> transactionMap) {
        LocalDate date = LocalDate.parse(transactionMap.get("date"));
        LocalTime time = LocalTime.parse(transactionMap.get("time"));
        String description = transactionMap.get("description");
        String vendor = transactionMap.get("vendor");
        double amount = Double.parseDouble(transactionMap.get("amount"));
        return new Transaction(date, time, description, vendor, amount);
    }

    private static void addTransactionToCSV(Transaction transaction) {
        try {
            FileWriter fileWriter = new FileWriter(FILENAME, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(transaction.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateTransactionCSV(List<Transaction> transactions) {
        try {
            FileWriter fileWriter = new FileWriter(FILENAME, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (Transaction transaction : transactions) {
                printWriter.println(transaction.toString());
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void readTransactionsFromCSV(String fileName, TransactionManager transactionManager) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");

                LocalDate date = LocalDate.parse(fields[0]);
                LocalTime time = LocalTime.parse(fields[1]);
                String description = fields[2];
                String vendor = fields[3];
                double amount = Double.parseDouble(fields[4]);

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactionManager.addTransaction(transaction);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }
}
