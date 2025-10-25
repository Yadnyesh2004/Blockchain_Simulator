package blockchain;

import models.Block;
import models.Transaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Blockchain class - Core implementation of a blockchain data structure
 *
 * This class manages the entire blockchain, including:
 * - Maintaining the chain of blocks
 * - Managing pending transactions
 * - Mining new blocks with proof-of-work
 * - Validating blockchain integrity
 */
public class Blockchain {

    // List to store all blocks in the blockchain (the chain itself)
    private List<Block> chain;

    // Temporary storage for transactions waiting to be added to a block
    private List<Transaction> pendingTransactions;

    // Mining difficulty - number of leading zeros required in block hash
    // Higher difficulty = more computational work required
    private int difficulty;

    /**
     * Constructor - Initialize blockchain with specified difficulty
     *
     * @param difficulty The mining difficulty (e.g., 4 means hash must start with "0000")
     */
    public Blockchain(int difficulty) {
        // Initialize empty chain
        this.chain = new ArrayList<>();

        // Initialize empty pending transactions list
        this.pendingTransactions = new ArrayList<>();

        // Set mining difficulty
        this.difficulty = difficulty;

        // Create the first block (genesis block) to start the chain
        createGenesisBlock();
    }

    /**
     * Create the first block (Genesis Block)
     *
     * The genesis block is special - it has no previous block to reference
     * It serves as the foundation/anchor of the entire blockchain
     */
    private void createGenesisBlock() {
        System.out.println("[INFO] Creating Genesis Block...");

        // Genesis block has no transactions
        List<Transaction> genesisTransactions = new ArrayList<>();

        // Create genesis block with:
        // - Index 0 (first block)
        // - Previous hash "0" (no previous block exists)
        // - Empty transaction list
        Block genesisBlock = new Block(0, "0", genesisTransactions);

        // Mine the genesis block to give it a valid hash
        genesisBlock.mineBlock(difficulty);

        // Add genesis block to the chain
        chain.add(genesisBlock);

        System.out.println("✓ Genesis Block Created!\n");
    }

    /**
     * Get the latest block in chain
     *
     * This is needed when creating a new block, as each block must
     * reference the hash of the previous block
     *
     * @return The most recently added block
     */
    public Block getLatestBlock() {
        // Return the last block in the chain
        return chain.get(chain.size() - 1);
    }

    /**
     * Add transaction to pending transactions
     *
     * Transactions are not immediately added to the blockchain.
     * They wait in a "pending" state until a miner includes them in a block.
     *
     * @param transaction The transaction to add to pending pool
     */
    public void addTransaction(Transaction transaction) {
        // Add to pending transactions list
        pendingTransactions.add(transaction);

        System.out.println("[+] Transaction added: " + transaction);
    }

    /**
     * Mine pending transactions into a new block
     *
     * This is the core mining process:
     * 1. Takes all pending transactions
     * 2. Creates a new block containing them
     * 3. Performs proof-of-work to find valid hash
     * 4. Adds the mined block to the chain
     * 5. Clears the pending transactions
     */
    public void minePendingTransactions() {
        // Check if there are any transactions to mine
        if (pendingTransactions.isEmpty()) {
            System.out.println("[WARNING] No pending transactions to mine!");
            return;
        }

        // Print mining header
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MINING NEW BLOCK");
        System.out.println("=".repeat(60));
        System.out.println("[INFO] Transactions in block: " + pendingTransactions.size());

        // Create new block with:
        // - Index: next position in chain (current size)
        // - Previous hash: hash of the latest block (creates the "chain" link)
        // - Transactions: copy of all pending transactions
        Block newBlock = new Block(
                chain.size(),                              // Index of new block
                getLatestBlock().getHash(),                // Link to previous block
                new ArrayList<>(pendingTransactions)       // Copy of pending transactions
        );

        // Perform proof-of-work mining
        // This finds a nonce that makes the block hash start with required zeros
        newBlock.mineBlock(difficulty);

        // Add the successfully mined block to the blockchain
        chain.add(newBlock);

        // Clear pending transactions since they're now in a block
        pendingTransactions.clear();

        System.out.println("[SUCCESS] Block #" + newBlock.getIndex() + " added to chain!");
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Validate blockchain integrity
     *
     * Checks three critical properties for each block:
     * 1. Hash integrity - hash matches the calculated hash
     * 2. Chain linkage - previous hash matches actual previous block's hash
     * 3. Proof-of-work - hash meets difficulty requirement
     *
     * If any block fails validation, the entire chain is invalid
     *
     * @return true if blockchain is valid, false otherwise
     */
    public boolean isChainValid() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("BLOCKCHAIN VALIDATION");
        System.out.println("=".repeat(60));

        // Start from block 1 (skip genesis block as it has no previous block)
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            System.out.println("[CHECK] Validating Block #" + i + "...");

            // Validation Check 1: Hash Integrity
            // Recalculate the hash and compare with stored hash
            // If they don't match, the block data has been tampered with
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("  ❌ Current hash is invalid!");
                return false;
            }

