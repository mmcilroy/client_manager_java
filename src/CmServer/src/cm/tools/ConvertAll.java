package cm.tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;

import cm.common.entity.db.EntityDbConn;
import cm.common.entity.domain.Address;
import cm.common.entity.domain.Appointment;
import cm.common.entity.domain.Customer;
import cm.common.entity.domain.PersonalDetails;
import cm.common.entity.domain.Product;
import cm.common.entity.domain.Relationship;
import cm.common.entity.domain.Task;
import cm.common.types.AppointmentType;
import cm.common.types.County;
import cm.common.types.EmploymentStatus;
import cm.common.types.MaritalStatus;
import cm.common.types.ProductType;
import cm.common.types.RelationshipType;
import cm.common.types.TaskAction;
import cm.common.types.TaskStatus;
import cm.common.types.TaskType;
import cm.common.types.Title;
import cm.server.CustomerService;


public class ConvertAll
{
	public static void main( String[] args ) throws Exception
	{
		BasicConfigurator.configure();

		newdb = new EntityDbConn( "31.222.191.71:8001", "DundonaldAgencies", "root", "radiat0r" );
		server = new CustomerService( newdb, null, "mmcilroy" );
		cleanDb( newdb );

		new ConvertAll( "dundonald" );
		new ConvertAll( "ballyclare" );

		EntityDbConn appdb = new EntityDbConn( "31.222.191.71:8001", "appointments", "root", "radiat0r" );
		ResultSet rs = appdb.execute( "select * from appointments" );

		while( rs.next() )
		{
			Appointment a = new Appointment();

			String type = rs.getString( "type" );
			if( type.compareTo( "Other" ) == 0 ) {
				a.type = AppointmentType.OTHER;
			}
			else
			if( type.compareTo( "Savings Review" ) == 0 ) {
				a.type = AppointmentType.SAVINGS_REVIEW;
			}
			else
			if( type.compareTo( "Mortgage & Protection Review" ) == 0 ) {
				a.type = AppointmentType.MORTGAGE_REVIEW;
			}
			else
			if( type.compareTo( "Current Account Opening" ) == 0 ) {
				a.type = AppointmentType.CURRENT_ACCOUNT_OPENING;
			}
			else
			if( type.compareTo( "Credit Card Opening" ) == 0 ) {
				a.type = AppointmentType.CREDIT_CARD_OPENING;
			}
			else {
				System.err.println( "Unknown appointment type: " + type ); return;
			}

			a.calendar = rs.getString( "calendar" );
			a.day = rs.getDate( "day" );
			a.startTime = rs.getTime( "startTime" );
			a.endTime = rs.getTime( "endTime" );
			a.note = rs.getString( "notes" );

			String clientSite = rs.getString( "clientsite" );
			int clientId = rs.getInt( "clientid" );

			try
			{
				if( clientSite != null && clientSite.length() > 0 && clientId > 0 ) {
					a.customerId = getCustomerId( clientSite, clientId );
				}
			}
			catch( Exception e ) {
				;
			}

			newdb.insert( a );
		}
	}

