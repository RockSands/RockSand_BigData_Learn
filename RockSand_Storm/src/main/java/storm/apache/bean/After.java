package storm.apache.bean;

public class After {
	private int id;

	private String name;

	private int value;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getName() {
		return this.name;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

}
