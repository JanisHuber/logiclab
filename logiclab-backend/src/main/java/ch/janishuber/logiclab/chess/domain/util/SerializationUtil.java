package org.example.chess.backend.util;

import java.io.*;

public class SerializationUtil {
    public static <T extends Serializable> T deepClone(T object) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(object);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);

            @SuppressWarnings("unchecked")
            T clonedObject = (T) in.readObject();
            return clonedObject;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Fehler beim Klonen des Objekts", e);
        }
    }
}
