package com.dongnao.jack.mapperTemplate;

public class XmlTemplate {
    
    /** 
     * @Fields operTypeStr select¡¢update¡¢delete¡¢insert 
     */
    private String operTypeStr;
    
    private String id;
    
    private String parameterType;
    
    private String resultClass;
    
    private String resultMap;
    
    private String content;
    
    public String getOperTypeStr() {
        return operTypeStr;
    }
    
    public void setOperTypeStr(String operTypeStr) {
        this.operTypeStr = operTypeStr;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getParameterType() {
        return parameterType;
    }
    
    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
    
    public String getResultClass() {
        return resultClass;
    }
    
    public void setResultClass(String resultClass) {
        this.resultClass = resultClass;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getResultMap() {
        return resultMap;
    }
    
    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }
}
