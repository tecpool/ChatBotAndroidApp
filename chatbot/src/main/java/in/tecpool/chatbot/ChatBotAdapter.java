package in.tecpool.chatbot;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by umesha on 07-03-2018.
 */

public class ChatBotAdapter extends RecyclerView.Adapter<ChatBotAdapter.MyViewHolderH> {


    Context context;
    ChatBotActivity activity;
    List<BO_BOT_SCRIPT> list;
    String id="";
    String uniqueId="";
    String selection="";
    String response="";
    String info[];
    boolean isPreviousBot;



    public class MyViewHolderH extends RecyclerView.ViewHolder {


        TextView tvMsg;
        LinearLayout ll,who,main,sub;
        GifImageView wait;
        ImageView botIcon;


        public MyViewHolderH(View view) {
            super(view);

            tvMsg=(TextView) view.findViewById(R.id.msg);
            botIcon=(ImageView) view.findViewById(R.id.bot_icon);
            ll=(LinearLayout) view.findViewById(R.id.ll);
            who=(LinearLayout) view.findViewById(R.id.who);
            main=(LinearLayout) view.findViewById(R.id.ll_main);
            sub=(LinearLayout) view.findViewById(R.id.ll_sub);
            wait=(GifImageView) view.findViewById(R.id.wait);
            isPreviousBot=false;

            Picasso.with(context).load(info[6]).into(botIcon);

        }
    }


    public ChatBotAdapter(Context context, Activity activity, List<BO_BOT_SCRIPT> botScripts,String[] botInformation) {
        this.list = botScripts;
        this.context = context;
        this.activity =(ChatBotActivity) activity;
        info=botInformation;




    }

