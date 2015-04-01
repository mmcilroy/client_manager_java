package cm.common.entity.domain;

import java.util.LinkedHashMap;

public class TaskHistoryCollection extends LinkedHashMap<Integer, TaskHistory>
{
	public void add( TaskHistory t ) {
		put( t.version, t );
	}

	private static final long serialVersionUID = 2954803908995153886L;
}
