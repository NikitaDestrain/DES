package gui;

import controller.DESUtils;
import controller.EncryptionConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class EncryptionWindow extends JFrame {

    public static EncryptionWindow instance;

    private JPanel allPanel;
    private JScrollPane scrollAll;

    private JPanel inTextPanel;
    private JTextArea in;
    private JTextArea inField;
    private JScrollPane inScrollText;

    private JPanel inKeyPanel;
    private JTextArea key;
    private JTextArea inKeyField;
    private JScrollPane keyScrollText;

    private JPanel outTextPanel;
    private JTextArea out;
    private JTextArea outField;
    private JScrollPane outScrollText;

    private JPanel buttonPanel;
    private JButton encrypt;
    private JButton decrypt;
    private JButton refresh;


    public EncryptionWindow() throws HeadlessException {
        instance = this;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(false);
        setTitle("DES");

        createAllPanel();
        add(allPanel, BorderLayout.NORTH);

        createInTextPanel();
        allPanel.add(inTextPanel);

        createInKeyPanel();
        allPanel.add(inKeyPanel);

        createOutTextPanel();
        allPanel.add(outTextPanel);

        createButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int action = JOptionPane.showConfirmDialog(
                        null, "Do you really want to close the app?",
                        "Warning!",
                        JOptionPane.YES_NO_OPTION);
                if (action == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    private void createAllPanel() {
        allPanel = new JPanel();
        scrollAll = new JScrollPane(allPanel);
        scrollAll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollAll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private void createInTextPanel() {
        inTextPanel = new JPanel();
        in = new JTextArea("In:");
        in.setEditable(false);
        in.setSize(15, 10);

        inField = new JTextArea(20, 20);

        inScrollText = new JScrollPane(inField);
        inScrollText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        inScrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inField.setEditable(true);
        inTextPanel.add(in);
        inTextPanel.add(inScrollText);
    }

    private void createInKeyPanel() {
        inKeyPanel = new JPanel();
        key = new JTextArea("Key:");
        key.setEditable(false);
        key.setSize(15, 10);

        inKeyField = new JTextArea(2, 10);

        keyScrollText = new JScrollPane(inKeyField);
        keyScrollText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        keyScrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inKeyPanel.add(key);
        inKeyPanel.add(keyScrollText);
    }

    private void createOutTextPanel() {
        outTextPanel = new JPanel();
        out = new JTextArea("Out:");
        out.setEditable(false);
        out.setSize(15, 10);

        outField = new JTextArea(20, 20);

        outScrollText = new JScrollPane(outField);
        outScrollText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outScrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        outField.setEditable(false);
        outTextPanel.add(out);
        outTextPanel.add(outScrollText);
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel();
        encrypt = new JButton("encrypt");
        decrypt = new JButton("decrypt");
        refresh = new JButton("refresh");

        refresh.addActionListener((ActionEvent e) -> {
            inField.setText("");
            inKeyField.setText("");
            outField.setText("");
        });

        encrypt.addActionListener((ActionEvent e) -> {
            try {
                if (inKeyField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Error! Enter the key!");
                } else {
                    if (inField.getText().equals(""))
                        JOptionPane.showMessageDialog(null, "Error! Enter the in message!");
                    else {
                        PrintWriter pw = new PrintWriter(new FileWriter(EncryptionConstants.DEFAULT_IN_PATH));
                        pw.write(inField.getText());
                        pw.close();

                        DESUtils.getInstance().encrypt(EncryptionConstants.DEFAULT_IN_PATH, inKeyField.getText());

                        File f1 = new File(EncryptionConstants.DEFAULT_OUT_PATH);
                        BufferedReader fin1 = new BufferedReader(new FileReader(f1));
                        String outText = fin1.readLine();

                        outField.setText(outText);
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error! Try later!");
            }
        });

        decrypt.addActionListener((ActionEvent e) -> {
            try {
                if (inKeyField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Error! Enter the key!");
                } else {
                    File f = new File(EncryptionConstants.DEFAULT_OUT_PATH);
                    BufferedReader fin = new BufferedReader(new FileReader(f));
                    PrintWriter pw = new PrintWriter(new FileWriter(EncryptionConstants.DEFAULT_IN_PATH));
                    pw.write(fin.readLine());
                    pw.close();
                    fin.close();
                    inField.setText(outField.getText());

                    System.out.println(f.length());

                    DESUtils.getInstance().decrypt(EncryptionConstants.DEFAULT_OUT_PATH, inKeyField.getText(),
                            (int) f.length() < 8 ? 8 : (int) f.length());

                    File f1 = new File(EncryptionConstants.DEFAULT_OUT_PATH);
                    BufferedReader fin1 = new BufferedReader(new FileReader(f1));
                    String outText = fin1.readLine();
                    fin1.close();
                    outField.setText(outText);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error! Try later!");
            }
        });

        buttonPanel.add(encrypt);
        buttonPanel.add(decrypt);
        buttonPanel.add(refresh);
    }


}
