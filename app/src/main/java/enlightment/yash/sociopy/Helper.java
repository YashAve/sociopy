package enlightment.yash.sociopy;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Helper {

    public static String KEY;

    public static boolean create7ZArchive(String zipFilePath, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);

            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            zipOutputStream.putNextEntry(new ZipEntry(file.getAbsolutePath()));

            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }

            fileInputStream.close();
            zipOutputStream.closeEntry();
            zipOutputStream.close();

            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    public static String getMd5Sum(File file) {
        String checksum = "unavailable";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                byte[] data = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                byte[] hash = MessageDigest.getInstance("MD5").digest(data);
                checksum = new BigInteger(1, hash).toString();
            } catch (Exception ignored) {

            }
        }
        return checksum;
    }

    public static File getJsonFile(final String filePath, JSONObject jsonObject) {
        File file = new File(filePath);
        try {
            Writer output = new BufferedWriter(new FileWriter(file));
            output.write(jsonObject.toString());
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public static <T> JSONObject getJSONObject(HashMap<String, T> values) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for (var value : values.entrySet()) {
            jsonObject.put(value.getKey(), value.getValue());
        }
        return jsonObject;
    }

    public static String encrypt(String argument) throws Exception {
        byte[] keyBytes = KEY.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, 0, 16, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedData = cipher.doFinal(argument.getBytes());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(encryptedData);
        }
        return null;
    }
}
