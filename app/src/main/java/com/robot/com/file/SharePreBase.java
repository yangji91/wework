package com.robot.com.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class SharePreBase {
    protected abstract String getFileName();

    public void saveInfo(String data){
        File file = new File(getFileName());
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写数据
        byte[] buffer = data.getBytes();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(buffer);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }
        }
    }

    public String loadData(){
        String sn = null;
        File file = new File(getFileName());
        FileInputStream fileInputStream = null;
        if(file.exists()){
            try {
                fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[100];
                int cnt = fileInputStream.read(buffer,0,buffer.length);
                sn = new String(buffer,0,cnt);
            } catch (Throwable e) {
                e.printStackTrace();
            }finally{
                if(fileInputStream != null){
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileInputStream = null;
                }
            }
        }
        return sn;
    }
}
