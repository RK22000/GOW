package rkan.project.gow_0b;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
}
