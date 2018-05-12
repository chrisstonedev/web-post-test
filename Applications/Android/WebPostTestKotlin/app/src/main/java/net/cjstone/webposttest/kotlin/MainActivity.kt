package net.cjstone.webposttest.kotlin

import android.content.DialogInterface
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.ViewGroup.LayoutParams
import android.widget.*
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.StatusLine
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mLinearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mLinearLayout = findViewById(R.id.content_main)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener({
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Send new message")

            // Set up the input
            val input = EditText(this@MainActivity)
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE
            builder.setView(input)

            // Set up the buttons
            builder.setPositiveButton("OK", { _: DialogInterface, _: Int ->
                postText(input.text.toString())
            })
            builder.setNegativeButton("Cancel", { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            })

            builder.show()
        })

        UpdateTask().execute()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item!!.itemId

        if (id == R.id.action_refresh) {
            UpdateTask().execute()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun postText(text: String) {
        try {
            PostTask().execute("posttext", "text", text)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        UpdateTask().execute()
    }

    fun postDelete(id: String) {
        try {
            PostTask().execute("postdelete", "id", id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        UpdateTask().execute()
    }

    inner class UpdateTask : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {
            refreshDisplay()
            return ""
        }

        private fun getJSONUrl(url: String): String {
            val str = StringBuilder()
            val client: HttpClient = DefaultHttpClient()
            val httpGet = HttpGet(url)
            try {
                val response: HttpResponse = client.execute(httpGet)
                val statusLine: StatusLine = response.statusLine
                val statusCode: Int = statusLine.statusCode
                if (statusCode == 200) { // Download OK
                    val entity: HttpEntity = response.entity
                    val content: InputStream = entity.content
                    val reader = BufferedReader(InputStreamReader(content))
                    var line: String? = null
                    while ({ line = reader.readLine(); line }() != null) {
                        str.append(line)
                    }
                } else {
                    Log.e("Log", "Failed to download result..")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return str.toString()
        }

        private fun refreshDisplay() {
            val url = "http://cjstone.net/webposttest/gettext.php"
            try {
                val data = JSONArray(getJSONUrl(url))
                val cardViews = ArrayList<CardView>()
                for (i in 0 until mLinearLayout.childCount) {
                    val view: View = mLinearLayout.getChildAt(i)
                    if (view is CardView) {
                        cardViews.add(view)
                    }
                }
                for (i in 0 until data.length()) {
                    val jsonObject: JSONObject = data.getJSONObject(i)

                    val id: String = jsonObject.getString(ID)
                    var date = jsonObject.getString(DATE)
                    val source: String = jsonObject.getString(SOURCE)
                    val text: String = jsonObject.getString(TEXT)

                    val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                    // replace with your start date string
                    val d: Date = df.parse(date)
                    var time: Long = d.time
                    time +=(3*60*60*1000)
                    date = Date(time).toString()

                    var createCard = true
                    for (j in 0 until cardViews.size) {
                        if (cardViews[j].id == Integer.parseInt(id)) {
                            cardViews.removeAt(j)
                            createCard = false
                            break
                        }
                    }
                    if (!createCard)
                        continue

                    val card = CardView(this@MainActivity)
                    card.id = 900 + Integer.parseInt(id)

                    val params = FrameLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    params.setMargins(0, 0, 0, 50)
                    card.layoutParams = params

                    card.radius = 9f
                    card.setContentPadding(15, 15, 15, 15)
                    card.setCardBackgroundColor(Color.parseColor("#FFF0F0F0"))
                    card.maxCardElevation = 15f
                    card.cardElevation = 9f

                    val innerLayout = LinearLayout(this@MainActivity)
                    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                    innerLayout.layoutParams = layoutParams
                    innerLayout.orientation = LinearLayout.VERTICAL

                    val textParams = ViewGroup.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    val mainTextView = TextView(this@MainActivity)
                    mainTextView.layoutParams = textParams
                    mainTextView.text = text
                    mainTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f)
                    mainTextView.setTextColor(Color.BLACK)
                    innerLayout.addView(mainTextView)
                    val subTextView = TextView(this@MainActivity)
                    subTextView.text = "$source * $date"
                    subTextView.layoutParams = textParams
                    subTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10f)
                    subTextView.setTextColor(Color.DKGRAY)
                    subTextView.gravity = Gravity.END
                    innerLayout.addView(subTextView)
                    card.addView(innerLayout)

                    val viewDetail = AlertDialog.Builder(this@MainActivity)
                    card.setOnLongClickListener({
                        viewDetail.setMessage("Delete message?")
                        viewDetail.setPositiveButton("Yes", { dialog: DialogInterface, _: Int ->
                            dialog.dismiss()
                            try {
                                postDelete(id)
                            } catch (e: Exception) {
                                val toasty = Toast.makeText(applicationContext,
                                e.message, Toast.LENGTH_LONG)
                                toasty.show()
                            }
                            UpdateTask().execute()
                        })
                        viewDetail.setNegativeButton("No", { dialog: DialogInterface, _: Int ->
                            dialog.dismiss()
                        })
                        viewDetail.show()
                        true
                    })
                    runOnUiThread({ mLinearLayout.addView(card) })
                }
                for (j in 0 until cardViews.size) {
                    val view: View = cardViews[j]
                    runOnUiThread({ mLinearLayout.removeView(view) })
                }
            } catch (e: JSONException) {
                val errToast = Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG)
                errToast.show()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    inner class PostTask : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg strings: String?): String {
            try {
                sendPost(strings[0].orEmpty(), strings[1].orEmpty(), strings[2].orEmpty())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        @Throws(Exception::class)
        private fun sendPost(urlPage: String, label: String, value: String) {
            val url = "http://cjstone.net/webposttest/$urlPage.php"

            val client = DefaultHttpClient()
            val post = HttpPost(url)

            val urlParameters = ArrayList<NameValuePair>()
            urlParameters.add(BasicNameValuePair("source", "Android (Kotlin)"))
            urlParameters.add(BasicNameValuePair(label, value))

            post.entity = UrlEncodedFormEntity(urlParameters)

            val response = client.execute(post)
            Log.v(TAG, "\nSending 'POST' request to URL : $url")
            Log.v(TAG, "Post parameters : " + post.entity)
            Log.v(TAG, "Response Code : " + response.statusLine.statusCode)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val ID = "id"
        private const val DATE = "date"
        private const val SOURCE = "source"
        private const val TEXT = "text"
    }
}
