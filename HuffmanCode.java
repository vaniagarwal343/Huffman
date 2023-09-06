// Vani Agarwal
// CSE 123
// P3: Huffman
// 2nd Jun 2023
// This class is a Huffman data compressor 

import java.util.*;
import java.io.*;

// This class is the HuffmanCode class. It is to be used
// in the compression of data. 
// It compresses files into smaller sizes based on the
// frequence of characters in the line. 

public class HuffmanCode {
    // TODO: Your Code Here
    // The overall root node of the tree
    private HuffmanNode root;

    // This class is a HuffmanNode in the HuffmanCode class 
    // Implements comparable and uses frequencies to order 
    // nodes. 
    private static class HuffmanNode implements Comparable<HuffmanNode> { 
        public Integer letter;
        public HuffmanNode left;
        public HuffmanNode right; 
        public int frequency; 


    // This class is a HuffmanNode in the HuffmanCode class 
    // Uses frequencies to order nodes. 
    // Paramater: 
    // frequency- the combined frequency of two child nodes with letters
    public HuffmanNode(int frequency) { 
        this.letter = -1; 
        this.frequency = frequency; 
        this.left = null;
        this.right = null; 
    }

    // This class is a HuffmanNode in the HuffmanCode class 
    // Uses frequencies to order nodes
    // Parameters: 
    // 1. letter- the letter assigned to this node
    // 2. frequency- the frequency with which the letter appears
    public HuffmanNode(int letter, int frequency) { 
        this.letter = letter; 
        this.frequency = frequency; 
        this.left = null; 
        this.right = null; 
    }

    // Method: compareTo
    // Compares two HuffmanNodes based on their frequency so that the priority queue 
    // can accurately order them 
    // Paramater: 
    // other HuffmanNode to compare
    public int compareTo(HuffmanNode other) { 
        if(this.frequency > other.frequency) { 
            return 1; 
        } else if (this.frequency == other.frequency) { 
            return 0;
        }
        else return -1;
        }
    }

    // Method: toString
    // Return: a string representation of the tree. 
    // The elements are returned in ascending order, enclosed in curly braces
    public String toString() { 
        String str = toString(this.root);
        return str;
    }

    // Method: toString
    // Performs an in-order traversal of the supplied tree, so that elements are returned in 
    // ascending order
    // Parameter: node at the root of the tree to be traversed 
    // Return: a string representation of the tree rooted at the given node.
    private String toString(HuffmanNode node) { 
        if (node == null) { 
            return "";
        }
        return (toString(node.left) + String.valueOf(node.frequency) + ", " + toString(node.right));
    }

    // HuffmanCode constructor: 
    // Constructs a Huffman coding tree using the given array of frequencies where the elements  
    // of the array are the number of occurrences of the character with the ASCII value of that 
    // index.
    // Paramater:
    // frequencies: array of frequencies
    public HuffmanCode(int[] frequencies) { 
        PriorityQueue<HuffmanNode> mergingQueue = new PriorityQueue<>();
        for (int i = 0; i < frequencies.length; i++) { 
            if (frequencies[i] != 0) { 
                HuffmanNode letter = new HuffmanNode((char)(i), frequencies[i]);
                mergingQueue.add(letter);
            }
        }
        constructTree(mergingQueue);
        this.root = mergingQueue.remove();
    }

    // Method: constructTree
    // A helper method that takes a priority queue of nodes ordered by frequency and builds a tree
    // Paramater: 
    // q: The priority queue of nodes ordered by frquency;
    public void constructTree(PriorityQueue<HuffmanNode> q) { 
        if (q.size() == 0) { 
            return; 
        }
        if (q.size() == 1) { 
            return;
        }
        HuffmanNode node1 = q.remove();
        HuffmanNode node2 = q.remove();
        HuffmanNode newNode = new HuffmanNode(node1.frequency + node2.frequency);
        newNode.left = node1; 
        newNode.right = node2; 
        q.add(newNode);
        constructTree(q);
    }

