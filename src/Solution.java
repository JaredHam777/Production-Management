import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;





public class Solution  {

	//sequence from initial prompt.  Feel free to change
	static String[] sequence = {"B", "G", "E", "A", "C", "I", "J", "D", "H", "F"};
	
	static Map<String, Integer> productionTimes = new HashMap<String, Integer>();
	static ArrayList<String> configOrder = new ArrayList<String>();
	static Integer[][] changeover;		//represents the asymmetric changeover matrix
	static int numberOfProducts;		
	
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		//import production times and changeover data from csv files
		readConfiguration();
		
		System.out.println("Number of products: " + numberOfProducts);
		System.out.println("-------------------------------------------");
		
		int baseCost=0;
		int changeOverCost=0;
		
		
		for(String product : sequence) {
			baseCost+=productionTimes.get(product);
		}

		
		for(int i=0; i<sequence.length-1; i++) {
			int changeover = getChangeover(sequence[i], sequence[i+1]);
			changeOverCost+=changeover;
		}
		//print information:
		
		System.out.println("Base Cost (sum of each product time): " + baseCost);
		System.out.println("Initial Changeover Cost (sum of each product changeovers): " + changeOverCost);
		System.out.println("Initial Total Cost (Changeover + Base): " + (changeOverCost+baseCost));
		
		
		//Greedy Heuristic: Neighbor Swap
		while(true)
		{
			int swappedProductIndex=0;
			String swappedProduct1="";
			String swappedProduct2="";
			
			int maxTimeSaved = Integer.MIN_VALUE;
			
			for(int i=0; i<sequence.length-1; i++) {
				String product1 = sequence[i];
				String product2 = sequence[i+1];				
				int currentChangeover = getChangeover(product1, product2);
				int alternateChangeover = getChangeover(product2, product1);	//changeover cost as a result of switching
				int timeSaved = currentChangeover-alternateChangeover;			//we want this value to be big
				if(timeSaved > maxTimeSaved) {
					maxTimeSaved = timeSaved;
					
					//tag our product index and name that we want to swap
					swappedProduct1=sequence[i];
					swappedProduct2=sequence[i+1];
					swappedProductIndex=i;
					
				}
			}
			
			if(maxTimeSaved>0) {
				//swap the neighbors we tagged earlier
				sequence[swappedProductIndex+1]=swappedProduct1;
				sequence[swappedProductIndex]=swappedProduct2;
			}	else	{
				//we couldn't find any beneficial product neighbors to swap
				break;
			}
			

		}
		
		System.out.println("Passes complete!");
		System.out.print("Best Sequence: ");
		printSequence(sequence);
		
		int bestChangeOverCost=0;
		
		for(int i=0; i<sequence.length-1; i++) {
			int changeover = getChangeover(sequence[i], sequence[i+1]);
			bestChangeOverCost+=changeover;
		}
		
		System.out.println("Best Sequence Changeover Cost: " + (bestChangeOverCost));
		System.out.println("Best Sequence Total Cost: " + (bestChangeOverCost+baseCost));
		
	}
	
	public static void printSequence(String[] seq) {
		for(String s : seq) {
			System.out.print(s + ", ");
		}
		System.out.println();
	}

	
	
	public static int getChangeover(String a, String b) {
		return changeover[configOrder.indexOf(a)][configOrder.indexOf(b)];
	}
	
	
	
	
	public static void readConfiguration() throws Exception	{
		FileReader productInfo = new FileReader("data/product_information.csv");
		FileReader productChangeover = new FileReader("data/product_changeover.csv");

		String line;	//represents a given line when scanning a csv
		Scanner sc1 = new Scanner(productInfo);
		while(sc1.hasNextLine()) {
			line = sc1.nextLine();
			String[] entries = line.split(",");
			if(entries.length!=2) {throw new Exception("production_information file not in correct format");}
			productionTimes.put(entries[0], Integer.parseInt(entries[1]));
			configOrder.add(entries[0]);
			numberOfProducts++;
		}		
		
		
		changeover = new Integer[numberOfProducts][numberOfProducts];
		
		sc1.close();
		
		Scanner sc2 = new Scanner(productChangeover);
		int lineNumber=0;
		while(sc2.hasNextLine())	{
			
			line = sc2.nextLine();
			//System.out.println(line);
			String[] entries = line.split(",");
			if(entries.length!=numberOfProducts) {throw new Exception("production_changeover file not in correct format. length: " + entries.length);}
			for(int i=0; i<entries.length; i++)	{
				Integer n=(Integer) null;				
				try {n=Integer.parseInt(entries[i]);}catch (Exception e) {}	//case where entry is '-' or not applicable				
				changeover[lineNumber][i] = n;
			}
			lineNumber++;
		}
		sc2.close();
	}
	
	




}
	
	
	
	
	
	

	
	

