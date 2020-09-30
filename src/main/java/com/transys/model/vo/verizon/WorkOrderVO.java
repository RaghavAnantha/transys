package com.transys.model.vo.verizon;

public class WorkOrderVO {
  public String ActualDateUtc;
  public Integer ActualDurationSeconds;
  
  public Address Address;
  
  public String ClientCustomerId;
  public String Description;
  public String DriverNumber;
  
  public Double Latitude;
  public Double Longitude;
  public Integer OnSiteDurationSecs;
  public Double RadiusInKM;
  
  public String ScheduledDateUtc;
  public Integer ScheduledDurationSecs;
  public String StatusChangeDateUtc;
  
  public String WorkOrderNumber;
  public String WorkOrderStatusCode;
  public String WorkOrderTypeCode;
}
