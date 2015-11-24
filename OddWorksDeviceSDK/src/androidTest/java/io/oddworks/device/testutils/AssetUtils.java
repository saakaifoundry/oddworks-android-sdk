package io.oddworks.device.testutils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by dan on 11/24/15.
 */
public class AssetUtils {
    public static final String TAG = AssetUtils.class.getSimpleName();
    public static final int INPUT_BUFFER_SIZE = 3000;

    /** reads a file in the assets into a string
     * @param path path should be relative to assets directory
     */
    public static String readFileToString(Context ctx, String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[INPUT_BUFFER_SIZE];
        InputStream is = null;
        try {
            is = ctx.getAssets().open(path);
            InputStreamReader reader = new InputStreamReader(is);
            int numRead = reader.read(buffer);
            while (numRead > 0) {
                sb.append(buffer, 0, numRead);
                numRead = reader.read(buffer);
            }
            return sb.toString();
        } catch (IOException e) {
            Log.e(TAG, "Failed to open asset file " + path);
            throw e;
        } finally {
            if(is != null)
                is.close();
        }
    }
}
