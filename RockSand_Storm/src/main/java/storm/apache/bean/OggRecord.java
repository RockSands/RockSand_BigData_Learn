package storm.apache.bean;

public class OggRecord {
	private String table;

	private String op_type;

	private String op_ts;

	private String current_ts;

	private String pos;

	private Before before;

	private After after;

	public void setTable(String table) {
		this.table = table;
	}

	public String getTable() {
		return this.table;
	}

	public void setOp_type(String op_type) {
		this.op_type = op_type;
	}

	public String getOp_type() {
		return this.op_type;
	}

	public void setOp_ts(String op_ts) {
		this.op_ts = op_ts;
	}

	public String getOp_ts() {
		return this.op_ts;
	}

	public void setCurrent_ts(String current_ts) {
		this.current_ts = current_ts;
	}

	public String getCurrent_ts() {
		return this.current_ts;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getPos() {
		return this.pos;
	}

	public void setBefore(Before before) {
		this.before = before;
	}

	public Before getBefore() {
		return this.before;
	}

	public void setAfter(After after) {
		this.after = after;
	}

	public After getAfter() {
		return this.after;
	}

}
