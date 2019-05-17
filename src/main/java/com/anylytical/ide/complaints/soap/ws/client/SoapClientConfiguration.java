package com.anylytical.ide.complaints.soap.ws.client;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

@Configuration
@ComponentScan( basePackages = "com.anylytical.ide.complaints.soap.ws.client")
@EnableAutoConfiguration
@ComponentScan
@EnableWs
public class SoapClientConfiguration {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.anylytical.ide.complaints.soap.ws.client.persistence.domain");
        return marshaller;
    }

    @Bean
    public WSSoapClient soapClient(Jaxb2Marshaller marshaller, SaajSoapMessageFactory saajSoapMessageFactory) {
        WSSoapClient client = new WSSoapClient();
        client.setDefaultUri("http://localhost:8080/ide/complaints/ws/");
        //client.

        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageFactory(saajSoapMessageFactory);
        return client;
    }
//
//    @Override
//    public void ClientIntercept()

    @Bean
    public SaajSoapMessageFactory messageFactory(){
        SaajSoapMessageFactory message = new SaajSoapMessageFactory();
        message.setSoapVersion(SoapVersion.SOAP_12);
        return message;
    }

}

