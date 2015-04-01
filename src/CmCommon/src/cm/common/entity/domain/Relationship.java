package cm.common.entity.domain;

import java.io.Serializable;

import cm.common.entity.Entity;
import cm.common.types.RelationshipType;

public class Relationship extends Entity implements Serializable
{
	public Integer customer1;
	public Integer customer2;
	public RelationshipType type;

	private static final long serialVersionUID = 5552369908230719913L;
}
