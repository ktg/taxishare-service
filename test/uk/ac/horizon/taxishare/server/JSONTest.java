package uk.ac.horizon.taxishare.server;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.horizon.taxishare.model.Instance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONTest
{
	private EntityManager entityManager;

	@After
	public void after() throws Exception
	{
		TestHelper.tearDown();
	}

	@Before
	public void before() throws Exception
	{
		entityManager = TestHelper.setUp();
	}

	@Test
	public void jsonTest()
	{
		final Instance instance = entityManager.find(Instance.class, TestHelper.getInstanceID());
		final Gson gson = new GsonBuilder().setExclusionStrategies(new ManyToOneExclusionStrategy()).create();
		System.out.println(gson.toJson(instance));
	}
}
