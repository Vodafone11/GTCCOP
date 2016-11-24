package com.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.ast.logger.Cacher;
import com.ast.logger.UtilityLogger;


public class CSVMakerTest {
	
	static UtilityLogger logger = null;
	public static String OIM_DB_IP = null;
	public static String OIM_DB_PORT = null;
	public static String OIM_DB_USER = null;
	public static String OIM_DB_PASSWORD = null;
	public static String OIM_USER_ID1 = null;
	public static String OIM_USER_PASSWORD1 = null;
	public static String OIM_USER_ID2 = null;
	public static String OIM_USER_PASSWORD2 = null;
	public static String OIM_DB_START_DATE = null;
	public static String OIM_DB_END_DATE = null;
	public static String CSV_FILE_PATH = null;
	public static String OIM_DB_INSTANCE = null;
	public static String OIM_DB_QUERY = null;
	

	public static void main(String[] args) {
		
		OIM_DB_IP = Cacher.getproperty("OIM_DB_IP");
		OIM_DB_PORT = Cacher.getproperty("OIM_DB_PORT");
		OIM_DB_INSTANCE = Cacher.getproperty("OIM_DB_INSTANCE");
		OIM_DB_USER = Cacher.getproperty("OIM_DB_USER");
		OIM_DB_PASSWORD = Cacher.getproperty("OIM_DB_PASSWORD");
		OIM_DB_QUERY = Cacher.getproperty("OIM_DB_QUERY");
		OIM_DB_START_DATE = Cacher.getproperty("OIM_DB_START_DATE");
		OIM_DB_END_DATE = Cacher.getproperty("OIM_DB_END_DATE");
		
		CSVMakerTest csvMaker=new CSVMakerTest();
		Connection con=csvMaker.getDBConnection();
		csvMaker.readDBData();
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("Test2.csv"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try{  
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			StringBuffer sb1=new StringBuffer();
			sb1.append("jdbc:oracle:thin:@");
			sb1.append(OIM_DB_IP);
			sb1.append(":");
			sb1.append(OIM_DB_PORT);
			sb1.append(":");
			sb1.append(OIM_DB_INSTANCE);
			Connection con1=DriverManager.getConnection(sb1.toString(),OIM_DB_USER,OIM_DB_PASSWORD);  
			Statement stmt=con.createStatement(); 
			
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
		        
		        System.out.println("OIM_DB_QUERY"+OIM_DB_QUERY);
		        
		        PreparedStatement p = con.prepareStatement("select usr_login,usr_last_name from usr where usr_email=?");
		        		p.setString(1,"DUDHERE4@SERENECORP.COM");
		        		
		        		ResultSet rs=p.executeQuery();
		        		while (rs.next()) {
		        			System.out.println(rs.getString("usr_login")+"::"+rs.getString("usr_last_name"));
		        		}
		        		
		        		
		        System.out.println(java.sql.Date.valueOf("2013-09-04"));//+OIM_DB_END_DATE);
			PreparedStatement preparedStatement = con.prepareStatement(OIM_DB_QUERY);
			preparedStatement.setString(1, OIM_DB_START_DATE);
			preparedStatement.setString(2, OIM_DB_END_DATE);
			preparedStatement.setString(3, OIM_DB_START_DATE);
			preparedStatement.setString(4, OIM_DB_END_DATE);
			
			//ResultSet rs=stmt.executeQuery(OIM_DB_QUERY);
			System.out.println("OIM_DB_QUERY"+OIM_DB_QUERY);
			 /*StringBuilder sb = new StringBuilder();
		        sb.append("#GTC Trusted Resource");
		        sb.append('\n');
		        sb.append(rs.getMetaData().getColumnName(1));
		        sb.append(',');
		        sb.append(rs.getMetaData().getColumnName(2));
		        sb.append(',');
		        sb.append(rs.getMetaData().getColumnName(3));
		        sb.append(',');
		        sb.append(rs.getMetaData().getColumnName(4));
		        sb.append(',');
		        sb.append(rs.getMetaData().getColumnName(5));
		        sb.append(',');
		        sb.append(rs.getMetaData().getColumnName(6));
		        sb.append(',');
		        sb.append(rs.getMetaData().getColumnName(7));
		        sb.append(',');
		        sb.append(rs.getMetaData().getColumnName(8));
		        sb.append('\n');
		        pw.write(sb.toString());
		     */   
			/*while(rs.next())  {
			System.out.println(rs.getString(1)+"::"+rs.getString(2)+"::"+rs.getString(3)+"::"+rs.getString(4)+"::"+rs.getString(5)+"::"+rs.getString(6)+"::"+rs.getString(7)+"::"+rs.getString(8)+"::"+rs.getString(9));
			sb = new StringBuilder();
	        sb.append((rs.getString(1)!=null)?rs.getString(1):"");
	        sb.append(',');
	        sb.append((rs.getString(2)!=null)?rs.getString(2):"");
	        sb.append(',');
	        sb.append((rs.getString(3)!=null)?rs.getString(3):"");
	        sb.append(',');
	        sb.append((rs.getString(4)!=null)?rs.getString(4):"");
	        sb.append(',');
	        sb.append((rs.getString(5)!=null)?rs.getString(5):"");
	        sb.append(',');
	        sb.append((rs.getString(6)!=null)?rs.getString(6):"");
	        sb.append(',');
	        sb.append((rs.getString(7)!=null)?rs.getString(7):"");
	        sb.append(',');
	        sb.append((rs.getString(8)!=null)?rs.getString(8):"");
	        sb.append(',');
	        sb.append((rs.getString(9)!=null)?rs.getString(9):"");
	        sb.append('\n');
	        pw.write(sb.toString());
	        sb=null;
	        pw.flush();
			}
			
			stmt=null; 
			rs=null;
			con.close(); 
			
	        System.out.println("done!");
			*/  
			}catch(Exception e){ 
				System.out.println(e);}  
			  
			}

	private void readDBData() {
		// TODO Auto-generated method stub
		
	}

	private Connection getDBConnection() {
		// TODO Auto-generated method stub
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
		System.out.println("Successfully connected to DB");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}  
	}

