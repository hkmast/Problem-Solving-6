package model;

import model.dataClass.CampingCarDataClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CampingCarModel extends AbstractModel<CampingCarDataClass>{

    @Override
	String getCreateQuery(CampingCarDataClass campingCar) {
        return "insert into campingcar_list(" +
                "cc_name,cc_number,cc_sits,cc_manufacture,cc_manufacture_year," +
                "cc_mileage,cc_rent_price,cc_regist_date,campingcar_rent_company_id)" +
                " values('"+
                campingCar.campingCarName+"','"+
                campingCar.campingCarNumber+"','"+
                campingCar.campingCarSits+"','"+
                campingCar.campingCarManufacutre+"','"+
                campingCar.campingCarManufactureYear+"','"+
                campingCar.campingCarMileage+"','"+
                campingCar.campingCarRentprice+"','"+
                campingCar.campingCarRegitstdate +"','"+
                campingCar.campingCarRentCompanyId+"')";
    }
	
	@Override
	String getReadAllQuery() {
        return "select * from campingcar_list";
    }

    @Override
    String getReadQuery(String id) {
        return " select * from campingcar_list where campingcar_list_id='"+id+"';";
    }

    @Override
    String getUpdateQuery(CampingCarDataClass campingCar) {
        return "update campingcar_list set cc_name='"+campingCar.campingCarName+
                "',cc_number='"+campingCar.campingCarNumber+
                "',cc_sits='"+campingCar.campingCarSits+
                "',cc_manufacture='"+campingCar.campingCarManufacutre+
                "',cc_manufacture_year='"+campingCar.campingCarManufactureYear+
                "',cc_mileage='"+campingCar.campingCarMileage+
                "',cc_rent_price='"+campingCar.campingCarRentprice+
                "',cc_regist_date='"+campingCar.campingCarRegitstdate +
                "',campingcar_rent_company_id='"+campingCar.campingCarRentCompanyId+"'" +
                " where campingcar_list_id='"+campingCar.campingCarId+"'";
    }

    @Override
    String getDeleteQuery(String id) {
        return "DELETE FROM campingcar_list WHERE campingcar_list_id = '"+id+"'";
    }
	
	@Override
    boolean isNullData(CampingCarDataClass campingCar) {
        return campingCar.isNull();
    }

    @Override
    ArrayList<CampingCarDataClass> toInfoFromResultSet(ResultSet resultSet) throws SQLException{
    	ArrayList<CampingCarDataClass> campingCarList = new ArrayList<>();
    	
    	while(resultSet.next()) {
    	    CampingCarDataClass campingCar = new CampingCarDataClass();
            campingCar.campingCarId = Integer.toString(resultSet.getInt(1));
            campingCar.campingCarName = resultSet.getString(2);
            campingCar.campingCarNumber = resultSet.getString(3);
            campingCar.campingCarSits = resultSet.getString(4);
            campingCar.campingCarManufacutre = resultSet.getString(5);
            campingCar.campingCarManufactureYear = resultSet.getString(6);
            campingCar.campingCarMileage = resultSet.getString(7);
            campingCar.campingCarRentprice = resultSet.getString(8);
            campingCar.campingCarRegitstdate = resultSet.getString(9);
            campingCar.campingCarRentCompanyId = resultSet.getString(10);
            campingCarList.add(campingCar);
    	}
    
        return campingCarList;
    }
}
