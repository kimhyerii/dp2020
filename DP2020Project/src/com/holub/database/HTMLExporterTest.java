package com.holub.database;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.junit.jupiter.api.Test;

class HTMLExporterTest {
	Table people = TableFactory.create( "people",
			   new String[]{ "First", "Last"		} );
	
	@Test
	void testHTMLExporter() throws IOException {
		people.insert( new String[]{ "Allen",	"Holub" 	} );
		people.insert( new String[]{ "Ichabod",	"Crane" 	} );
		people.insert( new String[]{ "Rip",		"VanWinkle" } );
		people.insert( new String[]{ "Goldie",	"Locks" 	} );
		
		Writer out = new FileWriter("people.html");
		people.export(new HTMLExporter(out));
		out.close();
	}
}
