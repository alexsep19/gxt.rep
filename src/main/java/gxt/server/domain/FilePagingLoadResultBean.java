package gxt.server.domain;

import java.util.List;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class FilePagingLoadResultBean extends PagingLoadResultBean<FileData>{
    protected FilePagingLoadResultBean() {}
    public FilePagingLoadResultBean(List<FileData> list, int totalLength, int offset) {
      super(list, totalLength, offset);
    }

}
