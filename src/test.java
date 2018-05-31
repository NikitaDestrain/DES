import controller.DES;
import controller.DESUtils;
import controller.EncryptionConstants;

import javax.crypto.KeyGenerator;
import javax.xml.crypto.Data;
import java.io.*;
import java.nio.ByteBuffer;

public class test {

    public static void main(String[] args) throws FileNotFoundException, Exception {
        String s = "1";
        System.out.println(s.length());

        DESUtils du = DESUtils.getInstance();
        DES des = DES.getInstance();
/*
        long[] blocks = du.messageToBlocks("messagee", true);
        long[] cipherBlocks = new long[blocks.length];
        long[] plainTexts = new long[blocks.length];
        byte[] bytes;

        for (int i = 0; i < blocks.length; i++) {
            cipherBlocks[i] = des.encrypt(blocks[i], "key");
        }

        String s1 = "";
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File("out.txt")));
        for (long block : cipherBlocks) {
            bytes = ByteBuffer.allocate(8).putLong(block).array();
            dos.write(bytes);
            s1 += new String(bytes);
        }
        dos.close();
        byte[] test = s1.getBytes();

        File file = new File("out.txt");
        byte[] fileBuff = new byte[(int) file.length()];


            DataInputStream fileStream = new DataInputStream(new FileInputStream(file));
            fileStream.readFully(fileBuff);
            fileStream.close();



        System.out.println("encmes - " + s1);
        System.out.println("length - " + s1.length());
        System.out.println();

        String s2 = "";

        for (int i = 0; i < cipherBlocks.length; i++) {
            plainTexts[i] = des.decrypt(cipherBlocks[i], "key");
        }

        for (long block : plainTexts) {
            bytes = ByteBuffer.allocate(8).putLong(block).array();
            s2 += new String(bytes);
        }

        System.out.println("decr mes - " + s2);
        System.out.println("length - " + s2.length());
*/


        /*DataInputStream dis = new DataInputStream(new FileInputStream(new File("out.txt")));
        long test = dis.readLong();
        dis.close();*/
        //System.out.println(test);
        System.out.println();
        System.out.println();
        System.out.println("TEST");
        /*File file = new File("in.txt");
        PrintWriter pw = new PrintWriter(new FileWriter(file));
        pw.write("kek");
        pw.close();
        System.out.println(file.length());*/
        du.encrypt(EncryptionConstants.DEFAULT_IN_PATH, "key");
        du.decrypt(EncryptionConstants.DEFAULT_OUT_PATH, "key", 8);
        //System.out.println("otvet- " + mes);

    }
}
