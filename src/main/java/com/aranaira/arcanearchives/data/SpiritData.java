package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.entity.personality.Personality;

public class SpiritData {
	
	public SpiritType type;
	public SpiritAge age;
	public Personality personality;
	
	public SpiritData()
	{
		this.type = SpiritType.GENERIC;
		this.age = SpiritAge.MOTE;
		SelectPersonality();
	}
	
	public SpiritData(SpiritType type, SpiritAge age)
	{
		this.type = type;
		this.age = age;
		SelectPersonality();
	}
	
	public SpiritData(SpiritType type, SpiritAge age, Personality personality)
	{
		this.type = type;
		this.age = age;
		this.personality = personality;
	}
	
	private void SelectPersonality()
	{
		this.personality = new Personality();
	}
	
	public SpiritType getType()
	{
		return this.type;
	}
	
	public SpiritAge getAge()
	{
		return this.age;
	}
	
	public Personality getPersonality()
	{
		return this.personality;
	}
	
	public enum SpiritType 
	{
		GENERIC, CROWN, MATTER, POWER, SPACE, TIME
	}
	
	public enum SpiritAge
	{
		MOTE, JUVE, ELD
	}
}
