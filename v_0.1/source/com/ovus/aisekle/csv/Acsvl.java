package com.ovus.aisekle.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Stream;
import java.util.function.BiConsumer;


/**
 *  @author AisekleOvus
 *  look for line by its part and do callback
 *  threre are 4 variants of method "find" where:
 *  File file - descriptor of file where to search
 *  String searchString - the string to search
 *  int inCloumn - you can organize serach in concrete column instead of whole file, this is not necessary param
 *  String delimiter - delimiter of columns in file. Not necessary param. It set to ";" by default 
 *  BiConsumer<Long, String> callback - BiConsumer for found line number(Long) and found line itself(String)
 */

public class Acsvl {
	//                                   Utility Methods
	
	private static String getFirstLine(File file) throws AcsvlException {
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			return br.readLine();
		}catch(FileNotFoundException fnfe) {
			throw new AcsvlException("Exception: File Not Found !", fnfe);
		}catch(IOException ioe) {
			throw new AcsvlException("Exception: IOException !", ioe);
		}
	}
	
	private static boolean columnsAccordance(File file, Map<Long, String> lines, String delimiter) throws AcsvlException{
        int fileColumnsCounter = getFirstLine(file).split(delimiter).length;
        return lines.entrySet().stream().allMatch(entry -> entry.getValue().split(delimiter).length == fileColumnsCounter);
    }
	
	//                                   Fine Line Method
	
	public static void findLine(File file, String searchString, BiConsumer<Long, String> callback) 
			throws AcsvlException {
		findLineMechanics(file, searchString, 0, ";", callback);
	}
	
	public static void findLine(File file, String searchString, int inColumn, BiConsumer<Long, String> callback) 
			throws AcsvlException {
		findLineMechanics(file, searchString, inColumn, ";", callback);
	}
	
	public static void findLine(File file, String searchString, String delimiter, BiConsumer<Long, String> callback) 
			throws AcsvlException {
		findLineMechanics(file, searchString, 0, delimiter, callback);
	}
	
	public static void findLine(File file, String searchString, int inColumn, String delimiter, BiConsumer<Long, String> callback)
			throws AcsvlException {
		findLineMechanics(file, searchString, inColumn, delimiter, callback);
	}
	
	private static void findLineMechanics(File file, String searchString, int inColumn, String delimiter, BiConsumer<Long, String> callback) 
			throws AcsvlException {

		int columnsTotal = getFirstLine(file).split(delimiter).length;		
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
        	long linesCounter = 0L;
        	String currentLine = "";
        	String[] currentLineAsArray;
        	while((currentLine = br.readLine()) != null) {
        		linesCounter++;
    		    currentLineAsArray = currentLine.split(delimiter);

        		if(columnsTotal != currentLineAsArray.length)                              
        			    throw new AcsvlException("Exception: There is variable amount of columns in file !", new AcsvlFileFormatException());
        		
        		if(inColumn != 0) {
        		    if(inColumn > currentLineAsArray.length)                                   
        		    	throw new AcsvlException("Exception: Column to find in does not exist !", new AcsvlInputFormatException());       
                        
        		    if(currentLineAsArray[inColumn-1].indexOf(searchString) != -1)
        		        callback.accept(linesCounter, currentLine);
        		    
        		}else {
        			if(currentLine.indexOf(searchString) != -1)
        			    callback.accept(linesCounter, currentLine);
        		}
        		
        	}
        }catch(FileNotFoundException fnfe) {
        	throw new AcsvlException("Exception: File Not Found !", fnfe);
        }catch(IOException ioe) {
        	throw new AcsvlException("Exception: IOException !", ioe);
        }
	}

	//                                   Edit Line Method

	public static void editLines(File file, Map<Long, String> lines) 
		throws AcsvlException {
		
		if(!lines.entrySet().stream().allMatch(entry -> entry.getValue().contains(";")))
			throw new AcsvlException("Exception: Not all given lines contains \";\" as delimiter !", new AcsvlInputFormatException());		    
		if(!getFirstLine(file).contains(";"))
			throw new AcsvlException("Exception: \";\" is not delimiter for given file !", new AcsvlFileFormatException());
        if(!columnsAccordance(file, lines, ";"))
        	throw new AcsvlException("Exception: different number of columns !", new AcsvlInputFormatException());
		editLinesMechanics(file, lines);
	}
	public static void editLines(File file, Map<Long, String> lines, String delimiter) 
		throws AcsvlException {
		
		if(!lines.entrySet().stream().allMatch(entry -> entry.getValue().contains(delimiter)))
			throw new AcsvlException("Exception: Not all given lines contains \""+delimiter+"\" as delimiter !", new AcsvlInputFormatException());		    
		if(!getFirstLine(file).contains(delimiter))
			throw new AcsvlException("Exception: \""+delimiter+"\" is not delimiter for given file !", new AcsvlFileFormatException());
        if(!columnsAccordance(file, lines, delimiter))
        	throw new AcsvlException("Exception: different number of columns !", new AcsvlInputFormatException());
		editLinesMechanics(file, lines);
		}	
	
	private static void editLinesMechanics(File file, Map<Long, String> lines)
		throws AcsvlException {
		long linesCounter = 1L;
		String currentLine = "";
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
	        File tempFile = File.createTempFile(file.getName()+"_", null, new File(file.getAbsolutePath()).getParentFile());
			BufferedWriter bwr = new BufferedWriter(new FileWriter(tempFile));
			while((currentLine = br.readLine()) != null) {
				if(lines.containsKey(Long.valueOf(linesCounter))) {
					String lineToWrite = lines.get(Long.valueOf(linesCounter));
					bwr.write(lineToWrite+System.lineSeparator(),0,lineToWrite.length()+1);
				}else {
					bwr.write(currentLine+System.lineSeparator(),0,currentLine.length()+1);					
				}
				linesCounter++;
			}
			bwr.flush();
			bwr.close();
			
			Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			tempFile.delete();
		}catch(IOException ioe) {
			throw new AcsvlException("Exception: Something wrong with I/O !", ioe);
		}
	}
	