	ConvertAll( String db ) throws Exception
	{
		char dbId = db.toUpperCase().charAt( 0 );
		EntityDbConn olddb = new EntityDbConn( "31.222.191.71:8001", db, "root", "radiat0r" );

		ResultSet rs = olddb.execute( "select * from client" );
		while( rs.next() )
		{
			PersonalDetails pd = toPersonalDetails( rs, db.toUpperCase() );
			Address a = toAddress( rs );

			String key = addressKey( a );
			if( !addressKeys.contains( key ) )
			{
				newdb.insert( a );
				addressKeys.add( key );
			}

			Customer c = server.createCustomer( a, pd );
			if( c == null ) {
				throw new Exception( "createCustomer failed" );
			}

			mapClientIdToCustomerId.put( String.format( "%c.%d", dbId, rs.getInt( "Id" ) ), c.personalDetails.id );
		}

		rs = olddb.execute( "select * from savings" );
		while( rs.next() )
		{
			Product p = toSavingsProduct( db, rs );
			server.createProduct( p, false );
			mapEntityIdToProductId.put( String.format( "%c.%s", dbId, rs.getString( "EntityId" ) ), p.id );
		}

		rs = olddb.execute( "select * from banking" );
		while( rs.next() )
		{
			Product p = toBankingProduct( db, rs );
			server.createProduct( p, false );
			mapEntityIdToProductId.put( String.format( "%c.%s", dbId, rs.getString( "EntityId" ) ), p.id );
		}

		rs = olddb.execute( "select * from creditcard" );
		while( rs.next() )
		{
			Product p = toCreditProduct( db, rs );
			server.createProduct( p, false );
			mapEntityIdToProductId.put( String.format( "%c.%s", dbId, rs.getString( "EntityId" ) ), p.id );
		}

		rs = olddb.execute( "select * from diary where Status='Outstanding' and Active='Y'" );
		while( rs.next() )
		{
			Task t = toTask( db, rs );
			server.createTask( t );
		}

		char dbKey = db.toUpperCase().charAt( 0 );

		rs = olddb.execute( "select * from relations" );
		while( rs.next() )
		{
			Relationship r = toRelationship( db, rs );
			if( r.customer1.compareTo( r.customer2 ) != 0 )
			{
				if( !relationshipKeys.contains( String.format( "%c.%d.%d", dbKey, r.customer2, r.customer1 ) ) )
				{
					server.createRelationship( r );
					relationshipKeys.add( String.format( "%c.%d.%d", dbKey, r.customer1, r.customer2 ) );
				}
			}
		}
	}

	static void cleanDb( EntityDbConn db ) throws SQLException
	{
		db.execute( "delete from PersonalDetails;" );
		db.execute( "delete from PersonalDetailsHistory;" );
		db.execute( "delete from Address;" );
		db.execute( "delete from AddressHistory;" );
		db.execute( "delete from Product;" );
		db.execute( "delete from ProductHistory;" );
		db.execute( "delete from Task;" );
		db.execute( "delete from TaskHistory;" );
		db.execute( "delete from Relationship;" );
		db.execute( "delete from RelationshipHistory;" );
		db.execute( "delete from Appointment;" );
		db.execute( "delete from AppointmentHistory;" );
		db.execute( "delete from Transaction;" );

		db.execute( "alter table PersonalDetails AUTO_INCREMENT=1;" );
		db.execute( "alter table Address AUTO_INCREMENT=1;" );
		db.execute( "alter table Product AUTO_INCREMENT=1;" );
		db.execute( "alter table Task AUTO_INCREMENT=1;" );
		db.execute( "alter table Relationship AUTO_INCREMENT=1;" );
		db.execute( "alter table Appointment AUTO_INCREMENT=1;" );
		db.execute( "alter table Transaction AUTO_INCREMENT=1;" );
	}

	String addressKey( Address a ) {
		return String.format( "%s:%s:%s:%s:%s", a.line1, a.line2, a.town, a.county, a.postcode ).replace( " ", "" ).trim();
	}

