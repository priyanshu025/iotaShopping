package util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public final class BoldTextView extends AppCompatTextView {

    public BoldTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.applyfont();
    }

    private final void applyfont(){
        Typeface boldTypeFace=Typeface.createFromAsset(this.getContext().getAssets(),"Montserrat-Bold.ttf");
        this.setTypeface(boldTypeFace);
    }
}
