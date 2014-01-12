import java.util.ArrayList;

/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 */


/*
 * This class is used as a container to send other classes the information read from a 
 * .txt file storing an object description
 * 
 * Each file has a series of attributes and related values
 * 
 */

public class GameFileContents {
	
	//Attributes
	ArrayList<String> attributes; //List of attributes read from file
	ArrayList<String> values; //List of values associated with attributes
	
	//Constructor
	public GameFileContents(ArrayList<String> attributes, ArrayList<String> values)
	{
		this.attributes = attributes;
		this.attributes.trimToSize();
		this.values = values;
		this.values.trimToSize();
		
		if(values.size() != attributes.size())
			System.out.println("Warning - size of values and attribute not equal.");
	}
	
	//Gets value of attribute contained inside
	public String getValue(String attribute)
	{
		if(containsAttribute(attribute))
			return(values.get(attributes.indexOf(attribute)));
		else
		{
			System.out.println("Warning - attribute \'" + attribute + "\' not defined for object.");
			return null;
		}
	}
	
	//Checks if attribute is contained
	public boolean containsAttribute(String attribute)
	{
		return attributes.contains(attribute);
	}
	
	//Converts object to string
	public String toString()
	{
		String out = "";
		
		for(int i = 0; i < attributes.size(); i++)
		{
			out += attributes.get(i) + "|" + values.get(i) + "\n";
		}
		
		return out;
	}

}
