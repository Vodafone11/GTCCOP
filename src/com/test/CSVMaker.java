package com.test;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.ast.logger.Cacher;
import com.ast.logger.UtilityLogger;


public class CSVMaker {

	static UtilityLogger logger = null;
	public static String OIM_DB_IP = null;
	public static String OIM_DB_PORT = null;
	public static String OIM_DB_USER = null;
	public static String OIM_DB_PASSWORD = null;
	public static String OIM_USER_ID1 = null;
	public static String OIM_USER_PASSWORD1 = null;
	public static String OIM_USER_ID2 = null;
	public static String OIM_USER_PASSWORD2 = null;
	public static String XL_LOGIN_CONFIG = null;
	public static String Query_Users_OIM1 = null;
	public static String CSV_FILE_PATH = null;
	public static String OIM_DB_INSTANCE = null;
	public static String OIM_DB_QUERY = null;
	public static String OIM_DB_QUERY1 = null;
	public static String OIM_DB_START_DATE = null;
	public static String OIM_DB_END_DATE = null;

	public static List<String> csvList=new ArrayList<String>();

	public static void main(String[] args) {

		OIM_DB_IP = Cacher.getproperty("OIM_DB_IP");
		OIM_DB_PORT = Cacher.getproperty("OIM_DB_PORT");
		OIM_DB_INSTANCE = Cacher.getproperty("OIM_DB_INSTANCE");
		OIM_DB_USER = Cacher.getproperty("OIM_DB_USER");
		OIM_DB_PASSWORD = Cacher.getproperty("OIM_DB_PASSWORD");
		OIM_DB_QUERY = Cacher.getproperty("OIM_DB_QUERY");
		OIM_DB_START_DATE = Cacher.getproperty("OIM_DB_START_DATE");
		OIM_DB_END_DATE = Cacher.getproperty("OIM_DB_END_DATE");
		CSV_FILE_PATH = Cacher.getproperty("CSV_FILE_PATH");

		logger =UtilityLogger.getUtilityLogger(Cacher.getproperty("LOGGER_INSTANCE_NAME"));

		System.out.println("-----------Utility Execution Started--------------");
		logger.info("-----------Utility Execution Started--------------");
		
		//String propertyPath="D:/CAPWorkspace/GTC_POC/files/OIMProp.properties";
		String propertyPath="./files/OIMProp.properties";

		CSVMaker csvMaker=new CSVMaker();
		Connection con=csvMaker.getDBConnection();
		List<String> finalcsvList=csvMaker.readDBData(con);
		if(!finalcsvList.isEmpty()){
			csvMaker.createCSV(finalcsvList);
			csvMaker.updatePropertyFile(propertyPath);
		} else{
			System.out.println("User data not found :: finalcsvList"+finalcsvList);
			logger.info("User data not found :: finalcsvList"+finalcsvList);
		}
		

	}

	private void updatePropertyFile(String propertyPath) {
		logger.info(":: updatePropertyFile :: ");
		System.out.println("Updating property file"+propertyPath);
		logger.info("Updating property file"+propertyPath);

		try{
			Format formatter = new SimpleDateFormat("dd-MMM-yy");
			String s = formatter.format(new Date());
			PropertiesConfiguration config = new PropertiesConfiguration(propertyPath);
			System.out.println("Existing OIM_DB_START_DATE  ::"+OIM_DB_START_DATE);
			logger.info("Existing OIM_DB_START_DATE  ::"+OIM_DB_START_DATE);
			config.setProperty("OIM_DB_START_DATE", s);
			System.out.println("New OIM_DB_START_DATE  ::"+s);
			logger.info("New OIM_DB_START_DATE  ::"+s);
			config.save();

			System.out.println("Property file Successfully Updated..");
			logger.info("Property file Successfully Updated..");
			System.out.println("-----------Utility Exit--------------");
			logger.info("-----------Utility Exit--------------");

			config=null;
			s=null;
			formatter=null;

		} catch (Exception e ) {
			logger.error(e.toString());
			e.printStackTrace();
		}

	}

	private void createCSV(List<String> finalcsvList) {
		logger.info(":: createCSV :: ");
		System.out.println("Creating CSV file"+CSV_FILE_PATH);
		logger.info("Creating CSV file"+CSV_FILE_PATH);
		PrintWriter pw = null;
		try {
			//pw = new PrintWriter(new File("Test2.csv"));
			pw = new PrintWriter(new File(CSV_FILE_PATH));

			StringBuilder sb = new StringBuilder();
			sb.append("#GTC Trusted Resource");
			sb.append('\n');
			sb.append("userId");
			sb.append(',');
			sb.append("firstname");
			sb.append(',');
			sb.append("lastname");
			sb.append(',');
			sb.append("email");
			sb.append(',');
			sb.append("organization");
			sb.append(',');
			sb.append("userType");
			sb.append(',');
			sb.append("employeeType");
			sb.append(',');
			sb.append("usr_status");
			sb.append(',');
			sb.append("partyID");
			sb.append('\n');
			pw.write(sb.toString());

			for(String temp:finalcsvList){
				pw.write(temp);
				pw.flush();
			}
			System.out.println("Creation of CSV file done!! You may validate it before running GTC");
			logger.info("Creation of CSV file done!! You may validate it before running GTC");
			sb=null;
			pw=null;
		} catch (Exception e1) {
			logger.error(e1.toString());
			e1.printStackTrace();
		}
	}

	private List<String> readDBData(Connection con) {
		logger.info(":: readDBData :: ");

		Statement stmt=null;
		ResultSet rs=null;
		try {
			stmt=con.createStatement();
			rs=stmt.executeQuery(OIM_DB_QUERY.replaceAll("OIM_START_DATE", OIM_DB_START_DATE).replaceAll("OIM_END_DATE", OIM_DB_END_DATE));	
			while(rs.next())  {
				StringBuffer sb =new StringBuffer();
				for (int i=1; i<= rs.getMetaData().getColumnCount();i++){
					sb.append((rs.getString(i)!=null)?rs.getString(i):"");
					if (i!=rs.getMetaData().getColumnCount())
						sb.append(',');
				}
				sb.append('\n');
				logger.info("Retrived User Data ::"+sb.toString());
				csvList.add(sb.toString());
				sb=null;
			}

		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		} finally {
			stmt=null; 
			rs=null;
		}
		return csvList;
	}

	private Connection getDBConnection() {
		// TODO Auto-generated method stub
		logger.info(":: getDBConnection :: ");
		Connection con=null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			StringBuffer sb1=new StringBuffer();
			sb1.append("jdbc:oracle:thin:@");
			sb1.append(OIM_DB_IP);
			sb1.append(":");
			sb1.append(OIM_DB_PORT);
			sb1.append(":");
			sb1.append(OIM_DB_INSTANCE);
			con=DriverManager.getConnection(sb1.toString(),OIM_DB_USER,OIM_DB_PASSWORD);
			System.out.println("Successfully connected to DB :: "+sb1);
			logger.info("Successfully connected to DB :: "+sb1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.toString());
			e.printStackTrace();
		}
		return con;
	}  
}