	PersonalDetails toPersonalDetails( ResultSet rs, String biz ) throws Exception
	{
		PersonalDetails pd = new PersonalDetails();
		pd.businessOwner = biz;
		pd.forename = getUTString( rs, "forename" );
		pd.middlename = getUTString( rs, "middlename" );
		pd.surname = getUTString( rs, "surname" );
		pd.homePhone = getUTString( rs, "homePhone" );
		pd.mobilePhone = getUTString( rs, "mobilePhone" );
		pd.email = getUTString( rs, "email" );

		String dob = rs.getString( "DOB" );
		if( dob != null && dob.length() > 0 ) {
			pd.birthDate = dateFormatDOB.parse( dob );
		}

		String title = rs.getString( "title" );

		if( title != null && title.length() > 0 )
		{
			if( title.compareTo( "DR" ) == 0 ) {
				pd.title = Title.DR;
			}
			else
			if( title.compareTo( "MASTER" ) == 0 ) {
				pd.title = Title.MASTER;
			}
			else
			if( title.compareTo( "MISS" ) == 0 ) {
				pd.title = Title.MISS;
			}
			else
			if( title.compareTo( "MR" ) == 0 ) {
				pd.title = Title.MR;
			}
			else
			if( title.compareTo( "MRS" ) == 0 ) {
				pd.title = Title.MRS;
			}
			else
			if( title.compareTo( "MS" ) == 0 ) {
				pd.title = Title.MS;
			}
			else
			if( title.compareTo( "REV" ) == 0 ) {
				pd.title = Title.REV;
			}
			else {
				throw new Exception( "Unknown title: " + title );
			}
		}
		else
		{
			pd.title = Title.MR;
		}
	
		String employment = rs.getString( "employment" );

		if( employment != null && employment.length() > 0 )
		{
			if( employment.compareTo( "Full Time" ) == 0 ) {
				pd.employmentStatus = EmploymentStatus.FULL_TIME;
			}
			else
			if( employment.compareTo( "Homemaker" ) == 0 ) {
				pd.employmentStatus = EmploymentStatus.HOMEMAKER;
			}
			else
			if( employment.compareTo( "Part Time" ) == 0 ) {
				pd.employmentStatus = EmploymentStatus.PART_TIME;
			}
			else
			if( employment.compareTo( "Retired" ) == 0 ) {
				pd.employmentStatus = EmploymentStatus.RETIRED;
			}
			else
			if( employment.compareTo( "Self Employed" ) == 0 ) {
				pd.employmentStatus = EmploymentStatus.SELF_EMPLOYED;
			}
			else
			if( employment.compareTo( "Student" ) == 0 ) {
				pd.employmentStatus = EmploymentStatus.STUDENT;
			}
			else
			if( employment.compareTo( "Unemployed" ) == 0 ) {
				pd.employmentStatus = EmploymentStatus.UNEMPLOYED;
			}
			else {
				throw new Exception( "Unknown employment status: " + employment );
			}
		}

		String marital = rs.getString( "maritalStatus" );

		if( marital != null && marital.length() > 0 )
		{
			if( marital.compareTo( "Divorced" ) == 0 ) {
				pd.maritalStatus = MaritalStatus.DIVORCED;
			}
			else
			if( marital.compareTo( "Living with spouse" ) == 0 ) {
				pd.maritalStatus = MaritalStatus.LIVING_WITH_SPOUSE;
			}
			else
			if( marital.compareTo( "Married" ) == 0 ) {
				pd.maritalStatus = MaritalStatus.MARRIED;
			}
			else
			if( marital.compareTo( "Seperated" ) == 0 ) {
				pd.maritalStatus = MaritalStatus.SEPERATED;
			}
			else
			if( marital.compareTo( "Single" ) == 0 ) {
				pd.maritalStatus = MaritalStatus.SINGLE;
			}
			else
			if( marital.compareTo( "Widower" ) == 0 ) {
				pd.maritalStatus = MaritalStatus.WIDOWER;
			}
			else {
				throw new Exception( "Unknown marital status: " + marital );
			}
		}

		return pd;
	}

	Address toAddress( ResultSet rs ) throws SQLException
	{
		Address a = new Address();
		a.line1 = getUTString( rs, "address1" );
		a.line2 = getUTString( rs, "address2" );
		a.town = getUTString( rs, "town" );
		a.postcode = getUTString( rs, "postcode" ).replace( " ", "" );

		String county = getUTString( rs, "county" );
		if( county.compareTo( "ANTRIM" ) == 0 ) {
			a.county = County.ANTRIM;
		}
		else
		if( county.compareTo( "ARMAGH" ) == 0 ) {
			a.county = County.ARMAGH;
		}
		else
		if( county.compareTo( "TYRONE" ) == 0 ) {
			a.county = County.TYRONE;
		}
		else
		if( county.compareTo( "FERMANAGH" ) == 0 ) {
			a.county = County.FERMANAGH;
		}
		else
		if( county.compareTo( "DOWN" ) == 0 ) {
			a.county = County.DOWN;
		}

		return a;
	}

