package thanhdnh.ueh.edu.article_app;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class Downloader {
  public static String cached_file_path = "";

  public interface DownloadListener {
    void onProgress(int progress);
    void onSuccess(File file);
    void onError(Exception e);
  }

  public static File downloadFile(String url, File cached) {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(url).build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) return null;
      String contentType = response.header("Content-Type", "");
      String extension = getExtensionFromMimeType(contentType);
      File file = File.createTempFile("downloaded_file", extension, cached);
      if (response.body() != null) {
        BufferedSink sink = Okio.buffer(Okio.sink(file));
        sink.writeAll(response.body().source());
        sink.close();
        return file;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void downloadWithProgress(String inputUrl, Handler mainHandler, File cachedDir, ProgressBar progressBar, DownloadListener listener) {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(inputUrl).build();

    if (progressBar != null) {
      mainHandler.post(() -> {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
      });
    }

    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        mainHandler.post(() -> {
          if (progressBar != null) progressBar.setVisibility(View.GONE);
          if (listener != null) listener.onError(e);
        });
      }

      @Override
      public void onResponse(Call call, Response response) {
        if (!response.isSuccessful()) {
          mainHandler.post(() -> {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
            if (listener != null) listener.onError(new IOException("HTTP error code: " + response.code()));
          });
          return;
        }

        try {
          long totalBytes = response.body() != null ? response.body().contentLength() : -1;
          InputStream inputStream = response.body().byteStream();
          String contentType = response.header("Content-Type", "");
          String extension = getExtensionFromMimeType(contentType);

          File file = File.createTempFile("download_progress_", extension, cachedDir);

          try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            long downloadedBytes = 0;
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
              outputStream.write(buffer, 0, bytesRead);
              downloadedBytes += bytesRead;
              if (totalBytes > 0) {
                int progress = (int) ((downloadedBytes * 100) / totalBytes);
                mainHandler.post(() -> {
                  if (progressBar != null) progressBar.setProgress(progress);
                  if (listener != null) listener.onProgress(progress);
                });
              }
            }
            outputStream.flush();
          }

          mainHandler.post(() -> {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
            if (listener != null) listener.onSuccess(file);
          });
        } catch (Exception e) {
          mainHandler.post(() -> {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
            if (listener != null) listener.onError(e);
          });
        }
      }
    });
  }

  public static void downloadWithProgress(String inputurl, Handler mainHandler, Context context, File where2store, ProgressBar progressBar, ImageView imageView) {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(inputurl).build();

    if (progressBar != null) {
      mainHandler.post(() -> {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
      });
    }

    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        mainHandler.post(() -> {
          if (progressBar != null) progressBar.setVisibility(View.GONE);
        });
      }

      @Override
      public void onResponse(Call call, Response response) {
        if (!response.isSuccessful()) {
          mainHandler.post(() -> {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
          });
          return;
        }

        long totalBytes = response.body().contentLength();
        InputStream inputStream = response.body().byteStream();
        String contentType = response.header("Content-Type", "");
        String extension = getExtensionFromMimeType(contentType);

        try (OutputStream outputStream = new FileOutputStream(where2store + "/downloaded_file" + extension)) {
          byte[] buffer = new byte[1024];
          long downloadedBytes = 0;
          int bytesRead;

          while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
            downloadedBytes += bytesRead;
            if (totalBytes > 0) {
              int progress = (int) ((downloadedBytes * 100) / totalBytes);
              mainHandler.post(() -> {
                if (progressBar != null) progressBar.setProgress(progress);
              });
            }
          }
          outputStream.flush();

          mainHandler.post(() -> {
            cached_file_path = where2store + "/downloaded_file" + extension;
            if (imageView != null) imageView.setImageURI(Uri.parse(cached_file_path));
            if (progressBar != null) progressBar.setVisibility(View.GONE);
          });
        } catch (Exception e) {
          mainHandler.post(() -> {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
          });
        }
      }
    });
  }

  private static String getExtensionFromMimeType(String mimeType) {
    Map<String, String> mimeMap = new HashMap<>();
    mimeMap.put("image/jpeg", ".jpg");
    mimeMap.put("image/png", ".png");
    mimeMap.put("application/json", ".json");
    return mimeMap.getOrDefault(mimeType, ".tmp");
  }
}
