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
		// ��HttpClient�������󣬷�Ϊ�岽
		// ��һ��������HttpClient����
		HttpClient httpCient = new DefaultHttpClient();
		HttpClientParams.setCookiePolicy(httpCient.getParams(),
				CookiePolicy.BROWSER_COMPATIBILITY);
		// �ڶ�����������������Ķ���,�����Ƿ��ʵķ�������ַ
		HttpGet httpGet = new HttpGet(httpgeturi);

		try {
			// ��������ִ�����󣬻�ȡ��������������Ӧ����
			HttpResponse httpResponse = httpCient.execute(httpGet);
			// ���Ĳ��������Ӧ��״̬�Ƿ����������״̬���ֵ��200��ʾ����
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// ���岽������Ӧ������ȡ�����ݣ��ŵ�entity����
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity, "utf-8");// ��entity���е�����ת��Ϊ�ַ���

			}

		} catch (Exception e) {
			response = "error";
			e.printStackTrace();
		}
		return response;

	}
}
