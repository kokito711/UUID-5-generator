package com.test.test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

@Component
public class UuidType5Generator {
    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final UUID NAMESPACE = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Autowired
    public UuidType5Generator() {
    }

    public UUID generateUUID(UUID namespace, String name) {
        return uuidGenerator(namespace, Objects.requireNonNull(name, "name == null").getBytes(UTF8));
    }

    public UUID generateUUID(String name) {
        return uuidGenerator(NAMESPACE, Objects.requireNonNull(name, "name == null").getBytes(UTF8));
    }

    private UUID uuidGenerator(UUID namespace, byte[] name) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("SHA-1 not supported");
        }
        md.update(toBytes(Objects.requireNonNull(namespace, "namespace is null")));
        md.update(Objects.requireNonNull(name, "name is null"));
        byte[] sha1Bytes = md.digest();
        sha1Bytes[6] &= 0x0f;  /* clear version        */
        sha1Bytes[6] |= 0x50;  /* set to version 5     */
        sha1Bytes[8] &= 0x3f;  /* clear variant        */
        sha1Bytes[8] |= 0x80;  /* set to IETF variant  */
        return fromBytes(sha1Bytes);
    }

    private static UUID fromBytes(byte[] data) {
        // Based on the private UUID(bytes[]) constructor
        long msb = 0;
        long lsb = 0;
        assert data.length >= 16;
        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (data[i] & 0xff);
        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);
        return new UUID(msb, lsb);
    }

    private static byte[] toBytes(UUID uuid) {
        // inverted logic of fromBytes()
        byte[] out = new byte[16];
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        for (int i = 0; i < 8; i++)
            out[i] = (byte) ((msb >> ((7 - i) * 8)) & 0xff);
        for (int i = 8; i < 16; i++)
            out[i] = (byte) ((lsb >> ((15 - i) * 8)) & 0xff);
        return out;
    }
}