    @Override
    public MyViewHolderH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bot, parent, false);

        return new MyViewHolderH(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolderH holder, final int position) {
        final BO_BOT_SCRIPT script = list.get(position);

        if(position>0)
        {
            BO_BOT_SCRIPT script1 = list.get(position-1);
            if(script1.typeId==1000)
            {
                holder.botIcon.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.botIcon.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            holder.botIcon.setVisibility(View.VISIBLE);

        }

        holder.wait.setVisibility(View.GONE);
       // activity.setRlVisibility(View.INVISIBLE,0);

        LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(context,R.drawable.bot_message);
        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.bot_shape);
        gradientDrawable.setColor(Color.parseColor("#" + info[2])); // change color
        holder.who.setBackground(layerDrawable);

        holder.tvMsg.setTextColor(Color.WHITE);
        holder.main.setGravity(Gravity.LEFT);
        holder.sub.setGravity(Gravity.LEFT);
        switch(script.typeId)
        {
            case 0:
                holder.wait.setVisibility(View.VISIBLE);
                holder.who.setBackgroundColor(Color.TRANSPARENT);
                holder.tvMsg.setText("");
                break;
            case 1:
            case 5:
            case 4:
            case 6:
                holder.tvMsg.setText(script.message);
                break;
            case 1000:
                isPreviousBot=false;
                holder.main.setGravity(Gravity.RIGHT);
                holder.sub.setGravity(Gravity.RIGHT);
                holder.tvMsg.setText(script.message);
                LayerDrawable layerDrawable1 = (LayerDrawable) ContextCompat.getDrawable(context,R.drawable.user_message);
                GradientDrawable gradientDrawable1 = (GradientDrawable) layerDrawable1.findDrawableByLayerId(R.id.user_shape);
                gradientDrawable1.setColor(Color.parseColor("#" + info[3])); // change color

                holder.who.setBackground(layerDrawable1);
              //  holder.who.invalidate();
                holder.botIcon.setVisibility(View.GONE);
                //     activity.setRlVisibility(0,script.id);
                holder.tvMsg.setTextColor(Color.WHITE);
                break;
            case 2:
                holder.tvMsg.setText(script.message);
              //  activity.setRlVisibility(0,script.id);
                List<BO_SCRIPT_DETAILS> details=script.scriptDetails;
                for (final BO_SCRIPT_DETAILS detail:details ) {
                    if(script.isAnswered=='N') {
                        Button myButton = new Button(context);
                        myButton.setText(detail.optionValue);
                        designButton(myButton);
                        try {
                            (holder.ll).addView(myButton);
                        } catch (Exception s) {
                            String s1 = s.toString();
                        }
                        myButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              //  Toast.makeText(context, "clicked on meee" + detail.redirectId, Toast.LENGTH_LONG).show();
                                script.isAnswered='Y';
                                BO_BOT_SCRIPT answer=script.addAnswer(detail.optionValue);
                                list.add(answer);
                                activity.callAsync(detail.scriptId, detail.optionValue);
                            }
                        });
                        holder.ll.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        holder.ll.setVisibility(View.GONE);
                    }
                }
                break;


            case 3:
                holder.tvMsg.setText(script.message);


                //  activity.setRlVisibility(0,script.id);
                List<BO_SCRIPT_DETAILS> details3=script.scriptDetails;

                if(script.isAnswered=='N') {
                for (final BO_SCRIPT_DETAILS detail:details3 ) {

                        final CheckBox chkBox = new CheckBox(context);
                       chkBox.setText(detail.optionValue);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    chkBox.setLayoutParams(lp);
                        try {
                            (holder.ll).addView(chkBox);
                        } catch (Exception s) {
                            String s1 = s.toString();
                        }

                    chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                          //  Toast.makeText(context, "clicked on meee" + detail.redirectId, Toast.LENGTH_LONG).show();
                            if(isChecked) {
                                detail.isSelected = 'Y';
                                selection=selection + chkBox.getText().toString()+ "," ;
                            }
                            else {
                                detail.isSelected = 'N';
                                selection=selection.replace(chkBox.getText().toString()+ ",","") ;
                            }
                          //  BO_BOT_SCRIPT answer=script.addAnswer(detail.optionValue);
                           // list.add(answer);
                           // activity.callAsync(detail.scriptId, detail.optionValue);
                        }
                    });



                        holder.ll.setVisibility(View.VISIBLE);


                }
                    Button btn=new Button(context);
                    designButton(btn);

                    btn.setText("Submit");
                    try {
                        (holder.ll).addView(btn);
                    } catch (Exception s) {
                        String s1 = s.toString();
                    }
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // Toast.makeText(context, "clicked on meee" + script.redirectId, Toast.LENGTH_LONG).show();
                            script.isAnswered='Y';
                            BO_BOT_SCRIPT answer=script.addAnswer(selection);
                            list.add(answer);
                            activity.callAsync(script.id,selection);
                        }
                    });
                }
                else
                {
                    holder.ll.setVisibility(View.GONE);
                }
                break;



            case 7:
                holder.tvMsg.setText(script.message);
                //  activity.setRlVisibility(0,script.id);
                List<BO_SCRIPT_DETAILS> details1=script.scriptDetails;
                if(script.isAnswered=='N') {
                    //final RatingBar ratingBar = new RatingBar(context, null, R.style.RatingBar);

                    final TextView tv=new TextView(context);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    tv.setLayoutParams(lp1);
                    tv.setGravity(Gravity.CENTER);
                    tv.setText("Please Select Rating");
                    tv.setTextColor(Color.WHITE);
                    tv.setPadding(2,2,2,2);

                    final RatingBar ratingBar =  new RatingBar(context);
                    //ratingBar.setScrollBarStyle(ContextCompat.sty(context, R.drawable.user_message)  R.style.RatingBar);
                    ratingBar.setNumStars((int)(script.maxValue*script.step));
                    ratingBar.setStepSize((float) script.step);
                    LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.parseColor("#" + info[3]), PorterDuff.Mode.SRC_ATOP);




                    final Button btn=new Button(context);
                    designButton(btn);
                    btn.setText("Submit Rating");
                    btn.setVisibility(View.GONE);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ratingBar.setLayoutParams(lp);
                    try {

                        (holder.ll).addView(ratingBar);
                        (holder.ll).addView(tv);
                        (holder.ll).addView(btn);
                    } catch (Exception s) {
                        String s1 = s.toString();
                    }
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                            int value=0;
                            if(script.step==1)
                            {
                                value=(int)rating;
                            }
                            else
                            {
                                value=(int)rating*2;
                            }
                            btn.setVisibility(View.VISIBLE);
                            activity.rv.scrollToPosition(list.size() - 1);
                            tv.setText(script.scriptDetails.get(value-1).optionValue);

                        }
                    });

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  Toast.makeText(context, "clicked on meee", Toast.LENGTH_LONG).show();
                            script.isAnswered='Y';
                            BO_BOT_SCRIPT answer=script.addAnswer(tv.getText().toString());
                            list.add(answer);
                            activity.callAsync(script.id,tv.getText().toString());
                        }
                    });
                    holder.ll.setVisibility(View.VISIBLE);

                }
                else
                {
                    holder.ll.setVisibility(View.GONE);
                }

                break;


            case 8:
                holder.tvMsg.setText(script.message);
                //  activity.setRlVisibility(0,script.id);

                    if(script.isAnswered=='N') {

                        final Button btn=new Button(context);
                        designButton(btn);
                        btn.setText("Submit Range");

                        btn.setVisibility(View.GONE);

                        if(script.step%1==0)
                        {
                            final RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(activity);
                            seekBar.setRangeValues((int) script.minValue, (int) script.maxValue);

                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            seekBar.setLayoutParams(lp);
                            try {
                                (holder.ll).addView(seekBar);
                                (holder.ll).addView(btn);
                            } catch (Exception s) {
                                String s1 = s.toString();
                            }
                            seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
                                @Override
                                public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                                    //Now you have the minValue and maxValue of your RangeSeekbar
                                    // Toast.makeText(context, minValue + "-" + maxValue, Toast.LENGTH_LONG).show();
                                    btn.setVisibility(View.VISIBLE);
                                    activity.rv.scrollToPosition(list.size() - 1);
                                }
                            });
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                  //  Toast.makeText(context, "clicked on meee", Toast.LENGTH_LONG).show();
                                    script.isAnswered='Y';
                                    BO_BOT_SCRIPT answer=script.addAnswer(seekBar.getSelectedMinValue() + "-" + seekBar.getSelectedMaxValue());
                                    list.add(answer);
                                    activity.callAsync(script.id,seekBar.getSelectedMinValue() + "-" + seekBar.getSelectedMaxValue());
                                }
                            });
                        }
                        else {
                            final RangeSeekBar<Float> seekBar = new RangeSeekBar<Float>(activity);
                            seekBar.setRangeValues((float) script.minValue, (float) script.maxValue);

                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            seekBar.setLayoutParams(lp);
                            try {
                                (holder.ll).addView(seekBar);
                                (holder.ll).addView(btn);
                            } catch (Exception s) {
                                String s1 = s.toString();
                            }
                            seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Float>() {
                                @Override
                                public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Float minValue, Float maxValue) {
                                    //Now you have the minValue and maxValue of your RangeSeekbar
                                    // Toast.makeText(context, minValue + "-" + maxValue, Toast.LENGTH_LONG).show();
                                    btn.setVisibility(View.VISIBLE);
                                    activity.rv.scrollToPosition(list.size() - 1);
                                }
                            });
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                  //  Toast.makeText(context, "clicked on meee", Toast.LENGTH_LONG).show();
                                    script.isAnswered='Y';
                                    BO_BOT_SCRIPT answer=script.addAnswer(seekBar.getSelectedMinValue() + "-" + seekBar.getSelectedMaxValue());
                                    list.add(answer);
                                    activity.callAsync(script.id,seekBar.getSelectedMinValue() + "-" + seekBar.getSelectedMaxValue());
                                }
                            });

                        }



