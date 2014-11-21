package mysqlconnect;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.HashMap;
import java.net.URLEncoder;  
import java.io.IOException;
import org.apache.commons.lang.StringUtils;

public class province {
	
	static SendRequestWithHttpClient httpsender; 
	
	 private static String ak = "ABGpnZ6mmGzFdo75vjgbYasn";
	/**
	 * @param args
	 * @throws URISyntaxException 
	 */
	 public static Map<String, String> testcity(String lng1, String lat1) throws IOException, URISyntaxException {
		 
		 httpsender = new SendRequestWithHttpClient();
    	 String address1 = lat1+","+lng1;
    	 
	     address1 = URLEncoder.encode(address1, "UTF-8");    
         URI uri = new URI("http://api.map.baidu.com/geocoder/v2/?ak="+ak+"&callback=renderReverse&location="+address1+"&output=json&pois=1");    
         System.out.println(uri);
        
         String str = httpsender.sendRequst(uri);
         
         
//        URLConnection connection = url.openConnection();
//        /**
//         * Ȼ���������Ϊ���ģʽ��URLConnectionͨ����Ϊ������ʹ�ã���������һ��Webҳ��
//         * ͨ����URLConnection��Ϊ���������԰����������Webҳ���͡��������������
//         */
//        connection.setDoOutput(true);
//        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(),"utf-8");
////        remember to clean up
//        out.flush();
//        out.close();
////        һ�����ͳɹ��������·����Ϳ��Եõ��������Ļ�Ӧ��
//        String res;
//        InputStream l_urlStream;
//        l_urlStream = connection.getInputStream();
//        BufferedReader in = new BufferedReader(new InputStreamReader(
//                l_urlStream,"UTF-8"));
//        StringBuilder sb = new StringBuilder("");
//        System.out.println(in.toString());
//        while ((res = in.readLine()) != null) {
//            sb.append(res.trim());
//        }
//        String str = sb.toString();
//        System.out.println(str);
        
        
        
        
        
        Map<String,String> map = null;
    	if(StringUtils.isNotEmpty(str)) {
      	int addStart = str.indexOf("city\":\"");
      	int addEnd = str.indexOf("\",\"district");
      	
      	if(addStart > 0 && addEnd > 0) {
        String addre = str.substring(addStart+7, addEnd);
       	map = new HashMap<String,String>();
       	map.put("addre",addre);
       	System.out.println(addre);
        	return map;		
      }
    }
    
    return null;
    
    }
	 
/*	 public static void main(String[] args) throws IOException {
			Map<String, String> json = province.testcity("24.444111","118.123131");
		    System.out.println("addre:" + json.get("addre"));
	 }
*/
}
