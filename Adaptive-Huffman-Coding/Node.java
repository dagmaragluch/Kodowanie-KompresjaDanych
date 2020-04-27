public class Node {

    public NodeType nodeType;
    public Node leftChild;
    public Node rightChild;
    public Node parent;
    private int weight;
    private int index;
    private int value;

    public enum NodeType {
        NYT,
        LEAF,
        INTERNAL,
        ROOT
    }


    //internal Node
    public Node(Node parent, Node leftChild, Node rightChild, int weight, int index) {
        this.parent = parent;
//        this.leftChild = leftChild;
//        this.rightChild = rightChild;
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
    public Node(Node parent, int index, int value) {
        this.parent = parent;
        this.weight = 1;
        this.index = index;
        this.value = value;
        this.nodeType = NodeType.LEAF;
    }

//    //Root Node
//    public Node(Node leftChild, Node rightChild, int weight, int index) {
//        this.parent = null;
//        this.leftChild = leftChild;
//        this.rightChild = rightChild;
//        this.weight = weight;
//        this.index = index;
//        this.nodeType = NodeType.ROOT;
//    }


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

    public int getValue() {
        return value;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setValue(int value) {
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
            case ROOT:
                return "ROOT: index: " + this.index + " weight: " + this.weight;    // + " left: " + this.leftChild.getIndex() + " right: " + this.rightChild.getIndex();
            default:
                throw new IllegalStateException("Unexpected value: " + this.nodeType);
        }
    }


}
