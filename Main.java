import java.io.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class Main {
	static Scanner sc = new Scanner(System.in);
	WristCuff wc;
	Set<Integer> chiralFrequencies;
	
	// Uses GSON to read the file inputed by the user
	private void readFile() 
	{
		boolean read = false;
		while (!read)
		{
			System.out.print("Please provide timefall shelter data source: ");
			String data = sc.nextLine();
			StringBuilder sb = new StringBuilder();
			try (BufferedReader file = new BufferedReader(new FileReader(data))) 
			{
				String str;
				while ((str = file.readLine()) != null) 
				{
					sb.append(str);
				}
				ArrayList<TimefallShelter> shelters = new Gson().fromJson(sb.toString(),new TypeToken<List<TimefallShelter>>() {}.getType());
				if (shelters != null) 
				{
					boolean fail = false;
					Pattern phonePattern = Pattern.compile("[+]\\s*[0-9]+\\s*[(]\\s*[0-9]\\s*[0-9]\\s*[0-9]\\s*[)]\\s*[0-9]\\s*[0-9]\\s*[0-9]\\s*[-]\\s*[0-9]\\s*[0-9]\\s*[0-9]\\s*[0-9]\\s*");
					Pattern guidPattern = Pattern.compile("([a-f0-9A-F]{8}(-[a-f0-9A-F]{4}){4}[a-f0-9A-F]{8})");
					for (var shelter : shelters) 
					{
						String guid = shelter.getGuid();
						String name = shelter.getName();
						String phone = shelter.getPhone();
						String address = shelter.getAddress();
						boolean missingParameters = (guid == null || name == null || phone == null || address == null);
						if (missingParameters && !fail) 
						{
							System.out.println("Missing data parameters, please try again");
							fail = true;
						} 
						else 
						{
							Matcher phoneMatcher = phonePattern.matcher(phone);
							boolean phoneMatchLength = true;;
							if (phoneMatcher.matches())
							{
								phoneMatchLength =  (phoneMatcher.group(0).toString().length() == phone.length());						
							}
							else if (!phoneMatchLength) 
							{
								System.out.println("Phone number in incorrect format, please enter another file");
							}
							Matcher guidMatcher = guidPattern.matcher(guid);
							boolean guidMatchLength = true;
							if (guidMatcher.matches())
							{
								guidMatchLength = (guidMatcher.group(0).toString().length() == guid.length());
							}
							else if (!guidMatchLength) 
							{
								System.out.println("Guid in incorrect format, please enter another file");
							}
							if (!phoneMatcher.matches() || !phoneMatchLength || !guidMatcher.matches() || !guidMatchLength)
							{
								fail = true;
							}

						}
					}
					if (!fail)
					{
						System.out.println("=== Data accepted === \n");
						wc = new WristCuff(shelters, data);
						read = true;
					}
				}
				else 
				{
					System.out.println("Could not find shelters in the file.");
				}
			} 
			catch (FileNotFoundException e)
			{
				System.out.println("The file " + data + " could not be found.");
				System.out.println();
			} 
			catch (JsonIOException e) 
			{
				System.out.println("Error with reading file.  Please try again.");
				System.out.println();
			} 
			catch (JsonParseException e) 
			{
				System.out.println("Could not parse the file " + data + ".");
				System.out.println();
			}
		}
	}

	// Gets the supported chiral frequencies from the user
	private void setSupportedFrequencies() 
	{
		chiralFrequencies = new HashSet<Integer>();
		boolean valid = false;
		while (!valid) {
			System.out.print("Please provide supported Chiral frequencies: ");
			String str = sc.nextLine();
			if (str == "")
			{
				chiralFrequencies = null;
				return;
			}
			var s = str.split(","); 
			valid = true;
			for (int i = 0; i < s.length; i++) 
			{
				s[i] = s[i].trim(); 
				try 
				{
					chiralFrequencies.add(Integer.parseInt(s[i]));
				} 
				catch (NumberFormatException e) 
				{
					valid = false;
				}
			}
			if (!valid)
			{
				chiralFrequencies.clear();
				System.out.println("Invalid chiral frequency detected");
				System.out.println("Please re-enter the frequncies");
			}
		}

	}

	// Prints the option menu
	private static void displayOptions() 
	{
		System.out.println("\n\t1) List all available shelters within the min and max of supported Chiral frequencies\n"
				+ "\t2) Search for a shelter by Chiral frequency\n" + "\t3) Search for a shelter by name\n"
				+ "\t4) Sort shelters by Chiral frequency\n"
				+ "\t5) Jump to a shelter with the lowest supported Chiral frequency");
	}

	// searches shelters by name
	private void searchByName() 
	{
		boolean found = false;
		while (!found) {
			System.out.print("\nWhat's the shelter name you are looking for? ");
			String name = sc.nextLine();
			TimefallShelter shelter = wc.searchByName(name);
			if (shelter == null) {
				System.out.println("\nNo such shelter...");
			} else {
				found = true;
				System.out.println("\nFound!\n");
				System.out.println(shelter);
			}
		}
	}

	// searches shelters by frequency
	private void searchByFrequency() 
	{
		boolean found = false;
		while (!found) 
		{
			try 
			{
				System.out.print("\nWhat Chiral frequency are you looking for? ");
				int frequency = Integer.parseInt(sc.nextLine());
				TimefallShelter shelter = wc.searchByFrequency(frequency);
				if (shelter == null) 
				{
					System.out.println("\nThat Chiral frequency does not exist.");
				} 
				else 
				{
					found = true;
					System.out.println("\n" + shelter);
				}
			} 
			catch (NumberFormatException e) 
			{
				System.out.println("\nInvalid input, please enter a number.");
			}
		}
	}

	// finds shelter to teleport to
	private void findShelter() 
	{
		try 
		{
			wc.sortShelters();
			System.out.println("\nShelters successfully sorted by Chiral frequency.");
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("File path" + wc.file + " not found");
		}
		boolean found = false;
		System.out.println("\n=== Commencing timefall shelter search ===");
		while (!found) {
			TimefallShelter shelter = wc.findShelter(chiralFrequencies);
			if (shelter == null) 
			{
				System.out.println("\n=== No shelter available. You are DOOMED. ===");
				return;
			} 
			else 
			{
				if (!shelter.getTimefall()) 
				{
					found = true;
					System.out.println("=== Matching timefall shelter found! ===");
					System.out.println(shelter);
				} 
				else 
				{
					System.out.println("=== Chiral frequency " + shelter.getChiralFrequency() + " unstable, Chiral jump unavailable. ===");
					System.out.println("=== Removing target shelter from the list of shelters and saving updated list to disk. ===\n");
					try 
					{
						wc.removeShelter(shelter);
					} 
					catch (FileNotFoundException e) 
					{
						System.out.println("Error, cannot save to file" + wc.file + ".");
					}
				}
			}
		}
		System.out.println("=== Commencing Chiral jump, see you in safety. ===");
	}
	
	// takes user input and executes corresponding option
	public static boolean choose(int choice, Main solution) 
	{
		if (choice == 1) 
		{
			solution.wc.listAllShelters(solution.chiralFrequencies);
			return false;
		}
		else if (choice == 2) 
		{
			solution.searchByFrequency();
			return false;
		} 
		else if (choice == 3) 
		{
			solution.searchByName();
			return false;
		} 
		else if (choice == 4) 
		{
			try 
			{
				solution.wc.sortShelters();
				System.out.println("\nShelters successfully sorted by Chiral frequency.");
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("File path" + solution.wc.file + " not found");
			}
			return false;
		} 
		else if (choice == 5) 
		{
			solution.findShelter();
			return true;
		}
		return false;
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) 
	{
		Main solution = new Main();
		System.out.println("Welcome to Bridges Link.\n");
		solution.readFile();
		solution.setSupportedFrequencies();
		boolean terminate = false;
		while (!terminate) 
		{
			int choice = 0;
			while (choice < 1 || choice > 5) 
			{
				solution.displayOptions();
				System.out.print("Choose an option: ");
				choice = Integer.parseInt(sc.nextLine());
			}
			terminate = choose(choice, solution);
		}
		sc.close();
	}
}