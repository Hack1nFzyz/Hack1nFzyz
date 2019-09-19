package net.fzyz.jerryc05.fzyz_app.core.utils.ydyg;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class LoginPOJO implements Serializable {
  private final static long serialVersionUID = 1;

  private String              cmpId;
  private String              dptName;
  private String              cmpName;
  private String              cmpPhone;
  private String              isChargeOnline;
  private String              isReserved;
  private String              isDisplayPrice;
  private String              isAllowIos;
  private String              isAllowAndroid;
  private String              isAllowChange;
  private List<Shop>          shops                = null;
  private Map<String, Object> additionalProperties = new HashMap<>();

  public LoginPOJO() {
  }

  public LoginPOJO(String cmpId, String dptName, String cmpName, String cmpPhone,
                   String isChargeOnline, String isReserved, String isDisplayPrice,
                   String isAllowIos, String isAllowAndroid, String isAllowChange,
                   List<Shop> shops) {
    super();
    this.cmpId = cmpId;
    this.dptName = dptName;
    this.cmpName = cmpName;
    this.cmpPhone = cmpPhone;
    this.isChargeOnline = isChargeOnline;
    this.isReserved = isReserved;
    this.isDisplayPrice = isDisplayPrice;
    this.isAllowIos = isAllowIos;
    this.isAllowAndroid = isAllowAndroid;
    this.isAllowChange = isAllowChange;
    this.shops = shops;
  }

  public String getCmpId() {
    return cmpId;
  }

  public void setCmpId(String cmpId) {
    this.cmpId = cmpId;
  }

  public LoginPOJO withCmpId(String cmpId) {
    this.cmpId = cmpId;
    return this;
  }

  public String getDptName() {
    return dptName;
  }

  public void setDptName(String dptName) {
    this.dptName = dptName;
  }

  public LoginPOJO withDptName(String dptName) {
    this.dptName = dptName;
    return this;
  }

  public String getCmpName() {
    return cmpName;
  }

  public void setCmpName(String cmpName) {
    this.cmpName = cmpName;
  }

  public LoginPOJO withCmpName(String cmpName) {
    this.cmpName = cmpName;
    return this;
  }

  public String getCmpPhone() {
    return cmpPhone;
  }

  public void setCmpPhone(String cmpPhone) {
    this.cmpPhone = cmpPhone;
  }

  public LoginPOJO withCmpPhone(String cmpPhone) {
    this.cmpPhone = cmpPhone;
    return this;
  }

  public String getIsChargeOnline() {
    return isChargeOnline;
  }

  public void setIsChargeOnline(String isChargeOnline) {
    this.isChargeOnline = isChargeOnline;
  }

  public LoginPOJO withIsChargeOnline(String isChargeOnline) {
    this.isChargeOnline = isChargeOnline;
    return this;
  }

  public String getIsReserved() {
    return isReserved;
  }

  public void setIsReserved(String isReserved) {
    this.isReserved = isReserved;
  }

  public LoginPOJO withIsReserved(String isReserved) {
    this.isReserved = isReserved;
    return this;
  }

  public String getIsDisplayPrice() {
    return isDisplayPrice;
  }

  public void setIsDisplayPrice(String isDisplayPrice) {
    this.isDisplayPrice = isDisplayPrice;
  }

  public LoginPOJO withIsDisplayPrice(String isDisplayPrice) {
    this.isDisplayPrice = isDisplayPrice;
    return this;
  }

  public String getIsAllowIos() {
    return isAllowIos;
  }

  public void setIsAllowIos(String isAllowIos) {
    this.isAllowIos = isAllowIos;
  }

  public LoginPOJO withIsAllowIos(String isAllowIos) {
    this.isAllowIos = isAllowIos;
    return this;
  }

  public String getIsAllowAndroid() {
    return isAllowAndroid;
  }

  public void setIsAllowAndroid(String isAllowAndroid) {
    this.isAllowAndroid = isAllowAndroid;
  }

  public LoginPOJO withIsAllowAndroid(String isAllowAndroid) {
    this.isAllowAndroid = isAllowAndroid;
    return this;
  }

  public String getIsAllowChange() {
    return isAllowChange;
  }

  public void setIsAllowChange(String isAllowChange) {
    this.isAllowChange = isAllowChange;
  }

  public LoginPOJO withIsAllowChange(String isAllowChange) {
    this.isAllowChange = isAllowChange;
    return this;
  }

  public List<Shop> getShops() {
    return shops;
  }

  public void setShops(List<Shop> shops) {
    this.shops = shops;
  }

  public LoginPOJO withShops(List<Shop> shops) {
    this.shops = shops;
    return this;
  }

  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  public LoginPOJO withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }

  public static class Shop implements Serializable {
    private final static long serialVersionUID = 1;

    private String              shopId;
    private String              shopName;
    private String              consumeType;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public Shop() {
    }

    public Shop(String shopId, String shopName, String consumeType) {
      super();
      this.shopId = shopId;
      this.shopName = shopName;
      this.consumeType = consumeType;
    }

    public String getShopId() {
      return shopId;
    }

    public void setShopId(String shopId) {
      this.shopId = shopId;
    }

    public Shop withShopId(String shopId) {
      this.shopId = shopId;
      return this;
    }

    public String getShopName() {
      return shopName;
    }

    public void setShopName(String shopName) {
      this.shopName = shopName;
    }

    public Shop withShopName(String shopName) {
      this.shopName = shopName;
      return this;
    }

    public String getConsumeType() {
      return consumeType;
    }

    public void setConsumeType(String consumeType) {
      this.consumeType = consumeType;
    }

    public Shop withConsumeType(String consumeType) {
      this.consumeType = consumeType;
      return this;
    }

    public Map<String, Object> getAdditionalProperties() {
      return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
      this.additionalProperties.put(name, value);
    }

    public Shop withAdditionalProperty(String name, Object value) {
      this.additionalProperties.put(name, value);
      return this;
    }
  }
}