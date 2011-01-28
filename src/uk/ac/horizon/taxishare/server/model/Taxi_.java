package uk.ac.horizon.taxishare.server.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import uk.ac.horizon.taxishare.server.model.Taxi.Status;

@Generated(value="Dali", date="2011-01-28T14:46:11.348+0000")
@StaticMetamodel(Taxi.class)
public class Taxi_ {
	public static volatile SingularAttribute<Taxi, Integer> id;
	public static volatile SingularAttribute<Taxi, Location> destination;
	public static volatile CollectionAttribute<Taxi, Person> people;
	public static volatile SingularAttribute<Taxi, Date> pickupTime;
	public static volatile SingularAttribute<Taxi, Date> requestTime;
	public static volatile SingularAttribute<Taxi, Date> arrivalTime;
	public static volatile SingularAttribute<Taxi, Integer> totalSpace;
	public static volatile SingularAttribute<Taxi, TaxiCompany> company;
	public static volatile SingularAttribute<Taxi, Instance> instance;
	public static volatile SingularAttribute<Taxi, Float> predictedCost;
	public static volatile SingularAttribute<Taxi, Status> status;
}
