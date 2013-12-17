package lesson.j2ee.ex4.travelagent;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lesson.j2ee.ex4.domain.Cabin;

//import org.jboss.annotation.ejb.RemoteBinding;

@Stateless
// @RemoteBinding(jndiBinding="TravelAgentRemote")
public class TravelAgentBean implements TravelAgentRemote {

	@PersistenceContext(unitName = "titan")
	private EntityManager manager;

	public void createCabin(Cabin cabin) {
		manager.persist(cabin);
	}

	public Cabin findCabin(int pKey) {
		return manager.find(Cabin.class, pKey);
	}

	public void updateCabin(Cabin cabin) {
		manager.merge(cabin);
	}
}
