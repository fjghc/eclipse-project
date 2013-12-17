package lesson.j2ee.ex4.travelagent;

import javax.ejb.Remote;

import lesson.j2ee.ex4.domain.Cabin;

@Remote
public interface TravelAgentRemote {
	public void createCabin(Cabin cabin);

	public Cabin findCabin(int pKey);

	public void updateCabin(Cabin cabin);

}
