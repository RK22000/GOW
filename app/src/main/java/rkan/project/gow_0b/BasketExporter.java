package rkan.project.gow_0b;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BasketExporter {
    private Basket mBasket;
    private final File mOutPutFile;
    public BasketExporter(Basket basket, File outPutDirectory) {
        mBasket = basket;
        //Date
        mOutPutFile = new File(outPutDirectory, "Basket_export_file.txt");
    }
    public void export() {
        try {
            FileWriter writer = new FileWriter(mOutPutFile);
            writer.append("Basket list \n")
                    .append(mBasket.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public File getExportedFile() {
        if (mOutPutFile.isFile()) {
            return mOutPutFile;
        } else {
            return null;
        }
    }
}
