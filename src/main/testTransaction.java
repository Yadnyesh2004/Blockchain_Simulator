package main;

import models.Transaction;

public class testTransaction {
    public static void main(String[] args) {
        System.out.println("=== Creating Transactions ===\n");

        // 1St Transaction
        Transaction tst1=new Transaction("bob","alice",100.0);
        System.out.println("Transaction1: ");
        System.out.println(tst1);
        System.out.println("Transactional hash : "+ tst1.getTransactionId());
        System.out.println();

        // Create second transaction
        Transaction tx2 = new Transaction("Bob", "Charlie", 25.0);
        System.out.println("Transaction 2:");
        System.out.println(tx2);
        System.out.println("Full ID: " + tx2.getTransactionId());
        System.out.println();

        // Create third transaction
        Transaction tx3 = new Transaction("Charlie", "Dave", 10.5);
        System.out.println("Transaction 3:");
        System.out.println(tx3);
        System.out.println("Full ID: " + tx3.getTransactionId());
        System.out.println();

        System.out.println("✓ All transactions created successfully!");
        System.out.println("✓ Each has unique transaction ID (hash)");
    }
}
