package com.anylytical.ide.complaints.ws.soap.client.test;

import com.anylytical.ide.complaints.soap.ws.client.Application;
import com.anylytical.ide.complaints.soap.ws.client.SoapClientInterceptors;
import com.anylytical.ide.complaints.soap.ws.client.WSSoapClient;
import com.anylytical.ide.complaints.soap.ws.client.persistence.domain.Complaint;
import com.anylytical.ide.complaints.soap.ws.client.persistence.domain.ComplaintDecisionStatusType;
import com.anylytical.ide.complaints.soap.ws.client.persistence.domain.ComplaintLodgingRequest;
import com.anylytical.ide.complaints.soap.ws.client.persistence.domain.ComplaintLodgingResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = Application.class)
@AutoConfigureMockMvc
public class WSSoapClientTest {

    private Logger logger = LoggerFactory.getLogger(WSSoapClientTest.class);


    private String payloadRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:com=\"http://www.anylytical.com/schema/ide/complaints\">\n" +
            "   <soap:Header/>\n" +
            "   <soap:Body>\n" +
            "      <com:complaintLodgingRequest>\n" +
            "         <!--Zero or more repetitions:-->\n" +
            "         <com:complaints>\n" +
            "            <com:complaintsDetails>\n" +
            "               <com:complaintReferences>DKDKD</com:complaintReferences>\n" +
            "               <com:complaintDateTime>2019-05-01T00:00:00.000+02:00</com:complaintDateTime>\n" +
            "               <!--Optional:-->\n" +
            "               <com:dateFinalized>2019-05-14T00:00:00.000+02:00</com:dateFinalized>\n" +
            "               <com:schemeReferences>SS</com:schemeReferences>\n" +
            "               <com:insurer>Bryte</com:insurer>\n" +
            "            </com:complaintsDetails>\n" +
            "            <com:complaintsManagement>\n" +
            "               <com:complaintDefinition>Insurance</com:complaintDefinition>\n" +
            "               <com:complaintManagementFramework>FAIS</com:complaintManagementFramework>\n" +
            "            </com:complaintsManagement>\n" +
            "            <com:settlementPayment>\n" +
            "               <com:complaintSettlementEstimate>1000</com:complaintSettlementEstimate>\n" +
            "               <com:complaintGoodwillPayment>10000</com:complaintGoodwillPayment>\n" +
            "               <com:compensationPayment>1000</com:compensationPayment>\n" +
            "            </com:settlementPayment>\n" +
            "            <com:policyHolderDetails>\n" +
            "               <com:firstName>Lodwin</com:firstName>\n" +
            "               <com:middleName>Mahlatse</com:middleName>\n" +
            "               <com:surname>Moloto</com:surname>\n" +
            "               <com:policyNumber>T56</com:policyNumber>\n" +
            "               <com:contact>\n" +
            "                  <com:cellphone>0658619011</com:cellphone>\n" +
            "                  <com:telephone>0110110111</com:telephone>\n" +
            "                  <com:emailAddress>lodwin@anylytical.co.za</com:emailAddress>\n" +
            "               </com:contact>\n" +
            "               <com:address>\n" +
            "                  <!--You have a CHOICE of the next 2 items at this level-->\n" +
            "                  <com:PostalAddress>\n" +
            "                     <com:AddressLine1>22 sloane</com:AddressLine1>\n" +
            "                     <com:Suburb>Bryanston</com:Suburb>\n" +
            "                     <com:City>Joburg</com:City>\n" +
            "                     <com:Province>Gauteng</com:Province>\n" +
            "                     <com:PostalCode>2191</com:PostalCode>\n" +
            "                  </com:PostalAddress>\n" +
            "               </com:address>\n" +
            "            </com:policyHolderDetails>\n" +
            "            <com:complaintPaymentType>Total</com:complaintPaymentType>\n" +
            "            <com:complaintDecisionStatus>Rejected</com:complaintDecisionStatus>\n" +
            "            <com:complaintEventLogStatus>Final</com:complaintEventLogStatus>\n" +
            "            <com:complaintProcessStatus>Open</com:complaintProcessStatus>\n" +
            "            <com:complaintItemQuestion>ComplaintItem001</com:complaintItemQuestion>\n" +
            "            <com:complaintOccurrenceQuestion>ComplaintOccurrence002</com:complaintOccurrenceQuestion>\n" +
            "            <com:complaintReason>PolicyPerformance</com:complaintReason>\n" +
            "            <com:complaintTransactionAmount>Recovery</com:complaintTransactionAmount>\n" +
            "         </com:complaints>\n" +
            "      </com:complaintLodgingRequest>\n" +
            "   </soap:Body>\n" +
            "</soap:Envelope>";



