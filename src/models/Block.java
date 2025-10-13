package models;

import utils.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

    // Block Header
    private int index;                              // Position in blockchain
    private long timestamp;                         // When block was created
    private String hash;                            // This block's hash
    private String previousHash;                    // Previous block's hash
    private int nonce;                              // Number used once (for mining)

    // Block Body
    private List<Transaction> transactions;         // List of transactions in this block
    private String merkleRoot;                      // Root of Merkle tree (transaction fingerprint)

    /**
     * Constructor - Create a new block
     */
    public Block(int index, String previousHash, List<Transaction> transactions) {
        this.index = index;
        this.previousHash = previousHash;
        this.transactions = new ArrayList<>(transactions); // Create copy of transactions
        this.timestamp = new Date().getTime();
        this.nonce = 0; // Start from 0
        this.merkleRoot = calculateMerkleRoot();
        this.hash = calculateHash(); // Calculate initial hash
    }

    /**
     * Calculate hash for this block
     * Combines all block data into one fingerprint
     */
    public String calculateHash() {
        String data = Integer.toString(index) +
                previousHash +
                Long.toString(timestamp) +
                Integer.toString(nonce) +
                merkleRoot;
        return StringUtil.applySha256(data);
    }

    /**
     * Mine this block using Proof-of-Work
     * Keep trying different nonce values until hash starts with required zeros
     * @param difficulty - Number of leading zeros required (e.g., 4 means "0000...")
     */
    public void mineBlock(int difficulty) {
        // Create target string (e.g., "0000" for difficulty 4)
        String target = StringUtil.getDifficultyString(difficulty);

        System.out.println("=".repeat(70));
        System.out.println("⛏️  MINING BLOCK #" + index + " (Difficulty: " + difficulty + ")");
        System.out.println("=".repeat(70));
        System.out.println("Target: Hash must start with " + target);
        System.out.println("Transactions in block: " + transactions.size());
        System.out.println();

        long startTime = System.currentTimeMillis();

        // Keep incrementing nonce until we find valid hash
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();

            // Print progress every 10000 attempts
            if (nonce % 10000 == 0) {
                System.out.println("[MINING] Trying nonce: " + nonce + " | Hash: " + hash.substring(0, 10) + "...");
            }
        }

        long endTime = System.currentTimeMillis();
        double miningTime = (endTime - startTime) / 1000.0;

        // Block successfully mined!
        System.out.println();
        System.out.println("✅✅✅ BLOCK MINED SUCCESSFULLY! ✅✅✅");
        System.out.println("━".repeat(70));
        System.out.println("📦 Block #" + index + " Details:");
        System.out.println("   Nonce Found:     " + nonce);
        System.out.println("   Final Hash:      " + hash);
        System.out.println("   Mining Time:     " + miningTime + " seconds");
        System.out.println("   Total Attempts:  " + nonce);
        System.out.println("━".repeat(70));
        System.out.println();
    }

    /**
     * Calculate Merkle Root from transactions
     * This creates a single hash representing all transactions
     */
    private String calculateMerkleRoot() {
        // If no transactions, return "0"
        if (transactions == null || transactions.isEmpty()) {
            return "0";
        }

        // Start with transaction IDs as leaves of tree
        List<String> treeLayer = new ArrayList<>();
        for (Transaction transaction : transactions) {
            treeLayer.add(transaction.getTransactionId());
        }

        // Build tree by hashing pairs until we have one hash
        while (treeLayer.size() > 1) {
            List<String> newTreeLayer = new ArrayList<>();

            // Hash pairs of nodes
            for (int i = 0; i < treeLayer.size(); i += 2) {
                if (i + 1 < treeLayer.size()) {
                    // Hash pair
                    String combined = treeLayer.get(i) + treeLayer.get(i + 1);
                    newTreeLayer.add(StringUtil.applySha256(combined));
                } else {
                    // Odd number: hash with itself
                    String combined = treeLayer.get(i) + treeLayer.get(i);
                    newTreeLayer.add(StringUtil.applySha256(combined));
                }
            }
            treeLayer = newTreeLayer;
        }

        // Return root of tree
        return treeLayer.get(0);
    }

    // Getter methods
    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public int getNonce() {
        return nonce;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    /**
     * String representation for printing
     */
    @Override
    public String toString() {
        return "Block #" + index + " [" +
                "hash=" + hash.substring(0, 16) + "..." +
                ", prevHash=" + previousHash.substring(0, 16) + "..." +
                ", txCount=" + transactions.size() +
                ", nonce=" + nonce +
                "]";
    }
}
