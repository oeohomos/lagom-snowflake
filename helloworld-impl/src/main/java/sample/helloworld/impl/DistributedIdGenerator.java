package sample.helloworld.impl;

import java.util.Arrays;

/**
 * A distributed Id generator based on snowflake, murmur3 and base58
 *
 * - Generated hashed distributed keys, 2^64, base 58 encoded
 * - nodeId is 1 for CLFY, and should be different for any other worker
 *
 * Created by orefalo on 6/28/16.
 */
public class DistributedIdGenerator {

    private final static String BASE58CHARS = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private final static char ENCODED_ZERO = '1';

    private SnowFlake snowflake;

    public DistributedIdGenerator(int nodeId) {
        snowflake = new SnowFlake(nodeId);
    }

    public String next() {

        long dht_seq = snowflake.next();
        long hash = murmur3(dht_seq);
        return DistributedIdGenerator.base58(hash);
    }

    /**
     * murmur3 hasher
     */
    private static final long murmur3(long k) {
        k ^= k >>> 33;
        k *= 0xff51afd7ed558ccdL;
        k ^= k >>> 33;
        k *= 0xc4ceb9fe1a85ec53L;
        k ^= k >>> 33;
        return k;
    }


    private static String base58(long l) {

        byte[] input = new byte[8];
        for (int i = 7; i >= 0; i--) {
            input[i] = (byte) (l & 0xFF);
            l >>= 8;
        }

        // Count leading zeros.
        int zeros = 0;
        while (zeros < input.length && input[zeros] == 0) {
            ++zeros;
        }
        // Convert base-256 digits to base-58 digits (plus conversion to ASCII characters)
        input = Arrays.copyOf(input, input.length); // since we modify it in-place
        char[] encoded = new char[input.length * 2]; // upper bound
        int outputStart = encoded.length;
        for (int inputStart = zeros; inputStart < input.length; ) {
            encoded[--outputStart] = BASE58CHARS.charAt(divmod(input, inputStart, 256, 58));
            if (input[inputStart] == 0) {
                ++inputStart; // optimization - skip leading zeros
            }
        }
        // Preserve exactly as many leading encoded zeros in output as there were leading zeros in input.
        while (outputStart < encoded.length && encoded[outputStart] == ENCODED_ZERO) {
            ++outputStart;
        }
        while (--zeros >= 0) {
            encoded[--outputStart] = ENCODED_ZERO;
        }
        // Return encoded string (including encoded leading zeros).
        return new String(encoded, outputStart, encoded.length - outputStart);
    }


    /**
     * Divides a number, represented as an array of bytes each containing a single digit
     * in the specified base, by the given divisor. The given number is modified in-place
     * to contain the quotient, and the return value is the remainder.
     *
     * @param number     the number to divide
     * @param firstDigit the index within the array of the first non-zero digit
     *                   (this is used for optimization by skipping the leading zeros)
     * @param base       the base in which the number's digits are represented (up to 256)
     * @param divisor    the number to divide by (up to 256)
     * @return the remainder of the division operation
     */
    private static byte divmod(byte[] number, int firstDigit, int base, int divisor) {
        // this is just long division which accounts for the base of the input digits
        int remainder = 0;
        for (int i = firstDigit; i < number.length; i++) {
            int digit = (int) number[i] & 0xFF;
            int temp = remainder * base + digit;
            number[i] = (byte) (temp / divisor);
            remainder = temp % divisor;
        }
        return (byte) remainder;
    }

    static public void main(String arg[]) {

        int nodeId = 1;
        DistributedIdGenerator g = new DistributedIdGenerator(nodeId);

        for (int i = 0; i < 100; i++) {
            System.out.println(g.next());
        }
    }

}
