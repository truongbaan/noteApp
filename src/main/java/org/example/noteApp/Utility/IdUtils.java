package org.example.noteapp.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IdUtils {

    public static String generateUniqueId() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(now);
    }
}
