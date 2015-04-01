package cm.common.entity.db;

import java.lang.reflect.Field;
import java.sql.ResultSet;

import cm.common.entity.Entity;

public class EntityDbUtil
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Entity toEntity( ResultSet rs, Entity e ) throws EntityDbError
	{
		String table = e.getClass().getSimpleName();
	
		try
		{
			for( Field field : e.getClass().getFields() )
			{
				String col = table + "." + field.getName();

				if( field != null )
				{
					if( field.getType().isEnum() )
					{
						String val = rs.getString( col );
						if( val != null )
						{
							Enum v = Enum.valueOf( (Class<Enum>)field.getType(), val );
							field.set( e, v );
							System.out.println( col + " = " + v.toString() );
						}
					}
					else
					{
						Object o = rs.getObject( col );
						field.set( e, o );
						if( o != null ) {
							System.out.println( col + " = " + o.toString() );
						} else {
							System.out.println( col + " = null" );
						}
					}
				}
			}

			return e;
		}
		catch( Exception e1 )
		{
			throw new EntityDbError( "Failed to convert " + table + " ResultSet", e1 );
		}
	}
}
