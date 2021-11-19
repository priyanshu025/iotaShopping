package util;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Gallery;

public class ImageChooser {
    public int IMAGE_REQUEST_CODE=1;

  public void setImage_picker(Activity activity){
      Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      activity.startActivityForResult(galleryIntent,IMAGE_REQUEST_CODE);
  }

    public  int getImageRequestCode() {
        return IMAGE_REQUEST_CODE;
    }
}
