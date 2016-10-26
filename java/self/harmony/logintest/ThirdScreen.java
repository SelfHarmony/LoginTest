package self.harmony.logintest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ThirdScreen extends AppCompatActivity {
    final String SAVED_TOKEN = "saved_token";
    String token;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    Button button;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_screen);
        loadToken();

        editText1 = (EditText) findViewById(R.id.editText5);
        editText2 = (EditText) findViewById(R.id.editText3);
        editText3 = (EditText) findViewById(R.id.editText4);
        button = (Button) findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundConnector().execute(String.valueOf(editText1.getText()),
                        String.valueOf(editText2.getText()), String.valueOf(editText3.getText()));
            }
        });
    }





    void loadToken() { //выгружаем token из общего shared preference

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        token = (mSharedPreference.getString(SAVED_TOKEN, ""));
        Log.i("third screen", token);
    }

    class BackgroundConnector extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String first_name = params[0];
            String last_name = params[1];
            String nickname = params[2];
            String data = "";
            try {
                HttpRequest request = HttpRequest.patch("http://52.29.233.100/api/v1/users/me?token="+token); //коннектим к базе
                request.part("first_name", first_name);                         //постим first_name"
                request.part("last_name", last_name);                         //постим last_name
                request.part("nickname", nickname);                         //постим nickname

                if (request.ok()) {                                   //проверяем что код ответа 200
                    data = request.body();
                    Log.i(this.getClass().getSimpleName(), data);
                }
            } catch (HttpRequest.HttpRequestException e) {
                e.printStackTrace();
            }

            return data;   //получаем и возвращаем ответ базы


        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(this.getClass().getSimpleName(), s);

                Intent intent = new Intent(context, CameraActivity.class);
                startActivity(intent);

        }

    }
}
