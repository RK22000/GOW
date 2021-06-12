package rkan.project.gow_0b;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BBFileIO<B> {
    private B mSubject;
    private File mFile;

    public BBFileIO(B subject, File file) {
        mSubject = subject;
        mFile = file;
    }

    public B getSubject() {
        return mSubject;
    }

    public boolean saveSubjectObject() {
        try {
            FileOutputStream fos = new FileOutputStream(mFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mSubject);
            oos.close();
            fos.close();
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean readSubjectObject() {
        try {
            FileInputStream fis = new FileInputStream(mFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            mSubject = (B) ois.readObject();
            ois.close();
            fis.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean readBrochureFile(Brochure brochure, File file) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line, item[];
            br.readLine();
            while ((line = br.readLine()) != null) {
                Log.d("BBFileIO", line);
                item = line.split(",");
                brochure.add(new GroceryItem(item[1], item[0], item[3], Double.parseDouble(item[2])));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
