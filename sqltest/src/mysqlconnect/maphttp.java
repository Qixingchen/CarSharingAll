package mysqlconnect;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.HashMap;
import java.net.URLEncoder;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;

public class maphttp {
	private static String ak = "ABGpnZ6mmGzFdo75vjgbYasn";
	static SendRequestWithHttpClient httpsender;

	public static Map<String, String> testPost(String lng1, String lat1,
			String lng2, String lat2, String city1, String city2)
			throws IOException, URISyntaxException {
		httpsender = new SendRequestWithHttpClient();
		String address1 = lat1 + "," + lng1;
		String address2 = lat2 + "," + lng2;
		address1 = URLEncoder.encode(address1, "UTF-8");
		address2 = URLEncoder.encode(address2, "UTF-8");
		city1 = URLEncoder.encode(city1, "UTF-8");
		city2 = URLEncoder.encode(city2, "UTF-8");
		URI uri = new URI(
				"http://api.map.baidu.com/direction/v1?mode=driving&origin="
						+ address1 + "&destination=" + address2
						+ "&origin_region=" + city1 + "&destination_region="
						+ city2 + "&output=json&ak=" + "&ak=" + ak);
		System.out.println(uri);

		String str = httpsender.sendRequst(uri);

		System.out.println(str);
		Map<String, String> map = null;
		if (StringUtils.isNotEmpty(str)) {
			int addStart = str.indexOf("distance\":");
			int addEnd = str.indexOf(",\"duration");
			if (addStart > 0 && addEnd > 0) {
				String address = str.substring(addStart + 10, addEnd);
				map = new HashMap<String, String>();
				map.put("address", address);
				return map;
			}
		}

		return null;

	}
	/*
	 * public static void main(String[] args) throws IOException { Map<String,
	 * String> json =
	 * maphttp.testPost("39.915285","116.403857","40.056878","116.30815"
	 * ,"北京","北京"); System.out.println("address :" + json.get("address")+"米"); }
	 */
}