	Product toSavingsProduct( String db, ResultSet rs ) throws Exception
	{
		Product p = new Product();
    	p.customerId = getCustomerId( db, rs.getInt( "ClientId" ) );
		p.type = ProductType.SAVINGS;
    	p.accountNumber = getUTString( rs, "Number" );
    	p.dateOpened = dateFormat.parse( rs.getString( "DateOpened" ) );
    	p.interestRate = toFloat( rs.getString( "InterestRate" ) );
    	p.openingBalance = toFloat( rs.getString( "InitialLumpSum" ) );
		p.active = rs.getString( "Active" ).charAt( 0 ) == 'Y';

		String name = rs.getString( "Type" ) + " " + rs.getString( "Name" );

        if( name.compareTo( "Childrens Flexible Saver for Kids" ) == 0 ) {
            p.name = "FLEXI KIDS SAVER";
        }
        else
        if( name.compareTo( "Instant Access Branch Saver" ) == 0 ) {
            p.name = "BRANCH SAVER";
        }
        else
        if( name.compareTo( "Instant Access Instant Access Saver" ) == 0 ) {
            p.name = "FLEXI CARD / BOOK";
        }
        else
        if( name.compareTo( "Savings Fixed 1 Year Fixed Rate Bond" ) == 0 ) {
            p.name = "1 YEAR BOND";
        }
        else
        if( name.compareTo( "Savings Fixed 2 Year Fixed Rate Bond" ) == 0 ) {
            p.name = "2 YEAR BOND";
        }
        else
        if( name.compareTo( "Savings Fixed 3 Year Fixed Rate Bond" ) == 0 ) {
            p.name = "3 YEAR BOND";
        }
        else
        if( name.compareTo( "Savings Tax Free 1 Year Postal ISA" ) == 0 ) {
            p.name = "1 YEAR FIXED ISA";
        }
        else
        if( name.compareTo( "Savings Tax Free 2 Year Postal ISA" ) == 0 ) {
            p.name = "2 YEAR FIXED ISA";
        }
        else
        if( name.compareTo( "Savings Tax Free Direct ISA" ) == 0 ) {
            p.name = "DIRECT ISA";
        }
        else
        if( name.compareTo( "Savings Tax Free Easy ISA" ) == 0 ) {
            p.name = "EASY ISA";
        }
        else
        {
        	p.type = ProductType.LEGACY_SAVINGS;
            p.name = name.toUpperCase().trim();
        }

		return p;
	}

	Product toBankingProduct( String db, ResultSet rs ) throws Exception
	{
		Product p = new Product();
		p.customerId = getCustomerId( db, rs.getInt( "ClientId" ) );
		p.accountNumber = getUTString( rs, "Number" );
    	p.dateOpened = dateFormat.parse( rs.getString( "ApplicationDate" ) );
		p.active = rs.getString( "Active" ).charAt( 0 ) == 'Y';

    	p.type = ProductType.LEGACY_BANKING;
        p.name = rs.getString( "Type" ) + " " + rs.getString( "Name" );

		return p;
	}

	Product toCreditProduct( String db, ResultSet rs ) throws Exception
	{
		Product p = new Product();
		p.customerId = getCustomerId( db, rs.getInt( "ClientId" ) );
    	p.dateOpened = dateFormat.parse( rs.getString( "ApplicationDate" ) );
		p.active = rs.getString( "Active" ).charAt( 0 ) == 'Y';

    	p.type = ProductType.LEGACY_CREDIT_CARD;
        p.name = rs.getString( "Type" ) + " " + rs.getString( "Name" );

		return p;
	}

