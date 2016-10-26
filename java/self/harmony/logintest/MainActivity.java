package self.harmony.logintest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String NEW_USER_URL = "http://52.29.233.100/api/v1/users";
    EditText editText;
    TextView textView;
    Button registerButton;
    Context context = this;
    private static String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.numberTextView);
        textView = (TextView) findViewById(R.id.textView);
        registerButton = (Button) findViewById(R.id.button);


        Selection.setSelection(editText.getText(), editText.getText().length()); //ставим курсор в конец
        editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); //форматируем номер в международный формат
        editText.addTextChangedListener(new TextWatcher() { //фиксируем код страны


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().startsWith("+7")) {
                    editText.setText("+7");
                    Selection.setSelection(editText.getText(), editText.getText().length());


                }


            }
        });





        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textView.setText(editText.getText());
                if (editText.length() >11) {
                    //Log.w(this.getClass().getSimpleName(), editText.getText().toString());
                    AsyncTask<String, Void, String> connector = new BackgroundConnector();
                    connector.execute(editText.getText().toString());
                }
            }
        });




    }

    class BackgroundConnector extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String phone = params[0];
            String data = "";
            try {
                HttpRequest request = HttpRequest.post(NEW_USER_URL); //коннектим к базе
                request.part("phone", phone);                         //постим номер
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
                user_id = root.getString("id");
                Log.e(this.getClass().getSimpleName(), user_id);
                Intent intent = new Intent(context, SecondScreen.class);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getUser_id() {
        return user_id;
    }


}