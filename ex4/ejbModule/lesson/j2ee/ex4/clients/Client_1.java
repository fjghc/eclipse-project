package lesson.j2ee.ex4.clients;

import java.util.Properties;

import lesson.j2ee.ex4.travelagent.TravelAgentRemote;
import lesson.j2ee.ex4.travelagent.ExtendedPersistenceContextRemote;
import lesson.j2ee.ex4.travelagent.TransactionPersistenceContextRemote;
import lesson.j2ee.ex4.domain.Cabin;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;

public class Client_1 {
	public static void main(String[] args) {
		try {
			Context jndiContext = getInitialContext();
			Object ref = jndiContext.lookup("TravelAgentBean/remote");
			TravelAgentRemote dao = (TravelAgentRemote) ref;

			ref = jndiContext
					.lookup("TransactionPersistenceContextBean/remote");
			TransactionPersistenceContextRemote txBean = (TransactionPersistenceContextRemote) ref;

			Cabin fetchedCabin = dao.findCabin(1);
			int oldBedCount = fetchedCabin.getBedCount();

			System.out
					.println("Set up transaction persistence context stateful bean");
			txBean.setCabin(1);
			txBean.updateBedCount(5);

			fetchedCabin = dao.findCabin(1);
			System.out.println("Cabin bed count will still be " + oldBedCount
					+ ": " + fetchedCabin.getBedCount());

			System.out
					.println("Set up extended persistence context stateful bean");

			ref = jndiContext.lookup("ExtendedPersistenceContextBean/remote");
			ExtendedPersistenceContextRemote extendedBean = (ExtendedPersistenceContextRemote) ref;

			extendedBean.setCabin(1);
			extendedBean.updateBedCount(5);

			fetchedCabin = dao.findCabin(1);
			System.out.println("Cabin bed count will be 5: "
					+ fetchedCabin.getBedCount());

			// cleanup
			txBean.remove();
			extendedBean.remove();
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}

	public static Context getInitialContext()
			throws javax.naming.NamingException {
		// ���ó�ʼ������
		Properties properties = new Properties();
		properties.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		properties.put(Context.PROVIDER_URL, "localhost:1099");
		return new InitialContext(properties);
	}
}
