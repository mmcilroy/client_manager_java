package cm.common.entity.domain;

import java.io.Serializable;
import java.util.Date;

import cm.common.entity.Entity;
import cm.common.types.ProductType;

public class Product extends Entity implements Serializable
{
	public ProductType type;
	public String name;
	public String accountNumber;
	public Date dateOpened;
	public Date reviewDate;
	public Float interestRate;
	public Float openingBalance;
	public Boolean active = true;
	public Integer customerId;

	private static final long serialVersionUID = -3962696701401823789L;
}
