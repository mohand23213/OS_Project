package application;

public class Space{
	public String spaceType;
	int limit;
	int base;
	Space(String type)
	{
		this.spaceType=type;
	}
	Space(String spaceType,int base,int limit)
	{
		this.spaceType=spaceType;
		this.limit=limit;
		this.base=base;
	}
	
	
}
