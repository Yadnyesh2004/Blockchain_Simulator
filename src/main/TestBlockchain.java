package main;

import blockchain.Blockchain;
import models.Transaction;

public class TestBlockchain {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain(4); // Set your difficulty (3 = faster, 4 = harder)
        
        // Add transactions for first block
        blockchain.addTransaction(new Transaction("Alice", "Bob", 50.0));
        blockchain.addTransaction(new Transaction("Bob", "Charlie", 25.0));
        blockchain.minePendingTransactions();
        
        // Add transactions for second block
        blockchain.addTransaction(new Transaction("Charlie", "Dave", 10.0));
        blockchain.addTransaction(new Transaction("Dave", "Eve", 7.5));
        blockchain.minePendingTransactions();

        blockchain.printChain();
        blockchain.isChainValid();
        blockchain.exportToJSON("output/blockchain.json"); // Creates JSON output
    }
}
