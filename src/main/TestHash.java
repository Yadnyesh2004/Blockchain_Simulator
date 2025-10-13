package main;

import utils.StringUtil;

public class TestHash {
    public static void main(String[] args) {
        String input = "Hello Blockchain";
        String hash = StringUtil.applySha256(input);
        System.out.println("Input: " + input);
        System.out.println("Hash: " + hash);
        
        // Test difficulty string
        System.out.println("Difficulty 4: " + StringUtil.getDifficultyString(4));
    }
}
