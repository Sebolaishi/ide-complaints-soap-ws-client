package com.anylytical.ide.complaints.soap.ws.client;



import com.anylytical.ide.complaints.soap.ws.client.persistence.domain.ComplaintLodgingRequest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.client.support.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;


import java.io.IOException;

@Configuration
@ComponentScan( basePackages = "com.anylytical.ide.complaints.soap.ws.client")
@EnableAutoConfiguration
@ComponentScan
@EnableWs
public class SoapClientConfiguration{

    WSSoapClient client = new WSSoapClient();

    public SoapClientConfiguration() {
    }


    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.anylytical.ide.complaints.soap.ws.client.persistence.domain");
        return marshaller;
    }

    @Bean
    public WSSoapClient soapClient(Jaxb2Marshaller marshaller, SaajSoapMessageFactory saajSoapMessageFactory) throws IOException {


        client.setDefaultUri("http://10.0.71.155:8280/complaints/2.0.0");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageFactory(saajSoapMessageFactory);

        //Intercepting payload request
        ClientInterceptor[] interceptors = new ClientInterceptor[]{ new SoapClientInterceptors()};
        PayloadValidatingInterceptor payloadValidatingInterceptor = new PayloadValidatingInterceptor();
        payloadValidatingInterceptor.setValidateResponse(true);
        payloadValidatingInterceptor.setValidateRequest(true);
        client.setInterceptors(interceptors);
        payloadValidatingInterceptor.setXsdSchema(complaintsSchema());
        interceptors.equals(payloadValidatingInterceptor);

        return client;
    }

    @Bean
    public SaajSoapMessageFactory messageFactory(){
        SaajSoapMessageFactory message = new SaajSoapMessageFactory();
        message.setSoapVersion(SoapVersion.SOAP_12);
        return message;
    }

    @Bean
    public XsdSchema complaintsSchema()
    {
        return  new SimpleXsdSchema( new ClassPathResource("/wsdl/complaintsSchema.xsd"));
    }




}