    String complaintsXMl3 = " <complaintLodgingRequest xmlns=\"http://www.anylytical.com/schema/ide/complaints\"><complaints><complaintsDetails><complaintReferences>DKDKD</complaintReferences><complaintDateTime>2019-05-01T00:00:00.000+02:00</complaintDateTime><dateFinalized>2019-05-14T00:00:00.000+02:00</dateFinalized><schemeReferences>SS</schemeReferences><insurer>Bryte</insurer></complaintsDetails><complaintsManagement><complaintDefinition>Insurance</complaintDefinition><complaintManagementFramework>FAIS</complaintManagementFramework></complaintsManagement><settlementPayment><complaintSettlementEstimate>1000</complaintSettlementEstimate><complaintGoodwillPayment>10000</complaintGoodwillPayment><compensationPayment>1000</compensationPayment></settlementPayment><policyHolderDetails><firstName>Lodwin</firstName><middleName>Mahlatse</middleName><surname>Moloto</surname><policyNumber>T56</policyNumber><contact><cellphone>0658619011</cellphone><telephone>0110110111</telephone><emailAddress>lodwin@anylytical.co.za</emailAddress></contact><address><PostalAddress><AddressLine1>22 sloane</AddressLine1><Suburb>Bryanston</Suburb><City>Joburg</City><Province>Gauteng</Province><PostalCode>2191</PostalCode></PostalAddress></address></policyHolderDetails><complaintPaymentType>Total</complaintPaymentType><complaintDecisionStatus>Rejected</complaintDecisionStatus><complaintEventLogStatus>Final</complaintEventLogStatus><complaintProcessStatus>Open</complaintProcessStatus><complaintItemQuestion>ComplaintItem001</complaintItemQuestion><complaintOccurrenceQuestion>ComplaintOccurrence002</complaintOccurrenceQuestion><complaintReason>PolicyPerformance</complaintReason><complaintTransactionAmount>Recovery</complaintTransactionAmount></complaints></complaintLodgingRequest>";

//    @Autowired
//    private ApplicationContext applicationContext;

    @Autowired
    private Jaxb2Marshaller jaxb2Marshaller;

//    private MockWebServiceClient mockWebServiceClient;


    @Autowired
    private WSSoapClient soapClient;


    public WSSoapClientTest() throws JAXBException {
    }

//
//    @Test(expected = Exception.class)
//    public void faultReturnTest() throws JAXBException {
//
//        ComplaintLodgingRequest complaintLodgingRequest = new ComplaintLodgingRequest();
//        Complaint complaint = new Complaint();
//        complaint.setComplaintDecisionStatus(ComplaintDecisionStatusType.REJECTED);
//        complaintLodgingRequest.getComplaints().add(complaint);
//
//        soapClient.lodgeComplaints(complaintLodgingRequest);
//    }
//

//    @Test
//    public void marshallComplaintsRequest() {
//
//        ComplaintLodgingRequest complaintLodgingRequest = new ComplaintLodgingRequest();
//        Complaint complaint = new Complaint();
//        complaint.setComplaintDecisionStatus(ComplaintDecisionStatusType.REJECTED);
//        complaintLodgingRequest.getComplaints().add(complaint);
//
//        StringResult stringResult = new StringResult();
//
//        jaxb2Marshaller.marshal(complaintLodgingRequest, stringResult);
//
//        assertNotNull(stringResult.toString());
//        assertFalse(StringUtils.isEmpty(stringResult.toString()));
//
//        logger.info(stringResult.toString());
//
//    }


    @Test
    public void handleRequestTest() throws JAXBException {

        SoapClientInterceptors handleRequest = new SoapClientInterceptors();

    }

    @Test
    public void successfulRequestTest() throws JAXBException {


        ComplaintLodgingRequest complaintLodgingRequest = (ComplaintLodgingRequest) jaxb2Marshaller.unmarshal(new StringSource(complaintsXMl3));

        assertNotNull(complaintLodgingRequest);

        ComplaintLodgingResponse complaintLodgingResponse = soapClient.lodgeComplaints(complaintLodgingRequest);

        assertNotNull(complaintLodgingResponse);

        assertNotNull(complaintLodgingResponse.getReceiptNumber());


    }


}
