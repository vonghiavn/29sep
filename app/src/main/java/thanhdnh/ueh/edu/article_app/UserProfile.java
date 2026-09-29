package thanhdnh.ueh.edu.article_app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfile {
  @SerializedName("id")
  @Expose
  private int id;

  @SerializedName("username")
  @Expose
  private String username;

  @SerializedName("email")
  @Expose
  private String email;

  @SerializedName("description")
  @Expose
  private String description;

  @SerializedName("avatar_url")
  @Expose
  private String avatar_url;

  @SerializedName("hobby")
  @Expose
  private String hobby;

  public UserProfile() {
  }

  public UserProfile(int id, String username, String email, String description, String avatar_url, String hobby) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.description = description;
    this.avatar_url = avatar_url;
    this.hobby = hobby;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAvatar_url() {
    return avatar_url;
  }

  public void setAvatar_url(String avatar_url) {
    this.avatar_url = avatar_url;
  }

  public String getHobby() {
    return hobby;
  }

  public void setHobby(String hobby) {
    this.hobby = hobby;
  }
}
