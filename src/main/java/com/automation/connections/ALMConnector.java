package com.automation.connections;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.xml.sax.SAXException;

public class ALMConnector {

	public static String USERNAME = "SRV-NWLAUTOQCUSER";
	public static String PASSWORD = "QcUs3rNw89jk2107";
	public static String ALM_URL = "http://vm-hpqcalm01t:8080/qcbin";
	public static String PROJECT_NAME = "Sample_Testqa";
	public static String DOMAIN_NAME = "DEFAULT";

	/*
	 * public static String USERNAME = "ssamantaray"; public static String PASSWORD
	 * = "Websterbank@124"; public static String ALM_URL =
	 * "http://vm-hpqcalm01p:8080/qcbin"; public static String PROJECT_NAME =
	 * "WOL_Defect_Tracking"; public static String DOMAIN_NAME = "ECOMMERCE";
	 */

	public static String SIGNIN_URI = ALM_URL + "/api/authentication/sign-in";
	public static String TEST_INSTANCES_URI = ALM_URL + "/rest/domains/" + DOMAIN_NAME + "/projects/" + PROJECT_NAME
			+ "/test-instances";
	public static String TEST_RUNS_URI = ALM_URL + "/rest/domains/" + DOMAIN_NAME + "/projects/" + PROJECT_NAME
			+ "/runs";
	public static String SIGNOUT_URI = ALM_URL + "/api/authentication/sign-out";

	/**
	 * Sign-in to ALM and get the CookiesList
	 * 
	 * @return cookiesList
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static List<Cookie> signin() throws ClientProtocolException, URISyntaxException, IOException {
		String encoding = Base64.getEncoder().encodeToString((USERNAME + ":" + PASSWORD).getBytes());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
		Map<String, Object> apiData = httpGetRequest(SIGNIN_URI, new HashMap<String, String>(), headers,
				new BasicCookieStore());
		return (List<Cookie>) apiData.get("cookiesList");
	}

	/**
	 * 
	 * @param testSetId
	 * @param cookiesList
	 * @return
	 * 
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */

