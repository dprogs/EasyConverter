package com.dprogs.EasyConverter.list;

public class EntryItem implements Item
{
	public final String title;
	public final String subtitle;

	/**
	 * EntryItem
	 * @param title
	 * @param subtitle
	 */
	public EntryItem(String title, String subtitle) 
	{
		this.title = title;
		this.subtitle = subtitle;
	}
	
	@Override
	public boolean isSection() 
	{
		return false;
	}

}
