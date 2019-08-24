package com.automation.connections;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LocalParseUtils {

	public static Document getXML(String xmlDom) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(xmlDom.getBytes()));
		return doc;
	}

	public static Node getXpathNode(Document doc, String XpathString)
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		NodeList result = getXPathList(doc, XpathString);

		if (result.item(0) == null) {
			throw new XPathExpressionException("Xpath not found");
		} else {
			return result.item(0);
		}
	}

	public static Node getXpathNode(String XpathString, String xmlDom)
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		NodeList result = getXPathList(getXML(xmlDom), XpathString);

		if (result.item(0) == null) {
			throw new XPathExpressionException("Xpath not found");
		} else {
			return result.item(0);
		}
	}

	public static String getXPathValue(String XpathString, String xmlDom)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		NodeList result = getXPathList(getXML(xmlDom), XpathString);

		if (result.item(0) == null) {
			throw new XPathExpressionException("Xpath not found");
		} else {
			return result.item(0).getTextContent();
		}
	}

	public static String getXPathValue(Document doc, String XpathString)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		NodeList result = getXPathList(doc, XpathString);

		if (result.item(0) == null) {
			throw new XPathExpressionException("Xpath not found");
		} else {
			return result.item(0).getTextContent();
		}
	}

	public static String getXPathValue(Node node)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		if (node == null) {
			throw new XPathExpressionException("Xpath not found");
		} else {
			return node.getTextContent();
		}
	}

	public static String getXPathAttribute(String attribute, String XpathString, String xmlDom)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		NodeList result = getXPathList(getXML(xmlDom), XpathString);

		if (result.item(0) == null) {
			throw new XPathExpressionException("Xpath not found");
		} else {
			return result.item(0).getAttributes().getNamedItem(attribute).getTextContent();
		}
	}

	public static String getXPathAttribute(Document doc, String attribute, String XpathString)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		NodeList result = getXPathList(doc, XpathString);

		if (result.item(0) == null) {
			throw new XPathExpressionException("Xpath not found");
		} else {
			return result.item(0).getAttributes().getNamedItem(attribute).getTextContent();
		}
	}

	public static String getXPathAttribute(Node node, String attribute)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		if (node == null) {
			throw new XPathExpressionException("Xpath not found");
		} else {
			return node.getAttributes().getNamedItem(attribute).getTextContent();
		}
	}

	public static NodeList getXPathList(Document doc, String XpathString)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile(XpathString);
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			return (NodeList) nodes;
		} catch (Exception ex) {
			throw new XPathExpressionException(" invalid xpath syntax or xpath not found: " + ex.toString());
		}
	}

	public static void main(String[] args)
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		String xmlDom = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Entities TotalResults=\"2\"><Entity Type=\"test-instance\"><ChildrenCount><Value>0</Value></ChildrenCount><Fields><Field Name=\"test-id\"><Value>1</Value></Field><Field Name=\"os-config\"><Value></Value></Field><Field Name=\"data-obj\"><Value></Value></Field><Field Name=\"is-dynamic\"><Value>N</Value></Field><Field Name=\"exec-time\"><Value></Value></Field><Field Name=\"cycle\"><Value></Value></Field><Field Name=\"has-linkage\"><Value>N</Value></Field><Field Name=\"exec-event-handle\"><Value></Value></Field><Field Name=\"exec-date\"/><Field Name=\"last-modified\"><Value>2019-06-11 09:47:04</Value></Field><Field Name=\"subtype-id\"><Value>hp.qc.test-instance.MANUAL</Value></Field><Field Name=\"cycle-id\"><Value>1</Value></Field><Field Name=\"attachment\"><Value></Value></Field><Field Name=\"id\"><Value>1</Value></Field><Field Name=\"plan-scheduling-date\"/><Field Name=\"assign-rcyc\"><Value></Value></Field><Field Name=\"test-config-id\"><Value>1001</Value></Field><Field Name=\"owner\"><Value>SRV-NWLAUTOQCUSER</Value></Field><Field Name=\"pinned-baseline\"><Value></Value></Field><Field Name=\"ver-stamp\"><Value>1</Value></Field><Field Name=\"test-instance\"><Value>1</Value></Field><Field Name=\"host-name\"><Value></Value></Field><Field Name=\"order-id\"><Value>1</Value></Field><Field Name=\"eparams\"><Value></Value></Field><Field Name=\"iterations\"><Value></Value></Field><Field Name=\"environment\"><Value></Value></Field><Field Name=\"actual-tester\"><Value></Value></Field><Field Name=\"name\"><Value>TC1_TestCase1 [1]</Value></Field><Field Name=\"bpta-change-awareness\"><Value></Value></Field><Field Name=\"plan-scheduling-time\"><Value></Value></Field><Field Name=\"status\"><Value>No Run</Value></Field></Fields><RelatedEntities/></Entity><Entity Type=\"test-instance\"><ChildrenCount><Value>0</Value></ChildrenCount><Fields><Field Name=\"test-id\"><Value>2</Value></Field><Field Name=\"os-config\"><Value></Value></Field><Field Name=\"data-obj\"><Value></Value></Field><Field Name=\"is-dynamic\"><Value>N</Value></Field><Field Name=\"exec-time\"><Value></Value></Field><Field Name=\"cycle\"><Value></Value></Field><Field Name=\"has-linkage\"><Value>N</Value></Field><Field Name=\"exec-event-handle\"><Value></Value></Field><Field Name=\"exec-date\"/><Field Name=\"last-modified\"><Value>2019-06-11 09:47:04</Value></Field><Field Name=\"subtype-id\"><Value>hp.qc.test-instance.MANUAL</Value></Field><Field Name=\"cycle-id\"><Value>1</Value></Field><Field Name=\"attachment\"><Value></Value></Field><Field Name=\"id\"><Value>2</Value></Field><Field Name=\"plan-scheduling-date\"/><Field Name=\"assign-rcyc\"><Value></Value></Field><Field Name=\"test-config-id\"><Value>1002</Value></Field><Field Name=\"owner\"><Value>SRV-NWLAUTOQCUSER</Value></Field><Field Name=\"pinned-baseline\"><Value></Value></Field><Field Name=\"ver-stamp\"><Value>1</Value></Field><Field Name=\"test-instance\"><Value>1</Value></Field><Field Name=\"host-name\"><Value></Value></Field><Field Name=\"order-id\"><Value>2</Value></Field><Field Name=\"eparams\"><Value></Value></Field><Field Name=\"iterations\"><Value></Value></Field><Field Name=\"environment\"><Value></Value></Field><Field Name=\"actual-tester\"><Value></Value></Field><Field Name=\"name\"><Value>TC2_TestCase2 [1]</Value></Field><Field Name=\"bpta-change-awareness\"><Value></Value></Field><Field Name=\"plan-scheduling-time\"><Value></Value></Field><Field Name=\"status\"><Value>No Run</Value></Field></Fields><RelatedEntities/></Entity><singleElementCollection>false</singleElementCollection></Entities>";

		Map<String, Map<String, String>> testCaseListDetails = getTestEntityDetails(xmlDom);
		
		
		
		for(String key : testCaseListDetails.keySet()) {
			System.out.println("Map for test case id - "+  key);
			for(String keysub : testCaseListDetails.get(key).keySet()) {
				System.out.println( "key - " + keysub  +" Value - " + testCaseListDetails.get(key).get(keysub));
			}
		}
	}
	
	public static List<String> getRunStepIDDetails(String xmlDOM)
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		String stepsEntityInstances = "//Entity[@Type='run-step']";
		List<String> testStepDetails = new ArrayList<String>();
		NodeList nodes = getXPathList(getXML(xmlDOM), stepsEntityInstances);
		for (int i = 0; i < nodes.getLength(); i++) {
			String testCaseID = getTestCaseDetail(xmlDOM, "id", i);
			testStepDetails.add(testCaseID);
		}
		return testStepDetails;
	}
	
	public static Map<String, Map<String, String>> getTestEntityDetails(String testCaseListResponse) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		String xmlDom = testCaseListResponse;
		Map<String, Map<String, String>> testCaseListDetails = new HashMap<String, Map<String, String>>();

		String testInstanceEntity = "//*[@Type='test-instance']";

		NodeList nodes = getXPathList(getXML(xmlDom), testInstanceEntity);

		for (int i = 0; i < nodes.getLength(); i++) {
			Map<String, String> testCaseEntity = new HashMap<String, String>();

//			Fill in the test Case ids
			String testCaseID = getTestCaseDetail(xmlDom, "test-id", i);
			testCaseEntity.put("test-id", testCaseID);

//			test-config-id
			testCaseEntity.put("test-config-id", getTestCaseDetail(xmlDom, "test-config-id", i));
			
//			ver-stamp
			testCaseEntity.put("ver-stamp", getTestCaseDetail(xmlDom, "ver-stamp", i));
			
//			name
			testCaseEntity.put("name", getTestCaseDetail(xmlDom, "name", i));
			
			
//			has-linkage
			testCaseEntity.put("has-linkage", getTestCaseDetail(xmlDom, "has-linkage", i));
			
//			testcycl-id
			testCaseEntity.put("testcycl-id", testCaseID);
			
			
//			cycle-id
			testCaseEntity.put("cycle-id", getTestCaseDetail(xmlDom, "cycle-id", i));
			
//			Owner
			testCaseEntity.put("owner", getTestCaseDetail(xmlDom, "owner", i));
			
			testCaseListDetails.put(testCaseID, testCaseEntity);
		}
		return testCaseListDetails;
	}
	
	public static String getTestCaseDetail(String xmlDom, String entryName, int index) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		String xpath = "//*[@Name='" + entryName + "']/Value";
		NodeList nodes = getXPathList(getXML(xmlDom), xpath);
		String returnValue = nodes.item(index).getChildNodes().item(0).getNodeValue();
		return returnValue;
	}
	
}
