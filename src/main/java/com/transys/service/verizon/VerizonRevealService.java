package com.transys.service.verizon;

import java.util.List;

import com.transys.model.vo.verizon.DriverVO;
import com.transys.model.vo.verizon.VehicleLocationVO;
import com.transys.model.vo.verizon.VehicleStatusVO;
import com.transys.model.vo.verizon.VehicleVO;
import com.transys.model.vo.verizon.WorkOrderStatusVO;
import com.transys.model.vo.verizon.WorkOrderTypeVO;
import com.transys.model.vo.verizon.WorkOrderVO;

public interface VerizonRevealService {
	 public String getToken();
	 
	 public VehicleVO getVehicle(String vehicleNum);
	 public List<VehicleVO> getAllVehicles();
	 public void updateVehicle(VehicleVO vehicleVO);
	 public VehicleLocationVO getVehicleLocation(String vehicleNum);
	 public List<VehicleLocationVO> getVehicleLocation(List<String> vehicleNumList);
	 public VehicleStatusVO getVehicleStatus(String vehicleNum);
	 public List<VehicleStatusVO> getVehicleStatus(List<String> vehicleNumList);
	 
	 public DriverVO getDriver(String driverNum);
	 public List<DriverVO> getAllDrivers();
	 public DriverVO createDriver(DriverVO driverVO);
	 public void updateDriver(DriverVO driverVO);
	 public void deleteDriver(String driverNum);
	 
	 public WorkOrderVO createWorkOrder(WorkOrderVO workOrderVO);
	 public WorkOrderVO getWorkOrder(String workOrderNum);
	 public void updateWorkOrder(WorkOrderVO workOrderVO);
	 public WorkOrderStatusVO getWorkOrderStatus(String workOrderNum);
	 public void updateWorkOrderStatus(String workOrderNum, WorkOrderStatusVO workOrderStatusVO);
	 
	 public List<WorkOrderStatusVO> getAllWorkOrderStatusTypes();
	 public WorkOrderStatusVO createWorkOrderStatusType(WorkOrderStatusVO workOrderStatusVO);
	 
	 public WorkOrderTypeVO getWorkOrderType(String code);
	 public List<WorkOrderTypeVO> getAllWorkOrderTypes();
	 public WorkOrderTypeVO createWorkOrderType(WorkOrderTypeVO workOrderTypeVO);
}
