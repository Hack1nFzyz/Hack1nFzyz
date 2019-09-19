package net.fzyz.jerryc05.fzyz_app.core.utils.ydyg;

public class BaseModel {
  @SerializedName("issuccessed")
  public int isSucceed;

  public BaseModel() {
    this.isSucceed = -1;
  }

  public boolean isSucceed() {
    final int isSucceed = this.isSucceed;
    boolean   b         = true;
    if (isSucceed != 1) {
      b = false;
    }
    return b;
  }
}
