package cm.common.entity;

import java.io.Serializable;
import java.util.Date;

public class Entity implements Serializable
{
	public Integer id = 0;
	public Integer transactionId = 0;
	public String createdBy;
	public Date createdOn;
	public Boolean live = true;

	private static final long serialVersionUID = 942488499072048275L;
}
