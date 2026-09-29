package thanhdnh.ueh.edu.article_app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ViewUserActivity extends AppCompatActivity {
  ImageView iv_detail;
  TextView tv_detail_title, tv_detail_email, tv_detail_description, tv_detail_hobby;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_user);
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }

    iv_detail = findViewById(R.id.iv_detail);
    tv_detail_title = findViewById(R.id.tv_detail_title);
    tv_detail_email = findViewById(R.id.tv_detail_email);
    tv_detail_description = findViewById(R.id.tv_detail_description);
    tv_detail_hobby = findViewById(R.id.tv_detail_hobby);

    int id = (int) getIntent().getLongExtra("id", 0);
    UserProfile user = UserData.getUserFromId(id);

    if (user != null) {
      Picasso.get()
          .load(user.getAvatar_url())
          .placeholder(android.R.drawable.ic_menu_gallery)
          .error(android.R.drawable.ic_menu_report_image)
          .resize(400, 400)
          .centerCrop()
          .into(iv_detail);

      tv_detail_title.setText(user.getUsername());
      tv_detail_email.setText(user.getEmail());
      tv_detail_description.setText(user.getDescription());
      tv_detail_hobby.setText("Sở thích: " + user.getHobby());
    }
  }
}