	public static Map<String, Map<String, String>> getTestCaseIdList(String testSetId, List<Cookie> cookiesList)
			throws ClientProtocolException, URISyntaxException, IOException, XPathExpressionException,
			ParserConfigurationException, SAXException {
		BasicCookieStore cookieStore = new BasicCookieStore();
		for (Cookie cookie : cookiesList) {
			cookieStore.addCookie(cookie);
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("query", "{cycle-id[" + testSetId + "]}");
		Map<String, Object> apiData = httpGetRequest(TEST_INSTANCES_URI, params, new HashMap<String, String>(),
				cookieStore);

		// Parse the response to fetch the list of test case IDs
		String respStr = IOUtils
				.toString(new InputStreamReader(((HttpResponse) apiData.get("response")).getEntity().getContent()));
		System.out.println("Test Case ID List RESPONSE STRING - " + respStr);

		Map<String, Map<String, String>> mapTestEntityList = LocalParseUtils.getTestEntityDetails(respStr);
		return mapTestEntityList;
	}

	/**
	 * 
	 * @param body
	 * @param cookiesList
	 * @return : TestRun ID
	 * 
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */

	public static String createTestRun(String body, List<Cookie> cookiesList) throws ClientProtocolException,
			URISyntaxException, IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		BasicCookieStore cookieStore = new BasicCookieStore();
		for (Cookie cookie : cookiesList) {
			cookieStore.addCookie(cookie);
		}
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeaders.CONTENT_TYPE, "application/xml");
		Map<String, Object> apiData = httpPostRequest(TEST_RUNS_URI, new HashMap<String, String>(), headers,
				cookieStore, body);
		String respStr = IOUtils
				.toString(new InputStreamReader(((HttpResponse) apiData.get("response")).getEntity().getContent()));
		// System.out.println("Test run create RESPONSE STRING - " + respStr);
		return respStr.split("<Field Name=\"id\"><Value>")[1].split("<")[0];
	}

	/**
	 * 
	 * @param body
	 * @param cookiesList
	 * @param testRunId
	 * @return
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static String updateTestRun(String body, List<Cookie> cookiesList, String testRunId,
			String testCaseUpdateStatus) throws ClientProtocolException, URISyntaxException, IOException,
			XPathExpressionException, ParserConfigurationException, SAXException {

		BasicCookieStore cookieStore = new BasicCookieStore();

		for (Cookie cookie : cookiesList) {
			cookieStore.addCookie(cookie);
		}

		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeaders.CONTENT_TYPE, "application/xml");
		headers.put(HttpHeaders.ACCEPT, "application/xml");
		String UPDATE_TEST_RUN_URI = TEST_RUNS_URI + "/" + testRunId;
		Map<String, Object> apiData = httpPutRequest(UPDATE_TEST_RUN_URI, new HashMap<String, String>(), headers,
				cookieStore, body);
		String respStr = IOUtils
				.toString(new InputStreamReader(((HttpResponse) apiData.get("response")).getEntity().getContent()));
		// System.out.println("Test Run " + testRunId + " Response String - " +
		// respStr);
		updateTestRunSteps(cookieStore, testRunId, testCaseUpdateStatus);
		return respStr;
	}

	/**
	 * 
	 * @param cookieStore
	 * @param testRunId
	 * @param status
	 * 
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */

	public static void updateTestRunSteps(BasicCookieStore cookieStore, String testRunId, String status)
			throws ClientProtocolException, URISyntaxException, IOException, XPathExpressionException,
			ParserConfigurationException, SAXException {

		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeaders.CONTENT_TYPE, "application/xml");

		String UPDATE_TEST_RUN_STEPS_URI = TEST_RUNS_URI + "/" + testRunId + "/run-steps";

		Map<String, Object> apiData = httpGetRequest(UPDATE_TEST_RUN_STEPS_URI, new HashMap<String, String>(),
				new HashMap<String, String>(), cookieStore);

		String respStr = IOUtils
				.toString(new InputStreamReader(((HttpResponse) apiData.get("response")).getEntity().getContent()));

		List<String> mapTestEntityList = LocalParseUtils.getRunStepIDDetails(respStr);

		String stepsBody = "<Entity Type='run-step'>\r\n" + " <Fields>\r\n" + "  <Field Name='status'><Value>" + status
				+ "</Value></Field>\r\n" + " </Fields>\r\n" + "</Entity>";

		for (String stepId : mapTestEntityList) {
			String STEPS_URI = UPDATE_TEST_RUN_STEPS_URI + "/" + stepId;
			httpPutRequest(STEPS_URI, new HashMap<String, String>(), headers, cookieStore, stepsBody);
		}
	}

	/**
	 * SignOut from ALM
	 * 
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static void signout() throws ClientProtocolException, URISyntaxException, IOException {
		String encoding = Base64.getEncoder().encodeToString((USERNAME + ":" + PASSWORD).getBytes());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
		Map<String, Object> apiData = httpGetRequest(SIGNOUT_URI, new HashMap<String, String>(), headers,
				new BasicCookieStore());
		System.out.println("ALM Sign Out status code - "
				+ ((HttpResponse) apiData.get("response")).getStatusLine().getStatusCode());
	}

	/**
	 * This method is used to generate HTTP GET requests for ALM Rest Services.
	 * 
	 * @param endpoint - Mention the endpoint of the service url
	 * @param params   - Map of Parameters to pass to request the service.
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static Map<String, Object> httpGetRequest(String endpoint, Map<String, String> params,
			Map<String, String> headers, BasicCookieStore cookieStore)
			throws URISyntaxException, ClientProtocolException, IOException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		URIBuilder uriBuilder = new URIBuilder(endpoint);
		for (String key : params.keySet()) {
			uriBuilder.addParameter(key, params.get(key));
		}
		HttpGet httpGet = new HttpGet(uriBuilder.build());
		for (String header : headers.keySet()) {
			httpGet.setHeader(header, headers.get(header));
		}
		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
		HttpResponse response = httpClient.execute(httpGet);
		returnMap.put("response", response);
		returnMap.put("cookiesList", cookieStore.getCookies());
		return returnMap;
	}

	/**
	 * 
	 * @param endpoint
	 * @param params
	 * @param headers
	 * @param cookieStore
	 * @param body
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */

	public static Map<String, Object> httpPostRequest(String endpoint, Map<String, String> params,
			Map<String, String> headers, BasicCookieStore cookieStore, String body)
			throws URISyntaxException, ClientProtocolException, IOException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		URIBuilder uriBuilder = new URIBuilder(endpoint);
		for (String key : params.keySet()) {
			uriBuilder.addParameter(key, params.get(key));
		}
		HttpPost httpPost = new HttpPost(uriBuilder.build());
		for (String header : headers.keySet()) {
			httpPost.setHeader(header, headers.get(header));
		}
		StringEntity se = new StringEntity(body);
		se.setContentType("text/xml");
		httpPost.setEntity(se);

		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
		HttpResponse response = httpClient.execute(httpPost);
		returnMap.put("response", response);
		returnMap.put("cookiesList", cookieStore.getCookies());
		return returnMap;
	}

	/**
	 * 
	 * @param endpoint
	 * @param params
	 * @param headers
	 * @param cookieStore
	 * @param body
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */

	public static Map<String, Object> httpPutRequest(String endpoint, Map<String, String> params,
			Map<String, String> headers, BasicCookieStore cookieStore, String body)
			throws URISyntaxException, ClientProtocolException, IOException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		URIBuilder uriBuilder = new URIBuilder(endpoint);
		for (String key : params.keySet()) {
			uriBuilder.addParameter(key, params.get(key));
		}
		HttpPut httpPut = new HttpPut(uriBuilder.build());
		for (String header : headers.keySet()) {
			httpPut.setHeader(header, headers.get(header));
		}
		StringEntity se = new StringEntity(body);
		se.setContentType("text/xml");
		httpPut.setEntity(se);

		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
		HttpResponse response = httpClient.execute(httpPut);
		returnMap.put("response", response);
		returnMap.put("cookiesList", cookieStore.getCookies());
		return returnMap;
	}

	public static void main(String[] args) throws ClientProtocolException, URISyntaxException, IOException,
			XPathExpressionException, ParserConfigurationException, SAXException {

		List<Cookie> cookiesList = signin();
		String testSetID = "1";
		String testCaseId = "3";
		String testCaseUpdateStattus = "Failed";

		Map<String, Map<String, String>> mapTestEntityList = getTestCaseIdList(testSetID, cookiesList);

		// Create a static JSON body file and replace the necessary data from map of
		// maps
		Map<String, String> testCaseDetailsMap = mapTestEntityList.get(testCaseId);

		String createTestRunBody = "<Entity Type='run'>\r\n" + " <Fields>\r\n"
				+ " <Field Name='name'><Value>@TESTCASENAME</Value></Field>\r\n"
				+ " <Field Name='test-instance'><Value>@TESTINSTANCE</Value></Field>\r\n"
				+ " <Field Name='testcycl-id'><Value>@TESTCYCLID</Value></Field> \r\n"
				+ " <Field Name='cycle-id'><Value>@CYCLEID</Value></Field>\r\n"
				+ " <Field Name='test-id'><Value>@TESTID</Value></Field>\r\n"
				+ " <Field Name='subtype-id'><Value>hp.qc.run.MANUAL</Value></Field>\r\n"
				+ " <Field Name='status'><Value>Not Completed</Value></Field>\r\n"
				+ " <Field Name='owner'><Value>@OWNER</Value></Field> \r\n"
				+ " <Field Name='test-config-id'><Value>@TESTCONFIGID</Value></Field> \r\n"
				+ " <Field Name='user-03'><Value>" + "19.08.0 Staging" + "</Value></Field>\r\n"
				+ " <Field Name='user-04'><Value>" + "Sprint 67" + "</Value></Field> \r\n" + " </Fields>\r\n"
				+ "</Entity>";

		createTestRunBody = createTestRunBody.replace("@TESTCASENAME", testCaseDetailsMap.get("name"))
				.replace("@TESTINSTANCE", testCaseDetailsMap.get("test-id"))
				.replace("@TESTCYCLID", testCaseDetailsMap.get("testcycl-id"))
				.replace("@CYCLEID", testCaseDetailsMap.get("cycle-id"))
				.replace("@TESTID", testCaseDetailsMap.get("test-id"))
				.replace("@OWNER", testCaseDetailsMap.get("owner"))
				.replace("@TESTCONFIGID", testCaseDetailsMap.get("test-config-id"));

		System.out.println("Create test run body " + createTestRunBody);

		String runId = createTestRun(createTestRunBody, cookiesList);

		System.out.println("Run ID : " + runId);

		// Make API call to update test Runs
		String updateTestRunBody = "<Entity Type='run'>\r\n" + " <Fields>\r\n"
				+ " <Field Name='name'><Value>@TESTCASENAME</Value></Field>\r\n"
				+ " <Field Name='test-instance'><Value>@TESTINSTANCE</Value></Field>\r\n"
				+ " <Field Name='testcycl-id'><Value>@TESTCYCLID</Value></Field>\r\n"
				+ " <Field Name='cycle-id'><Value>@CYCLEID</Value></Field>\r\n"
				+ " <Field Name='test-id'><Value>@TESTID</Value></Field>\r\n"
				+ " <Field Name='subtype-id'><Value>hp.qc.run.MANUAL</Value></Field>\r\n"
				+ " <Field Name='status'><Value>@STATUS</Value></Field>\r\n"
				+ " <Field Name='owner'><Value>@OWNER</Value></Field>\r\n"
				+ " <Field Name='test-config-id'><Value>@TESTCONFIGID</Value></Field>\r\n"
				+ " <Field Name='user-03'><Value>" + "19.08.0 Staging" + "</Value></Field> \r\n"
				+ " <Field Name='user-04'><Value>" + "Sprint 67" + "</Value></Field> \r\n"
				+ " <Field Name='has-linkage'><Value>@HASLINKAGE</Value></Field>\r\n"
				+ " <Field Name='host'><Value>Sanjay</Value></Field>\r\n"
				+ " <Field Name='draft'><Value>N</Value></Field>\r\n"
				+ " <Field Name='ver-stamp'><Value>@VERSTAMP</Value></Field>\r\n"
				+ " <Field Name='duration'><Value>0</Value></Field>\r\n" + " </Fields>\r\n" + "</Entity>";

		updateTestRunBody = updateTestRunBody.replace("@TESTCASENAME", testCaseDetailsMap.get("name"))
				.replace("@TESTINSTANCE", testCaseDetailsMap.get("test-id"))
				.replace("@TESTCYCLID", testCaseDetailsMap.get("testcycl-id"))
				.replace("@CYCLEID", testCaseDetailsMap.get("cycle-id"))
				.replace("@TESTID", testCaseDetailsMap.get("test-id"))
				.replace("@OWNER", testCaseDetailsMap.get("owner"))
				.replace("@TESTCONFIGID", testCaseDetailsMap.get("test-config-id"))
				.replace("@VERSTAMP", testCaseDetailsMap.get("ver-stamp"))
				.replace("@HASLINKAGE", testCaseDetailsMap.get("has-linkage"))
				.replace("@STATUS", testCaseUpdateStattus);

		System.out.println("Update run body : " + updateTestRunBody);

		updateTestRun(updateTestRunBody, cookiesList, runId, testCaseUpdateStattus);
		signout();
	}
}