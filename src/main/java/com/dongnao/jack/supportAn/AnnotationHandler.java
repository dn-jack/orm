package com.dongnao.jack.supportAn;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.dongnao.jack.annotation.Repository;

public class AnnotationHandler {
    
    static List<String> packageNames = new ArrayList<String>();
    
    /** 
     * @Fields hasRepositoryIntfList 有Repository注解的list 
     */
    static List<String> hasRepositoryIntfList = new ArrayList<String>();
    
    /** 
     * @Description 扫描packageName，支持对Repository的支持 读取所有的.class文件
     * @param @param packageName
     * @param @return 参数 
     * @return List<String> 返回类型  
     * @throws 
     */
    public static List<String> scanPackage(String packageName) {
        URL pathUrl = AnnotationHandler.class.getClassLoader().getResource("/"
                + replaceTo(packageName));
        
        String pathFile = pathUrl.getFile();
        
        File file = new File(pathFile);
        
        System.out.println(pathFile + "是否是文件夹：" + file.isDirectory());
        
        System.out.println(pathFile + "是否是文件：" + file.isFile());
        
        String[] filelist = file.list();
        
        for (String path : filelist) {
            File eachPathFile = new File(pathFile + path);
            
            if (eachPathFile.isDirectory()) {
                scanPackage(packageName + "." + path);
            }
            else {
                System.out.println(packageName + "." + eachPathFile.getName());
                packageNames.add(packageName + "." + eachPathFile.getName());
            }
        }
        
        return packageNames;
    }
    
    public static List<String> filterFiles() {
        if (packageNames.size() <= 0) {
            return null;
        }
        
        for (String className : packageNames) {
            try {
                Class<?> ccName = Class.forName(className.replace(".class", ""));
                
                if (ccName.isAnnotationPresent(Repository.class)) {
                    hasRepositoryIntfList.add(className.replace(".class", ""));
                }
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        return hasRepositoryIntfList;
    }
    
    public static void init(String packageName) {
        scanPackage(packageName);
        filterFiles();
    }
    
    public static String replaceTo(String path) {
        return path.replaceAll("\\.", "/");
    }
    
    public static List<String> getPackageNames() {
        return packageNames;
    }
    
    public static void setPackageNames(List<String> packageNames) {
        AnnotationHandler.packageNames = packageNames;
    }
    
    public static List<String> getHasRepositoryIntfList() {
        return hasRepositoryIntfList;
    }
    
    public static void setHasRepositoryIntfList(
            List<String> hasRepositoryIntfList) {
        AnnotationHandler.hasRepositoryIntfList = hasRepositoryIntfList;
    }
}
