package com.holub.database;

import java.io.*;
import java.util.Iterator;

public class HTMLExporter implements Table.Exporter {
	private final Writer out;
	private int	height;
	
	public HTMLExporter(Writer out)
	{
		this.out = out;
	}
	
	@Override
	public void startTable() throws IOException {
		out.write("<html>"
				+ "<body>"
				+ "<table border=\"1\">");
	}

	@Override
	public void storeMetadata(String tableName, int width, int height, Iterator columnNames) throws IOException {
		out.write(tableName == null ? "<caption>anonymous</caption>" : "<caption>"+tableName+"</caption>" );
		out.write("<thead><tr>");
		
		while (columnNames.hasNext()) {
			Object datum = columnNames.next();
			out.write("<th>" + datum.toString() + "</th>");
		}
		
		out.write("</tr></thead>"
				+ "<tbody>");
		storeRow(columnNames);
		
	}

	@Override
	public void storeRow(Iterator data) throws IOException {
		if(data.hasNext()) {
			out.write("<tr>");
			
			while(data.hasNext()){
				Object datum = data.next();
				out.write("<td>" + datum.toString() + "</td>");
			}
	
			out.write("</tr>");
		}
	}

	@Override
	public void endTable() throws IOException {
		out.write("</tbody>"
				+ "</table>"
				+ "</body>"
				+ "</html>");
	}
	
	public static class Test{ 	
		public static void main( String[] args ) throws IOException
		{	
			Table people = TableFactory.create( "people",
						   new String[]{ "First", "Last"		} );
			people.insert( new String[]{ "Allen",	"Holub" 	} );
			people.insert( new String[]{ "Ichabod",	"Crane" 	} );
			people.insert( new String[]{ "Rip",		"VanWinkle" } );
			people.insert( new String[]{ "Goldie",	"Locks" 	} );
			
			Writer out = new FileWriter("people.html");
			people.export(new HTMLExporter(out));
			out.close();
		}
	}

}
