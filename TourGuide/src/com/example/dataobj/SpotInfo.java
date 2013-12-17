package com.example.dataobj;

/**
 * 描述景点信息的类
 * 
 * @author fan
 * @version 0.1
 */
public class SpotInfo {

	private String name;

	private String description;

	/**
	 * 存放枚举类型，表明景点或位置点类型： classify类型：1景点，2餐饮，3娱乐，4休息点，5卫生间，6大门
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

	//得到姓名
	public void setName(String name) {
		this.name = name;
	}

	//得到描述
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
