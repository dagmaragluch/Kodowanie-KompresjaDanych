import java.util.*;

public class HuffmanTree {

    public Node root;
    public Node NYT;
    public final int n = 256;   //number of letters in the our alphabet (8-bits strings of ASCII codes)
    public final int Z = 2 * n - 1;   //max number of nodes
    public int actualHighestIndex = Z;
    public StringBuilder codeNYT = new StringBuilder();

    private List<Node> nodes = new ArrayList<>();       //list of all nodes
    private Map<Character, Node> leafs = new HashMap<>();       //map of leaf (by value)

    //init HuffmanTree
    public HuffmanTree() {
        this.root = new Node(null, Z);
        actualHighestIndex--;
        this.NYT = root;
        nodes.add(root);
    }

    /**
     * insert first occurrence of value into tree:
     * - replace NYT with 3 nodes: new NYT, internal node and leaf
     * - update tree (if necessary)
     */

    public void insertNewValue(char value) {

        int oldIndex = NYT.getIndex();

        Node newNYT = new Node(NYT, oldIndex - 2);
        Node newLeaf = new Node(NYT, oldIndex - 1, value);
        nodes.add(newNYT);
        nodes.add(newLeaf);
        leafs.put(value, newLeaf);
        actualHighestIndex--;

        Node oldNYT = NYT;
        NYT.nodeType = Node.NodeType.INTERNAL;
        NYT.leftChild = newNYT;
        NYT.rightChild = newLeaf;
        NYT = newNYT;

        updateParentChain(newLeaf);
        updateTree(oldNYT);
    }

    /**
     * insert value, that is already in tree:
     * - increase weight in leaf with this value
     * - update tree
     */
    public void insertOldValue(char value) {
        Node leafToUpdate = leafs.get(value);
        leafToUpdate.increaseWeight();
        updateParentChain(leafToUpdate);
        updateTree(leafToUpdate);
    }

    /**
     * if any node break the "sibling property"
     * recursively rebuilds the tree
     */
    public void updateTree(Node node) {
        sortNodesByIndex();
        while (node != root) {
            Node nodeToSwap = isConflictWithNode(node);
            if (nodeToSwap != null) {
                swapNodes(nodeToSwap, node);
            }
            node = node.parent;
        }
        updateWeights();
    }

    /**
     * check, if is conflict with node:
     * return null if no
     * otherwise return node, that must be swap
     */
    public Node isConflictWithNode(Node node) {
        int index = node.getIndex();
        int weight = node.getWeight();

        for (Node n : nodes) {
            if (n.getIndex() > index && n.getWeight() < weight) {
                return n;
            }
        }
        return null;
    }

    /**
     * swap 2 nodes in tree,
     * reserving indexes and parent nodes
     */
    public void swapNodes(Node newNodePosition, Node oldNodeGettingSwapped) {
        int newIndex = newNodePosition.getIndex();
        int oldIndex = oldNodeGettingSwapped.getIndex();

        // keep track of parents of both nodes getting swapped.
        Node oldParent = oldNodeGettingSwapped.parent;
        Node newParent = newNodePosition.parent;

        // need to know if nodes were left or right child.
        boolean oldNodeWasOnRight, newNodePositionOnRight;
        oldNodeWasOnRight = newNodePositionOnRight = false;

        if (newNodePosition.parent.rightChild == newNodePosition) {
            newNodePositionOnRight = true;
        }
        if (oldNodeGettingSwapped.parent.rightChild == oldNodeGettingSwapped) {
            oldNodeWasOnRight = true;
        }
        if (newNodePositionOnRight) {
            newParent.rightChild = oldNodeGettingSwapped;
        } else {
            newParent.leftChild = oldNodeGettingSwapped;
        }
        if (oldNodeWasOnRight) {
            oldParent.rightChild = newNodePosition;
        } else {
            oldParent.leftChild = newNodePosition;
        }

        // update the parent pointers and index
        oldNodeGettingSwapped.parent = newParent;
        newNodePosition.parent = oldParent;
        oldNodeGettingSwapped.setIndex(newIndex);
        newNodePosition.setIndex(oldIndex);
    }


    public boolean isValueInTree(char value) {
        return leafs.containsKey(value);
    }

    /**
     * increase by 1 all weights in track
     * from new or update leaf to root
     */
    public void updateParentChain(Node newLeaf) {
        Node actualNode = newLeaf;

        do {
            actualNode.getParent().increaseWeight();
            actualNode = actualNode.getParent();
        } while (actualNode.getParent() != null);
    }

    public void sortNodesByIndex() {
        nodes.sort(Comparator.comparing(Node::getIndex));
    }

    /**
     * update all weights in tree:
     * set parent weight as sum of weights their children's
     */
    public void updateWeights() {
        for (Node n : nodes) {
            if (n.nodeType == Node.NodeType.INTERNAL)
                n.setWeight(n.leftChild.getWeight() + n.rightChild.getWeight());
        }
    }

    /** Functions useful in encoding/decoding */


    /**
     * convert char symbol to ASCII code (binary string)
     */
    public String generateOriginSymbolCode(char symbol) {
        return String.format("%08d", Integer.parseInt(Integer.toBinaryString(symbol)));
    }

    /**
     * convert binary string to char symbol
     */
    public char getSymbolFromASCIICode(String codeASCII) {
        int decimalValueOfSymbol = Integer.parseInt(codeASCII, 2);    //get decimal value
        return (char) decimalValueOfSymbol;
    }


    /**
     * generate code of symbol using Huffman Tree:
     * - start from leaf with symbol and go to root
     * - if node is right child add 1 to code, if left add 0
     * - because we go from leaf to root, need to reverse string-code
     */
    public String generateEncodingSymbolCode(char symbol) {
        Node actualNode = leafs.get(symbol);
        Node parent;
        StringBuilder encodingSymbolCode = new StringBuilder();

        while (actualNode.parent != null) {
            parent = actualNode.parent;
            if (parent.leftChild == actualNode) {
                encodingSymbolCode.append(0);
            } else {
                encodingSymbolCode.append(1);
            }
            actualNode = parent;
        }
        encodingSymbolCode.reverse();

        return encodingSymbolCode.toString();
    }

    /**
     * generate code NYT: add next zeros
     */
    public String generateCodeNYT() {
        String codeNYTstr = codeNYT.toString();
        codeNYT.append("0");
        return codeNYTstr;
    }

    public boolean isEmpty() {
        return root == NYT;
    }


}
