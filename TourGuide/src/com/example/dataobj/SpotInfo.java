package com.example.dataobj;

/**
 * ����������Ϣ����
 * 
 * @author fan
 * @version 0.1
 */
public class SpotInfo {

	private String name;

	private String description;

	/**
	 * ���ö�����ͣ����������λ�õ����ͣ� classify���ͣ�1���㣬2������3���֣�4��Ϣ�㣬5�����䣬6����
	 */
	private Integer classify;

	public SpotInfo(String name, String description, Integer classify) {
		super();
		this.name = name;
		this.description = description;
		this.classify = classify;
	}

	public String getName() {
		return name;
	}

	//�õ�����
	public void setName(String name) {
		this.name = name;
	}

	//�õ�����
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getClassify() {
		return classify;
	}

	public void setClassify(Integer classify) {
		this.classify = classify;
	}

}
