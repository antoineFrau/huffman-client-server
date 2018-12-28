package FilesManager;

import java.util.Arrays;

public class BinaryUtils {

    // Convert Bytes to String
    public static String bytesToBinaryString(byte[] bytes){
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            result += String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(" ", "0");
        }
        return result;
    }

    // Convert String bianry ("11111111") to bytes
    public static byte[] convertStringToBytes(String s){
        s = s.replaceAll("\\s+","");
        String[] list = s.split("(?<=\\G........)");
        byte[] result = new byte[list.length];
        for (int i = 0; i < list.length; i++) {
            result[i] = (byte) Long.parseLong(list[i], 2);
        }
        return result;
    }

    // Able to troncate a byte array
    public static byte[] subbytes(byte[] source, int srcBegin, int srcEnd) {
        byte destination[];

        destination = new byte[srcEnd - srcBegin];
        getBytes(source, srcBegin, srcEnd, destination, 0);

        return destination;
    }

    public static void getBytes(byte[] source, int srcBegin, int srcEnd, byte[] destination,
                                int dstBegin) {
        System.arraycopy(source, srcBegin, destination, dstBegin, srcEnd - srcBegin);
    }

    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
