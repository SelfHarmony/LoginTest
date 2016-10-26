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

import org.json.JSONException;
import org.json.JSONObject;


public class SecondScreen extends AppCompatActivity {
    public static final String TOKEN_URL = "http://52.29.233.100/api/v1/token";
    Context context = this;
    Button button;
    EditText editText;
    static SharedPreferences sPref;
    private static String token;
    final String SAVED_TOKEN = "saved_token";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_screen);
        button = (Button) findViewById(R.id.button2);
        editText = (EditText) findViewById(R.id.editText2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundConnector().execute(String.valueOf(editText.getText()), MainActivity.getUser_id());
            }
        });

    }

    class BackgroundConnector extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            int code = Integer.parseInt(params[0]);
            String user_id = params[1];
            String data = "";
            try {
                HttpRequest request = HttpRequest.post(TOKEN_URL); //коннектим к базе
                request.part("code", code);                         //постим код
                request.part("user_id", user_id);                         //постим user_id
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
            try {
                JSONObject root = new JSONObject(s);
                token = root.getString("token");
                saveToken();
                Log.e(this.getClass().getSimpleName(), token);
                Intent intent = new Intent(context, ThirdScreen.class);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    void saveToken() { //сохраняем token в общедоступный SharedPreference

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SAVED_TOKEN, token);
        editor.apply();
    }


}
