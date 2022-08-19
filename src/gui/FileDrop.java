package gui;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;

public class FileDrop extends TransferHandler {

    @Override
    public boolean canImport(TransferSupport support) {
        if(!support.isDrop())
            return false;
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean importData(TransferSupport support) {
        if(!canImport(support))
            return false;

        try {
            List<File> files = (List<File>)support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            Thread thread = new Thread(() -> {
                try {
                    new DroppedFilesProcessor(files).process();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
