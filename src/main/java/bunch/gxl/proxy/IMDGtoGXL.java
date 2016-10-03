package bunch.gxl.proxy;

/**
 * Title:        Bunch Clustering Tool
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      Drexel University
 * @author
 * @version 1.0
 */

public interface IMDGtoGXL {
    public boolean convert();
    public void setOptions(String mdgF, String gxlF);
    public void setOptions(String mdgF, String gxlF, boolean embed);
    public void setOptions(String mdgF, String gxlF, String gxlPath);
    public void setOptions(String mdgF, String gxlF, String gxlPath, boolean embed);
}