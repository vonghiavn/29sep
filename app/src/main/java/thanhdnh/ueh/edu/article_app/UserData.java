package thanhdnh.ueh.edu.article_app;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UserData {
  public static UserList data;
  private Context context;
  private GridView gridview;

  private static final String SAMPLE_JSON = "{\n" +
      "  \"users\": [\n" +
      "    {\n" +
      "      \"id\": 1,\n" +
      "      \"username\": \"Alex Dev\",\n" +
      "      \"email\": \"alex.dev@example.com\",\n" +
      "      \"description\": \"Senior Android Developer with 5+ years of experience in Kotlin and Java.\",\n" +
      "      \"avatar_url\": \"https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400\",\n" +
      "      \"hobby\": \"Coding, Photography, Chess\"\n" +
      "    },\n" +
      "    {\n" +
      "      \"id\": 2,\n" +
      "      \"username\": \"Sarah Miller\",\n" +
      "      \"email\": \"sarah.m@example.com\",\n" +
      "      \"description\": \"UI/UX Designer creating intuitive and beautiful mobile interfaces.\",\n" +
      "      \"avatar_url\": \"https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400\",\n" +
      "      \"hobby\": \"Painting, Sketching, Traveling\"\n" +
      "    },\n" +
      "    {\n" +
      "      \"id\": 3,\n" +
      "      \"username\": \"Michael Brown\",\n" +
      "      \"email\": \"michael.b@example.com\",\n" +
      "      \"description\": \"Backend architect specializing in cloud infrastructure and microservices.\",\n" +
      "      \"avatar_url\": \"https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=400\",\n" +
      "      \"hobby\": \"Hiking, Gaming, Cooking\"\n" +
      "    },\n" +
      "    {\n" +
      "      \"id\": 4,\n" +
      "      \"username\": \"Emily Rose\",\n" +
      "      \"email\": \"emily.rose@example.com\",\n" +
      "      \"description\": \"Data Scientist exploring artificial intelligence and deep learning models.\",\n" +
      "      \"avatar_url\": \"https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=400\",\n" +
      "      \"hobby\": \"Reading, Yoga, Piano\"\n" +
      "    },\n" +
      "    {\n" +
      "      \"id\": 5,\n" +
      "      \"username\": \"David Kim\",\n" +
      "      \"email\": \"david.k@example.com\",\n" +
      "      \"description\": \"Cybersecurity specialist focused on mobile application security.\",\n" +
      "      \"avatar_url\": \"https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400\",\n" +
      "      \"hobby\": \"Cycling, Swimming, Gaming\"\n" +
      "    },\n" +
      "    {\n" +
      "      \"id\": 6,\n" +
      "      \"username\": \"Jessica Taylor\",\n" +
      "      \"email\": \"jessica.t@example.com\",\n" +
      "      \"description\": \"Product Manager guiding product strategy and cross-functional teams.\",\n" +
      "      \"avatar_url\": \"https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400\",\n" +
      "      \"hobby\": \"Running, Coffee Tasting, Books\"\n" +
      "    },\n" +
      "    {\n" +
      "      \"id\": 7,\n" +
      "      \"username\": \"Daniel White\",\n" +
      "      \"email\": \"daniel.w@example.com\",\n" +
      "      \"description\": \"DevOps Engineer passionate about automation and continuous integration.\",\n" +
      "      \"avatar_url\": \"https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400\",\n" +
      "      \"hobby\": \"Gardening, Music, DIY\"\n" +
      "    },\n" +
      "    {\n" +
      "      \"id\": 8,\n" +
      "      \"username\": \"Sophia Lee\",\n" +
      "      \"email\": \"sophia.l@example.com\",\n" +
      "      \"description\": \"Mobile Quality Assurance Lead ensuring bug-free user experiences.\",\n" +
      "      \"avatar_url\": \"https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400\",\n" +
      "      \"hobby\": \"Baking, Board Games, Movies\"\n" +
      "    },\n" +
      "    {\n" +
      "      \"id\": 9,\n" +
      "      \"username\": \"James Wilson\",\n" +
      "      \"email\": \"james.w@example.com\",\n" +
      "      \"description\": \"Full Stack Engineer building cross-platform solutions.\",\n" +
      "      \"avatar_url\": \"https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=400\",\n" +
      "      \"hobby\": \"Basketball, Guitar, Traveling\"\n" +
      "    },\n" +
      "    {\n" +
      "      \"id\": 10,\n" +
      "      \"username\": \"Olivia Harris\",\n" +
      "      \"email\": \"olivia.h@example.com\",\n" +
      "      \"description\": \"Tech Content Creator and Digital Marketer sharing mobile tech tips.\",\n" +
      "      \"avatar_url\": \"https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400\",\n" +
      "      \"hobby\": \"Vlogging, Photography, Dancing\"\n" +
      "    }\n" +
      "  ]\n" +
      "}";

  public UserData(Context context, GridView gridview) {
    this.context = context;
    this.gridview = gridview;
  }

  public static UserProfile getUserFromId(int id) {
    if (data == null || data.getUsers() == null) return null;
    for (int i = 0; i < data.getUsers().size(); i++) {
      if (data.getUsers().get(i).getId() == id) {
        return data.getUsers().get(i);
      }
    }
    return null;
  }

  public void loadData(String url, Activity activity, ProgressBar progressBar) {
    Handler mainHandler = new Handler(Looper.getMainLooper());
    Downloader.downloadWithProgress(url, mainHandler, context.getCacheDir(), progressBar, new Downloader.DownloadListener() {
      @Override
      public void onProgress(int progress) {
      }

      @Override
      public void onSuccess(File file) {
        try {
          String jsonStr = readText(file);
          Gson gson = new Gson();
          data = gson.fromJson(jsonStr, UserList.class);
          if (data == null || data.getUsers() == null || data.getUsers().isEmpty()) {
            loadFallbackData();
          }
        } catch (Exception e) {
          loadFallbackData();
        }
        updateGrid(activity);
      }

      @Override
      public void onError(Exception e) {
        loadFallbackData();
        updateGrid(activity);
      }
    });
  }

  private void loadFallbackData() {
    Gson gson = new Gson();
    data = gson.fromJson(SAMPLE_JSON, UserList.class);
  }

  private void updateGrid(Activity activity) {
    activity.runOnUiThread(() -> {
      if (data != null && data.getUsers() != null) {
        UserAdapter adapter = new UserAdapter(data.getUsers(), context);
        gridview.setAdapter(adapter);
      }
    });
  }

  public String readText(File file) {
    try (InputStream stream = new FileInputStream(file);
         BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      StringBuilder buffer = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        buffer.append(line).append("\n");
      }
      return buffer.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }
}
