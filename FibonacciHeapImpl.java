import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class FibonacciHeapImpl {
    private Node max;

    /**
     * This method handles the insert logic of fibonacci heap.
     * Inserts the new node and returns that node back to the calling function.
     *
     * @param key - hashtag name
     * @param val - frequency
     * @return - the newly inserted node
     */
    public Node insert(String key, int val) {
        Node node = getNewNode(key, val);
        if (max == null) {
            max = node;

        } else {
            node.previous = max;
            node.next = max.next;
            max.next.previous = node;
            max.next = node;

        }
        if (node.valueCount > max.valueCount) {
            /* updates the max node pointer if the
            new node's value is greater than current max.
            */
            max = node;
        }
        return node;
    }


    /**This method meld the nodes. Basically
     * this node melds the top level root nodes into circular doubly linked list
     * and updates the max pointer
     * @param nodeMap : takes a map containing key as degree of node
     * and value as the node itself (these nodes are melded)
     */
    public void meld(Map<Integer, Node> nodeMap) {
        max = null;
        Node node;
        for (Integer key : nodeMap.keySet()) {
            if (max == null) {
                max = nodeMap.get(key);
                node = max;
                node.next = node;
                node.previous = node;
                node.childCut = null;
                node.parent = null;
            } else {
                node = nodeMap.get(key);
                node.childCut = null;
                node.previous = max;
                node.next = max.next;
                max.next.previous = node;
                max.next = node;
                node.parent = null;
            }
            if (node.valueCount > max.valueCount) {
                // updates the max pointer if necessary
                max = node;
            }
        }
    }

    /**
     *  This function melds a list of nodes (joins them in a doubly circular linked list).
     *  Works similar to meld function defined above just the parameter type is different.
     *
     * @param node : Takes input as the list of nodes to be melded
     */
    private void meldNodes(LinkedList<Node> node) {

        Node temp;
        Node updatedMax = max;

        for (int i = 0; i < node.size(); i++) {
            temp = node.get(i);
            temp.childCut = null;
            temp.parent = null;
            temp.previous = max;
            temp.next = max.next;
            max.next.previous = temp;
            max.next = temp;
            if (max.valueCount < temp.valueCount) {
                updatedMax = temp;
            }
        }
        max = updatedMax;
    }

    /**
     *
     * removes the max element from the fibonacci heap,
     * calls a logic to do a pairwise combine of nodes of same degree after remove max.
     * Also, calls the logic to update the max pointer.
     *
     * @return - It returns the removed max node of fibonacci heap
     */

    public Node removeMax() {
        if (max == null) {
            return null;
        }

        Node first;
        Node valToReturn = max;
        Node cur;
        Node next;
        int count = 0;

        /* This map stores key as degree of a node and value as the Node itself.
        It is used for pairwise combine operation */
        Map<Integer, Node> degreeMap = new HashMap<>();

        if (max.child != null) {

            first = max.child;
            max.child = null;
            cur = first;
            while (cur != first || count == 0) {
                next = cur.next;
                cur.parent = null;
                cur.previous = cur;
                cur.next = cur;
                cur.childCut = null;

                // checks if there is an existing node with the same degree in the degreeMap hashmap
                if (degreeMap.containsKey(cur.degree)) {
                    // if there is another node with the same degree, it does a pairwise combine
                    pairWiseCombine(cur, degreeMap.get(cur.degree), degreeMap);
                } else {
                    degreeMap.put(cur.degree, cur);
                }
                cur = next;
                count++;
            }
        }

        cur = max.next;
        while (cur != max) {
            next = cur.next;
            cur.parent = null;
            cur.previous = cur;
            cur.childCut = null;
            cur.next = cur;

            // calls the pairwise combine logic
            if (degreeMap.containsKey(cur.degree)) {
                pairWiseCombine(cur, degreeMap.get(cur.degree), degreeMap);
            } else {
                degreeMap.put(cur.degree, cur);
            }
            cur = next;
        }

        // melds the nodes into circular doubly linked list
        meld(degreeMap);
        valToReturn.next = null;
        valToReturn.previous = null;
        valToReturn.parent = null;
        valToReturn.child = null;

        // returns the max node which was removed from fibonacci heap
        return valToReturn;

    }


    /**
     *  This method does a pairwaise combine of 2 nodes(node1, node2) with the same degree
     * @param temp1 : First node for pairwise combine
     * @param temp2 : Second Node for pairwise combine
     * @param degreeMap : takes a map which contains key as the degree of node and value as the node itself
     */

    private void pairWiseCombine(Node temp1, Node temp2,
                                 Map<Integer, Node> degreeMap) {
        Node nodeMax;
        Node nodeMin;

        if (temp1.valueCount > temp2.valueCount) {
            nodeMax = temp1;
            nodeMin = temp2;
        } else {
            nodeMax = temp2;
            nodeMin = temp1;
        }
        nodeMin.childCut = false;
        if (nodeMax.child == null) {
            nodeMax.child = nodeMin;
            nodeMin.parent = nodeMax;
            nodeMin.previous = nodeMin;
            nodeMin.next = nodeMin;
        } else {
            //melding the min node with childrens of the max node
            Node tem = nodeMax.child.previous;
            tem.next = nodeMin;
            nodeMin.previous = tem;
            nodeMin.next = nodeMax.child;
            nodeMax.child.previous = nodeMin;
            nodeMin.parent = nodeMax;

        }
        // after doing the pairwise combine, deletes the value of degree from the map
        degreeMap.remove(nodeMax.degree);
        //updates the degree of parent
        nodeMax.degree += 1;
        Node
                tempNode;
        int deg;

        // this logic takes care of calling the pairwise combine in a recursive way
        if (degreeMap.containsKey(nodeMax.degree)) {
            tempNode = degreeMap.get(nodeMax.degree);
            deg = nodeMax.degree;
            /* call pairwise combine recursively as long as nodes with the same
             degree exist in the "degreeMap" hashmap */
            pairWiseCombine(nodeMax, tempNode, degreeMap);
            degreeMap.remove(deg);
        } else {
            degreeMap.put(nodeMax.degree, nodeMax);
        }
    }

    /**
     *  This method does the increase key operation for a existing node.
     *   It increases the value of existing node by the value passed(val)
     *
     * @param node : Takes the parameter as node whose value (here frequency) has to be increased
     * @param val : The value by which the frequency or value has to be increased.
     */
    public void increaseKey(Node node, int val) {
        node.valueCount = node.valueCount + val;

        LinkedList<Node> nodeList = new LinkedList<>();

        if (node.parent != null) {
            /* checks if after increasing the key's value of current node,
             do we need to remove the current node if current node key's value is greater than parent*/
            if (node.valueCount > node.parent.valueCount) {

                node.childCut = true;
                // calls the child cut logic for the nodes
                nodeList.addAll(childCut(node));
            }
        }
        if (!nodeList.isEmpty()) {
            // melds the nodes
            meldNodes(nodeList);
        } else if (node.valueCount > max.valueCount) {
            // updates the max node is required
            max = node;
        }
    }


    /**
     *  this method takes care of child cut and cascading child cut operation
     * @param node : the node from where the child cut starts, propagating upwards
     * @return list of removed nodes due to childcut to be melded later with top level root nodes
     */
    private LinkedList<Node> childCut(Node node) {
        LinkedList<Node>
                newTopLevelNodes = new LinkedList<>();
        Node temp;
        Node cur = node;

        /* "while" loop takes care of cascading child cut.
        "while" loop only stops if the child cut value of node is false
        or when the current node is the root node.
        Otherwise the child cut propagates from the removed node to the top till the root node.
         */
        while (cur != null && cur.childCut) {
            if (cur.parent != null) {
                if (cur.parent.child == cur) {
                    if (cur.next != cur) {
                        temp = cur.previous;
                        cur.parent.child = cur.next;
                        cur.next.previous = temp;
                        temp.next = cur.next;
                    } else {
                        cur.parent.child = null;
                    }
                } else {
                    // remove cur node from doubly linked list
                    temp = cur.previous;
                    cur.next.previous = temp;
                    temp.next = cur.next;
                }
                if (cur.parent.degree > 0)
                    cur.parent.degree -= 1; // Updating the degree of parent after losing the child

                cur.next = cur;
                cur.previous = cur;
                newTopLevelNodes.add(cur);
                // if the current node is root node, the child cut stops
                if (cur.parent.childCut == null) {
                    break;
                } else if (cur.parent.childCut == false) {
                    /* if any parent or super parent nodes of the removed node has child cut value as false, the casacading childcut operation
                    stops by setting the child cut value of node to true
                     */
                    cur.parent.childCut = true;
                    break;
                }
            }
            cur = cur.parent;
        }
        return newTopLevelNodes;
    }


    /**
     * remove a node in the fibonacci heap
     * @param node : the node to be removed
     *
     */
    public void remove(Node node)
    {
        node.childCut = true;
        LinkedList<Node> nodes = childCut(node);
        if(node.child != null)
        {
            int count = 0;
            Node temp = node.child;
            Node first = node.child;
            // get all the child of the removed node
            while (temp != first || count == 0)
            {
                count++;
                temp.previous = temp;
                temp.next = temp;
                temp = temp.next;
                nodes.add(temp);
            }
        }
        // dereference the node to be removed
        node.parent = null;
        node.child = null;
        node.previous = null;
        node.next = null;
        nodes.remove(node);
        if(!nodes.isEmpty())
        {
            /* meld the child nodes of removed node and
             existing top level root nodes into circular doubly linked list */
            meldNodes(nodes);
        }
    }

    /**
     * return a new instance of Node class
     * @param key : the hashtag name
     * @param val : the frequency of the hashtag
     * @return : the newly created node object
     */
    Node getNewNode(String key, int val) {
        Node node
                = new Node(key, val);
        return node;
    }

}
