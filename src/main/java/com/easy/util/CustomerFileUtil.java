package com.easy.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author 武天
 * @date 2022/12/1 16:14
 */
@Slf4j
public class CustomerFileUtil {


    /**
     * 移动文件到指定位置
     *
     * @param fileFullNameCurrent 要移动的文件全路径
     * @param fileFullNameTarget  移动到目标位置的文件全路径
     * @return 是否移动成功， true：成功；否则失败
     */
    public static Boolean moveFileToTarget(String fileFullNameCurrent, String fileFullNameTarget) {
        boolean ismove = false;

        File oldName = new File(fileFullNameCurrent);

        if (!oldName.exists()) {
            log.warn("{}", "要移动的文件不存在！");
            return ismove;
        }

        if (oldName.isDirectory()) {
            log.warn("{}", "要移动的文件是目录，不移动！");
            return false;
        }

        File newName = new File(fileFullNameTarget);

        if (newName.isDirectory()) {
            log.warn("{}", "移动到目标位置的文件是目录，不能移动！");
            return false;
        }

        String pfile = newName.getParent();
        File pdir = new File(pfile);

        if (!pdir.exists()) {
            pdir.mkdirs();
            log.warn("{}", "要移动到目标位置文件的父目录不存在，创建：" + pfile);
        }

        ismove = oldName.renameTo(newName);
        return ismove;
    }


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean isDelete = deleteDir(new File(dir, child));
                    if (!isDelete) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    public static void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs();
            File filelist = new File(oldPath);
            String[] file = filelist.list();
            File temp;
            if (file != null) {
                for (String s : file) {
                    if (oldPath.endsWith(File.separator)) {
                        temp = new File(oldPath + s);
                    } else {
                        temp = new File(oldPath + File.separator + s);
                    }

                    if (temp.isFile()) {
                        FileInputStream input = new FileInputStream(temp);
                        FileOutputStream output = new FileOutputStream(newPath
                                + "/" + (temp.getName()).toString());
                        byte[] bufferarray = new byte[1024 * 64];
                        int prereadlength;
                        while ((prereadlength = input.read(bufferarray)) != -1) {
                            output.write(bufferarray, 0, prereadlength);
                        }
                        output.flush();
                        output.close();
                        input.close();
                    }
                    if (temp.isDirectory()) {
                        copyFolder(oldPath + "/" + s, newPath + "/" + s);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
        }
    }
}
