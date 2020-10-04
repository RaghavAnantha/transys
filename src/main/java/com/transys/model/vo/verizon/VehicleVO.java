package com.transys.model.vo.verizon;

import com.transys.model.vo.BaseVO;

public class VehicleVO extends BaseVO {
	public String VehicleNumber;
   public String Name;
   public String RegistrationNumber;
   public String VIN;
   public String Make;
   public Integer Year;
   public String Model;
   public Double TankCapacity;
   public Double HighwayMPG;
   public Double CityMPG;
   public Integer VehicleSize;
	public String getVehicleNumber() {
		return VehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		VehicleNumber = vehicleNumber;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getRegistrationNumber() {
		return RegistrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		RegistrationNumber = registrationNumber;
	}
	public String getVIN() {
		return VIN;
	}
	public void setVIN(String vIN) {
		VIN = vIN;
	}
	public String getMake() {
		return Make;
	}
	public void setMake(String make) {
		Make = make;
	}
	public Integer getYear() {
		return Year;
	}
	public void setYear(Integer year) {
		Year = year;
	}
	public String getModel() {
		return Model;
	}
	public void setModel(String model) {
		Model = model;
	}
	public Double getTankCapacity() {
		return TankCapacity;
	}
	public void setTankCapacity(Double tankCapacity) {
		TankCapacity = tankCapacity;
	}
	public Double getHighwayMPG() {
		return HighwayMPG;
	}
	public void setHighwayMPG(Double highwayMPG) {
		HighwayMPG = highwayMPG;
	}
	public Double getCityMPG() {
		return CityMPG;
	}
	public void setCityMPG(Double cityMPG) {
		CityMPG = cityMPG;
	}
	public Integer getVehicleSize() {
		return VehicleSize;
	}
	public void setVehicleSize(Integer vehicleSize) {
		VehicleSize = vehicleSize;
	}
}
