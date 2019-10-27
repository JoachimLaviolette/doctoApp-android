package if26.android.doctoapp.Services;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;

public class ImageService {
    private Context context;
    private Resources resources;

    public ImageService(Context context) {
        this.context = context;
        this.resources = this.context.getResources();
    }

    /**
     * Get URI from the provided path
     * @param path The path of the file to get the URI from
     * @return The URI
     */
    public static Uri GetURIFromPath(String path) {
        return Uri.fromFile(new File(path));
    }

    /**
     * Get URI from the provided file
     * @param file The file to get the URI from
     * @return The URI
     */
    public static Uri GetURIFromFile(File file) {
        return Uri.fromFile(new File(file.getAbsolutePath()));
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
