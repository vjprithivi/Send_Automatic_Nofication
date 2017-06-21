package com.techsofficial.prithivi.nofication;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pc54 on 09-06-2017.
 */

public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = this.getClass().getName();
    private Context context;
    private LayoutInflater inflater;
    List<Datafish> data= Collections.emptyList();
    Datafish current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterFish(Context context, List<Datafish> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.data, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        final Datafish current=data.get(position);
        myHolder.Number.setText("Mobile Number :"+current.mobilenumber);
        myHolder.Number.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        myHolder.Number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.editext, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptView);

                final EditText editText = (EditText) promptView.findViewById(R.id.edit1);
                // setup a dialog window
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                      /*  resultText.setText("Hello, " + editText.getText());*/


                                HashMap postData2 = new HashMap();
                                Date now = new Date();
                                DateFormat formatter = DateFormat.getInstance();
                                String dateStr = formatter.format(now);
                                System.out.println(dateStr);


                                postData2.put("apikey", "AIzaSyB1gbtol9EHivOyMuVbRcDPstdBqfccJR0");
                                postData2.put("regtoken",""+current.token);
                                postData2.put("message", ""+editText.getText().toString());


                                PostResponseAsyncTask taskInsert = new PostResponseAsyncTask(context,
                                        postData2, new AsyncResponse()
                                {
                                    @Override
                                    public void processFinish(String s) {
                                        Log.d(TAG, s);
                                        if (s.contains("success")) {
                                            Toast.makeText(context, " Successfully", Toast.LENGTH_LONG).show();


                                        } else {
                                            Toast.makeText(context, "Error while uploading.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                taskInsert.execute("http://hateyou.esy.es/quiz/people/send.php");




                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();





          /*      Toast.makeText(context, ""+current.token, Toast.LENGTH_LONG).show();*/
            }
        });
        myHolder.date.setText("Date of Joined:"+current.data);

    /*    myHolder.textSize.setText("Size: " + current.sizeName);
        myHolder.textType.setText("Category: " + current.catName);
        myHolder.textPrice.setText("Rs. " + current.price + "\\Kg");
        myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));*/

        // load image into imageview using glide
    /*    Glide.with(context).load("http://192.168.1.7/test/images/" + current.fishImage)
                .placeholder(R.drawable.ic_img_error)
                .error(R.drawable.ic_img_error)
                .into(myHolder.ivFish);*/

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView Number;
        TextView date;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);



          Number = (TextView) itemView.findViewById(R.id.number);

           date = (TextView) itemView.findViewById(R.id.date);

        }

    }

    public void showChangeLangDialog() {



    }


}