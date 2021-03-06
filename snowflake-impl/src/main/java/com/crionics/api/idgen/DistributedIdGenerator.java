package com.crionics.api.idgen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * A distributed Id generator based on snowflake_64, murmur3_128 and base58
 * <p>
 * - Generated hashed distributed keys, 2^128, base 58 encoded
 * - key = base58(murmur3_128(snowflake(nodeId)))
 * - nodeId is 1 for CLFY, and should be different for any other worker
 * <p>
 * Created by orefalo on 6/28/16.
 */
public class DistributedIdGenerator {

    private final static String BASE58CHARS = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private final static char ENCODED_ZERO = '1';

    // DO NOT CHANGE THIS NUMBER IF YOU HAVE EXISTING DATA (hashes)!
    // The seed should however be changed when you introduce hashing to your project.
    // typically the value will be different per application/client.
    private int seed = 1575865055;

    private SnowFlake snowflake;

    private final Logger log = LoggerFactory.getLogger(DistributedIdGenerator.class);

    private static DistributedIdGenerator instance = new DistributedIdGenerator();

    public static DistributedIdGenerator getInstance() {
        return instance;
    }

    private DistributedIdGenerator() {

        if (seed == 1575865055) {

            log.warn("For security reasons, you should generate a new random SEED for your application. Please customize code.");
        }

        // TODO: calculate node id, based on IP and port
        snowflake = new SnowFlake(0);
    }

    public String[] next(int count) throws IllegalArgumentException {

        if (count < 1)
            throw new IllegalArgumentException("Id count must be > 1");

        String[] ids = new String[count];

        for (int i = 0; i < count; i++)
            ids[i] = next();

        return ids;
    }

    public String next() {

        long dht_seq = snowflake.next();
        byte[] seq = longToByteArray(dht_seq);
        byte[] hash = MurmurHash3.murmurhash3_x64_128(seq, 0, 8, seed);
        return base58Bytes(hash);
    }

    private static byte[] longToByteArray(long l) {
        byte[] retVal = new byte[8];

        for (int i = 0; i < 8; i++) {
            retVal[i] = (byte) l;
            l >>= 8;
        }
        return retVal;
    }

    private static String base58Bytes(byte[] input) {
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

    public static void main(String[] arg) {

        for (int i = 0; i < 100; i++)
            System.out.println(DistributedIdGenerator.getInstance().next());
    }


}
