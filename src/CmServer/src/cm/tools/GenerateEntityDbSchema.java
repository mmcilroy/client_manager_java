package cm.tools;

import java.lang.reflect.Field;
import java.util.Date;

import cm.common.entity.domain.Address;
import cm.common.entity.domain.Appointment;
import cm.common.entity.domain.PersonalDetails;
import cm.common.entity.domain.Product;
import cm.common.entity.domain.Relationship;
import cm.common.entity.domain.Task;

public class GenerateEntityDbSchema
{
	public static void main( String[] args )
	{
//		generateSchema( Reservation.class );

		generateSchema( PersonalDetails.class );
		generateSchema( Address.class );
		generateSchema( Product.class );
		generateSchema( Task.class );
		generateSchema( Relationship.class );
		generateSchema( Appointment.class );
	}

	private static void generateSchema( Class<?> c )
	{
		for( int t=1; t<=2; t++ )
		{
			String tableName = c.getSimpleName();

			if( t == 2 ) {
				tableName = tableName + "History";
			}
		
			System.out.println( "drop table " + tableName + ";" );
			System.out.println( "create table " + tableName + " ( " );

			Field[] fields = c.getFields();
			for( int i=0; i<fields.length; i++ )
			{
				String col = fields[i].getName();
				String type;
	
				if( col.compareTo( "id" ) == 0 ) {
					type = "int NOT NULL AUTO_INCREMENT";
				}
				else
				if( ( tableName.compareTo( "PersonalDetails" ) == 0 || tableName.compareTo( "PersonalDetailsHistory" ) == 0 ) && col.compareTo( "birthDate" ) == 0 ) {
					type = "date";
				}
				else
				if( ( tableName.compareTo( "Task" ) == 0 || tableName.compareTo( "TaskHistory" ) == 0 ) && col.compareTo( "due" ) == 0 ) {
					type = "date";
				}
				else
				if( ( tableName.compareTo( "Appointment" ) == 0 || tableName.compareTo( "AppointmentHistory" ) == 0 ) && ( col.compareTo( "startTime" ) == 0 || col.compareTo( "endTime" ) == 0 ) ) {
					type = "time";
				}
				else
				if( ( tableName.compareTo( "Appointment" ) == 0 || tableName.compareTo( "AppointmentHistory" ) == 0 ) && col.compareTo( "day" ) == 0 ) {
					type = "date";
				}
				else {
					type = getType( fields[i] );
				}

				System.out.println( String.format( "    %s %s,", fields[i].getName(), type ) );
			}

			if( t == 1 )
			{
				System.out.println( "    PRIMARY KEY (`id`) " );
			}
			else
			{
				System.out.println( String.format( "    version int," ) );
				System.out.println( "    PRIMARY KEY (`id`,`version`) " );
			}
		
			System.out.println( ") ENGINE=InnoDB;" );
		}
	}

	private static String getType( Field f )
	{
		if( f.getType() == Integer.class ) {
			return "int";
		}
		else
		if( f.getType() == Float.class ) {
			return "float";
		}
		else
		if( f.getType() == Boolean.class ) {
			return "boolean";
		}
		else
		if( f.getType() == String.class || f.getType().isEnum() ) {
			return "text";
		}
		else
		if( f.getType() == Date.class ) {
			return "datetime";
		}

		return null; 
	}
}