	Task toTask( String db, ResultSet rs ) throws Exception
	{
		Task t = new Task();
		t.due = rs.getDate( "Date" );
		t.note = rs.getString( "Notes" );
		t.customerId = getCustomerId( db, rs.getInt( "ClientId" ) );
		t.productId = getProductId( db, rs.getString( "AccountEntityId" ) );
		t.status = TaskStatus.OUTSTANDING;

		String action = rs.getString( "Task" );

		if( action.compareTo( "Appointment" ) == 0 ) {
			t.action = TaskAction.APPOINTMENT;
		}
		else
		if( action.compareTo( "Birthday Card" ) == 0 ) {
			t.action = TaskAction.BIRTHDAY_CALL;
		}
		else
		if( action.compareTo( "HI Renewal" ) == 0 ) {
			t.action = TaskAction.HI_RENEWAL;
		}
		else
		if( action.compareTo( "Hot Lead" ) == 0 ) {
			t.action = TaskAction.HOT_LEAD;
		}
		else
		if( action.compareTo( "Job" ) == 0 ) {
			t.action = TaskAction.JOB;
		}
		else
		if( action.compareTo( "New Business" ) == 0 ) {
			t.action = TaskAction.NEW_BUSINESS;
		}
		else
		if( action.compareTo( "Phone" ) == 0 ) {
			t.action = TaskAction.PHONE;
		}
		else
		if( action.compareTo( "Review" ) == 0 ) {
			t.action = TaskAction.REVIEW;
		}
		else
		if( action.compareTo( "Write" ) == 0 ) {
			t.action = TaskAction.WRITE;
		}
		else
		if( action.compareTo( "DD Review" ) == 0 ) {
			t.action = TaskAction.DD_REVIEW;
		}
		else
		if( action.compareTo( "Coursety Call" ) == 0 ) {
			t.action = TaskAction.COURTESY_CALL;
		}
		else {
			throw new Exception( "Unknown task action: " + action );
		}

		if( t.action == TaskAction.NEW_BUSINESS && t.productId != null && t.productId > 0 ) {
			t.type = TaskType.AUTO;
		}
		else
		if( t.action == TaskAction.REVIEW && t.productId != null && t.productId > 0 ) {
			t.type = TaskType.AUTO;
		}
		else {
			t.type = TaskType.MANUAL;
		}

		return t;
	}

	Relationship toRelationship( String db, ResultSet rs ) throws Exception
	{
		Relationship r = new Relationship();

		String type = rs.getString( "Type" );

		if( type.compareTo( "Family Member" ) == 0 ) {
			r.type = RelationshipType.FAMILY_MEMBER;
		}
		else
		if( type.compareTo( "Partner" ) == 0 ) {
			r.type = RelationshipType.PARTNER;
		}
		else
		if( type.compareTo( "Spouse" ) == 0 ) {
			r.type = RelationshipType.SPOUSE;
		}
		else {
			throw new Exception( "Unknown relationship type: " + type );
		}

		r.customer1 = getCustomerId( db, rs.getInt( "ClientId" ) );
		r.customer2 = getCustomerId( db, rs.getInt( "RelationClientId" ) );
		
		return r;
	}

	static Integer getCustomerId( String db, int clientId ) throws Exception
	{
		String key = String.format( "%c.%d", db.toUpperCase().charAt( 0 ), clientId );
		Integer i = mapClientIdToCustomerId.get( key );
		if( i == null ) {
			throw new Exception( "Unknown client id: " + key );
		} else {
			return i;
		}
	}

	Integer getProductId( String db, String entityId )
	{
		if( entityId != null && entityId.length() > 0 )
		{
			String key = String.format( "%c.%s", db.toUpperCase().charAt( 0 ), entityId );
			return mapEntityIdToProductId.get( key );
		}
		else
		{
			return null;
		}
	}

	String getUTString( ResultSet rs, String name ) throws SQLException {
		return rs.getString( name ).toUpperCase().trim();
	}

	Float toFloat( String s )
	{
		if( s == null ) {
			return null;
		}

		if( s.length() == 0 ) {
			return null;
		}

		String decimalPart = "";
		String fractionPart = "";;
		boolean afterDecimalPoint = false;
		for( int i=0; i<s.length(); i++ )
		{
			Character c = s.charAt( i ); 

			if( c == '.' ) {
				afterDecimalPoint = true;
			}

			if( Character.isDigit( c ) )
			{
				if( afterDecimalPoint ) {
					fractionPart += c;
				} else {
					decimalPart += c;
				}
			}
		}

		try {
			return Float.parseFloat( decimalPart + "." + fractionPart );
		} catch( Exception e ) {
			return null;
		}
	}

	static CustomerService server;
	static EntityDbConn newdb;

	static Map<String, Integer> mapClientIdToCustomerId = new HashMap<String, Integer>();

	Set<String> addressKeys = new HashSet<String>();

	Set<String> relationshipKeys = new HashSet<String>();

	Map<String, Integer> mapEntityIdToProductId = new HashMap<String, Integer>();

	SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
	SimpleDateFormat dateFormatDOB = new SimpleDateFormat( "yyyy-MM-dd" );
}