            // Validation Check 2: Chain Linkage
            // Verify that the current block's previousHash matches the actual previous block's hash
            // This ensures the chain hasn't been broken or reordered
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                System.out.println("  ❌ Previous hash doesn't match!");
                return false;
            }

            // Validation Check 3: Proof-of-Work
            // Create the target string (e.g., "0000" for difficulty 4)
            // Verify the hash starts with the required number of zeros
            String target = new String(new char[difficulty]).replace('\0', '0');
            if (!currentBlock.getHash().substring(0, difficulty).equals(target)) {
                System.out.println("  ❌ Proof-of-work not satisfied!");
                return false;
            }

            System.out.println("  ✓ Block #" + i + " is valid!");
        }

        System.out.println("\n✓✓✓ BLOCKCHAIN IS VALID! ✓✓✓");
        System.out.println("=".repeat(60) + "\n");
        return true;
    }

    /**
     * Export blockchain to JSON file
     *
     * Saves the entire blockchain state to a JSON file for:
     * - Persistence (saving blockchain state)
     * - Analysis (examining blockchain data)
     * - Sharing (transferring blockchain to other systems)
     *
     * @param filename The path/name of the output JSON file
     */
    public void exportToJSON(String filename) {
        try {
            // Create Gson object with pretty printing for readable JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Create file writer for output
            FileWriter writer = new FileWriter(filename);

            // Create wrapper object containing all blockchain data
            BlockchainData data = new BlockchainData();
            data.chain = this.chain;              // All blocks
            data.difficulty = this.difficulty;     // Mining difficulty
            data.chainLength = this.chain.size();  // Number of blocks
            data.isValid = this.isChainValid();    // Validation status

            // Convert to JSON and write to file
            gson.toJson(data, writer);
            writer.close();

            System.out.println("[SUCCESS] Blockchain exported to: " + filename);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to export blockchain");
            e.printStackTrace();
        }
    }

    /**
     * Print blockchain summary
     *
     * Displays a human-readable summary of the entire blockchain
     * including statistics and details of each block
     */
    public void printChain() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("BLOCKCHAIN SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("Total Blocks: " + chain.size());
        System.out.println("Difficulty: " + difficulty);
        System.out.println();

        // Iterate through all blocks and print details
        for (Block block : chain) {
            System.out.println(block);  // Uses Block's toString() method
            System.out.println("  Transactions: " + block.getTransactions().size());
            System.out.println("  Hash: " + block.getHash());
            System.out.println();
        }
    }

    // Getter methods for accessing blockchain data
    public List<Block> getChain() { return chain; }
    public int getDifficulty() { return difficulty; }

    /**
     * Inner class for JSON export structure
     *
     * This wrapper class organizes blockchain data for JSON export,
     * including metadata like validity status and chain length
     */
    private static class BlockchainData {
        List<Block> chain;      // The actual chain of blocks
        int difficulty;         // Mining difficulty setting
        int chainLength;        // Total number of blocks
        boolean isValid;        // Whether chain passes validation
    }
}