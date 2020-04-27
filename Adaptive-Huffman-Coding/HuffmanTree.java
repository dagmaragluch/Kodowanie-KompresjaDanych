import java.util.*;

public class HuffmanTree {

    public Node root;
    public Node NYT;
    public final int n = 256;   //number of letters in the our alphabet (8-bits strings of ASCII codes)
    public final int Z = 2 * n - 1;   //max number of nodes
    public int actualHighestIndex = Z;

    private List<Node> nodes = new ArrayList<>();
    private Map<Integer, Node> nodeByIndex = new HashMap<>();
    private Map<Integer, Node> leafs = new HashMap<>();

    //init HuffmanTree
    public HuffmanTree() {
        this.root = new Node(null, Z);
        actualHighestIndex--;
        this.NYT = root;
        nodes.add(root);
        nodeByIndex.put(root.getIndex(), root);
    }

    public void insertIntoTree(int value) {

        //first occurrence of value
        if (!leafs.containsKey(value)) {
            insertNewValue(value);
        }
        //value is already in Tree
        else {
            insertOldValue(value);
        }
    }

    public Node insertNewValue(int value) {

        int oldIndex = NYT.getIndex();

        Node newNYT = new Node(NYT, oldIndex - 2);
        Node newLeaf = new Node(NYT, oldIndex - 1, value);
        nodes.add(newNYT);
        nodes.add(newLeaf);
        leafs.put(value, newLeaf);
        nodeByIndex.put(newNYT.getIndex(), newNYT);
        nodeByIndex.put(newLeaf.getIndex(), newLeaf);
        actualHighestIndex--;


        Node oldNYT = NYT;
        NYT.nodeType = Node.NodeType.INTERNAL;
        NYT.leftChild = newNYT;
        NYT.rightChild = newLeaf;
        NYT = newNYT;

        updateParentChain(newLeaf);
        sortNodesByIndex();
        updateTree(oldNYT);

        return oldNYT;

    }


    public Node insertOldValue(int value) {
        Node leafToUpdate = leafs.get(value);
        leafToUpdate.increaseWeight();
        updateParentChain(leafToUpdate);
        updateTree(leafToUpdate);

        return leafToUpdate;
    }

    public void updateTree(Node node) {
        sortNodesByIndex();
        while (node != root) {
            Node nodeToSwap = isConflictWithNode(node);
            System.out.println("Before " + isConflictWithNode(node));
            if (nodeToSwap != null) {
                System.out.println("OK " + nodeToSwap.getIndex());
                swapNodes(nodeToSwap, node);

            }
//            node.increment(); // Increment node weight.
            node = node.parent;
        }
        updateWeights();
    }

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


    public void swapNodes(Node newNodePosition, Node oldNodeGettingSwapped) {
        int newIndex = newNodePosition.getIndex();
        int oldIndex = oldNodeGettingSwapped.getIndex();

        // Keep track of parents of both nodes getting swapped.
        Node oldParent = oldNodeGettingSwapped.parent;
        Node newParent = newNodePosition.parent;

        // Need to know if nodes were left or right child.
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
        // Update the parent pointers.
        oldNodeGettingSwapped.parent = newParent;
        newNodePosition.parent = oldParent;
        oldNodeGettingSwapped.setIndex(newIndex);
        newNodePosition.setIndex(oldIndex);


        // Swap the indices of the nodes in order arraylist.
//        order.set(newIndex, oldNodeGettingSwapped);
//        order.set(oldIndex, newNodePosition);
//        updateNodeIndices();
    }


    /******************/

    public void setIndex(Node node) {
        node.setIndex(actualHighestIndex);
        actualHighestIndex--;
    }


    public Node getNodeByIndex(int index) {
        return nodeByIndex.get(index);
    }


    public boolean isValueInTree(int value) {
        for (Node n : nodes) {
            if (n.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public void updateParentChain(Node newLeaf) {
        Node actualNode = newLeaf;

        do {
            actualNode.getParent().increaseWeight();
            actualNode = actualNode.getParent();
        } while (actualNode.getParent() != null);
    }

    public void printTreeFromList() {
        for (Node n : nodes) {
            System.out.println(n.toString());
        }
    }

    public void printTreeFromMap() {
        for (Map.Entry<Integer, Node> entry : nodeByIndex.entrySet()) {
            System.out.println("key: " + entry.getKey().toString() + entry.getValue().toString());
        }
    }


    public void sortNodesByIndex() {
        nodes.sort(Comparator.comparing(Node::getIndex));
    }

    public void updateWeights() {
        for (Node n : nodes) {
            if (n.nodeType == Node.NodeType.INTERNAL)
                n.setWeight(n.leftChild.getWeight() + n.rightChild.getWeight());
        }
    }


}
