package cm.util;

import java.util.Date;

import cm.common.entity.domain.Product;
import cm.common.types.ProductType;

public class ProductValidator
{
	public static String isValid( Product p )
	{
		if( p.type == null ) {
			return "Type is manditory";
		}

		if( Util.isNullOrEmpty( p.name ) ) {
			return "Name is manditory";
		}

		if( Util.isNullOrEmpty( p.accountNumber ) ) {
			return "AccountNumber is manditory";
		}

		if( p.type == ProductType.SAVINGS )
		{
			if( p.interestRate == null ) {
				return "InterestRate is manditory";
			}
	
			if( p.openingBalance == null ) {
				return "OpeningBalance is manditory";
			}
		}

		if( p.dateOpened == null ) {
			return "DateOpened is manditory";
		}

		Date today = new Date();
		if( Util.compareDays( p.dateOpened, today ) > 0 ) {
			return "DateOpened cannot be in the future";
		}

		if( p.type == ProductType.SAVINGS )
		{
			if( p.reviewDate == null ) {
				return "ReviewDate is manditory for savings products";
			}
	
			if( Util.compareDays( p.reviewDate, p.dateOpened ) <= 0 ) {
				return "ReviewDate must be after DateOpened";
			}
		}

		String accNumFormat = StringFormat.toFormat( p.accountNumber );

		if( p.type == ProductType.BANKING && accNumFormat.compareTo( "999999 99999999" ) != 0 ) {
			return "Bank account numbers must be formatted as 123456 12345678";
		}

		if( p.type == ProductType.CREDIT_CARD && accNumFormat.compareTo( "9999999999999999" ) != 0 ) {
			return "Credit card account numbers must be formatted as 16 digits";
		}

		if( p.type == ProductType.SAVINGS )
		{
			if( p.name.compareTo( "1 YEAR FIXED ISA" ) == 0 ||
				p.name.compareTo( "2 YEAR FIXED ISA" ) == 0 ||
				p.name.compareTo( "DIRECT ISA" ) == 0 ||
				p.name.compareTo( "EASY ISA" ) == 0 )
			{
				if( accNumFormat.compareTo( "999999 99999999" ) != 0 ) {
					return "ISA savings account numbers must be formatted as 123456 12345678";
				}
			}
			else
			{
				if( accNumFormat.compareTo( "A99999999AAA" ) != 0 ) {
					return "Savings account numbers must be formatted as R12345678ABC";
				}
			}
		}
	
		return null;
	}
}
