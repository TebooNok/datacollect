package com.pginfo.datacollect.dto;

import java.io.Serializable;

public class BaseRequest implements Serializable {

    private static final long serialVersionUID = 1874420777944213087L;

    // 页码,0为不使用分页
    private int page = 0;

    // 每页数据量
    private int dataNum = 10;

    // 导出标签，为1时生成并导出xls
    private int exportFlag = 0;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getDataNum() {
        return dataNum;
    }

    public void setDataNum(int dataNum) {
        this.dataNum = dataNum;
    }

    public int getExportFlag() {
        return exportFlag;
    }

    public void setExportFlag(int exportFlag) {
        this.exportFlag = exportFlag;
    }
}
