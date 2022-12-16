package com.nguyenhuutrong.bai3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.squareup.picasso.Picasso;

import javax.net.ssl.HttpsURLConnection;

public class ItemFood extends AppCompatActivity {

    ListView lv;
    ArrayList<String> arrItem;
    ArrayAdapter  adapter;
    TextView title,dec;
    Button more, cancel;
    String urlFood;

    public static ArrayList<Food> arrFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_food);
        // lấy acti cũ
        Intent intent = getIntent();
        //get chuỗi
        // xét tiêu đề
        String url = intent.getStringExtra("url");
        String food_title = intent.getStringExtra("title");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.img_1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // nngay2 hiện tại
        Date date = Calendar.getInstance().getTime();
        //
        // Display a date in day, month, year format
        //
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String today = formatter.format(date);

        // xét tiêu đề
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("PetFoodIndustry.com "+food_title+" " + today);

        lv = (ListView) findViewById(R.id.list_item);


        // load rss đang xét
       new ReadRSS().execute(url);
        // sự kiện click vào 1 item trong listView
       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           private ImageView image;

           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // hiển thị dialog
               Dialog dialog = new Dialog(ItemFood.this);
                dialog.setContentView(R.layout.layout_dialog);
               title = (TextView) dialog.findViewById(R.id.dialog_item_title);

               dec = (TextView) dialog.findViewById(R.id.dialog_item_dec);
               image = (ImageView) dialog.findViewById(R.id.dialog_image);

               more = (Button) dialog.findViewById(R.id.more);
               cancel = (Button) dialog.findViewById(R.id.cancel);
                Food f = arrFood.get(i);
               title.setText(f.getTitle());
               dec.setText(f.getDec());

               //load ảnh
               ImageView imgView = (ImageView) dialog.findViewById(R.id.dialog_image);
               Picasso.get().load(f.getImage()).into(imgView);
               dialog.show();

                // cancel
               cancel.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if (dialog.isShowing()) {
                           dialog.dismiss();
                       }
                   }
               });

               // more
               more.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(f.getLink()));
                       startActivity(intent);
                   }
               });

           }
       });


    }
    public void addItem(){

        arrItem = new ArrayList<>();
        for (Food value : arrFood) {
            arrItem.add(value.getTitle());
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrItem);
        lv.setAdapter(adapter);
    }
    private class ReadRSS extends AsyncTask<String ,Void, String> {

        // đọc dữ liệu
        @Override
        protected String doInBackground(String... strings) {
            //khởi tạo đường dẫn
            StringBuilder content = new StringBuilder();
            try {
                // khởi tạo đường dẫn
                URL url = new URL(strings[0]);

                // đọc đường dẫn kết nối
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                // tạo dòng lưu trữ
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    //đọc dòng
                    content.append(line+"\n");

                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }
        // trả dữ liệu sau khi đọc
        @Override
        protected void onPostExecute(String s) {
            arrFood = new ArrayList<>();
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();
            // đọc document
            Document document = parser.getDocument(s);
            String title="", link="", dec="", image="";
            // khởi tạo noteList
            NodeList nodeList = document.getElementsByTagName("item");
            // đọc mô tả
            NodeList nodeListDescription = document.getElementsByTagName("description");
            for (int i = 0;i<nodeList.getLength();i++) {
                // tạo phần tử
                Element element = (Element) nodeList.item(i);
                title = parser.getValue(element, "title");
                link = parser.getValue(element, "link");


                dec = nodeListDescription.item(i + 1).getTextContent();
                dec = dec.replace("<p>","");
                dec = dec.replace("</p>","");



                Node media = document.getElementsByTagName("media:content").item(i);
                if(media != null){
                    image = media.getAttributes().getNamedItem("url").getNodeValue();
                }else{
                    image = "https://t4.ftcdn.net/jpg/04/70/29/97/360_F_470299797_UD0eoVMMSUbHCcNJCdv2t8B2g1GVqYgs.jpg";
                }

                Food f = new Food(title, link, dec, image);
                arrFood.add(f);
            }

                addItem();
        }
}

}