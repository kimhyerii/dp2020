package com.holub.database;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class XMLTest {
	Table people = TableFactory.create( "people",
			   new String[]{ "First", "Last"		} );
	
	@Test
	void testXML() throws IOException {
		people.insert( new String[]{ "Allen",	"Holub" 	} );
		people.insert( new String[]{ "Ichabod",	"Crane" 	} );
		people.insert( new String[]{ "Rip",		"VanWinkle" } );
		people.insert( new String[]{ "Goldie",	"Locks" 	} );
		
		Writer out = new FileWriter("people.xml");
		people.export(new XMLExporter(out));
		out.close();
		
		Reader in = new FileReader("people.xml");
		Table inppl = new ConcreteTable(new XMLImporter(in));
		in.close();
	
		Cursor cur1 = people.rows();
		Cursor cur2 = inppl.rows();
		
		while (cur1.advance() && cur2.advance()) {
			for (Iterator columns = cur1.columns(), columns2 = cur2.columns(); 
					columns.hasNext(); columns2.hasNext()) {
				String tmp1 = (String)columns.next();
				String tmp2 = (String)columns2.next();
				assertEquals(tmp1, tmp2);
			}
		}
	}

}