//                                           Add Lines Method	
	
    public static void addLines(File file, Iterable<String> lines)	throws AcsvlException{
	    addLinesMechanics(file, lines, ";");
    }
    public static void addLines(File file, Iterable<String> lines, String delimiter)	throws AcsvlException{
	    addLinesMechanics(file, lines, delimiter);
    }
    public static void addLine(File file, String line) throws AcsvlException{
    	TreeSet<String> lines = new TreeSet<>();
    	lines.add(line);
	    addLinesMechanics(file, lines, ";");
    }
    public static void addLine(File file, String line, String delimiter) throws AcsvlException{
    	TreeSet<String> lines = new TreeSet<>();
    	lines.add(line);
	    addLinesMechanics(file, lines, delimiter);
    }
    
    private static void addLinesMechanics(File file, Iterable<String> lines, String delimiter) throws AcsvlException{
    	int columnsInFileCounter = 0;
    	String firstLine = "";
    	if(file.exists()) {
    		Iterator<String> linesIterator = lines.iterator();
            firstLine = getFirstLine(file);
    	    columnsInFileCounter = firstLine.split(delimiter).length;
    	    if(!firstLine.contains(delimiter))
    	    	throw new AcsvlException("Exception: \""+delimiter+"\" is not delimiter for given file !", new AcsvlFileFormatException());
    	    while(linesIterator.hasNext()) {
    	    	String currentLine = linesIterator.next();
    	    	if(currentLine.split(delimiter).length != columnsInFileCounter)
    	    		throw new AcsvlException("Exception: different number of columns !", new AcsvlInputFormatException());
    	    	if(!currentLine.contains(delimiter))
    	    		throw new AcsvlException("Exception: Not all given lines contains \""+delimiter+"\" as delimiter !", new AcsvlInputFormatException());	
    	    }
    	} 
    	try(BufferedWriter bwr = new BufferedWriter(new FileWriter(file, true))) {
    	    Iterator<String> writerLinesIterator = lines.iterator();
    	    while(writerLinesIterator.hasNext())
    	    	bwr.write(writerLinesIterator.next()+System.lineSeparator());
    	    
    	    bwr.flush();
    	} catch(IOException ioe) {
    		throw new AcsvlException("Exception: Something wrong with I/O !", ioe);
    	}
    }
}
