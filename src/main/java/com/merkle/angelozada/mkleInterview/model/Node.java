package com.merkle.angelozada.mkleInterview.model;

//Class to model a Node and build a linked list
public class Node {
    public int index;
    public Node next;

    public Node(int index, Node next) {
        this.index = index;
        this.next = next;
    }
}
