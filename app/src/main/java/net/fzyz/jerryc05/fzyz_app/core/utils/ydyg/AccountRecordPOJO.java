package net.fzyz.jerryc05.fzyz_app.core.utils.ydyg;

import android.util.JsonReader;
import android.util.JsonToken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public final class AccountRecordPOJO implements Serializable {
  private final static long serialVersionUID = 1;

  public String         flag;
  //  public String         rspCode;
  public String         rspDsc;
  public List<RspDatum> rspData;

  @SuppressWarnings({"MethodCallInLoopCondition", "ObjectAllocationInLoop"})
  public static AccountRecordPOJO parseJson(@NonNull final String json) throws IOException {
    final AccountRecordPOJO accountRecordPOJO = new AccountRecordPOJO();
    final JsonReader        jsonReader        = new JsonReader(new StringReader(json));

    jsonReader.beginObject();
    while (jsonReader.hasNext())
      switch (jsonReader.nextName()) {
        case "flag":
          accountRecordPOJO.flag = jsonReader.nextString();
          break;
        case "rsp_dsc":
          accountRecordPOJO.rspDsc = jsonReader.nextString();
          break;
        case "rsp_data":
          jsonReader.beginArray();
          accountRecordPOJO.rspData = new ArrayList<>(10);

          while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            final RspDatum rspDatum = new RspDatum();
            while (jsonReader.hasNext())
              switch (jsonReader.nextName()) {
                case "change_type_ch":
                  rspDatum.changeTypeCh = jsonReader.nextString();
                  break;
                case "amount":
                  rspDatum.amount = jsonReader.nextString();
                  break;
                case "rec_status_ch":
                  rspDatum.recStatusCh = jsonReader.nextString();
                  break;
                case "rec_updatetime":
                  rspDatum.recUpdatetime = jsonReader.nextString();
                  break;
                case "detail":
                  if (jsonReader.peek() == JsonToken.NULL)
                    jsonReader.skipValue();
                  else
                    jsonReader.beginObject();

                  rspDatum.detail = new Detail();
                  while (jsonReader.hasNext()) {
                    switch (jsonReader.nextName()) {
                      case "repast_name":
                        if (jsonReader.peek() == JsonToken.NULL)
                          jsonReader.skipValue();
                        else
                          rspDatum.detail.repastName = jsonReader.nextString();
                        break;
                      case "shop_name":
                        rspDatum.detail.shopName = jsonReader.nextString();
                        break;
                      default:
                        jsonReader.skipValue();
                    }
                  }
                  jsonReader.endObject();
                  break;
                default:
                  jsonReader.skipValue();
              }
            jsonReader.endObject();
            accountRecordPOJO.rspData.add(rspDatum);
          }
          jsonReader.endArray();
          break;
        default:
          jsonReader.skipValue();
      }
    //      jsonReader.endObject();
    jsonReader.close();
    return accountRecordPOJO;
  }

  public static class RspDatum implements Serializable {
    private final static long serialVersionUID = 1;

    //    public String refferId;
    //    public String changeType;
    public String changeTypeCh;
    public String amount;
    //    public String recStatus;
    public String recStatusCh; // 流出/流入
    public String recUpdatetime;
    @Nullable
    public Detail detail;
  }

  public static class Detail implements Serializable {
    private final static long serialVersionUID = 1;

    //    public String changeTypeZh;
    //    public String amount;
    //    public String cardNo;
    //    public String recUpdatetime;
    @Nullable
    public String repastName; // 午餐/null
    //    public String memName;
    //    public String reverseAccount;
    //    public String cashAccount;
    public String shopName;
    //    public String orderType;
    //    public String unreverseAccount;
  }
}