public class Node {


    int degree = 0; // the number of children of the node
    Node previous; // previous pointer of this node
    Node next; // next pointer of this node
    Node parent; // the parent of this node
    Node child; // points to the child of this node
    Boolean childCut = null; // chidcut value of this node
    String key; // the key of this node which is hashtag name in our case
    int valueCount; // this is the frequency of the particular hashtag


    /**
     *  constructor to return the new object of Node class
     * @param key : hashtag name in our case
     * @param val : frequency of the hashtag
     */
    public Node(String key, int val) {
        this.key = key;
        this.valueCount = val;
        this.next = this;
        this.previous = this;
        this.parent = null;
    }
}
