import java.util.*;
import java.io.*;

import com.ovus.aisekle.csv.*;

public class Main {
	public static void main(String[] args) {
        File goldFile = new File("Gold.csv");
        File copperFile = new File("Copper.csv");
        File copperInGoldFile = new File("CopperInGold.csv");
        String delimiter = ",";
/*        LinkedHashSet <String> copperInGoldLines = new LinkedHashSet<>(); 
        
        try(Scanner scanner = new Scanner(copperFile)) {
        	
            while(scanner.hasNext()) {
                String copperFileLine = scanner.nextLine();
    	        Integer priceCopperInGold = 0;
      	        String[] copperFileAsArray = copperFileLine.split(delimiter);
      	        Acsvl.findLine(goldFile, copperFileAsArray[0], 1, delimiter, (n, str) -> {
      	       	    String[] goldFileLineAsArray = str.split(delimiter);
      	       	    String date = goldFileLineAsArray[0];
      	            Double copperInGoldClose = Double.parseDouble(copperFileAsArray[1])*2200/Double.parseDouble(goldFileLineAsArray[1]);
      	            Double copperInGoldOpen = Double.parseDouble(copperFileAsArray[2])*2200/Double.parseDouble(goldFileLineAsArray[2]);
      	            Double copperInGoldHigh = Double.parseDouble(copperFileAsArray[3])*2200/Double.parseDouble(goldFileLineAsArray[3]);
      	            Double copperInGoldLow = Double.parseDouble(copperFileAsArray[4])*2200/Double.parseDouble(goldFileLineAsArray[4]);
      	            copperInGoldLines.add(date+delimiter+copperInGoldClose.toString()+delimiter
      	            		+copperInGoldOpen.toString()+delimiter
      	            		+copperInGoldHigh.toString()+delimiter
      	            		+copperInGoldLow.toString()+delimiter);
      	        });
            }
            Acsvl.addLines(copperInGoldFile, copperInGoldLines);
        }catch(FileNotFoundException fnfe) {
        	System.out.println("File Not Found !");
        }catch(AcsvlException acsvle) {
        	acsvle.printStackTrace();
        }*/
    	String[] copperFileLineAsArray = new String[256];
    	String[] goldFileLineAsArray = new String[256];
    	long linesCounter = 0L;
    	Long lineNumber = 0L;
        try(Scanner scanner = new Scanner(copperFile)) {
        	BufferedWriter bwr = new BufferedWriter(new FileWriter(copperInGoldFile));
        	while(scanner.hasNext()) {
        		linesCounter++;
        		/*String[] */copperFileLineAsArray = scanner.nextLine().split(delimiter);
        		lineNumber = Acsvl.findNextLine(goldFile, copperFileLineAsArray[0], 0, 1, delimiter).getKey();
        		/*String[] */goldFileLineAsArray = Acsvl.findNextLine(goldFile, copperFileLineAsArray[0], 0, 1, delimiter).getValue().split(delimiter);
        		if(lineNumber > 0) {
  	       	        String date = goldFileLineAsArray[0];
  	                Double copperInGoldClose = Double.parseDouble(copperFileLineAsArray[1])*2200/Double.parseDouble(goldFileLineAsArray[1]);
  	                Double copperInGoldOpen = Double.parseDouble(copperFileLineAsArray[2])*2200/Double.parseDouble(goldFileLineAsArray[2]);
  	                Double copperInGoldHigh = Double.parseDouble(copperFileLineAsArray[3])*2200/Double.parseDouble(goldFileLineAsArray[3]);
  	                Double copperInGoldLow = Double.parseDouble(copperFileLineAsArray[4])*2200/Double.parseDouble(goldFileLineAsArray[4]);
  	                bwr.write(date+delimiter+String.format(Locale.US,"%1$.3g", copperInGoldClose)+delimiter
  	            		+String.format(Locale.US,"%1$.3g", copperInGoldOpen)+delimiter
  	            		+String.format(Locale.US,"%1$.3g", copperInGoldHigh)+delimiter
  	            		+String.format(Locale.US,"%1$.3g", copperInGoldLow)+delimiter+System.lineSeparator());
        		}        		
        	}
        	bwr.flush();
        	bwr.close();
        }catch(Exception e) {
        	System.out.println(linesCounter+" : goldFileLineAsArray.length = "+goldFileLineAsArray.length+" copperFileLineAsArray.length = "+copperFileLineAsArray.length);
        	System.out.println("copperFileLineAsArray = \n"+Arrays.toString(copperFileLineAsArray));
        	System.out.println("goldFileLineAsArray = \n"+Arrays.toString(goldFileLineAsArray));
        	e.printStackTrace();
        }
    }
}
