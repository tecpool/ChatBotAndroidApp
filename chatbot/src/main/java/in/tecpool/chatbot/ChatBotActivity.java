package in.tecpool.chatbot;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatBotActivity extends AppCompatActivity {

    String botKey,userKey;
    String response;
    String id;
    List<BO_BOT_SCRIPT> list;

    ChatBotAdapter adapter;
    BO_BOT_SCRIPT boScript;
    RecyclerView rv;
    RelativeLayout rlTypeBox,rlEnd,rlBack;
    Button btnReply,btnEnd;
    EditText etReply;
    String info[];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        list=new ArrayList<BO_BOT_SCRIPT>();
        rv=(RecyclerView)findViewById(R.id.rv);
        rlTypeBox=(RelativeLayout)findViewById(R.id.rl_typebox);
        rlEnd=(RelativeLayout)findViewById(R.id.rl_end);
        rlBack=(RelativeLayout)findViewById(R.id.content_chat_bot);
        btnEnd=(Button) findViewById(R.id.btn_end);
        btnReply=(Button) findViewById(R.id.btn_reply);
        etReply=(EditText) findViewById(R.id.et_reply);


      //  setRlVisibility(0,0);

        try {
            botKey = getIntent().getStringExtra("botKey");
            botKey.equals("");
            userKey = getIntent().getStringExtra("userKey");
            id = "0";
            response = "";
            new AsyncValidateBot().execute();
        }
        catch (Exception a)
        {

        }


        // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etReply.getText().toString().equals(""))
                {
                    Toast.makeText(getBaseContext(),"Please enter text to reply",Toast.LENGTH_LONG).show();
                }
                else
                {
                    response=etReply.getText().toString();
                    BO_BOT_SCRIPT script=new BO_BOT_SCRIPT();
                    BO_BOT_SCRIPT answer=script.addAnswer(response);
                    list.add(answer);

                    new AsyncGetQuestionFromServer().execute();
                    setRlVisibility(0,Integer.parseInt(id));
                    etReply.setText("");
                }
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatBotActivity.this.finish();
            }
        });


    }


    public void setRlVisibility(int vs,int id1)
    {
        id=String.valueOf(id1);
        if(vs==0) {
            rlTypeBox.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) rv.getLayoutParams();
            marginLayoutParams.setMargins(0, 0, 0, 0);
            rv.setLayoutParams(marginLayoutParams);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = this.getCurrentFocus();
            if (view == null) {
                view = new View(this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
        else
        {
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) rv.getLayoutParams();
            marginLayoutParams.setMargins(0, 0, 0, 80);
            rv.setLayoutParams(marginLayoutParams);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = this.getCurrentFocus();
            if (view == null) {
                view = new View(this);
            }

            rlTypeBox.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            rv.scrollToPosition(list.size() - 1);
        }
    }

    public void callAsync(int redirectId,String responce)
    {

        id=String.valueOf(redirectId);
        response=responce;
        new AsyncGetQuestionFromServer().execute();
    }


    public class AsyncGetQuestionFromServer extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            boScript=new BO_BOT_SCRIPT("wait");
            list.add(boScript);

                rv.setHasFixedSize(true);
                rv.setLayoutManager(new LinearLayoutManager(ChatBotActivity.this));
                adapter= new ChatBotAdapter(getBaseContext(),ChatBotActivity.this, list,info);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                rv.scrollToPosition(list.size() - 1);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

        }

        @Override
        protected String doInBackground(String... params) {

            WebserviceCall com = new WebserviceCall(getBaseContext(), ChatBotActivity.this);
            try {
                Thread.sleep(2000);

                String param13[][] = {{"studentId", userKey}, {"uniqueId", botKey}, {"id", id}, {"response", response}};
                String aResponse13 = com.getStringWithoutActivity("getBotDetails", param13);
                if (aResponse13.toString().startsWith("{")) {

                    list.remove(list.size()-1);
                    boScript=new BO_BOT_SCRIPT(aResponse13);
                    list.add(boScript);
                    return "OK";
                } else {
                    return aResponse13;
                }
            } catch (Exception ds) {

            }

            return "OK";

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("OK"))
            {
                if(list.get(list.size()-1).redirectId==1001)
                {
                    list.remove(list.size()-1);
                    adapter.notifyDataSetChanged();
                    rlTypeBox.setVisibility(View.GONE);
                    rlEnd.setVisibility(View.VISIBLE);
                    ViewGroup.MarginLayoutParams marginLayoutParams =
                            (ViewGroup.MarginLayoutParams) rv.getLayoutParams();
                    marginLayoutParams.setMargins(0, 0, 0,100);
                    rv.setLayoutParams(marginLayoutParams);
                }
                else {
                    rlEnd.setVisibility(View.GONE);

                    if (list.get(list.size() - 1).typeId == 5 || list.get(list.size() - 1).typeId == 6 || list.get(list.size() - 1).typeId == 4) {
                        setRlVisibility(1, list.get(list.size() - 1).id);
                        if (list.get(list.size() - 1).typeId == 5) {
                            etReply.setSingleLine(true);
                            etReply.setInputType(InputType.TYPE_CLASS_TEXT);
                        } else if (list.get(list.size() - 1).typeId == 6) {
                            etReply.setSingleLine(true);
                            etReply.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else if (list.get(list.size() - 1).typeId == 4) {
                            etReply.setSingleLine(false);
                            etReply.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);

                        }

                    } else {
                        setRlVisibility(0, 0);
                    }
                    if (boScript.typeId == 1) {
                        setRlVisibility(0, 0);
                        id = String.valueOf(boScript.id);
                        new AsyncGetQuestionFromServer().execute();
                    }


                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new LinearLayoutManager(ChatBotActivity.this));
                    adapter = new ChatBotAdapter(getBaseContext(), ChatBotActivity.this, list, info);
                    rv.setAdapter(adapter);

                    adapter.notifyDataSetChanged();


                    rv.scrollToPosition(list.size() - 1);

                }
            }
            else {
                list.remove(list.size()-1);
                //     rv.setHasFixedSize(true);
                //      rv.setLayoutManager(new LinearLayoutManager(ChatBotActivity.this));
                //       adapter= new ChatBotAdapter(getBaseContext(),ChatBotActivity.this, list,info);
                //      rv.setAdapter(adapter);

                adapter.notifyDataSetChanged();


                rv.scrollToPosition(list.size() - 1);
                Toast toast = Toast.makeText(getBaseContext(), result.toString(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                toast.show();
            }
        }

    }


    public class AsyncValidateBot extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog;

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
            WebserviceCall com = new WebserviceCall(getBaseContext(), ChatBotActivity.this);
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

                rlBack.setBackgroundColor(Color.parseColor("#" + info[4]));





                new AsyncGetQuestionFromServer().execute();
            }
            else {
                Toast toast = Toast.makeText(getBaseContext(), result.toString(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                toast.show();
            }
        }

    }

}
