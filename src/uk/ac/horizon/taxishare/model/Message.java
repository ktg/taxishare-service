package uk.ac.horizon.taxishare.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sun.istack.internal.NotNull;

@Entity
public class Message
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String externalMessageID;
	private String body;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeReceived;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	@NotNull
	private String phoneNumber; // UserID
	private boolean incoming;

	// private Provider provider;
	// private Application application;

	public Message()
	{
	}

	public Message(final String number, final String body, final boolean incoming)
	{
		this.body = body;
		this.phoneNumber = number;
		this.incoming = incoming;
	}

	public String getBody()
	{
		return body;
	}

	public long getId()
	{
		return id;
	}

	public Date getLastUpdate()
	{
		return lastUpdate;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public String getProviderMessageID()
	{
		return externalMessageID;
	}

	public Date getTimeReceived()
	{
		return timeReceived;
	}

	public boolean isIncoming()
	{
		return incoming;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public void setIncoming(final boolean incoming)
	{
		this.incoming = incoming;
	}

	public void setLastUpdate(final Date lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}

	public void setProviderMessageID(final String providerMessageID)
	{
		this.externalMessageID = providerMessageID;
	}

	public void setTimeReceived(final Date timeReceived)
	{
		this.timeReceived = timeReceived;
	}
}