package com.merkle.angelozada.mkleInterview.BootstrapData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

//Class managed by Spring, in charge of loading the data for the dictionary of valid words
@Component
public class BootstrapData implements CommandLineRunner {

    //static array that is public and static, for easy access
    public static ArrayList<String> scrabbleDict = new ArrayList<>();

    @Override
    public void run(String... args){

        System.out.println();
        System.out.println("******WELCOME TO WORLD LIST GENERATOR******");
        System.out.print("Loading dictionary data from local file...");

        try{

            //The app always will try to load the data from a local source
            loadFromLocal();
            System.out.println("completed");
        }catch (Exception e){
            System.out.println("Loading dictionary data from url...");
            try{

                //If not possible it will create a http connection in order to download the data
                loadFromUrl();
            }catch (Exception s){
                System.out.println("*******URL NOT FOUND*******");
            }
        }

        System.out.println();
    }

    //Method to load the data from a local source
    public static void loadFromLocal() throws IOException{

        BufferedReader reader = null;
        String word;

        try{
            reader = new BufferedReader(new FileReader("scrabbleDict.txt"));
            while((word = reader.readLine()) != null){
                scrabbleDict.add(word);
            }
        }catch (IOException e){
            System.out.println("*******FILE NOT FOUND*******");
            throw new IOException(e);
        }finally {
            reader.close();
            scrabbleDict.remove(0);
            scrabbleDict.remove(0);
        }
    }

    //Method to load the data from an url and then download it locally
    public static void loadFromUrl() throws MalformedURLException, ProtocolException, IOException {

        URL url = new URL("https://www.wordgamedictionary.com/twl06/download/twl06.txt");
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod("GET");

        //MANAGE TIMEOUTS HERE
        int status = httpConnection.getResponseCode();

        InputStreamReader inputStream = null;
        BufferedReader in = null;

        //Buffer to write to a local file
        BufferedWriter writer = new BufferedWriter(new FileWriter("scrabbleDict.txt"));

        try{
            inputStream = new InputStreamReader(httpConnection.getInputStream());
            in = new BufferedReader(inputStream);

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                //every line loaded to the program
                scrabbleDict.add(inputLine);

                //is written into a file
                writer.write(inputLine+"\n");
            }

            //Removing title and blank space
            scrabbleDict.remove(0);
            scrabbleDict.remove(0);

        }finally {
            in.close();
            writer.close();
        }
    }
}
