package controller;

public class DES {

    private final static byte[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    private final static byte[] FP = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };


    private static DES instance;
    private KeyGenerator keyGenerator;
    private FeistelFunction feistelFunction;

    private DES() {
        keyGenerator = KeyGenerator.getInstance();
        feistelFunction = FeistelFunction.getInstance();
    }

    public static DES getInstance() {
        if (instance == null) instance = new DES();
        return instance;
    }

    public long encrypt(long block, String key) {
        return cipher(block, key, true);
    }

    public long decrypt(long block, String key) {
        return cipher(block, key, false);
    }

    private long cipher(long block, String key, boolean encrypt) {
        long[] roundKeys = keyGenerator.generateRoundKeys(key);
        block = initialPermutation(block);

        int leftHalf = (int) (block >> 32);
        int rightHalf = (int) block;
        int FOutput;

        for (int i = 0; i < EncryptionConstants.NUM_OF_ROUNDS; i++) {
            if (encrypt)
                FOutput = feistelFunction.F(rightHalf, roundKeys[i]);
            else
                FOutput = feistelFunction.F(rightHalf, roundKeys[(EncryptionConstants.NUM_OF_ROUNDS - 1) - i]);

            leftHalf ^= FOutput;

            leftHalf ^= rightHalf;
            rightHalf ^= leftHalf;
            leftHalf ^= rightHalf;
        }

        long joinedHalves = ((rightHalf & EncryptionConstants.MASK_32_BITS) << 32 | (leftHalf & EncryptionConstants.MASK_32_BITS));

        return finalPermutation(joinedHalves);
    }

    private long initialPermutation(long input) {
        return DESUtils.genericPermutation(input, IP, 64);
    }

    private long finalPermutation(long input) {
        return DESUtils.genericPermutation(input, FP, 64);
    }
}
