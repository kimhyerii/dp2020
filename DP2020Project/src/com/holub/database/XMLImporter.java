package com.holub.database;

import java.io.*;
import java.util.*;
import com.holub.tools.ArrayIterator;

public class XMLImporter implements Table.Importer {
	private BufferedReader  in;
	private ArrayList<String> columnNames = new ArrayList<String>();
	private String          tableName;
	private String check;
	
	public XMLImporter( Reader in )
	{	this.in = in instanceof BufferedReader
						? (BufferedReader)in
                        : new BufferedReader(in)
	                    ;
	}
		
	@Override
	public void startTable() throws IOException {
		in.mark(1);
		
		in.readLine(); 
		in.readLine();
		check = in.readLine().substring(1);
		check = check.replaceAll("<", "");
		check = check.replaceAll(">", "");

		boolean end = false;
		while(!end) {
			String line = in.readLine();

			if (line.contains(check)) {
				end = true;
			}
			else {
				line = line.substring(1);
				line = line.replace('<', ' ');
				line = line.replace('>', ' ');
		        String [] wordlist = line.split(" ");
		        columnNames.add(wordlist[0]);
			}
		}
		
		in.reset();
		in.readLine(); //<?xml version="1.0"?>
		tableName = in.readLine().substring(1);
		tableName = tableName.replace('<', ' ');
		tableName = tableName.replace('>', ' ');
		in.readLine();
	}

	@Override
	public String loadTableName() throws IOException {
		return tableName;
	}

	@Override
	public int loadWidth() throws IOException {
		return columnNames.size();
	}

	@Override
	public Iterator loadColumnNames() throws IOException {
		return new ArrayIterator(columnNames.toArray());
	}

	@Override
	public Iterator loadRow() throws IOException {
		Iterator row = null;
		ArrayList <String> rowElements = new ArrayList<String>();
		
		int i = 0;
		while(i < columnNames.size()) {
			if( in != null ) {
				String line = in.readLine();
		
				if( line == null ) {
					in = null;
					break;
				}
					
				else{
					line = line.substring(1);
					line = line.replace('<', ' ');
					line = line.replace('>', ' ');
			        String [] wordlist = line.split(" ");
				        
				        
			        for (String word : wordlist) {
			        	word = word.replace(" ", "");
						if (word.charAt(0) == '/') {}
						else if(columnNames.contains(word)){}
						else if(check.equals(word)) {}
						else if(tableName.equals(word)){}
						else{
							rowElements.add(word);
							i++;
						}
					}
				}
			}
		}
		
		if (i == columnNames.size()) {
			String [] rowlist = new String[rowElements.size()];
	        rowlist = rowElements.toArray(rowlist);
			row = new ArrayIterator( rowlist );
		}
		
		return row;
	}

	@Override
	public void endTable() throws IOException {
	}
}
