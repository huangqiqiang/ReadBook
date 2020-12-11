package com.qq.readbook;

import java.io.File;

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook
 * @Date : 下午 12:23
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
public class FileUtils {

    public static void main(String[] args) {
        File file = new File("C://Users//Administrator//Desktop//Documents");  //放置文件目录
        showFile(file);
    }

    static String newPath = "D://wx//";

    public static void showFile(File file) {
        if (file.isFile()) { //判断是否为文件 ，如是打印文件名
            System.out.println(file.getAbsolutePath() + "   ==" + file.getName());
            String oFileName = file.getName();
            String file4name = file.getParent().substring(file.getParent().lastIndexOf("\\"));
            System.out.println("--------------------- " + file4name);
            if (oFileName.endsWith(".pic_hd")) {
                String newFileName = oFileName.replace(".pic_hd", "_t.jpg");
                File newFile = new File(newPath + "imgh", file4name + newFileName);
                boolean flag = file.renameTo(newFile);
                if (flag) {
                    System.out.println("success  :       " + newFile.getAbsolutePath());
                } else {
                    System.out.println("failure  :       " + newFile.getAbsolutePath());
                }

            } else if (oFileName.endsWith(".pic_thum")) {
                String newFileName = oFileName.replace(".pic_thum", "_t.jpg");
                File newFile = new File(newPath + "imgs", file4name + newFileName);
                boolean flag = file.renameTo(newFile);
                if (flag) {
                    System.out.println("success  :       " + newFile.getAbsolutePath());
                } else {
                    System.out.println("failure  :       " + newFile.getAbsolutePath());
                }

            } else if (oFileName.endsWith(".pic")) {
                String newFileName = oFileName.replace(".pic", ".jpg");
                File newFile = new File(newPath + "img", file4name + newFileName);
                boolean flag = file.renameTo(newFile);
                if (flag) {
                    System.out.println("success  :       " + newFile.getAbsolutePath());
                } else {
                    System.out.println("failure  :       " + newFile.getAbsolutePath());
                }
            } else if (oFileName.endsWith(".jpg") || oFileName.endsWith(".png")) {
                File newFile = new File(newPath + "img", file4name + file.getName());
                if (file.renameTo(newFile)) {
                    System.out.println("success  :       " + newFile.getAbsolutePath());
                } else {
                    System.out.println("failure  :       " + newFile.getAbsolutePath());
                }
            } else if (oFileName.contains(".mp4")) {
                File newFile = new File(newPath + "video", file4name + oFileName);
                boolean flag = file.renameTo(newFile);
                if (flag) {
                    System.out.println("success  :       " + newFile.getAbsolutePath());
                } else {
                    System.out.println("failure  :       " + newFile.getAbsolutePath());
                }
            }


        } else {
            File[] files = file.listFiles();  //如不是文件，则将文件夹下的所有文件夹定义为数组
            for (int i = 0; i < files.length; i++) {
                showFile(files[i]); //循环进行调用
            }
        }
    }


}
