package controller;

import java.io.*;
import java.nio.ByteBuffer;

public class DESUtils {

    private static DESUtils instance;
    private DES des;

    private DESUtils() {
        des = DES.getInstance();
    }

    public static DESUtils getInstance() {
        if (instance == null) instance = new DESUtils();
        return instance;
    }

    private byte[] addTo64BitBlocks(byte[] input) {
        int newLength = input.length;

        while (newLength % 8 != 0)
            ++newLength;

        byte[] res = new byte[newLength];

        for (int i = 0; i < input.length; i++) {
            res[i] = input[i];
        }
        return res;
    }

    private long[] messageToBlocks(String path) throws IOException {
        File file = new File(path);
        byte[] input = new byte[(int) file.length()];

        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        dis.readFully(input);
        dis.close();

        if (input.length % 8 != 0)
            input = addTo64BitBlocks(input);

        long blocks[] = new long[input.length / 8];

        for (int i = 0, j = -1; i < input.length; i++) {
            if (i % 8 == 0)
                j++;
            blocks[j] <<= 8;
            blocks[j] |= input[i];
        }

        return blocks;
    }

    private long[] decryptMessageToBlocks(String path, int size) throws IOException {
        File file = new File(path);
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        int lengthOfFile = size / 8;
        long res[] = new long[lengthOfFile];


        int i = 0;
        while (lengthOfFile > 0) {
            res[i] = dis.readLong();
            i++;
            lengthOfFile--;
        }

        dis.close();
        return res;
    }

    public static long genericPermutation(long input, byte[] indexTable, int inputLength) {
        long output = 0;
        int index;

        for (byte anIndexTable : indexTable) {
            index = inputLength - anIndexTable;
            output = (output << 1) | ((input >> index) & 1);
        }

        return output;
    }

    public void encrypt(String path, String key) throws IOException {
        long[] blocks = messageToBlocks(path);
        long[] encryptBlocks = new long[blocks.length];
        byte[] bytes;

        for (int i = 0; i < blocks.length; i++) {
            encryptBlocks[i] = des.encrypt(blocks[i], key);
        }

        DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(EncryptionConstants.DEFAULT_OUT_PATH)));
        for (long block : encryptBlocks) {
            bytes = ByteBuffer.allocate(8).putLong(block).array();
            dos.write(bytes);
        }
        dos.close();
    }


    public void decrypt(String path, String key, int size) throws IOException {
        long[] blocks = decryptMessageToBlocks(path, size);
        long[] decryptBlocks = new long[blocks.length];
        byte[] bytes;

        for (int i = 0; i < blocks.length; i++) {
            decryptBlocks[i] = des.decrypt(blocks[i], key);
        }

        DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(EncryptionConstants.DEFAULT_OUT_PATH)));
        for (long block : decryptBlocks) {
            bytes = ByteBuffer.allocate(8).putLong(block).array();
            dos.write(bytes);
        }
        dos.close();
    }

}
