package utils;

import java.security.MessageDigest;

/**
 * StringUtil - Utility class for blockchain cryptographic operations
 *
 * This class provides essential methods for:
 * 1. Hashing data using SHA-256 algorithm
 * 2. Generating difficulty target strings for proof-of-work mining
 *
 * @author Your Name
 * @version 1.0
 */
public class StringUtil {

    /**
     * Applies SHA-256 cryptographic hash function to an input string.
     *
     * SHA-256 (Secure Hash Algorithm 256-bit) is a one-way cryptographic function
     * that converts input of any size into a fixed 256-bit (32-byte) hash value,
     * typically rendered as a 64-character hexadecimal string.
     *
     * Key Properties:
     * - Deterministic: Same input always produces same output
     * - One-way: Cannot reverse the hash to get original input
     * - Collision-resistant: Nearly impossible to find two inputs with same hash
     * - Avalanche effect: Small input change completely changes output
     *
     * @param input The string to be hashed (can be any length)
     * @return A 64-character hexadecimal string representing the SHA-256 hash
     * @throws RuntimeException if SHA-256 algorithm is not available (shouldn't happen)
     *
     * Example:
     *   applySha256("Hello") → "185f8db32271fe25f561a6fc938b2e264306ec304eda518007d1764826381969"
     */
    public static String applySha256(String input) {
        try {
            // Step 1: Get an instance of the SHA-256 message digest algorithm
            // MessageDigest is Java's cryptographic hashing utility class
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Step 2: Convert input string to bytes and apply SHA-256 hashing
            // - input.getBytes("UTF-8"): Converts string to byte array using UTF-8 encoding
            // - digest.digest(): Performs the actual SHA-256 hashing operation
            // - Result: 32-byte array (256 bits ÷ 8 bits/byte = 32 bytes)
            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            // Step 3: Convert byte array to hexadecimal string representation
            // Each byte (8 bits) becomes 2 hex characters (4 bits each)
            // So 32 bytes → 64 hex characters
            StringBuilder hexString = new StringBuilder();

            // Iterate through each byte in the hash
            for (byte b : hash) {
                // Convert byte to hex string
                // 0xff & b: Masks the byte to handle negative values (converts to 0-255 range)
                // Integer.toHexString(): Converts integer to hexadecimal string
                String hex = Integer.toHexString(0xff & b);

                // If hex is single character (0-9, a-f), prepend '0' for padding
                // Example: byte value 5 → "5" → "05"
                // This ensures each byte always produces exactly 2 hex characters
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Return the complete 64-character hexadecimal hash string
            return hexString.toString();

        } catch (Exception e) {
            // SHA-256 is a standard algorithm and should always be available
            // If this fails, it indicates a serious JVM configuration issue
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a difficulty target string for proof-of-work mining.
     *
     * In blockchain mining, the "difficulty" determines how hard it is to mine a block.
     * A valid block hash must start with a certain number of leading zeros.
     * This method creates that target pattern.
     *
     * How Mining Works:
     * 1. Miner creates a block with data + nonce (random number)
     * 2. Calculates hash of the block
     * 3. Checks if hash starts with required number of zeros (difficulty)
     * 4. If not, increment nonce and try again (proof-of-work)
     * 5. Repeat until valid hash is found
     *
     * @param difficulty The number of leading zeros required in a valid hash
     *                   Higher difficulty = more zeros = harder to mine = more secure
     * @return A string containing 'difficulty' number of zeros
     *
     * Examples:
     *   getDifficultyString(1) → "0"          (easy: hash must start with "0")
     *   getDifficultyString(3) → "000"        (medium: hash must start with "000")
     *   getDifficultyString(5) → "00000"      (hard: hash must start with "00000")
     *
     * Real-world context:
     * - Bitcoin difficulty adjusts to maintain ~10 minute block times
     * - As of 2024, Bitcoin difficulty requires ~19-20 leading zeros
     * - Each additional zero makes mining ~16x harder (16 possible hex values)
     */
    public static String getDifficultyString(int difficulty) {
        // Create a character array of size 'difficulty', filled with null characters ('\0')
        // Then replace all null characters with '0'
        // Result: A string of zeros with length equal to difficulty
        return new String(new char[difficulty]).replace('\0', '0');
    }
}