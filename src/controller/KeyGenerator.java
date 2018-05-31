package controller;

public class KeyGenerator {

    private final static byte[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    private final static byte[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    private final static byte[] CIRCULAR_SHIFTS = {
            1, 1, 2, 2,
            2, 2, 2, 2,
            1, 2, 2, 2,
            2, 2, 2, 1
    };

    private static KeyGenerator instance;

    private KeyGenerator() {
    }

    public static KeyGenerator getInstance() {
        if (instance == null) instance = new KeyGenerator();
        return instance;
    }

    private int circularLeftShift(int input, int shift) {
        return ((input << shift) & EncryptionConstants.MASK_28_BITS) | (input >> (28 - shift));
    }

    private long permutationChoice1(long input) {
        return DESUtils.genericPermutation(input, PC1, 64);
    }

    private long permutationChoice2(long input) {
        return DESUtils.genericPermutation(input, PC2, 56);
    }

    public long[] generateRoundKeys(String key) {
        long permutationKey = permutationChoice1(getLongKeyFromString(key));
        int halfA = (int) (permutationKey >> 28);
        int halfB = (int) (permutationKey & EncryptionConstants.MASK_28_BITS);

        long[] roundKeys = new long[EncryptionConstants.NUM_OF_ROUNDS];

        for (int i = 0; i < EncryptionConstants.NUM_OF_ROUNDS; i++) {
            halfA = circularLeftShift(halfA, CIRCULAR_SHIFTS[i]);
            halfB = circularLeftShift(halfB, CIRCULAR_SHIFTS[i]);

            long joinedHalves = ((halfA & EncryptionConstants.MASK_32_BITS) << 28) | (halfB & EncryptionConstants.MASK_32_BITS);
            roundKeys[i] = permutationChoice2(joinedHalves);
        }
        return roundKeys;
    }

    private long getLongKeyFromString (String key) {
        byte[] keyBytes;
        long key64 = 0;

        if (key.length() > 8) {
            key = key.substring(0, 8);
        }

        keyBytes = key.getBytes();
        for (byte keyByte : keyBytes) {
            key64 <<= 8;
            key64 |= keyByte;
        }
        return key64;
    }
}
