import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WristCuff {

	ArrayList<TimefallShelter> shelters;
	String file;

	// set initial values for needed members
	public WristCuff(ArrayList<TimefallShelter> s, String f) 
	{
		shelters = s;
		file = f;

	}

	// list all available shelters within the min and max of supported Chiral frequencies
	void listAllShelters(Set<Integer> chiralFrequencies) 
	{
		if (chiralFrequencies == null)
		{
			return;
		}
		ArrayList<TimefallShelter> availible = new ArrayList<TimefallShelter>();
		for (var shelter : shelters) 
		{
			if (chiralFrequencies.contains(shelter.getChiralFrequency()) && !shelter.getTimefall()) 
			{
				availible.add(shelter);
			}
		}
		System.out.println("\n" + availible.size() + " results");
		for (var shelter : availible) 
		{
			System.out.println("\n" + shelter);
		}
	}

	// search timefall shelters by name
	public TimefallShelter searchByName(String name) 
	{
		for (var shelter : shelters) 
		{
			if (shelter.getName().equalsIgnoreCase(name)) // source: https://www.geeksforgeeks.org/java-string-equalsignorecase-method-with-examples/
			{
				return shelter;
			}
		}

		return null;
	}

	// search timefall shelters by chiral frequency
	public TimefallShelter searchByFrequency(int chiralFrequency) 
	{
		for (var shelter : shelters) 
		{
			if (shelter.getChiralFrequency() == chiralFrequency) 
			{
				return shelter;
			}
		}
		return null;
	}

	// find an available shelter with the lowest supported Chiral frequency
	public TimefallShelter findShelter(Set<Integer> chiralFrequencies) 
	{
		if (chiralFrequencies == null)
		{
			return null;
		}
		TimefallShelter l = null;
		for (var shelter : shelters) 
		{
			int freq = shelter.getChiralFrequency();
			if (l == null && chiralFrequencies.contains(freq))
			{
				l = shelter;
			} 
			else if (chiralFrequencies.contains(freq) && (freq < l.getChiralFrequency()))
			{
				l = shelter;
			}
		}
		return l;
	}
	
	
	// removes timefall shelter
	public void removeShelter(TimefallShelter shelter)  throws FileNotFoundException
	{
		shelters.remove(shelter);
		save();
	}

	// sort shelters by Chiral frequency
	public void sortShelters() throws FileNotFoundException 
	{
		shelters.sort(null);
		save();
	}

	// saves the updated list of shelters to disk
	// source: https://stackoverflow.com/questions/29319434/how-to-save-data-with-gson-in-a-json-file
	public void save() throws FileNotFoundException 
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String str = gson.toJson(shelters);
		BufferedWriter bw = null;
		try 
		{
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(str);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{	
			if (bw != null) 
			{ 
				try 
				{
					bw.close();
				} 
				catch (IOException e) 
				{
					System.out.println("Error, cannot complete save to file" + file + ".");
				}
			}
		}
	}
}