package com.holub.database;

import java.io.*;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class HTMLExporter implements Table.Exporter {
	private final Writer out;
	private int	width;
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
		this.width = width;
		int i = 0;
		
		out.write(tableName == null ? "<caption>anonymous</caption>" : "<caption>"+tableName+"</caption>" );
		out.write("<thead><tr>");
		
		while (i < width) {
			Object datum = columnNames.next();
			out.write("<th>" + datum.toString() + "</th>");
			i++;
		}
		
		out.write("</tr></thead>"
				+ "<tbody>");
		storeRow(columnNames);
		
	}

	@Override
	public void storeRow(Iterator data) throws IOException {
		out.write("<tr>");
		
		while(data.hasNext()){
			Object datum = data.next();
			out.write("<td>" + datum.toString() + "</td>");
		}

		out.write("</tr>");
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
