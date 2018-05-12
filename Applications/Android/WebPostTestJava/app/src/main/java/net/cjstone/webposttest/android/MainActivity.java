package net.cjstone.webposttest.android;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String SOURCE = "source";
    private static final String TEXT = "text";
    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLinearLayout = (LinearLayout) findViewById(R.id.content_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Send new message");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postText(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        new UpdateTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            new UpdateTask().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void postText(String text) {
        try {
            new PostTask().execute("posttext", "text", text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new UpdateTask().execute();
    }

    private void postDelete(String id) {
        try {
            new PostTask().execute("postdelete", "id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new UpdateTask().execute();
    }

    private class UpdateTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            refreshDisplay();
            return null;
        }

        private String getJSONUrl(String url) {
            StringBuilder str = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) { // Download OK
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        str.append(line);
                    }
                } else {
                    Log.e("Log", "Failed to download result..");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str.toString();
        }

        private void refreshDisplay() {
            String url = "http://cjstone.net/webposttest/gettext.php";
            try {
                JSONArray data = new JSONArray(getJSONUrl(url));
                ArrayList<CardView> cardViews = new ArrayList<>();
                for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
                    View view = mLinearLayout.getChildAt(i);
                    if (view instanceof CardView) {
                        cardViews.add((CardView)view);
                    }
                }
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);

                    final String id = jsonObject.getString(ID);
                    String date = jsonObject.getString(DATE);
                    String source = jsonObject.getString(SOURCE);
                    String text = jsonObject.getString(TEXT);

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    // replace with your start date string
                    Date d = df.parse(date);
                    Long time = d.getTime();
                    time +=(3*60*60*1000);
                    date = new Date(time).toString();

                    boolean createCard = true;
                    for (int j = 0; j < cardViews.size(); j++) {
                        if (cardViews.get(j).getId() == Integer.parseInt(id)) {
                            cardViews.remove(j);
                            createCard = false;
                            break;
                        }
                    }
                    if (!createCard)
                        continue;

                    final CardView card = new CardView(MainActivity.this);
                    card.setId(900 + Integer.parseInt(id));

                    CardView.LayoutParams params = new CardView.LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 0, 50);
                    card.setLayoutParams(params);

                    card.setRadius(9);
                    card.setContentPadding(15, 15, 15, 15);
                    card.setCardBackgroundColor(Color.parseColor("#FFF0F0F0"));
                    card.setMaxCardElevation(15);
                    card.setCardElevation(9);

                    LinearLayout innerLayout = new LinearLayout(MainActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT
                    );
                    innerLayout.setLayoutParams(layoutParams);
                    innerLayout.setOrientation(LinearLayout.VERTICAL);

                    CardView.LayoutParams textParams = new CardView.LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                    );
                    TextView mainTextView = new TextView(MainActivity.this);
                    mainTextView.setLayoutParams(textParams);
                    mainTextView.setText(text);
                    mainTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    mainTextView.setTextColor(Color.BLACK);
                    innerLayout.addView(mainTextView);
                    TextView subTextView = new TextView(MainActivity.this);
                    subTextView.setText(source + " * " + date);
                    subTextView.setLayoutParams(textParams);
                    subTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    subTextView.setTextColor(Color.DKGRAY);
                    subTextView.setGravity(Gravity.END);
                    innerLayout.addView(subTextView);
                    card.addView(innerLayout);

                    final AlertDialog.Builder viewDetail = new AlertDialog.Builder(MainActivity.this);
                    card.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            viewDetail.setMessage("Delete message?");
                            viewDetail.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    try {
                                        postDelete(id);
                                    } catch (Exception e) {
                                        Toast toasty = Toast.makeText(getApplicationContext(),
                                                e.getMessage(), Toast.LENGTH_LONG);
                                        toasty.show();
                                    }
                                    new UpdateTask().execute();
                                }
                            });
                            viewDetail.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            viewDetail.show();
                            return true;
                        }
                    });
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mLinearLayout.addView(card);
                        }
                    });
                }
                for (int j = 0; j < cardViews.size(); j++) {
                    final View view = cardViews.get(j);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mLinearLayout.removeView(view);
                        }
                    });
                }
            } catch (JSONException e) {
                Toast errToast = Toast.makeText(MainActivity.this, e.getMessage(), Toast
                        .LENGTH_LONG);
                errToast.show();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private class PostTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
                sendPost(strings[0], strings[1], strings[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void sendPost(String urlPage, String label, String value) throws Exception {
            String url = "http://cjstone.net/webposttest/" + urlPage + ".php";

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("source", "Android (Java)"));
            urlParameters.add(new BasicNameValuePair(label, value));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);
            Log.v(TAG, "\nSending 'POST' request to URL : " + url);
            Log.v(TAG, "Post parameters : " + post.getEntity());
            Log.v(TAG, "Response Code : " + response.getStatusLine().getStatusCode());
        }
    }
}