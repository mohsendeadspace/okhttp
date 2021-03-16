package okhttp.app.mastergame.com.okhttp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


  private String url="http://api.icndb.com/jokes/random";
  private TextView txtjoke;
  private Button btnGetRandomjoke;
  private ProgressDialog progressDialog;
  String joke="";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    txtjoke=(TextView)findViewById(R.id.txtjoke);
    btnGetRandomjoke=(Button) findViewById(R.id.btnGetRandomjoke);

    btnGetRandomjoke.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new Getjoke().execute();
      }
    });


  }

  private JSONObject getJokeJSONByOkHttp(String url)
  {
    try {
      OkHttpClient okHttpClient = new OkHttpClient();
      Request request = new Request.Builder()
        .url(url)
        .build();

      Response response = okHttpClient.newCall(request).execute();
      return new JSONObject(response.body().string());
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return  null;
  }
  private  class Getjoke extends AsyncTask<Void,Void,Void>{

    @Override
    protected Void doInBackground(Void... params) {
      JSONObject jsonObject=getJokeJSONByOkHttp(url);
      try {
        JSONObject jsonObject1=jsonObject.getJSONObject("value");
        joke=jsonObject.getString("joke");
      }catch (JSONException e){
        e.printStackTrace();
      }

      return null;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog=new ProgressDialog(MainActivity.this);
      progressDialog.setTitle(getString(R.string.app_name));
      progressDialog.setMessage(getString(R.string.loading_joke_message));
      progressDialog.show();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      progressDialog.dismiss();
      if(!joke.equals(""))
      {
        txtjoke.setText(Html.fromHtml(joke));
      }
    }
  }
}
