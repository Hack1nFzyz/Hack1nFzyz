package net.fzyz.jerryc05.fzyz_app.core.utils.ydyg;

import android.util.JsonReader;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

@SuppressWarnings({"unused", "InnerClassMayBeStatic", "WeakerAccess"})
public class MemberLoginPOJO implements Serializable {
  private final static long serialVersionUID = 1;

  public String  flag;
  //  public String  rspCode;
  public String  rspDsc;
  public RspData rspData;

  @SuppressWarnings({"MethodCallInLoopCondition", "ObjectAllocationInLoop"})
  public static MemberLoginPOJO parseJson(@NonNull final String json) throws IOException {
    final MemberLoginPOJO memberLoginPOJO = new MemberLoginPOJO();
    final JsonReader      jsonReader      = new JsonReader(new StringReader(json));

    jsonReader.beginObject();
    while (jsonReader.hasNext())
      switch (jsonReader.nextName()) {
        case "flag":
          memberLoginPOJO.flag = jsonReader.nextString();
          break;
        case "rsp_dsc":
          memberLoginPOJO.rspDsc = jsonReader.nextString();
          break;
        case "rsp_data":
          jsonReader.beginObject();
          memberLoginPOJO.rspData = memberLoginPOJO.new RspData();
          while (jsonReader.hasNext())
            switch (jsonReader.nextName()) {
              case "access_token":
                memberLoginPOJO.rspData.accessToken = jsonReader.nextString();
                break;
              case "member_id":
                memberLoginPOJO.rspData.memberId = jsonReader.nextString();
                break;
              case "mem_name":
                memberLoginPOJO.rspData.memName = jsonReader.nextString();
                break;
              case "expires_in":
                memberLoginPOJO.rspData.expiresIn = jsonReader.nextInt();
                break;
              case "company":
                jsonReader.beginObject();
                memberLoginPOJO.rspData.company = memberLoginPOJO.rspData.new Company();
                while (jsonReader.hasNext())
                  switch (jsonReader.nextName()) {
                    case "dpt_name":
                      memberLoginPOJO.rspData.company.dptName = jsonReader.nextString();
                      break;
                    case "cmp_name":
                      memberLoginPOJO.rspData.company.cmpName = jsonReader.nextString();
                      break;
                    default:
                      jsonReader.skipValue();
                  }
                jsonReader.endObject();
                break;
              case "charge_fee":
                memberLoginPOJO.rspData.chargeFee = jsonReader.nextString();
                break;
              case "is_alipay":
                memberLoginPOJO.rspData.isAlipay = jsonReader.nextString();
                break;
              case "alipay_chargefee":
                memberLoginPOJO.rspData.alipayChargefee = jsonReader.nextString();
                break;
              case "alipay_appid":
                memberLoginPOJO.rspData.alipayAppid = jsonReader.nextString();
                break;
              case "is_wxpay":
                memberLoginPOJO.rspData.isWxpay = jsonReader.nextString();
                break;
              case "wxpay_chargefee":
                memberLoginPOJO.rspData.wxpayChargefee = jsonReader.nextString();
                break;
              case "wxpay_appid":
                memberLoginPOJO.rspData.wxpayAppid = jsonReader.nextString();
                break;
              case "wxpay_mchappid":
                memberLoginPOJO.rspData.wxpayMchappid = jsonReader.nextString();
                break;
              case "is_unionpay":
                memberLoginPOJO.rspData.isUnionpay = jsonReader.nextString();
                break;
              case "unionpay_chargefee":
                memberLoginPOJO.rspData.unionpayChargefee = jsonReader.nextString();
                break;
              default:
                jsonReader.skipValue();
            }
          jsonReader.endObject();
          break;
        default:
          jsonReader.skipValue();
      }
    //      jsonReader.endObject();
    jsonReader.close();
    return memberLoginPOJO;
  }

  public class RspData implements Serializable {
    private final static long serialVersionUID = 1;

    public String  accessToken;
    public String  memberId;
    public String  memName;
    //    public String  defaultShopId;
    //    public String  cardno;
    //    public String  cardstatus;
    public int     expiresIn;
    //        public String  hint;
    public Company company;
    public String  chargeFee;
    public String  isAlipay;
    public String  alipayChargefee;
    public String  alipayAppid;
    public String  isWxpay;
    public String  wxpayChargefee;
    public String  wxpayAppid;
    public String  wxpayMchappid;
    //    public String  isBestpay;
    //    public String  bestpayChargefee;
    //    public String  isCcbpay;
    //    public String  ccbpayChargeFee;
    //    public String  isCcbjhpay;
    //    public String  ccbjhpayChargeFee;
    public String  isUnionpay;
    public String  unionpayChargefee;
    //    public String  isUnionjhpay;
    //    public String  unionjhpayChargefee;
    //    public String  isPay4canteen;
    //    public String  startImg;
    //    public String  iosUrl;
    //    public String  androidUrl;
    //    public String  isAppAdmin;
    //    public String  isCgadmin;
    //    public String  trackingUrl;
    //    public String  isLyadmin;

    public class Company implements Serializable {
      private final static long serialVersionUID = 1;

      //    public String     cmpId;
      public String dptName;
      public String cmpName;
      //    public String     cmpPhone;
      //    public String     isChargeOnline;
      //    public String     isReserved;
      //    public String     isDisplayPrice;
      //    public String     isAllowIos;
      //    public String     isAllowAndroid;
      //    public String     isAllowChange;
      //    public List<Shop> shops = null;

      //  public class Shop implements Serializable {
      //    private final static long serialVersionUID = 1;
      //
      //    public String shopId;
      //    public String shopName;
      //    public String consumeType;
      //  }
    }
  }
}