package main;

import blockchain.Blockchain;
import models.Transaction;
import java.util.Scanner;

public class BlockchainSimulator {

    private static Blockchain blockchain;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        System.out.println("=".repeat(60));
        System.out.println("    🔗 BLOCKCHAIN SIMULATOR - INTERACTIVE MODE");
        System.out.println("=".repeat(60));
        System.out.println();

        // Initialize blockchain with user-selected difficulty
        int difficulty = getDifficulty();
        blockchain = new Blockchain(difficulty);

        // Main menu loop
        boolean running = true;
        while (running) {
            running = showMenu();
        }

        System.out.println("\n👋 Thank you for using Blockchain Simulator!");
        scanner.close();
    }

    private static int getDifficulty() {
        System.out.println("Select mining difficulty:");
        System.out.println("  1 - Easy (3 leading zeros)");
        System.out.println("  2 - Medium (4 leading zeros)");
        System.out.println("  3 - Hard (5 leading zeros)");
        System.out.print("Enter choice (1-3): ");

        int choice = getIntInput(1, 3);
        int difficulty = 3 + (choice - 1);

        System.out.println("✓ Difficulty set to " + difficulty + " leading zeros\n");
        return difficulty;
    }

    private static boolean showMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MAIN MENU");
        System.out.println("=".repeat(60));
        System.out.println("1. Add Transaction");
        System.out.println("2. Mine Pending Transactions");
        System.out.println("3. View Blockchain");
        System.out.println("4. Validate Blockchain");
        System.out.println("5. Export to JSON");
        System.out.println("6. View Pending Transactions");
        System.out.println("7. Demonstrate Tampering");
        System.out.println("0. Exit");
        System.out.println("=".repeat(60));
        System.out.print("Enter your choice: ");

        int choice = getIntInput(0, 7);
        System.out.println();

        switch (choice) {
            case 1:
                addTransaction();
                break;
            case 2:
                minePendingTransactions();
                break;
            case 3:
                viewBlockchain();
                break;
            case 4:
                validateBlockchain();
                break;
            case 5:
                exportToJSON();
                break;
            case 6:
                viewPendingTransactions();
                break;
            case 7:
                demonstrateTampering();
                break;
            case 0:
                return false;
        }
        return true;
    }

    private static void addTransaction() {
        System.out.println("─".repeat(60));
        System.out.println("ADD NEW TRANSACTION");
        System.out.println("─".repeat(60));

        System.out.print("Enter sender name: ");
        String sender = scanner.nextLine().trim();

        System.out.print("Enter recipient name: ");
        String recipient = scanner.nextLine().trim();

        System.out.print("Enter amount: ");
        double amount = getDoubleInput();

        if (sender.isEmpty() || recipient.isEmpty()) {
            System.out.println("❌ Error: Sender and recipient cannot be empty!");
            return;
        }

        if (amount <= 0) {
            System.out.println("❌ Error: Amount must be positive!");
            return;
        }

        Transaction transaction = new Transaction(sender, recipient, amount);
        blockchain.addTransaction(transaction);

        System.out.println("✓ Transaction added successfully!");
        System.out.println("  " + transaction);
    }

    private static void minePendingTransactions() {
        System.out.println("─".repeat(60));
        System.out.println("MINING BLOCK");
        System.out.println("─".repeat(60));

        blockchain.minePendingTransactions();
    }

    private static void viewBlockchain() {
        System.out.println("─".repeat(60));
        System.out.println("BLOCKCHAIN OVERVIEW");
        System.out.println("─".repeat(60));

        blockchain.printChain();
    }

    private static void validateBlockchain() {
        System.out.println("─".repeat(60));
        System.out.println("VALIDATING BLOCKCHAIN");
        System.out.println("─".repeat(60));

        boolean isValid = blockchain.isChainValid();

        if (isValid) {
            System.out.println("\n✅ Blockchain is VALID!");
        } else {
            System.out.println("\n❌ Blockchain is INVALID!");
        }
    }

    private static void exportToJSON() {
        System.out.println("─".repeat(60));
        System.out.println("EXPORT TO JSON");
        System.out.println("─".repeat(60));

        System.out.print("Enter filename (default: output/blockchain.json): ");
        String filename = scanner.nextLine().trim();

        if (filename.isEmpty()) {
            filename = "output/blockchain.json";
        }

        blockchain.exportToJSON(filename);
    }

    private static void viewPendingTransactions() {
        System.out.println("─".repeat(60));
        System.out.println("PENDING TRANSACTIONS");
        System.out.println("─".repeat(60));

        if (blockchain.getChain().size() > 0) {
            System.out.println("Note: Use 'Mine Pending Transactions' to add them to blockchain");
        }
    }

    private static void demonstrateTampering() {
        System.out.println("─".repeat(60));
        System.out.println("TAMPERING DETECTION DEMO");
        System.out.println("─".repeat(60));

        if (blockchain.getChain().size() < 2) {
            System.out.println("❌ Not enough blocks to demonstrate tampering!");
            System.out.println("   Add transactions and mine at least one block first.");
            return;
        }

        System.out.println("\n[INFO] Simulating tampering with Block #1...");

        var block1 = blockchain.getChain().get(1);
        if (block1.getTransactions().isEmpty()) {
            System.out.println("❌ Block #1 has no transactions to tamper with!");
            return;
        }

        var originalTx = block1.getTransactions().get(0);
        System.out.println("Original transaction: " + originalTx);

        // Create tampered transaction
        Transaction tamperedTx = new Transaction(
                originalTx.getSender(),
                originalTx.getRecipient(),
                originalTx.getAmount() + 1000.0
        );

        block1.getTransactions().set(0, tamperedTx);
        System.out.println("Tampered transaction: " + tamperedTx);
        System.out.println("\n[ACTION] Revalidating blockchain...\n");

        boolean isValid = blockchain.isChainValid();

        if (!isValid) {
            System.out.println("❌ TAMPERING DETECTED! Blockchain is now invalid.");
            System.out.println("This demonstrates blockchain's immutability!");
        }
    }

    // Helper methods for input validation
    private static int getIntInput(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.print("Please enter a number between " + min + " and " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private static double getDoubleInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
}
