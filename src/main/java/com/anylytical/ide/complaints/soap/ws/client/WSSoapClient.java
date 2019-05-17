package com.anylytical.ide.complaints.soap.ws.client;

import com.anylytical.ide.complaints.soap.ws.client.persistence.domain.ComplaintLodgingRequest;
import com.anylytical.ide.complaints.soap.ws.client.persistence.domain.ComplaintLodgingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.bind.JAXBException;


@Component
public class WSSoapClient extends WebServiceGatewaySupport {


    private static final Logger log = LoggerFactory.getLogger(WSSoapClient.class);

    public WSSoapClient() {

    }

    public WSSoapClient( @Autowired WebServiceMessageFactory messageFactory) {
        super(messageFactory);
    }

    public ComplaintLodgingResponse lodgeComplaints(ComplaintLodgingRequest complaintLodgingRequest) throws JAXBException {
         return (ComplaintLodgingResponse) getWebServiceTemplate().marshalSendAndReceive(complaintLodgingRequest);
    }

}

