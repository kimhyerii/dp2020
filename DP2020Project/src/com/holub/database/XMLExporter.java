package com.holub.database;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class XMLExporter implements Table.Exporter {
	private final Writer out;
	private String tableName;
	private ArrayList<String> headers = new ArrayList<String>();
	
	public XMLExporter (Writer out) {
		this.out = out;
	}
	
	@Override
	public void startTable() throws IOException {
		out.write("<?xml version=\"1.0\"?>\n");
	}

	@Override
	public void storeMetadata(String tableName, int width, int height, Iterator columnNames) throws IOException {
		this.tableName = tableName;
		
		if (tableName == null) {
			this.tableName = "anonymous";
		}
		out.write("<" + tableName+ ">\n");
		
		while (columnNames.hasNext()) {			
			Object datum = columnNames.next();
			headers.add(datum.toString());
		}
		storeRow(columnNames);
	}

	@Override
	public void storeRow(Iterator data) throws IOException {
		Iterator headerIter = headers.iterator();

		if(data.hasNext()) {
			out.write("<data>\n");
			
			while(data.hasNext()){
				Object datum = data.next();
				Object header = headerIter.next();
				out.write("<" + header + ">" + datum.toString() + "</"+ header + ">\n");
			}
			
			out.write("</data>\n");
		}
	}

	@Override
	public void endTable() throws IOException {
		out.write("</" + tableName+ ">\n");
	}
}
