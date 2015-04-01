package cm.util;

import java.util.Date;

import cm.common.entity.domain.PersonalDetails;

public class PersonalDetailsValidator
{
	public static String isValid( PersonalDetails pd )
	{
		if( Util.isNullOrEmpty( pd.businessOwner ) ) {
			return "BusinessOwner is manditory";
		}

		if( pd.title == null ) {
			return "Title is manditory";
		}

		if( Util.isNullOrEmpty( pd.forename ) ) {
			return "Forename is manditory";
		}

		if( Util.isNullOrEmpty( pd.surname ) ) {
			return "Surname is manditory";
		}

		if( pd.employmentStatus == null ) {
			return "EmploymentStatus is manditory";
		}

		if( pd.maritalStatus == null ) {
			return "MaritalStatus is manditory";
		}

		if( pd.birthDate == null ) {
			return "BirthDate is manditory";
		}

		Date today = new Date();
		if( pd.birthDate.compareTo( today ) >= 0 ) {
			return "BirthDate cannot be in the future";
		}

		return null;
	}
}
