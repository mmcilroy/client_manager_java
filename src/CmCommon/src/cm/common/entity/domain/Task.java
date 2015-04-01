package cm.common.entity.domain;

import java.io.Serializable;
import java.util.Date;

import cm.common.entity.Entity;
import cm.common.types.TaskAction;
import cm.common.types.TaskStatus;
import cm.common.types.TaskType;

public class Task extends Entity implements Serializable
{
	public Date due;
	public TaskAction action;
	public TaskStatus status;
	public String note;
	public Integer customerId = 0;
	public Integer productId = 0;
	public TaskType type;

	private static final long serialVersionUID = 8619166316041781717L;
}
