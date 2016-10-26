package self.harmony.logintest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

public class Camera extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    Button button;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.camera_layout);
        getFragmentManager().beginTransaction().add(R.id.fragment_layout, new NativeCameraFragment()).commit();
    }


}