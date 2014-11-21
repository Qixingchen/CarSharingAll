package mysqlconnect;

import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.net.URLEncoder;  
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.lang.StringUtils;

public class maphttp {
  private static String ak = "ABGpnZ6mmGzFdo75vjgbYasn";
    
    public static Map<String, String> testPost(String lng1, String lat1,String lng2, String lat2,String city1,String city2) throws IOException {
    	 String address1 = lat1+","+lng1;
    	 String address2 = lat2+","+lng2;
	     address1 = URLEncoder.encode(address1, "UTF-8");    
	     address2 = URLEncoder.encode(address2, "UTF-8");
    	 city1 = URLEncoder.encode(city1, "UTF-8");    
	     city2 = URLEncoder.encode(city2, "UTF-8");
         URL url = new URL("http://api.map.baidu.com/direction/v1?mode=driving&origin="+  address1 + "&destination="+ address2 +"&origin_region="+city1+"&destination_region="+city2+"&output=json&ak="+"&ak="+ak);    
//         System.out.println(url);
         
         
         
         
        URLConnection connection = url.openConnection();
        /**
         * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
         * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
         */
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection
                .getOutputStream(), "utf-8");
//        remember to clean up
        out.flush();
        out.close();
//        一旦发送成功，用以下方法就可以得到服务器的回应：
        String res;
        InputStream l_urlStream;
        l_urlStream = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                l_urlStream,"UTF-8"));
        StringBuilder sb = new StringBuilder("");
        while ((res = in.readLine()) != null) {
            sb.append(res.trim());
        }
        
        
        String str = sb.toString();
        System.out.println(str);
        Map<String,String> map = null;
    	if(StringUtils.isNotEmpty(str)) {
      	int addStart = str.indexOf("distance\":");
      	int addEnd = str.indexOf(",\"duration");
      	if(addStart > 0 && addEnd > 0) {
        String address = str.substring(addStart+10, addEnd);
       	map = new HashMap<String,String>();
       	map.put("address", address);
        	return map;		
      }
    }
    
    return null;
    
    }
  /*   public static void main(String[] args) throws IOException {
	Map<String, String> json = maphttp.testPost("39.915285","116.403857","40.056878","116.30815","北京","北京");
    System.out.println("address :" + json.get("address")+"米");
}*/
}