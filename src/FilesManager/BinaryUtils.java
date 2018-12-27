package FilesManager;

import java.util.Arrays;

public class BinaryUtils {

    public static String bytesToBinaryString(byte[] bytes){
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            result += String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(" ", "0");
        }
        return result;
    }

    public static byte[] convertStringToBytes(String s){
        s = s.replaceAll("\\s+","");
        String[] list = s.split("(?<=\\G........)");
        byte[] result = new byte[list.length];
        for (int i = 0; i < list.length; i++) {
            result[i] = (byte) Long.parseLong(list[i], 2);
        }
        return result;
    }

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
}
