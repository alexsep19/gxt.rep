package gxt.server.domain;

public class FileData {
    private String created;
    private String name;
    private String size;
//    public static String COL_IMG = "img";
//    public static String COL_NAME = "file";
//    public static String COL_SIZE = "size";
//    public static String COL_DATE = "date";

    public FileData(){}
    
    public FileData(String Name, String Size, String Created){
      name = Name;
      size = Size;
      created = Created;
    }

    public String getCreated() {
	return created;
    }

    public String getName() {
	return name;
    }

    public String getSize() {
	return size;
    }
    public Integer getVersion(){return 1;}
}
