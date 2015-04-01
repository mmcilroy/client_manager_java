package cm.common.types;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import cm.common.entity.domain.Address;
import cm.common.entity.domain.PersonalDetails;
import cm.common.entity.domain.Task;

public class TaskSearchResult implements Serializable
{
	public void add( Task t, PersonalDetails d, Address a )
	{
		entries.put( t.id, new Entry( t, d, a ) );
	}

	public Entry get( int id )
	{
		return entries.get( id );
	}

	public int size()
	{
		return entries.size();
	}

	public Iterator<Entry> iterator()
	{
		return entries.values().iterator();
	}

	public Collection<Entry> values()
	{
		return entries.values();
	}

	public class Entry implements Serializable
	{
		Entry( Task t, PersonalDetails d, Address a )
		{
			task = t;
			details = d;
			address = a;
		}

		public Task task;
		public PersonalDetails details;
		public Address address;

		private static final long serialVersionUID = -1455282647627574723L;
	}

	private LinkedHashMap<Integer, Entry> entries = new LinkedHashMap<Integer, Entry>();

	private static final long serialVersionUID = 1930505283100214845L;
}
