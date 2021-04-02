package com.merkle.angelozada.mkleInterview;

import com.merkle.angelozada.mkleInterview.service.DictionaryWalk;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Scanner;

@SpringBootApplication
public class MkleInterviewApplication {

	//Entry point of the application after Spring load the data following indications of BootstrapData.java
	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(MkleInterviewApplication.class, args);

		//Getting the bean from the context
		DictionaryWalk dictionaryWalk = (DictionaryWalk) context.getBean("dictionaryWalk");

		//Variables managing the input of the user
		String start = null;
		String target = null;

		boolean bfs = true;
		boolean keepGoing = true;

		String userResponse;

		//reading the input by the user
		Scanner input = new Scanner(System.in);

		while (keepGoing) {
			//Ask the user for the length of output, this will depend of the algorithm used for search
			System.out.print("Select (s)hort or (l)ong list (s/l): ");
			userResponse = input.nextLine ();

			//Keep default BFS search is wrong input
			if(userResponse.equals("l")){
				bfs = false;
			}
			System.out.println("Please provide a starting word and a target word");

			//Ask user for the start word
			System.out.print ("From: ");
			while(start == null || start.length() == 0){
				start = input.nextLine ();
				if(start.length () == 0) {
					System.out.println("INVALID EMPTY INPUT, please provide valid word");
					System.out.print ("From: ");
				}
			}

			//Ask user for the target word
			System.out.print ("To: ");
			while(target == null || target.length() == 0){
				target = input.nextLine ();
				if(target.length () == 0) {
					System.out.println("INVALID EMPTY INPUT, please provide valid word");
					System.out.print ("To: ");
				}
			}

			start = start.toLowerCase();
			target = target.toLowerCase();

			//Call to the generation of the list. DictionaryWalk.java
			dictionaryWalk.generate_List(target, start, bfs);

			System.out.print("Continue? (y/n): ");
			userResponse = input.nextLine();
			if(userResponse.equals("n")){
				keepGoing = false;
			}

			//control of search algorithm back to the default (BFS)
			//words back to null
			bfs = true; start = null; target = null;
			System.out.println();
		}
		input.close();

		System.out.println("Thanks by: Angel Lozada 04/2021");
	}

	//Class to track performance
	public static class Timeit{
	    public static void code(Runnable block){
	        long start = System.nanoTime();
	        try{
	            block.run();
            }finally {
	            long end = System.nanoTime();
                System.out.println("Time taken(s): " + (end-start)/1.0e9);
            }
        }
    }
}
