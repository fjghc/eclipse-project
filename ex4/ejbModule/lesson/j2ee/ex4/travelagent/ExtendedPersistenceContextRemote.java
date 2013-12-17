package lesson.j2ee.ex4.travelagent;

import javax.ejb.Remote;

@Remote
public interface ExtendedPersistenceContextRemote {
	public void setCabin(int pk);

	public void updateBedCount(int newBedCount);

	public void remove();
}
