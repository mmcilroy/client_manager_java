package cm.common.entity.domain;

import java.util.LinkedHashMap;

public class TaskCollection extends LinkedHashMap<Integer, Task>
{
	public void add( Task t ) {
		put( t.id, t );
	}

	private static final long serialVersionUID = 2954803908995153886L;
}
