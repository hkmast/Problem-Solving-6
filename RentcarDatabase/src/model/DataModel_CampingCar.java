package model;

import view.dataClass.CampingCarInfo;

import java.sql.*;
import java.util.ArrayList;

public class DataModel_CampingCar {
    // SQL 연결
    static Connection con;
    Statement stmt,stmt2;
    ResultSet rs2;
    String Driver="";
    String url="jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true&useSSL=false";
    String userid="madang";
    String pwd="madang";

    public DataModel_CampingCar() {
        conDB();
    }

    public void conDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //System.out.println("드라이버 로드 성공");
        } catch(ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            //System.out.println("데이터베이스 연결 준비...");
            con= DriverManager.getConnection(url, userid, pwd);
            // System.out.println("데이터베이스 연결 성공");
        } catch(SQLException e1) {
            e1.printStackTrace();
        }
    }

/* [모델] 현재 등록된 캠핑카들의 리스트를 만들어 반환 */
    public ArrayList<CampingCarInfo> getCampingCarList() {
        ArrayList<CampingCarInfo> campingCarList = new ArrayList<>();
        try {
            stmt2 = con.createStatement();
            String query2 = " select * from campingcar_list"; /* SQL 문 */
            rs2 = stmt2.executeQuery(query2);

/* SQL을 통해 가져온 데이터를 데이터 클래스 형태로 재구성 */
            while (rs2.next()) {
                CampingCarInfo campingCar = toCampingCarFromResultSet(rs2);
                campingCarList.add(campingCar);
            }
        } catch(Exception e1) {
            System.out.println(e1);
        }
        return campingCarList;
    }

    public int addCampingCar(CampingCarInfo campingCar) {
/* 컨트롤러를 통해 받아온 데이터를 모델에서 입력 */
        try {
            stmt = con.createStatement();
            String query="insert into campingcar_list(cc_name,cc_number,cc_sits,cc_manufacture,cc_manufacture_year,cc_mileage,cc_rent_price,cc_regist_date,campingcar_rent_company_id)"
                    + " values('"+
                    campingCar.name+"','"+
                    campingCar.number+"','"+
                    campingCar.seats+"','"+
                    campingCar.manufacturer+"','"+
                    campingCar.builtDate+"','"+
                    campingCar.mileage+"','"+
                    campingCar.rentalFee+"','"+
                    campingCar.registryDate +"','"+
                    campingCar.companyId+"')";
/* 성공 여부를 반환 */
            return stmt.executeUpdate(query);
        }catch(Exception e1) {
/* 데이터 중 하나라도 비어있던 경우 */
            if(isNull(campingCar)) {
/* 다른 상황을 알려주는 결과값을 반환
* (!) 분류 숫자 대신 다른 방법 써야 함 */
                return 2;
            }
            System.out.println(e1);
            return 0;
        }
    }

/* 컨트롤러에서 넘겨 준 id를 찾아서 같은 id를 갖는 캠핑카를 반환 */
    public CampingCarInfo searchCampingCar(String id) {
        CampingCarInfo campingCar = new CampingCarInfo();
        try {
            stmt2 = con.createStatement();
            String query2=" select * from campingcar_list where campingcar_list_id='"+id+"';"; /* SQL 문 */
            rs2 = stmt2.executeQuery(query2);
/* 같은 값이 있는 경우 캠핑카 데이터 클래스 형태로 반환 */
            if(rs2.next()) {
                campingCar = toCampingCarFromResultSet(rs2);
            }
        }catch(Exception e1) {
            System.out.println(e1);
        }
        return campingCar;
    }

    public int updateCampingCar(CampingCarInfo campingCar) {
/* 수정 결과의 성공 여부를 담을 변수 */
        try {
            stmt = con.createStatement();
/* 입력받은 데이터를 SQL 형태로 구성 */
            String query="update campingcar_list set cc_name='"+campingCar.name+
                    "',cc_number='"+campingCar.number+
                    "',cc_sits='"+campingCar.seats+
                    "',cc_manufacture='"+campingCar.manufacturer+
                    "',cc_manufacture_year='"+campingCar.builtDate+
                    "',cc_mileage='"+campingCar.mileage+
                    "',cc_rent_price='"+campingCar.rentalFee+
                    "',cc_regist_date='"+campingCar.registryDate +
                    "',campingcar_rent_company_id='"+campingCar.companyId+"'" +
                    " where campingcar_list_id='"+campingCar.id+"'";
/* 성공 여부를 저장 */
            return stmt.executeUpdate(query);
        }catch(Exception e1) {
/* 데이터가 비어있던 경우의 상태 */
            if(isNull(campingCar)) {
                return 2;
            }
            System.err.println(e1);
            return 0;
        }
    }

    public int deleteCampingCar(String id) {
        int result1 = 0;
        int result2 = 0;
        try {
            stmt = con.createStatement();
            String query="DELETE FROM campingcar_list WHERE campingcar_list_id = '"+id+"'";
            result1 = stmt.executeUpdate(query);
            query="DELETE FROM rentcar_list WHERE rent_id = '"+id+"'";
            result2 = stmt.executeUpdate(query);
        } catch (Exception e1) {
            //System.out.println(e1);
        }
/* 캠핑카 목록과 대여 중인 캠핑카 목록 둘 다에서 삭제된 경우에만 성공
* (!) 그런데 대여 중인 캠핑카를 삭제하면 문제가 될 수 있다.
* (!) 대여 중인지를 먼저 파악하고, 대여 중이면 삭제를 거부해야 한다. */
        if (result1 == 1 && result2 == 1) {
            return 1;
        } else {
            return 0;
        }
    }

/* [모델] ResultSet을 캠핑카 데이터 클래스 형태로 바꿔주는 기능 */
    private CampingCarInfo toCampingCarFromResultSet(ResultSet result) {
        CampingCarInfo campingCar = new CampingCarInfo();
        try {
            campingCar.id = Integer.toString(result.getInt(1));
            campingCar.name = result.getString(2);
            campingCar.number = result.getString(3);
            campingCar.seats = result.getString(4);
            campingCar.manufacturer = result.getString(5);
            campingCar.builtDate = result.getString(6);
            campingCar.mileage = result.getString(7);
            campingCar.rentalFee = result.getString(8);
            campingCar.registryDate = result.getString(9);
            campingCar.companyId = result.getString(10);
        } catch(Exception e1) {
            System.out.println(e1);
        }
        return campingCar;
    }

/* 캠핑카 데이터 클래스에 빈 값이 들어 있는지 확인 */
    private boolean isNull(CampingCarInfo campingCar) {
        return campingCar.name.length() == 0 ||
                campingCar.number.length() == 0 ||
                campingCar.seats.length() == 0 ||
                campingCar.manufacturer.length() == 0 ||
                campingCar.builtDate.length() == 0 ||
                campingCar.mileage.length() == 0 ||
                campingCar.rentalFee.length() == 0 ||
                campingCar.registryDate.length() == 0 ||
                campingCar.companyId.length() == 0;
    }
}
