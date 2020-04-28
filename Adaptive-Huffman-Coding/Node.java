public class Node {

    public NodeType nodeType;
    public Node leftChild;
    public Node rightChild;
    public Node parent;
    private int weight;
    private int index;
    private char value;

    public enum NodeType {
        NYT,
        LEAF,
        INTERNAL
    }


    //internal Node
    public Node(Node parent, Node leftChild, Node rightChild, int weight, int index) {
        this.parent = parent;
        this.weight = weight;
        this.index = index;
        this.nodeType = NodeType.INTERNAL;
    }

    //NYT Node
    public Node(Node parent, int index) {
        this.parent = parent;
        this.weight = 0;
        this.index = index;
        this.nodeType = NodeType.NYT;
    }

    //Leaf Node
    public Node(Node parent, int index, char value) {
        this.parent = parent;
        this.weight = 1;
        this.index = index;
        this.value = value;
        this.nodeType = NodeType.LEAF;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public Node getParent() {
        return parent;
    }

    public int getWeight() {
        return weight;
    }

    public int getIndex() {
        return index;
    }

    public char getValue() {
        return value;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public void increaseWeight() {
        this.weight++;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public String toString() {
        switch (this.nodeType) {
            case LEAF:
                return "LEAF: index: " + this.index + " weight: " + this.weight + " value: " + this.value;
            case NYT:
                    return "NYT: index: " + this.index + " weight: " + this.weight;
            case INTERNAL:
                return "INTERNAL: index: " + this.index + " weight: " + this.weight;    // + " parent: " + (this.parent != null ? this.parent.getIndex() : "NULL") + " left: " + this.leftChild.getIndex() + " right: " + this.rightChild.getIndex();
            default:
                throw new IllegalStateException("Unexpected value: " + this.nodeType);
        }
    }


}
