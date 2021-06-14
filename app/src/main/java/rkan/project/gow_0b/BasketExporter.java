package rkan.project.gow_0b;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BasketExporter {
    private Basket mBasket;
    private final File mOutPutFile;
    public BasketExporter(Basket basket, File outPutDirectory) {
        mBasket = basket;
        //Date
        mOutPutFile = new File(outPutDirectory, "Basket_export_file.txt");
    }
    public boolean export() {
        try {
            FileWriter writer = new FileWriter(mOutPutFile);
            writer.append("Basket list \n")
                    .append(mBasket.toString());
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public File getExportedFile() {
        if (mOutPutFile.isFile()) {
            return mOutPutFile;
        } else {
            return null;
        }
    }

    public Basket getBasket() {
        return mBasket;
    }

    public boolean saveBasketObject() {
        try {
            FileOutputStream fos = new FileOutputStream(mOutPutFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mBasket);
            oos.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean readBasketObject() {
        try {
            FileInputStream fis = new FileInputStream(mOutPutFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            mBasket = (Basket) ois.readObject();
            ois.close();
            fis.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
