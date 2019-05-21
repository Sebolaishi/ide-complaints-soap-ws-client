package com.anylytical.ide.complaints.soap.ws.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.client.support.interceptor.ClientInterceptorAdapter;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;

import java.io.IOException;

@Component
public class SoapClientInterceptors implements ClientInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SoapClientInterceptors.class);

    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {

            TransportContext context = TransportContextHolder.getTransportContext();
            HttpUrlConnection connection = (HttpUrlConnection) context.getConnection();

            try {
                connection.addRequestHeader("Authorization","Bearer 3e582677-7a3d-347e-a60e-ade505a07e0f");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            return true;

    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {

        log.info("\n\n\n\t\t\t...............Complaint Successfully Loaded................\n\n");
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        log.info("\"\\n\\n\\n\\t\\t\\t............... Please verify your payload ................\\n\\n\"");
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {

    }

}
