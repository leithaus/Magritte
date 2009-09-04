package com.biosimilarity.magritte.util;

import java.io.*;
import java.lang.Thread;
import java.net.URL;

public class ResourceTest
{
    String _relativePathString = "";
    String _absolutePathString = "";

    public void testResource()
    {
	System.out.println("Testing relative path...");

	ClassLoader contextClassLoader =
	    Thread.currentThread().getContextClassLoader();
	
	URL relativeResourceURL =
	    contextClassLoader.getResource(_relativePathString);
	displayResource(relativeResourceURL);

	System.out.println("Testing absolute path...");

	URL absoluteResourceURL =
	    contextClassLoader.getResource(_absolutePathString);
	displayResource(absoluteResourceURL);
	
	System.out.println("Resource access test complete");
    }

    public ResourceTest()
    {
    }

    public ResourceTest( String relativePathString, String absolutePathString )
    {
	_relativePathString = relativePathString;
	_absolutePathString = absolutePathString;
    }

    private void displayResource(URL url)
    {
	if (url != null) 
	    {
		try {
		    InputStream is = url.openStream();
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
		    String nextLine = null;
		    while((nextLine = br.readLine()) != null)
			{
			    System.out.println(nextLine);
			}
		}
		catch (IOException ioe)
		    {
			ioe.printStackTrace();
		    }
	    }
	else
	    {
		System.out.println("Null resource");
	    }
    }

    public static void main(String[] args)
	throws IOException
    {
	new ResourceTest();
    }
}