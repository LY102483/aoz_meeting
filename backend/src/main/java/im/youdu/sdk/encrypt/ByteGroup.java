package im.youdu.sdk.encrypt;

import java.util.ArrayList;


class ByteGroup {
    private final ArrayList<Byte> byteContainer = new ArrayList<Byte>();

    byte[] toBytes() {
        byte[] bytes = new byte[byteContainer.size()];
        for (int i = 0; i < byteContainer.size(); i++) {
            bytes[i] = byteContainer.get(i);
        }
        return bytes;
    }

    ByteGroup addBytes(byte[] bytes) {
        for (byte b : bytes) {
            byteContainer.add(b);
        }
        return this;
    }

    int size() {
        return byteContainer.size();
    }
}
