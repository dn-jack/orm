package com.dongnao.jack.mapperTemplate;

import java.util.ArrayList;
import java.util.List;

public class ResultMapBean {
    public String id;
    
    public String type;
    
    List<ResultBean> results = new ArrayList<ResultBean>();
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public List<ResultBean> getResults() {
        return results;
    }
    
    public void setResults(List<ResultBean> results) {
        this.results = results;
    }
}
