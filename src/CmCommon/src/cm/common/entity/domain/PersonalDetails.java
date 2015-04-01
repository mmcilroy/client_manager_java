package cm.common.entity.domain;

import java.io.Serializable;
import java.util.Date;

import cm.common.entity.Entity;
import cm.common.types.EmploymentStatus;
import cm.common.types.MaritalStatus;
import cm.common.types.Title;

public class PersonalDetails extends Entity implements Serializable
{
	public String businessOwner;
	public Title title;
	public String forename;
	public String middlename;
	public String surname;
	public String mailingname;
	public String salutation;
	public String homePhone;
	public String mobilePhone;
	public String email;
	public EmploymentStatus employmentStatus;
	public MaritalStatus maritalStatus;
	public Date birthDate;
	public Integer addressId = 0;
	public Boolean dataPermission = false;
	public Boolean ffni = false;

	private static final long serialVersionUID = -9137780052156485833L;
}
