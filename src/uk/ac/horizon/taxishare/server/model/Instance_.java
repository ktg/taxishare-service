package uk.ac.horizon.taxishare.server.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-01-28T14:46:45.848+0000")
@StaticMetamodel(Instance.class)
public class Instance_ {
	public static volatile SingularAttribute<Instance, Integer> id;
	public static volatile SingularAttribute<Instance, Location> location;
	public static volatile CollectionAttribute<Instance, Location> destinations;
	public static volatile CollectionAttribute<Instance, Taxi> taxis;
	public static volatile SingularAttribute<Instance, Boolean> enabled;
	public static volatile SingularAttribute<Instance, String> number;
	public static volatile CollectionAttribute<Instance, TaxiCompany> companies;
}
