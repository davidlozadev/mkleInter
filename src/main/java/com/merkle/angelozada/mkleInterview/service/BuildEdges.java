package com.merkle.angelozada.mkleInterview.service;

import com.merkle.angelozada.mkleInterview.BootstrapData.BootstrapData;
import com.merkle.angelozada.mkleInterview.model.Node;
import com.merkle.angelozada.mkleInterview.model.Word;

import java.util.*;

public class BuildEdges {
    static int count = 0;

    static ArrayList<String> scrabbleDict = BootstrapData.scrabbleDict;
    static Set<String> visited = new HashSet<>();

    static int dictSize = scrabbleDict.size();

    static int visited_max = 0;

    //******************************************************************************************************************
    //***************************METHODS--------------------------------------------------------------------------------
    //*****************************************************************************************************************
    public static void breadth_First_Search(String start, String target) {
        Word object_root = new Word("", -1);
        Word current_object, object_Next;
        Node neighborsCurrentObj;

        LinkedList<Word> toExplore = new LinkedList<>();
        //Set<String> visited = new HashSet<>();

        //Check if both words are valid.
        int headIndex = bSearchWord(start);
        int targetIndex = bSearchWord(target);

        Word start_object = buildWordObject(headIndex);
        Word target_object = buildWordObject(targetIndex);

        visited_max++;

        object_root.visited = visited_max;
        start_object.parent = object_root;
        toExplore.offer(start_object);

        System.out.print("Looking for a connection between " + target.toUpperCase() + " and " + start.toUpperCase() + ": ");

        while ((current_object = toExplore.poll()) != null) {

            if(visited.contains(current_object.word)){
                continue;
            }else{
                visited.add(current_object.word);
            }

            //This Is necessary for the printing
            if (current_object.visited < visited_max) {
                current_object.visited = visited_max;
            }
            else {
                continue;
            }

            // Check whether the current object is equal to the tail_object, thus a solution has been found.
            // If so, then print the solution
            if (current_object.word.equals(target)) {
                //count++;
                System.out.println("FOUND!");
                print_Solution(current_object);
                return;
            }
            // If the object does not have any links to any other objects then pass to words_Adjacent_To and attempt to find at least 1
            if (!current_object.linked) {
                //System.out.println(object.word);
                //current_object.adjacentWords = findAdjacentWords(current_object.word);
                current_object.adjacentWords = adjacentWords(current_object.word);
                current_object.linked = true;
            }
            neighborsCurrentObj = current_object.adjacentWords;

            // While the object has a link to another object
            while (neighborsCurrentObj != null) {
                object_Next = buildWordObject(neighborsCurrentObj.index);
                if (object_Next.parent == null || object_Next.parent.visited < visited_max) {
                    object_Next.parent = current_object;
                }

                toExplore.offer(object_Next);
                neighborsCurrentObj = neighborsCurrentObj.next;
            }
        }

        System.out.println(target + "/" + start + " cannot be connected via adjacent to words.");
        return;
    }

    //Helper Method to construct Word Objects
    private static Word buildWordObject(int wordIndex) {
        String word = scrabbleDict.get(wordIndex);
        Word wordObj = new Word(word, wordIndex);
        return wordObj;
    }

    static void print_Solution(Word wordObj) {
        while (wordObj != null && wordObj.visited == visited_max) {
            System.out.println(wordObj.word);
            wordObj = wordObj.parent;
        }
        while (wordObj != null && visited.contains(wordObj)) {
            System.out.println(wordObj.word);
            wordObj = wordObj.parent;
        }
        return;
    }

    public static int bSearchWord(String word) {
        int lowerBound = 0;
        int upperBound = dictSize - 1;
        int curIn;

        while (true) {
            curIn = (lowerBound + upperBound) / 2;
            if (scrabbleDict.get(curIn).equals(word))
                return curIn; // found it
            else if (lowerBound > upperBound)
                return -1; // can't find it
            else // divide range
            {
                if (compareWords(scrabbleDict.get(curIn), word))
                    lowerBound = curIn + 1; // it's in upper half
                else
                    upperBound = curIn - 1; // it's in lower half
            }
        }
    }

    public static boolean compareWords(String word1, String word2) {
        int result = word1.compareTo(word2);

        if (result < 0)
            return true;

        return false;
    }

    public static Node adjacentWords (String word) {

        StringBuffer sbWord = new StringBuffer();
        int indexOfWord = -1;
        char charToSub;
        Node neighborsLinkedList = null;

        sbWord.append(word);

        //ArrayList<String> adjacent = new ArrayList<>();

        for(int letterIndex = 0; letterIndex < sbWord.length(); letterIndex++){
            for(int decimalChar = 97; decimalChar < 123; decimalChar++){

                charToSub = (char) decimalChar;
                sbWord.setCharAt(letterIndex, charToSub);
                indexOfWord = bSearchWord(sbWord.toString());

                if(indexOfWord != -1 && ! word.equals(sbWord.toString())){

                    neighborsLinkedList = new Node (indexOfWord, neighborsLinkedList);
                }
            }
            sbWord.replace(0,sbWord.length(),word);
        }

        for(int letterIndex = 0; letterIndex < sbWord.length(); letterIndex++){

            sbWord.insert(letterIndex, ' ');

            for(int decimalChar = 97; decimalChar < 123; decimalChar++){

                charToSub = (char) decimalChar;
                sbWord.setCharAt(letterIndex,charToSub);
                indexOfWord = bSearchWord(sbWord.toString());

                if(indexOfWord != -1 && ! word.equals(sbWord.toString())){

                    neighborsLinkedList = new Node (indexOfWord, neighborsLinkedList);
                }
            }
            sbWord.deleteCharAt(letterIndex);
        }

        //Just remove one letter
        for(int letterIndex = 0; letterIndex < sbWord.length(); letterIndex++) {

            sbWord.deleteCharAt(letterIndex);
            indexOfWord = bSearchWord(sbWord.toString());

            if (indexOfWord != -1 && !word.equals(sbWord.toString())) {

                neighborsLinkedList = new Node(indexOfWord, neighborsLinkedList);
            }
            sbWord.replace(0,sbWord.length(),word);
        }

        return neighborsLinkedList;
    }
}


