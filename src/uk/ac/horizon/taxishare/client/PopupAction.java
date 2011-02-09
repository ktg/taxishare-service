package uk.ac.horizon.taxishare.client;


public class PopupAction
{
	private final String name;
	private final Runnable runnable;

	public PopupAction(final String name, final Runnable runnable)
	{
		this.name = name;
		this.runnable = runnable;
	}
	
	public String getName()
	{
		return name;
	}

	public void execute()
	{
		runnable.run();
	}	
}
