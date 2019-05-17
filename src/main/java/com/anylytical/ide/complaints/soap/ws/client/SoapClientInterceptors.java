package com.anylytical.ide.complaints.soap.ws.client;

import org.springframework.ws.client.WebServiceClientException;

import javax.xml.ws.handler.MessageContext;

public interface SoapClientInterceptors {

    boolean handleRequest(MessageContext messageContext) throws WebServiceClientException;

    boolean handleResponse(MessageContext messageContext) throws WebServiceClientException;

    boolean handleFault(MessageContext messageContext) throws WebServiceClientException;

    void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException;
}
