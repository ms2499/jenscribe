package com.jenscribe.demo.Dao;

import com.tandem.ext.enscribe.EnscribeFile;
import com.tandem.ext.enscribe.EnscribeFileException;
import com.tandem.ext.enscribe.EnscribeOpenOptions;
import com.tandem.ext.guardian.GuardianInput;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EnscribeDao {
    public EnscribeFile openFile(String fileName) {
        try {
            System.out.println("EnscribeDao : open file "+fileName);
            EnscribeFile ensFile = new EnscribeFile("/G/FU1/SAMOSS/" + fileName);
            EnscribeOpenOptions openAttr = new EnscribeOpenOptions();
            openAttr.setAccess(EnscribeOpenOptions.READ_WRITE);
            openAttr.setExclusion(EnscribeOpenOptions.SHARED);
            ensFile.open(openAttr);
            System.out.println("EnscribeDao : open file success!");
            return ensFile;
        }catch (EnscribeFileException ensFileEx) {
            System.out.println("Open file " + fileName + " failed!, Guardian error = " + ensFileEx.getErrorNum());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getData(EnscribeFile ensFile){
        try {
            String fullFileName = ensFile.getFileName();
            String fileName = fullFileName.substring(fullFileName.lastIndexOf("/") + 1);
            System.out.println("EnscribeDao : start read file:" + fileName);

            String recNmae = fileName.charAt(0) + fileName.toLowerCase().substring(1);
            Class<?> cls = Class.forName("com.example.testjava." + "IO_"+ recNmae +"_r");
            Object fileRec = cls.getConstructor().newInstance();

            int countRead = 0;
            countRead = ensFile.read((GuardianInput) fileRec);

            if (countRead == -1) {
                System.out.println("EnscribeDao : read " + fileName + " EOF!");
                return null;
            }else
                return fileRec;
        }catch (EnscribeFileException ensFileEx){
            System.out.println("Get data "+ensFileEx.getFileName()+" failed!, err = "+ensFileEx.getErrorNum());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
