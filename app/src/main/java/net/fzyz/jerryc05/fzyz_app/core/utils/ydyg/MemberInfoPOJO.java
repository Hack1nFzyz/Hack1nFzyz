package net.fzyz.jerryc05.fzyz_app.core.utils.ydyg;

import android.util.JsonReader;
import android.util.JsonToken;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

public final class MemberInfoPOJO implements Serializable {
  private final static long serialVersionUID = 1;

  public String  flag;
  //  public String  rspCode;
  public String  rspDsc;
  public RspData rspData;

  @SuppressWarnings({"MethodCallInLoopCondition", "ObjectAllocationInLoop"})
  public static MemberInfoPOJO parseJson(@NonNull final String json) throws IOException {
    final MemberInfoPOJO memberInfoPOJO = new MemberInfoPOJO();
    final JsonReader     jsonReader     = new JsonReader(new StringReader(json));

    jsonReader.beginObject();
    while (jsonReader.hasNext())
      switch (jsonReader.nextName()) {
        case "flag":
          memberInfoPOJO.flag = jsonReader.nextString();
          break;
        case "rsp_dsc":
          memberInfoPOJO.rspDsc = jsonReader.nextString();
          break;
        case "rsp_data":
          if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.skipValue();
            break;
          }
          jsonReader.beginObject();
          memberInfoPOJO.rspData = new RspData();
          while (jsonReader.hasNext())
            if (jsonReader.nextName().equals("cash_account"))
              memberInfoPOJO.rspData.cashAccount = jsonReader.nextString();
            else
              jsonReader.skipValue();
          jsonReader.endObject();
          break;
        default:
          jsonReader.skipValue();
      }
    //      jsonReader.endObject();
    jsonReader.close();
    return memberInfoPOJO;
  }

  private MemberInfoPOJO() {
  }

  public static class RspData implements Serializable {
    private final static long serialVersionUID = 1;

    //    public String phone;
    //    public String loginId;
    //    public String memSex;
    //    public int    unRead;
    //    public String money;
    //    public String reverseAccount;
    //    public String unreversedAccount;
    public String cashAccount;
    //    public String spPhone;
    //    public String spCompany;
    //    public Object memImage;
  }
}