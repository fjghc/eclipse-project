package lesson.j2ee.ex4.clients;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import lesson.j2ee.ex4.domain.Cabin;
import lesson.j2ee.ex4.travelagent.TravelAgentRemote;

public class Client {
	public static void main(String[] args) {
		try {
			Context jndiContext = getInitialContext();
			Object ref = jndiContext.lookup("TravelAgentBean/remote");
			// Object ref = jndiContext.lookup("TravelAgentRemote");

			// ����EJB����֧��IIOP��RMI����ת�Ͳ���CORBA�Ĺ��й��ܣ�
			// ������Ҫ��ʽ�ضԴ�lookup()���صĶ�����С�խ����
			// TravelAgentRemote dao = (TravelAgentRemote)ref;
			TravelAgentRemote dao = (TravelAgentRemote) PortableRemoteObject
					.narrow(ref, TravelAgentRemote.class);

			
			Cabin cabin_1 = new Cabin();
			//cabin_1.setId(1);
			cabin_1.setName("Master Suite");
			cabin_1.setDeckLevel(1);
			cabin_1.setShipId(1);
			cabin_1.setBedCount(3);

			Cabin cabintmp = dao.findCabin(1);
			/*if(cabintmp != null)
			{
				System.out.println(cabintmp.getName());
				System.out.println(cabintmp.getDeckLevel());
				System.out.println(cabintmp.getShipId());
				System.out.println(cabintmp.getBedCount());
			}*/
			dao.createCabin(cabin_1);

			Cabin cabin_2 = dao.findCabin(1);
			cabin_2.setShipId(88);
			dao.updateCabin(cabin_2);
			System.out.println(cabin_2.getName());
			System.out.println(cabin_2.getDeckLevel());
			System.out.println(cabin_2.getShipId());
			System.out.println(cabin_2.getBedCount());

		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}

	public static Context getInitialContext() throws NamingException {
		// ���ó�ʼ������
		Properties properties = new Properties();
		properties.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		properties.put(Context.PROVIDER_URL, "localhost:1099");
		return new InitialContext(properties);
	}
}
