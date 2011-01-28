package uk.ac.horizon.taxishare.server.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-01-28T14:46:11.301+0000")
@StaticMetamodel(Message.class)
public class Message_ {
	public static volatile SingularAttribute<Message, Long> id;
	public static volatile SingularAttribute<Message, String> externalMessageID;
	public static volatile SingularAttribute<Message, String> body;
	public static volatile SingularAttribute<Message, Date> timeReceived;
	public static volatile SingularAttribute<Message, Date> lastUpdate;
	public static volatile SingularAttribute<Message, String> phoneNumber;
	public static volatile SingularAttribute<Message, Boolean> incoming;
}
