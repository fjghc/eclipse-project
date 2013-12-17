package lesson.j2ee.ex4.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;

// ��֪persistence provider������һ��ӳ�䵽���ݿ��ʵ���࣬
// ���ҿ����ܹ���EntityManager����
@Entity
// bean class��ӳ�䵽�ĸ����ݿ����ѡ��ȱʡֵ����ķ��޶�����
@Table(name = "CABIN")
// Serializable��Ϊʵ�岻�Ǳ���ģ������ʵ����Serializable�ӿڣ�
// �������session bean��Զ�̽ӿڷ����Ĳ����ͷ���ֵ
public class Cabin implements java.io.Serializable {
	private int id;
	private String name;
	private int deckLevel;
	private int shipId;
	private int bedCount;

	// ��Ӧ���ݿ������
	@Id
	@Column(name = "CABIN_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int pk) {
		id = pk;
	}

	// javax.persistence.Column����ӳ�䵽���ݿ����ֶ�
	// ��ѡ��ȱʡֵ�ǳ�Ա�������ƣ�
	@Column(name = "CABIN_NAME")
	public String getName() {
		return name;
	}

	public void setName(String str) {
		name = str;
	}

	@Column(name = "CABIN_DECK_LEVEL")
	public int getDeckLevel() {
		return deckLevel;
	}

	public void setDeckLevel(int level) {
		deckLevel = level;
	}

	@Column(name = "CABIN_SHIP_ID")
	public int getShipId() {
		return shipId;
	}

	public void setShipId(int sid) {
		shipId = sid;
	}

	@Column(name = "CABIN_BED_COUNT")
	public int getBedCount() {
		return bedCount;
	}

	public void setBedCount(int bed) {
		bedCount = bed;
	}
}
