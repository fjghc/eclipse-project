package lesson.j2ee.ex4.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;

// 告知persistence provider，这是一个映射到数据库的实体类，
// 并且可以受管于EntityManager服务
@Entity
// bean class被映射到哪个数据库表，可选（缺省值是类的非限定名）
@Table(name = "CABIN")
// Serializable作为实体不是必须的，但如果实现了Serializable接口，
// 则可用作session bean中远程接口方法的参数和返回值
public class Cabin implements java.io.Serializable {
	private int id;
	private String name;
	private int deckLevel;
	private int shipId;
	private int bedCount;

	// 对应数据库表主键
	@Id
	@Column(name = "CABIN_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int pk) {
		id = pk;
	}

	// javax.persistence.Column用于映射到数据库表的字段
	// 可选（缺省值是成员属性名称）
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
