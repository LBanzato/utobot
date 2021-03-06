package nl.focalor.utobot.base.model.service;

import java.util.Set;
import nl.focalor.utobot.base.model.Person;

public interface IPersonService {
	public Person get(long id);

	public void create(Person person);

	public void addNick(long personId, String nick);

	public Person find(String name, Boolean fuzzy);

	public Set<Person> load(String name, String provinceName, Boolean fuzzy);
}
