package if26.android.doctoapp.Services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;

public class ImageService {
    private Context context;

    public ImageService(Context context) {
        this.context = context;
    }

    /**
     * Get URI from the provided path
     * @param path The path of the file to get the URI from
     * @return The URI
     */
    public static Uri GetURIFromPath(String path) {
        File f = new File(path);

        if (!f.exists()) return null;

        return Uri.fromFile(f);
    }

    /**
     * Get URI from the provided file
     * @param file The file to get the URI from
     * @return The URI
     */
    public static Uri GetURIFromFile(File file) {
        if (!file.exists()) return null;

        return Uri.fromFile(file);
    }

    /**
     * Retrieve the bitmap from the provided URI
     * @param uri The URI to get the bitmap from
     * @return The bitmap
     */
    public Bitmap GetBmpFromURI(Uri uri) {
        ImageView picture = new ImageView(this.context);
        picture.setImageURI(uri);

        return ((BitmapDrawable) picture.getDrawable()).getBitmap();
    }
}
