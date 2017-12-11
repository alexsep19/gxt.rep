package gxt.client.domain;

public class LabVal {
	private String value;
	private String label;

	public LabVal(){}
	public LabVal(String Value, String Label){
	    value = Value; label = Label;
	}
	
	public String getLabel() {
	    return label;
	}

	public void setLabel(String label) {
	    this.label = label;
	}

	public String getValue() {
	    return value;
	}

	public void setValue(String value) {
	    this.value = value;
	}
@Override
public String toString(){
	return value;
}
}
