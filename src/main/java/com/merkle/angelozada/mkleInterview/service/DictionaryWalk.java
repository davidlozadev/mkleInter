package com.merkle.angelozada.mkleInterview.service;

import com.merkle.angelozada.mkleInterview.BootstrapData.BootstrapData;
import com.merkle.angelozada.mkleInterview.model.Node;
import com.merkle.angelozada.mkleInterview.model.Word;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DictionaryWalk {

    //static ArrayList referencing the array created by the BootstrapData class
    static ArrayList<String> scrabbleDict = BootstrapData.scrabbleDict;

    //static Array to keep track of the visited nodes
    static ArrayList<String> visited = new ArrayList<>();

    //variable to keep track of the maximum visits permitted by node (1)
    static int max_visits = 0;

    //******************************************************************************************************************
    //***************************METHODS--------------------------------------------------------------------------------
    //*****************************************************************************************************************

    //choose the algorithm for searching the list
    public void generate_List(String start, String target, boolean bfs){

        if(bfs){
            //Will call the method that do the BFS
            generate_Shortest_List(start, target);
        }else{
            //Will call the method that do the DFS
            generate_Longest_List(start, target);
        }

        //Because visited is static, after each function completion, is erased
        visited = new ArrayList<>();
    }

    //execute a BFS
    private void generate_Shortest_List(String start, String target) {

        //Because the result list is stored in a Node that works like a linked list of individual Nodes,
        //in order to obtain a list from start to target, the root node should be the target, that's why
        //when calling the function the order of the words is changed

        //Root node, that controls the printing
        Word object_root = new Word("", -1);

        //Current visiting node
        Word current_object, object_Next;
        Node neighborsCurrentObj;

        //Queue that store the variables to explore
        LinkedList<Word> toExplore = new LinkedList<>();


        //Check if both words are valid.
        int startIndex = b_Search_Word(start);
        int targetIndex = b_Search_Word(target);

        if(startIndex == -1 || targetIndex == -1){
            if(startIndex == -1){
                System.out.println("Please provide a valid TARGET word");
            }else{
                System.out.println("Please provide a valid START word");
            }
            return;
        }

        //Creating equivalent objects for the start and target word
        Word start_object = build_Word_Object(startIndex);
        Word target_object = build_Word_Object(targetIndex);

        //Max visits allowed by Node = 1
        max_visits++;

        object_root.visited = max_visits;
        target_object.parent = object_root;

        //First step of the BFS, offer the starting object
        toExplore.offer(start_object);

        System.out.print("Generating list between " + target + " and " + start + ": ");

        while (!toExplore.isEmpty()) {

            //Take an element from the head of the queue
            current_object = toExplore.poll();

            //check that the current object, it was not previous visited, if it was, get next element in the queue
            if(visited.contains(current_object.word)){
                continue;
            }else{
                visited.add(current_object.word);
            }

            //Mark this object as visited
            if (current_object.visited < max_visits) {
                current_object.visited = max_visits;
            }
            else {
                continue;
            }

            //Because is the first time visiting this node(word), we need to explore it (get all its "neighbors" adjacent words)
            if (!current_object.linked) {

                //call the function that will check for all the adjacent words
                current_object.adjacentWords = adjacent_Words(current_object.word);
                current_object.linked = true;
            }

            //When we reach a node that is equal to the target node, the result will be displayed
            if (current_object.word.equals(target)) {

                System.out.println("FOUND!");

                //When found call the object to print the solution list
                print_Solution(current_object);
                return;
            }

            //all the adjacent words for the current word will be added to the queue (toExplore)
            neighborsCurrentObj = current_object.adjacentWords;

            //iterate over all the adjacent words
            while (neighborsCurrentObj != null) {

                //build a word object to be added to the queue
                object_Next = build_Word_Object(neighborsCurrentObj.index);

                if (object_Next.parent == null || object_Next.parent.visited < max_visits) {

                    //keep track of who is this adjacent word coming from
                    object_Next.parent = current_object;
                }

                //add it to the queue
                toExplore.offer(object_Next);

                //iterate to the next adjacent word
                neighborsCurrentObj = neighborsCurrentObj.next;
            }
        }

        //if the queue is empty, all the nodes were visited and no solution was found
        System.out.println("LIST NOT FOUND! \nImpossible to connect " + target + " with " + start);
        return;
    }

    //execute a DFS
    private void generate_Longest_List(String start, String target) {

        Word object_root = new Word("", -1);
        Word current_object, object_Next;
        Node neighborsCurrentObj;

        //Stack for variables to visit
        LinkedList<Word> toExplore = new LinkedList<>();
        //Set<String> visited = new HashSet<>();

        //Check if both words are valid.
        int startIndex = b_Search_Word(start);
        int targetIndex = b_Search_Word(target);

        if(startIndex == -1 || targetIndex == -1){
            if(startIndex == -1){
                System.out.println("Please provide a valid TARGET word");
            }else{
                System.out.println("Please provide a valid START word");
            }
            return;
        }

        Word start_object = build_Word_Object(startIndex);
        Word target_object = build_Word_Object(targetIndex);

        max_visits++;

        object_root.visited = max_visits;
        //start_object.parent = object_root;
        target_object.parent = object_root;
        toExplore.offer(start_object);

        System.out.print("Generating list between " + target.toUpperCase() + " and " + start.toUpperCase() + ": ");

        while (!toExplore.isEmpty()) {

            //Will remove the top of the stack, will go on a DFS
            current_object = toExplore.pollLast();
            if(visited.contains(current_object.word)){
                continue;
            }else{
                visited.add(current_object.word);
            }

            //This Is necessary for the printing
            if (current_object.visited < max_visits) {
                current_object.visited = max_visits;
            }
            else {
                continue;
            }

            if (!current_object.linked) {

                current_object.adjacentWords = adjacent_Words(current_object.word);
                current_object.linked = true;
            }

            if (current_object.word.equals(target)) {

                System.out.println("FOUND!");
                print_Solution(current_object);
                return;
            }


            neighborsCurrentObj = current_object.adjacentWords;

            // Explore all the neighbors
            while (neighborsCurrentObj != null) {

                object_Next = build_Word_Object(neighborsCurrentObj.index);

                if (object_Next.parent == null || object_Next.parent.visited < max_visits) {
                    object_Next.parent = current_object;
                }

                toExplore.offer(object_Next);
                neighborsCurrentObj = neighborsCurrentObj.next;
            }
        }

        System.out.println("LIST NOT FOUND! \nImpossible to connect: " + target + "/" + start);
        return;
    }

    //******************************************************************************************************************
    //********************HElPER METHODS--------------------------------------------------------------------------------
    //******************************************************************************************************************

    //finds all the adjacent words to a word, and return a Node
    private static Node adjacent_Words (String word) {

        //StringBuffer to avoid the creation of new strings every time we modify the original one
        StringBuffer sbWord = new StringBuffer();

        //this variable will store the index in the dictionary of the adjacent word found
        int indexOfWord = -1;

        char charToSub;

        //This is the like linked list object, that will contain a reference to adjacent word to the current
        //visiting word
        Node neighborsLinkedList = null;


        sbWord.append(word);

        //This method will iterate through each character changing its value from a to z
        //Will find adjacents words of the same length

        //navigate the word
        for(int letterIndex = 0; letterIndex < sbWord.length(); letterIndex++){

            //replace each character starting at 'a' and finishing at 'z'
            for(int decimalChar = 97; decimalChar < 123; decimalChar++){

                charToSub = (char) decimalChar;
                sbWord.setCharAt(letterIndex, charToSub);

                //search if the current adjacent is a valid word in the dictionary
                indexOfWord = b_Search_Word(sbWord.toString());

                //if valid and different than the original word, construct a new node from the previous node
                if(indexOfWord != -1 && ! word.equals(sbWord.toString())){

                    neighborsLinkedList = new Node (indexOfWord, neighborsLinkedList);
                }
            }
            //back to original word
            sbWord.replace(0,sbWord.length(),word);
        }

        //This method will add one more character at the start,in between and end of each character of the word
        // and will iterate the added character from a to z
        //Will find adjacent words of larger length

        //navigate the word
        for(int letterIndex = 0; letterIndex < sbWord.length(); letterIndex++){
            //add new character to the word
            sbWord.insert(letterIndex, ' ');

            //replace each added character starting at 'a' and finishing at 'z'
            for(int decimalChar = 97; decimalChar < 123; decimalChar++){

                charToSub = (char) decimalChar;
                sbWord.setCharAt(letterIndex,charToSub);

                //search if the current adjacent is a valid word in the dictionary
                indexOfWord = b_Search_Word(sbWord.toString());

                if(indexOfWord != -1 && ! word.equals(sbWord.toString())){

                    //if valid and different than the original word, construct a new node from the previous node
                    neighborsLinkedList = new Node (indexOfWord, neighborsLinkedList);
                }
            }
            //delete added character (return to original word)
            sbWord.deleteCharAt(letterIndex);
        }

        //This method will remove each character of the word (one by one)
        //Will find adjacent words of smaller length

        for(int letterIndex = 0; letterIndex < sbWord.length(); letterIndex++) {
            //delete one character per iteration
            sbWord.deleteCharAt(letterIndex);

            //search if the current adjacent is a valid word in the dictionary
            indexOfWord = b_Search_Word(sbWord.toString());

            if (indexOfWord != -1 && !word.equals(sbWord.toString())) {

                //if valid and different than the original word, construct a new node from the previous node
                neighborsLinkedList = new Node(indexOfWord, neighborsLinkedList);
            }
            //back to the original word
            sbWord.replace(0,sbWord.length(),word);
        }

        return neighborsLinkedList;
    }

    //construct and return a new Word object
    private static Word build_Word_Object(int wordIndex) {
        String word = scrabbleDict.get(wordIndex);
        Word wordObj = new Word(word, wordIndex);
        return wordObj;
    }

    //search if the current adjacent is a valid word in the dictionary, through a binary search
    private static int b_Search_Word(String word) {
        int lowerBound = 0;
        int upperBound = scrabbleDict.size() - 1;
        int curIn;

        while (true) {
            curIn = (lowerBound + upperBound) / 2;
            if (scrabbleDict.get(curIn).equals(word))
                return curIn; // found it
            else if (lowerBound > upperBound)
                return -1; // can't find it
            else // divide range
            {
                if (compare_Words(scrabbleDict.get(curIn), word))
                    lowerBound = curIn + 1; // it's in upper half
                else
                    upperBound = curIn - 1; // it's in lower half
            }
        }
    }

    //helper method to the binary search
    private static boolean compare_Words(String word1, String word2) {
        int result = word1.compareTo(word2);

        if (result < 0)
            return true;

        return false;
    }

    //print the list between target and start
    private static void print_Solution(Word wordObj) {
        //will print first the start word, only it was already visited
        while (wordObj != null && wordObj.visited == max_visits) {
            System.out.println(wordObj.word);
            wordObj = wordObj.parent;
        }
        return;
    }

}

