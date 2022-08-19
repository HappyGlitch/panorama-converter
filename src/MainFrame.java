import gui.FileDrop;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public static void main(String[] args) {
        new MainFrame();
    }

    private JPanel content = new JPanel();

    public MainFrame() {
        super("Equirectangular Projection!");
        setup();
    }

    private void setup() {
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupContent();
        setVisible(true);
    }

    private void setupContent() {
        getContentPane().add(content);
        content.setLayout(new GridBagLayout());
        content.setTransferHandler(new FileDrop());


        content.add(new JLabel("Drop image file here"));
    }
}
