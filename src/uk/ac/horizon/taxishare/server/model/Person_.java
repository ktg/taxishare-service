package uk.ac.horizon.taxishare.server.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-01-28T14:46:11.348+0000")
@StaticMetamodel(Person.class)
public class Person_ {
	public static volatile SingularAttribute<Person, Integer> id;
	public static volatile SingularAttribute<Person, String> name;
	public static volatile SingularAttribute<Person, String> number;
	public static volatile SingularAttribute<Person, Taxi> taxi;
}
