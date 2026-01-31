package com.datastruct;

public class HuffmanNode extends BTNode<Integer, Character>{
    private MyArrayList<String> codeList;

    public HuffmanNode(int freq, char letter, HuffmanNode node1, HuffmanNode node2) {
        super(freq, letter);
        setLlink(node1);
        setRlink(node2);
    }

    public MyArrayList<String> getHuffmanCodes(HuffmanNode root, int n) {
        String s = " ";
        codeList = new MyArrayList<String>(n);
        printCode(root, s);
        return codeList;
    } 

    //print code (deretan bit)
    public void printCode(HuffmanNode root, String s) {
        if (root.getLlink() == null && root.getRlink() == null && Character.isLetter(root.getData())) {
            //System.out.println(" " + root.data + "    |  " + s);
            codeList.add(root.getData() + " " + s);
            return;
        }
        printCode((HuffmanNode)root.getLlink(), s + "1");
        printCode((HuffmanNode)root.getRlink(), s + "0");
    }
}
