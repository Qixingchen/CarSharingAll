package mysqlconnect;

import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class SendRequestWithHttpClient {
	private URI httpgeturi;

	public String sendRequst(URI uri) {
		httpgeturi = uri;
		String response = null;
		// 用HttpClient发送请求，分为五步
		// 第一步：创建HttpClient对象
		HttpClient httpCient = new DefaultHttpClient();
		HttpClientParams.setCookiePolicy(httpCient.getParams(),
				CookiePolicy.BROWSER_COMPATIBILITY);
		// 第二步：创建代表请求的对象,参数是访问的服务器地址
		HttpGet httpGet = new HttpGet(httpgeturi);

		try {
			// 第三步：执行请求，获取服务器发还的相应对象
			HttpResponse httpResponse = httpCient.execute(httpGet);
			// 第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 第五步：从相应对象当中取出数据，放到entity当中
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity, "utf-8");// 将entity当中的数据转换为字符串

			}

		} catch (Exception e) {
			response = "error";
			e.printStackTrace();
		}
		return response;

	}
}
