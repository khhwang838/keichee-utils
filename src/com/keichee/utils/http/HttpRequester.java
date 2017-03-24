/**
 * Created on 2017. 3. 24. by Kihyun Hwang
 */
package com.keichee.utils.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequester {

	private static final Logger logger = LoggerFactory.getLogger(HttpRequester.class);

	private final String USER_AGENT = "Mozilla/5.0";

	private final static String url_prefix = "https://";
	private final static String url_postfix = "/api/users";
	private static String url;
	private static String adminInfo;
	private static String charset;
	private static int successCnt;
	private static int failCnt;

	/*
	 * 사용자 자동 등록
	 * 파일경로를 입력받아서 API 요청
	 */
	public static void main(String[] args) throws Throwable {

		if ( args.length != 2 ){
			printUsage();
			System.exit(-1);
		}

		setPropertyInfo();

		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), charset));

			// Create a trust manager that does not validate certificate chains
			avoidCertValidation();

			String userInfo;
			url = url_prefix + args[1] + url_postfix;

			while( (userInfo = br.readLine()) != null ){

				URL obj = new URL(url);
				HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
				try{
					sendPost(con, userInfo);
					successCnt++;
					logger.info("Success !!    {}", userInfo);
				}catch(Throwable e){
					int responseCode = con.getResponseCode();
					if ( responseCode == 401 ){
						logger.error("Please check administrator's user ID and password.");
						System.exit(-1);
					}
					failCnt++;
					logger.error("[{}] {}", userInfo, getErrorContents(e));

				}
			}
		}catch(Throwable e){
			e.printStackTrace();
			System.exit(-1);
		}finally{
			if ( br != null ){
				br.close();
			}
			logger.info("===========================");
			logger.info("Total : {}", successCnt + failCnt);
			logger.info("Success : {}", successCnt);
			logger.info("Failed : {}", failCnt);
			System.exit(0);
		}
	}

	private static void setPropertyInfo() {
		adminInfo = System.getProperty("au.admin");
		if ( adminInfo == null ){
			adminInfo = "admin:admin";
		}
		charset = System.getProperty("au.charset");
		if ( charset == null ){
			charset = Charset.defaultCharset().name();
		}
	}

	private static String getErrorContents(Throwable e) {
		StringBuffer sb = new StringBuffer();
		sb.append(e.getMessage()).append(System.lineSeparator());
//		StackTraceElement[] errContents = e.getStackTrace();
//		for ( StackTraceElement ste : errContents ){
//			sb.append("\t").append(ste.toString()).append(System.lineSeparator());
//		}
		return sb.toString();
	}

	/**
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private static void avoidCertValidation() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return new X509Certificate[0];
					}
					@Override
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
					}
					@Override
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
					}
				}
		};
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	// HTTP POST request
	private static void sendPost(final HttpsURLConnection con, final String userInfo) throws Exception {

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");

		String encoded = Base64.encodeBase64String(adminInfo.getBytes(Charset.defaultCharset()));
		con.setRequestProperty("Authorization", "Basic " + encoded);

		String[] userInfoArr = userInfo.split(Pattern.quote(":"));
		String userJSONData = "{\"name\":\"" + userInfoArr[0] + "\",\"credential\":\"" + userInfoArr[1] + "\"}";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(userJSONData);
		wr.flush();
		wr.close();

//		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'POST' request to URL : " + url);
//		System.out.println("Post parameters : " + userInfo);
//		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

//		// print result
		if ( response.toString().trim().length() > 0 ){
			logger.info("[RESP] {}", response.toString());
		}

	}

	private static void printUsage(){
		System.out.println("Usage : java -jar adduser.jar {user data file} {hostname:port} ");
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("default) java -jar adduser.jar /idm/userdata.txt localhost:9443");
		System.out.println();
		System.out.println("optional) java -Dau.charset=euc-kr -Dau.admin=admin:aaaaa -jar adduser.jar /idm/userdata.txt localhost:9443");
		System.out.println("-------------------------------------------------------------------------");
	}


	// HTTP GET request
		private void sendGet() throws Exception {

			String url = "http://www.google.com/search?q=mkyong";

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			System.out.println(response.toString());

		}
}
