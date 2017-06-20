package com.dongnao.jack.supportAn;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.dongnao.jack.annotation.Repository;

public class AnnotationHandler {
    
    static List<String> packageNames = new ArrayList<String>();
    
    /** 
     * @Fields hasRepositoryIntfList ��Repositoryע���list 
     */
    static List<String> hasRepositoryIntfList = new ArrayList<String>();
    
    /** 
     * @Description ɨ��packageName��֧�ֶ�Repository��֧�� ��ȡ���е�.class�ļ�
     * @param @param packageName
     * @param @return ���� 
     * @return List<String> ��������  
     * @throws 
     */
    public static List<String> scanPackage(String packageName) {
        URL pathUrl = AnnotationHandler.class.getClassLoader().getResource("/"
                + replaceTo(packageName));
        
        String pathFile = pathUrl.getFile();
        
        File file = new File(pathFile);
        
        System.out.println(pathFile + "�Ƿ����ļ��У�" + file.isDirectory());
        
        System.out.println(pathFile + "�Ƿ����ļ���" + file.isFile());
        
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
