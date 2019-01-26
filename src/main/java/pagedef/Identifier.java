package pagedef;

import enums.IdentifyBy;

public class Identifier {

	private IdentifyBy by = null;
	private String value = null;

	public Identifier(String by, String value) {
		this.setIdType(by);
		this.setValue(value);
	}

	public IdentifyBy getIdType() {
		return by;
	}

	private void setIdType(String by) {
		this.by = IdentifyBy.valueOf(by.toUpperCase());
	}

	public String getValue() {
		return value;
	}

	private void setValue(String value) {
		this.value = value;
	}

}
