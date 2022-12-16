package com.example.myrelativelayout;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //  Khởi tạo các đối tượng trên layout

    //  biến SeekBar để điều khiển seekbar
    private SeekBar skControl;
    //  mảng chứa các ô màu, ô màu ở đây dùng thẳng nguyên cái relative layout
    private RelativeLayout[] panels = new RelativeLayout[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  activity này dùng layout nào thì set setContentView() của layout đó
        //  ở đây dùng linear_layout
        setContentView(R.layout.relative_layout);
        //  gán giá trị cho ID tương ứng để code
        skControl = findViewById(R.id.skbColor);

        //  không có colorPanel4 vì trong đề nó không đổi màu
        panels[0] = findViewById(R.id.colorPanel1);
        panels[1] = findViewById(R.id.colorPanel2);
        panels[2] = findViewById(R.id.colorPanel3);
        panels[3] = findViewById(R.id.colorPanel5);
        //  code sự kiện cho seekbar
        skControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //  i là index, vị trí của cái cục tròn của thanh kéo seekbar
                //  giá trị bằng 0, tức là cái cục tròn của thanh kéo seekbar đang ở đầu
                if (i == 0) {
                    //  thường để cho nó màu mặc định của nó ban đầu, còn không thì cho
                    //  số đại, thêm mắm dặm muối miễn là trong khoảng từ 1 - 255 là ok
                    panels[0].setBackgroundColor(Color.rgb(105, 119, 180));
                    panels[1].setBackgroundColor(Color.rgb(214, 80, 149));
                    panels[2].setBackgroundColor(Color.rgb(164, 29, 33));
                    panels[3].setBackgroundColor(Color.rgb(38, 59, 158));
                }
                //  giá trị khác 0, là cái cục tròn của thanh kéo seekbar nằm đâu cũng được
                else {
                    //  ở đâu cũng được thì cộng trừ nhân chia cho 1 số nằm trong khoảng từ
                    //  1 - 255 là ok
                    panels[0].setBackgroundColor(Color.rgb(i + 200, i + 100, 150));
                    panels[1].setBackgroundColor(Color.rgb(i - 204, i + 102, 155));
                    panels[2].setBackgroundColor(Color.rgb(180 + i, 0, 133));
                    panels[3].setBackgroundColor(Color.rgb(184 - i, 182, 152));
                }
            }

            //  2 hàm dưới đây là nó bắt phải implement, để đây chứ không có chức năng gì hết trơn
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //bước khởi tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  Inflater?

        //  dịch ra có nghĩa là mở rộng, làm to ra (dịch hơi chuối), vậy nó làm công việc gì?

        //  công việc của nó là sử dụng những cái resource layout mình đã tạo ra
        //  để biến nó trở thành một View để không phải mắc công code lại bằng tay

        //  ở đây là đang inflate (làm cho cái activity của mình (tham số 2) được mở rộng ra) và bỏ cái
        //  menu đã tạo rồi vào cái activity
        getMenuInflater().inflate(R.menu.mymenu, menu);

        //  vì là hàm boolean nên nó thường trả về true,
        //  true là để xác nhận menu đã được tạo và có thể sử dụng
        //  nếu false thì nó sẽ không hiển thị, tức là không tạo được menu
        return super.onCreateOptionsMenu(menu);
    }

    //  Ở đây là mình sẽ dạy cho cái nút lựa chọn (nút 3 chấm bên góc phải màn hình)
    //  trong menu biết là khi mình bấm vào nó thì nó thực hiện công việc gì, giống
    //  như viết sự kiện cho button (nút) vậy đó
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  ở đây, khi bấm vào lựa chọn trên menu, nó sẽ chạy hàm showDialog()
        //  kéo xuống hàm showDialog() để xem nha
        showDialog();
        //  giải thích ở đây giống với dòng return ở trên hàm onCreateOptionsMenu(Menu menu),
        //  khác cái là nó là item
        return super.onOptionsItemSelected(item);
    }

    //  hàm này là để hiển thị cái dialog (hộp thoại nhắc người dùng đưa ra quyết định
    //  hoặc nhập thông tin bổ sung) do mình tự tạo ra
    void showDialog() {
        //  Dialog là một hộp thoại, tham số trong constructor (hàm khởi tạo) là một Context (ngữ cảnh) (?)

        //  Context là đối tượng cho biết các thông tin về activity, ở đây sử dụng this "là nó dựa vào cái
        //  activity của mình để tạo một dialog mới
        Dialog dialog = new Dialog(this);
        //  dùng hàm setContentView() để đặt cái dialog mình vừa tạo ở trên bằng cái dialog mình đã tạo bằng file xml
        dialog.setContentView(R.layout.custom_dialog);
        //  hàm show() để hiển thị cái dialog đó ra
        dialog.show();
        //  Lấy mấy cái TextView trong dialog để set sự kiện
        //  ở đây có 2 TextView, (tượng trưng cho 2 button),
        //  vậy sao không dùng đại button luôn đi chứ gì?
        //  tại nó nhìn không giống đề
        TextView visitButton = (TextView) dialog.findViewById(R.id.visitButton);
        TextView notNowButton = (TextView) dialog.findViewById(R.id.notNowButton);
        // Bắt đầu set sự kiện cho 2 nút vừa nói trên

        //  Nút thứ nhất - Visit MOMA - dùng để hiện ra trang web của MOMA
        //  (trang web mà thiết kế cái hình có mấy cái ô của mình)
        //  hàm này ai code Java rồi chắc cũng thấy quen
        visitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Rồi Toast là gì nữa z pa?

                //  Là cái hiển thị thông báo nhanh trên điện thoại thôi chứ không có gì ghê :v
                //  Giống mấy cái thông báo nhanh "Không có kết nối mạng", "Đã gỡ cài đặt ứng dụng..."
                //  Thường xuất hiện 1, 2 giây rồi biến mất

                //  Ở đây thì khi bấm vào nút Visit MOMA, nó sẽ hiển thị dòng chữ thông báo nhanh "Visiting site..."
                Toast.makeText(MainActivity.this, "Visiting site...", Toast.LENGTH_SHORT).show();

                //  Intent?

                //  Là đối tượng được sử dụng để bắt đầu một activity con từ 1 activity đang mở
                //  Ở đây nó sẽ mở 1 cái WebViewActivity (activity này chỉ mở 1 trang web trong ứng dụng
                //  đang mở (My Relative Layout), không phải mở trang web trong ứng dụng trình duyệt trên máy)

                //  Vì sẽ được mở trên ứng dụng hiện tại, nên mới dùng tham số thứ 1 là MainActivity.this
                //  tham số thứ 2 là class WebViewActivity đã được code, qua đó để xem thêm
                //  WebViewActivity.class là để lấy cái đại diện cho WebViewActivity (?)
                //  Ừa đọc dòng trên t cũng không hiểu gì :)
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                //  Sau đó thì bắt đầu chạy cái intent trên bằng hàm startActivity() trên MainActivity
                MainActivity.this.startActivity(intent);
            }
        });


        //  Nút thứ 2 - Cancel: đơn giản là nút hủy thôi, không làm gì hết
        notNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast lên màn hình để báo cho người ta biết là "À mày hủy rồi đó"
                Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                // xong đóng cái dialog lại (ẩn đi)
                dialog.hide();
            }
        });
    }
}