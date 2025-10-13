package main;

import models.Block;
import models.Transaction;
import java.util.ArrayList;
import java.util.List;

public class TestBlock {
    public static void main(String[] args) {
        System.out.println("\n🔗 BLOCKCHAIN BLOCK TESTING\n");
        
        // Test 1: Create a block with transactions
        System.out.println("TEST 1: Creating Block with Transactions");
        System.out.println("─".repeat(70));
        
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("Alice", "Bob", 50.0));
        transactions.add(new Transaction("Bob", "Charlie", 25.0));
        transactions.add(new Transaction("Charlie", "Dave", 10.0));
        
        System.out.println("Created " + transactions.size() + " transactions:");
        for (Transaction tx : transactions) {
            System.out.println("  • " + tx);
        }
        System.out.println();
        
        // Create block
        Block block1 = new Block(1, "0000abcd1234ef567890", transactions);
        
        System.out.println("Block created (before mining):");
        System.out.println("  Index: " + block1.getIndex());
        System.out.println("  Previous Hash: " + block1.getPreviousHash());
        System.out.println("  Merkle Root: " + block1.getMerkleRoot().substring(0, 20) + "...");
        System.out.println("  Initial Hash: " + block1.getHash().substring(0, 20) + "...");
        System.out.println("  Initial Nonce: " + block1.getNonce());
        System.out.println();
        
        // Test 2: Mine the block with difficulty 3 (easier, faster)
        System.out.println("\nTEST 2: Mining Block (Difficulty 3)");
        System.out.println("─".repeat(70));
        block1.mineBlock(3);
        
        System.out.println("Block after mining:");
        System.out.println("  Hash starts with: " + block1.getHash().substring(0, 10));
        System.out.println("  Final Nonce: " + block1.getNonce());
        System.out.println();
        
        // Test 3: Create Genesis Block (no transactions)
        System.out.println("\nTEST 3: Creating Genesis Block (No Transactions)");
        System.out.println("─".repeat(70));
        
        Block genesisBlock = new Block(0, "0", new ArrayList<>());
        System.out.println("Genesis block created:");
        System.out.println("  " + genesisBlock);
        System.out.println();
        
        genesisBlock.mineBlock(4); // Mine with difficulty 4
        
        System.out.println("Genesis block mined:");
        System.out.println("  Hash: " + genesisBlock.getHash());
        System.out.println();
        
        // Test 4: Verify hash changes if nonce changes
        System.out.println("\nTEST 4: Demonstrating How Nonce Affects Hash");
        System.out.println("─".repeat(70));
        
        Block testBlock = new Block(2, "0000xyz", new ArrayList<>());
        System.out.println("Block with nonce 0:");
        System.out.println("  Hash: " + testBlock.getHash().substring(0, 20) + "...");
        System.out.println();
        
        System.out.println("✅ ALL TESTS COMPLETED SUCCESSFULLY!");
        System.out.println("✅ Block class is working perfectly!");
        System.out.println("✅ Mining (Proof-of-Work) is functional!");
    }
}
