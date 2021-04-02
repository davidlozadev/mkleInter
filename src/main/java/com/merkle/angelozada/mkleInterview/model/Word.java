package com.merkle.angelozada.mkleInterview.model;

//Class to model a word
public class Word {
    public String word;
    public int index;
    public Node adjacentWords;
    public Word parent;
    public boolean linked;
    public int visited;

    public Word(String word, int index) {
        this.word = word;
        this.index = index;
        this.adjacentWords = null;
        this.linked = false;
        this.visited = 0;
        this.parent = null;
    }

    @Override
    public String toString() {
        return word;
    }
}