// Get noticed while dragging

                        holder.ll.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        holder.ll.setVisibility(View.GONE);
                    }

                break;



            case 9:
                holder.tvMsg.setText(script.message);
                //  activity.setRlVisibility(0,script.id);
                if(script.isAnswered=='N') {
                    final DatePickerDialog datePickerDialog;
                    final SimpleDateFormat dateFormatter;
                    final TextView tv=new TextView(context);

                    final Button btn=new Button(context);
                    designButton(btn);
                    btn.setText("Submit Date");

                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    tv.setLayoutParams(lp1);
                    tv.setText("Click Here To Select Date");
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextColor(Color.WHITE);
                    tv.setPadding(5,5,5,5);
                    dateFormatter= new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                    Calendar newCalendar = Calendar.getInstance();
                    datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);

                            tv.setText(dateFormatter.format(newDate.getTime()));
                            btn.setVisibility(View.VISIBLE);
                            activity.rv.scrollToPosition(list.size() - 1);
                        }
                    },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            datePickerDialog.show();
                        }
                    });



                    btn.setVisibility(View.GONE);
                    try {
                       // (holder.ll).addView(dp);
                        (holder.ll).addView(tv);
                        (holder.ll).addView(btn);
                    } catch (Exception s) {
                        String s1 = s.toString();
                    }


                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // Toast.makeText(context, "clicked on meee", Toast.LENGTH_LONG).show();
                            script.isAnswered='Y';
                            BO_BOT_SCRIPT answer=script.addAnswer(tv.getText().toString());
                            list.add(answer);
                            activity.callAsync(script.id,tv.getText().toString());
                        }
                    });
                    holder.ll.setVisibility(View.VISIBLE);

                }
                else
                {
                    holder.ll.setVisibility(View.GONE);
                }

                break;


            case 10:
                holder.tvMsg.setText(script.message);
                //  activity.setRlVisibility(0,script.id);
                if(script.isAnswered=='N') {
                    final TimePickerDialog timePickerDialog;
                    final SimpleDateFormat dateFormatter;
                    final TextView tv=new TextView(context);

                    final Button btn=new Button(context);
                    designButton(btn);

                    btn.setText("Submit Time");

                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    tv.setLayoutParams(lp1);
                    tv.setText("Click Here To Select Time");
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextColor(Color.WHITE);
                    tv.setPadding(5,5,5,5);
                    dateFormatter= new SimpleDateFormat("H:mm", Locale.US);
                    Calendar newCalendar = Calendar.getInstance();
                    timePickerDialog=new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            Calendar newDate=Calendar.getInstance();
                            newDate.set(0,0,0,hourOfDay,minute);
                            tv.setText(dateFormatter.format(newDate.getTime()));
                            btn.setVisibility(View.VISIBLE);
                            activity.rv.scrollToPosition(list.size() - 1);
                        }
                    },newCalendar.get(Calendar.HOUR),newCalendar.get(Calendar.MINUTE),false);



                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            timePickerDialog.show();
                        }
                    });






                    btn.setVisibility(View.GONE);
                    try {
                        // (holder.ll).addView(dp);
                        (holder.ll).addView(tv);
                        (holder.ll).addView(btn);
                    } catch (Exception s) {
                        String s1 = s.toString();
                    }


                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  Toast.makeText(context, "clicked on meee", Toast.LENGTH_LONG).show();
                            script.isAnswered='Y';
                            BO_BOT_SCRIPT answer=script.addAnswer(tv.getText().toString());
                            list.add(answer);
                            activity.callAsync(script.id,tv.getText().toString());
                        }
                    });
                    holder.ll.setVisibility(View.VISIBLE);

                }
                else
                {
                    holder.ll.setVisibility(View.GONE);
                }

                break;


        }


    }

    public void designButton(Button myButton)
    {

        myButton.setGravity(Gravity.CENTER);
        myButton.setPadding(0,0,0,0);
        myButton.setTextColor(Color.parseColor("#" + info[3]));
        myButton.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_button_border));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,7,0,5);
        myButton.setLayoutParams(lp);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void callAsync(int redirectId)
    {


    }



}





