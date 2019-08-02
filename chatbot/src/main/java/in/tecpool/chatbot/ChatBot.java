package in.tecpool.chatbot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by uvclient2 on 02/08/2019.
 */

public class ChatBot extends pl.droidsonroids.gif.GifImageView implements View.OnClickListener {

    Activity activity;
    String botKey,userKey;
    String info[];
    public pl.droidsonroids.gif.GifImageView gifImageView;


    public ChatBot(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity, String botKey,String userKey)
    {
        this.activity=activity;
        this.botKey=botKey;
        this.userKey=userKey;
        gifImageView=this;
        new AsyncValidateBot().execute();

        this.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        activity.startActivity(new Intent(activity,ChatBotActivity.class).putExtra("botKey",botKey).putExtra("userKey",userKey));
    }
    public class AsyncValidateBot extends AsyncTask<String, Integer, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

        }

        @Override
        protected String doInBackground(String... params) {
            WebserviceCall com = new WebserviceCall(activity.getBaseContext(), activity);
            try {

                String param13[][] = {{"uniqueId", botKey}};
                String aResponse13 = com.getStringWithoutActivity("getBotMaster", param13);

                return aResponse13;
            } catch (Exception ds) {
                return ds.toString();
            }


        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.startsWith("OK"))
            {
                info=result.substring(3).split(",");

                Picasso.with(activity).load(info[5]).into(gifImageView);
//                gifImageView.set(Color.parseColor("#" + info[4]));

            }
            else
            {
                Toast toast = Toast.makeText(activity.getBaseContext(), result.toString(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                toast.show();
            }
        }

    }


}
