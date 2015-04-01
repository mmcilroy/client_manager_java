package cm.util;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cm.common.entity.domain.Address;
import cm.common.entity.domain.PersonalDetails;
import cm.common.entity.domain.Product;
import cm.common.entity.domain.Task;

public class Util
{
	public static void showPanel( JPanel panel )
	{
		JFrame frame = new JFrame();
		frame.setTitle( "TEST" );
		frame.setLayout( new BorderLayout() );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.add( panel, BorderLayout.CENTER );
		frame.setSize( 700, 500 );
		frame.setLocationRelativeTo( null );
		frame.setVisible( true );
	}

	public static String nns( String s ) {
		return s == null ? "" : s; // non null string
	}

	public static String nne( Enum<?> e ) {
		return e == null ? "" : e.toString(); // non null enum
	}

	public static Float nnf( Float f ) {
		return f == null ? 0.0f : f; // non null float
	}

	public static boolean isNullOrEmpty( String s ) {
		return s == null || ( s != null && s.length() == 0 );
	}

	public static boolean isValidDecimalString( String s )
	{
		try {
			Float.parseFloat( s );
		} catch( Exception e ) {
			return false;
		}

		return true;
	}

	public static String toSqlString( Date date )
	{
		Calendar cal = new GregorianCalendar();
		cal.setTime( date );

		return String.format(
			"%d-%d-%d",
			cal.get( Calendar.YEAR ),
			cal.get( Calendar.MONTH )+1,
			cal.get( Calendar.DAY_OF_MONTH ) );
	}

	public static String toCustomerName( PersonalDetails pd )
	{
		if( pd != null )
		{
			return( String.format( "%s %s %s %s",
				nne( pd.title ),
				nns( pd.forename ),
				nns( pd.middlename ),
				nns( pd.surname ) ) );
		}

		return "";
	}

	public static String toDateString( Date date ) {
		return date == null ? "" : dateFormat.format( date );
	}

	public static String toDateTimeString( Date date ) {
		return date == null ? "" : dateTimeFormat.format( date );
	}

	public static void display( Product p, JLabel label )
	{
		if( p != null )
		{
			label.setText( String.format(
					"<html><table width=250 hspace=5 vspace=5 cellpadding=1 cellspacing=0>" +
					"<tr><td><b>Type</b></td><td>%s</td></tr>" +
					"<tr><td><b>Name</b></td><td>%s</td></tr>" +
					"<tr><td><b>Number</b></td><td>%s</td></tr>" +
					"<tr><td><b>DateOpened</b></td><td>%s</td></tr>" +
					"<tr><td><b>ReviewDate</b></td><td>%s</td></tr>" +
					"<tr><td><b>InterestRate</b></td><td>%.02f</td></tr>" +
					"<tr><td><b>OpeningBalance</b></td><td>%.02f</td></tr>" +
					"<tr><td><b>Active</b></td><td>%s</td></tr>" +
					"</table>",
					nne( p.type ),
					nns( p.name ),
					nns( p.accountNumber ),
					toDateString( p.dateOpened ),
					toDateString( p.reviewDate ),
					nnf( p.interestRate ),
					nnf( p.openingBalance ),
					p.active == null ? "N" : ( p.active ? "Y" : "N" ) ) );
		}
		else
		{
			label.setText( "" );
		}
	}

	public static void display( Address a, JLabel label )
	{
		if( a != null )
		{
			label.setText( String.format(
				"<html><table width=250 hspace=5 vspace=5 cellpadding=1 cellspacing=0>" +
				"<tr><td><b>Line1</b></td><td>%s</td></tr>" +
				"<tr><td><b>Line2</b></td><td>%s</td></tr>" +
				"<tr><td><b>Town</b></td><td>%s</td></tr>" +
				"<tr><td><b>County</b></td><td>%s</td></tr>" +
				"<tr><td><b>Postcode</b></td><td>%s</td></tr>" +
				"</table>",
				Util.nns( a.line1 ),
				Util.nns( a.line2 ),
				Util.nns( a.town ),
				Util.nne( a.county ),
				Util.nns( a.postcode ) ) );
		}
		else
		{
			label.setText( "" );
		}
	}

	public static void display( PersonalDetails pd, JLabel label )
	{
		if( pd != null )
		{
			label.setText( String.format(
				"<html><table width=250 hspace=5 vspace=5 cellpadding=1 cellspacing=0>" +
				"<tr><td><b>BusinessOwner</b></td><td>%s</td></tr>" +
				"<tr><td><b>Mailingname</b></td><td>%s</td></tr>" +
				"<tr><td><b>Salutation</b></td><td>%s</td></tr>" +
				"<tr><td><b>BirthDate</b></td><td>%s</td></tr>" +
				"<tr><td><b>HomePhone</b></td><td>%s</td></tr>" +
				"<tr><td><b>MobilePhone</b></td><td>%s</td></tr>" +
				"<tr><td><b>Email</b></td><td>%s</td></tr>" +
				"<tr><td><b>Employment Status</b></td><td>%s</td></tr>" +
				"<tr><td><b>MartialStatus</b></td><td>%s</td></tr>" +
				"<tr><td><b>DataPermission</b></td><td>%s</td></tr>" +
				"<tr><td><b>FFNI</b></td><td>%s</td></tr>" +
				"</table>",
				nns( pd.businessOwner ),
				nns( pd.mailingname ),
				nns( pd.salutation ),
				toDateString( pd.birthDate ),
				nns( pd.homePhone ),
				nns( pd.mobilePhone ),
				nns( pd.email ),
				nne( pd.employmentStatus ),
				nne( pd.maritalStatus ),
				pd.dataPermission == null ? "N" : ( pd.dataPermission ? "Y" : "N" ),
				pd.ffni == null ? "N" : ( pd.ffni ? "Y" : "N" ) ) );
		}
		else
		{
			label.setText( "" );
		}
	}

	public static void display( Task task, JLabel label )
	{
		if( task != null )
		{
			label.setText( String.format(
				"<html><table width=300 hspace=5 vspace=5 cellpadding=1 cellspacing=0>" +
				"<tr><td><b>Created</b></td><td>%s by <b>%s</b></td></tr>" +
				"<tr><td><b>Due</b></td><td>%s</td></tr>" +
				"<tr><td><b>Action</b></td><td>%s</td></tr>" +
				"<tr><td><b>Status</b></td><td>%s</td></tr>" +
				"<tr><td valign='top'><b>Note</b></td><td>%s</td></tr>" +
				"</table>",
				toDateTimeString( task.createdOn ),
				nns( task.createdBy ),
				toDateString( task.due ),
				nne( task.action ),
				nne( task.status ),
				nns( task.note ).replace( "\n", "<br/>" ) ) );
		}
		else
		{
			label.setText( "" );
		}
	}

	public static int compareDays( Date d1, Date d2 )
	{
		Integer n1 = toInt( d1 );
		Integer n2 = toInt( d2 );
		return n1.compareTo( n2 );
	}

	private static Integer toInt( Date d )
	{
		Calendar c = new GregorianCalendar();
		c.setTime( d );
		return c.get( Calendar.YEAR ) * 366 +
			   c.get( Calendar.MONTH ) * 31 +
			   c.get( Calendar.DAY_OF_MONTH );
	}

	private static SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat( "dd/MM/yyyy HH:mm" );
}