    // Method: save
    // This method should store the current Huffman Code to the given output stream in 
    // the standard format
    public void save(PrintStream output) { 
        saveHelper("", output, this.root);
    }

    // Method: saveHelper
    // helper method for the save method
    // Parameter: 
    // 1. val: the string containing information on 
    // how to traverse the leaf node 
    // 2. output: the output stream to be printed to 
    // 3. node: the HuffmanNode with data
    public void saveHelper(String val, PrintStream output, HuffmanNode node) { 
        if(node.left == null && node.right == null) { 
            output.println(node.letter);
            output.println(val);
            return; 
        }
        if (node.left != null) { 
            saveHelper(val + "0", output, node.left);
        }
        if (node.right != null) { 
            saveHelper(val + "1", output, node.right);
        }
    }

    // Constructor: HuffmanCode
    // Constructs a HuffmanTree from a file that contains the description of a tree
    // where the file will contain a pair of lines to represent each character in the Huffman code
    // Parameter: 
    // input: the input scanner 
    // Exceptions thrown: IllegalArgumentException
    public HuffmanCode(Scanner input) throws IllegalArgumentException {
        this.root = new HuffmanNode(0);

        while (input.hasNextLine()) {
            int letter = Integer.parseInt(input.nextLine());
            String val = input.nextLine();
            if (!buildCode(root, val, letter)) {
                throw new IllegalArgumentException();
            }
        }
    }

    // method: buildCode
    // A helper method for HuffmanCode
    // Parameters: 
    // 1. tree: the original root node to start with 
    // 2. bal: the bits containing information on how to traverse through the tree 
    // 3. letter: the letter being coded
    private boolean buildCode(HuffmanNode tree, String value, Integer letter) {
        String currChar = "" + value.charAt(0);
        if (value.length() == 1) {
            if (currChar.equals("0")) {
                HuffmanNode newNode = new HuffmanNode(letter, 1);
                tree.left = newNode;
                return true;
            }
            if (currChar.equals("1")) {
                HuffmanNode newNode = new HuffmanNode(letter, 1);
                tree.right = newNode;
                return true;
            }
        }
        if (currChar.equals("0")) {
            if (tree.left == null) {
                HuffmanNode tempNode = new HuffmanNode(0);
                tree.left = tempNode;
            }
            return buildCode(tree.left, value.substring(1), letter);
        }
        if (currChar.equals("1")) {
            if (tree.right == null) {
                HuffmanNode tempNode = new HuffmanNode(0);
                tree.right = tempNode;
            }
            return buildCode(tree.right, value.substring(1), letter);
        }
        return false;
    }

    // Method: translate
    // This method reads individual bits from the input stream and writes the corresponding
    // characters to the output. It stops reading when the BitInputStream is empty.
    // Parameters: 
    // 1. the binary input given
    // 2. the output stream 
    public void translate(BitInputStream input, PrintStream output) {
        HuffmanNode tempNode = this.root;
        boolean lastCharRead = false;
    
        while (input.hasNextBit() || !lastCharRead) {
            if (tempNode.right == null && tempNode.left == null) {
                int currChar = tempNode.letter;
                output.write(currChar);
                tempNode = this.root;
                lastCharRead = true;
            }
        
            if (input.hasNextBit()) {
                int bit = input.nextBit();
                if (bit == 0) {
                    tempNode = tempNode.left;
                } else {
                    tempNode = tempNode.right;
                }
            }
        }
        if (tempNode.right == null && tempNode.left == null) {
                int currChar = tempNode.letter;
                output.write(currChar);
                tempNode = this.root;
                lastCharRead = true;
            }
        
        if (input.hasNextBit()) {
            int bit2 = input.nextBit();
            if (bit2 == 0) {
                tempNode = tempNode.left;
            } else {
                tempNode = tempNode.right;
            }
        }      
    }
}

