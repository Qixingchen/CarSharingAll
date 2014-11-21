package mysqlconnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
//import java.security.AlgorithmConstraints;
import java.sql.*;
import java.util.Map;

public class testsql {
	public static Connection getConnection() throws SQLException,
			java.lang.ClassNotFoundException {
		String url = "jdbc:mysql://car.qixingchen.me:3306/carsharing";
		Class.forName("com.mysql.jdbc.Driver");
		String userName = "carsharingremote";
		String password = "qwQ!zlB*C5TOJoEc";
		Connection con = DriverManager.getConnection(url, userName, password);
		return con;
	}

	// public static void main(String[] args) throws IOException {
	public static int itersum = 0;

	public static void Algorithm() throws IOException, URISyntaxException {
		try {
			maphttp mh = new maphttp();
			Connection con = getConnection();
			Statement sql = con.createStatement();
			Integer p = 0; // the number of total person
			Integer d = 0; // the number of the drivers
			Integer k = 0; // the max permit drivers
			// String query = "select * from commute_request";
			String query = "select * from commute_request where DealStatus=0"; // 从申请拼车的人中进行算法计算
			// String
			// query1="select count(*) as dcount from commute_request where SupplyCar='y'";
			// //查询有车的人的数量
			String query1 = null;
			String query2 = "select DestinationX,DestinationY,count(UserID) as co from commute_request where DealStatus=0 Group by DestinationX,DestinationY";
			String query3 = null;
			ResultSet result = sql.executeQuery(query2);
			// ResultSet result1=sql.executeQuery(query1);
			String city = "";
			String city1 = "";
			String city2 = "";
			String city3 = "";
			System.out.println("commute_                  request表数据如下：");
			System.out.println("---------------------------------");
			System.out.println("出发地经度" + " " + "出发地纬度" + " " + "目的地经度" + " "
					+ "目的地纬度");
			System.out.println("---------------------------------");
			result.last();
			int rowCount = result.getRow();
			System.out.println(rowCount); // test the row of the select
			String[] DesX = new String[rowCount];
			String[] DesY = new String[rowCount];
			int[] totalPerson = new int[rowCount];
			String Destinationlng = null;
			String Destinationlat = null;
			result.first(); // return to the first
			int i = 0;
			DesX[i] = result.getString("DestinationX");
			DesY[i] = result.getString("DestinationY");
			totalPerson[i] = result.getInt("co");
			i++;
			String distance = "";
			String mes = "";
			String ids = "";
			String y = "";
			String command = "";
			String pathname = "debug\\car.exe";
			int isd = 0;
			int weekrepeat = 0;
			while (result.next()) { // get the X,Y from database to array
				DesX[i] = result.getString("DestinationX");
				DesY[i] = result.getString("DestinationY");
				totalPerson[i] = result.getInt("co");
				i++;
			}
			result.close();
			int j = 0;
			String type = "上下班拼车";
			int sum = 0;
			while (j < rowCount) { // 根据不同目的地分别调用算法
				city = "";
				String[] StartPlacelng = new String[totalPerson[j]];
				String[] StartPlacelat = new String[totalPerson[j]];
				String[] UserID = new String[totalPerson[j]];
				/*
				 * PreparedStatement ps=con.prepareStatement(
				 * "insert into carshare_deal values(?,?,?,?)");
				 * //往carshare_deal中插入数据 sum=itersum+1; ps.setLong(1,sum);
				 * ps.setString(2,GetNowTime.getTime()); ps.setString(3,type);
				 * ps.setLong(4,0); ps.executeUpdate(); System.out.println(sum);
				 */
				distance = "";
				System.out.println(DesX[j] + " " + DesY[j]);
				query3 = "Select * from commute_request where DestinationX="
						+ DesX[j] + "and DestinationY=" + DesY[j]
						+ "and DealStatus=0"; // get the total information from
												// table group by X,Y
				ResultSet result1 = sql.executeQuery(query3);
				int num = 0;
				mes = "";
				ids = "";
				while (result1.next()) { // 获取每个点的经纬度
					isd = 0;
					StartPlacelng[num] = result1.getString("StartPlaceX");
					StartPlacelat[num] = result1.getString("StartPlaceY");
					UserID[num] = result1.getString("UserID");
					Destinationlng = result1.getString("DestinationX");
					Destinationlat = result1.getString("DestinationY");
					mes = mes + " " + StartPlacelng[num] + " "
							+ StartPlacelat[num] + " ";
					ids = ids + " " + UserID[num];
					y = result1.getString("SupplyCar");
					if (y.charAt(0) == 'y')
						isd = 1;
					weekrepeat = result1.getInt("WeekRepeat");
					mes = mes + isd + " " + weekrepeat;
					// System.out.println(StartPlacelng + " " + StartPlacelat +
					// " " + Destinationlng+" "+Destinationlat);
					// Map<String, String> json =
					// maphttp.testPost(StartPlacelng[num],StartPlacelat[num],
					// Destinationlng, Destinationlat, city, city);
					// distance=distance+json.get("address")+" ";
					num++;
				}
				// System.out.println(mes);
				Map<String, String> json1 = province.testcity(Destinationlng,
						Destinationlat);
				city = city + json1.get("addre"); // 获取该地点所在的省份
				for (int it = 0; it < totalPerson[j]; it++) { // 获取任意两点间的实际距离
					for (int it1 = 0; it1 < totalPerson[j]; it1++) {
						try {
							city1 = "";
							city2 = "";
							Map<String, String> json2 = province.testcity(
									StartPlacelng[it], StartPlacelat[it]);
							city1 = city1 + json2.get("addre");
							json2 = province.testcity(StartPlacelng[it1],
									StartPlacelat[it1]);
							city2 = city2 + json2.get("addre");
							// System.out.println(city1+" "+city2);
							Map<String, String> json = maphttp.testPost(
									StartPlacelng[it], StartPlacelat[it],
									StartPlacelng[it1], StartPlacelat[it1],
									city1, city2);
							// System.out.println("address :" +
							// json.get("address")+"米");

							// warning>>>>>>>>>>>>>>>>
							distance = distance + json.get("address") + " ";

						} catch (Exception ex) {
							System.err.println("Exception:" + ex.getMessage());
						}
					}
				}
				for (int it = 0; it < totalPerson[j]; it++) { // 获取任意点到目的地的实际距离
					city3 = "";
					Map<String, String> json2 = province.testcity(
							StartPlacelng[it], StartPlacelat[it]);
					city3 = city3 + json2.get("addre");
					Map<String, String> json = maphttp.testPost(
							StartPlacelng[it], StartPlacelat[it],
							Destinationlng, Destinationlat, city3, city);
					distance = distance + json.get("address") + " ";
				}
				// System.out.println(distance);
				// System.out.println(totalPerson[j]);
				// result.close();
				query1 = "Select * from commute_request where DestinationX="
						+ DesX[j] + "and DestinationY=" + DesY[j]
						+ "and DealStatus=0 and SupplyCar='y'"; // get the
																// number of
																// driver
				ResultSet result2 = sql.executeQuery(query1);
				result2.last();
				d = result2.getRow();
				// System.out.println(d); //test the row of the select
				k = d;
				// System.out.println(k);
				result2.close();
				// sql.addBatch("insert into carshare_deal (DealID,DealTime,SharingType,FinshStatus) values("+j+","+GetNowTime.getTime()+",'上下班拼车',0)");
				// sql.executeBatch();
				command = pathname + " " + totalPerson[j] + " " + d + " " + k
						+ " " + DesX[j] + " " + DesY[j] + mes + " " + distance
						+ " " + (itersum + 1) + " " + ids;
				System.out.println(command);
				Runtime rt = Runtime.getRuntime();
				Process pro = rt.exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						pro.getInputStream(), "GBK"));
				String msg = null;
				// StringBuilder carshare = new StringBuilder("");
				String st = "OK";
				while ((msg = br.readLine()) != null) {
					// carshare.append(msg.trim());
					if (msg.equals(st)) {
						PreparedStatement ps1 = con
								.prepareStatement("update commute_request set DealStatus=? where DestinationX="
										+ DesX[j]
										+ "and DestinationY="
										+ DesY[j] + " and DealStatus=0");
						ps1.setInt(1, 1);
						ps1.executeUpdate();
					}

					System.out.println(msg);
				}
				j++;
				itersum++;

			}
			// result1.close();
			sql.close();
			con.close();
		} catch (java.lang.ClassNotFoundException e) {
			System.err.println("ClassNotFoundException:" + e.getMessage());
		} catch (SQLException ex) {
			System.err.println("SQLException:" + ex.getMessage());
		}
	}

	public static void main(String[] args) throws IOException,
			URISyntaxException {
		// TimerTask task=new TimerTask(){
		// public void run(){
		// try {
		testsql.Algorithm();
		// }
		// catch (IOException e) {
		// TODO Auto-generated catch block
		/*
		 * e.printStackTrace(); } } }; Timer time=new Timer();
		 * time.schedule(task, 0,5000*60*24); //每隔2小时调用1次算法
		 */
	}
}