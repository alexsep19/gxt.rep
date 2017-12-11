package gxt.server.domain;

public class listData {
    private String name;
    private String value;

    
    public listData(String name, String value) {
      super();
      this.name = name;
      this.value = value;
    }
    
//    public long getId() {
//	      return Long.valueOf(value);
//	    }

    public String getName() {
      return name;
    }
    public String getValue() {
      return value;
    }
    public void setName(String name) {
      this.name = name;
    }
    public void setValue(String value) {
      this.value = value;
    }
    
    public Integer getVersion(){return 1;}
}
