public class TimefallShelter implements Comparable<TimefallShelter> 
{
	private int chiralFrequency;
	private boolean timefall;
	private String name;
	private String guid;
	private String phone;
	private String address;
	
	
	// get functions
	public int getChiralFrequency()
	{
		return chiralFrequency;
	}
	
	public boolean getTimefall()
	{
		return timefall;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getGuid()
	{
		return guid;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	// set functions
	public void setChiralFrequency(int i)
	{
		chiralFrequency = i;
	}
	
	public void setTimefall(boolean b)
	{
		timefall = b;
	}
	
	public void setName(String str)
	{
		name = str;
	}
	
	public void setGuid(String str)
	{
		guid = str;
	}	

	public void setPhone(String str)
	{
		phone = str;
	}
	
	public void setAddress(String str)
	{
		address = str;
	}
	

	// compare shelters by their chiral frequency
	@Override
	public int compareTo(TimefallShelter shelter) {
		
		if(chiralFrequency < shelter.chiralFrequency)
		{
			return -1;
		}
		else if(chiralFrequency > shelter.chiralFrequency) 
		{
			return 1;
		}
		return 0;
	}

	// string representation of a shelter
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Shelter information:");
		sb.append("\n- Chiral frequency: " + chiralFrequency);
		sb.append("\n- Timefall: ");
		if (timefall) 
		{
			sb.append("Current");
		}
		else
		{
			sb.append("None");
		}
		sb.append("\n- GUID: " + guid);
		sb.append("\n- Name: " + name);
		sb.append("\n- Phone: " + phone);
		sb.append("\n- Address: " + address);
		sb.append("\n");
		return sb.toString();
	}
}