/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Dispositivos;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TooManyListenersException;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import Interfaces.*;

/**
 *
 * @author eriko
 */
public class CamadaSIP implements SipListener {
    
    private IProcessadorMensagem processadorMensagem;

   
    private String usuario;
    private SipStack sipStack;
    private SipFactory sipFactory;
    private AddressFactory addressFactory;
    private HeaderFactory headerFactory;
    private MessageFactory messageFactory;
    private SipProvider sipProvider;
    
    public CamadaSIP(String usuario,String ip,int porta) throws PeerUnavailableException,TransportNotSupportedException,InvalidArgumentException,
            ObjectInUseException,TooManyListenersException
    {
        setUsuario(usuario);
	sipFactory = SipFactory.getInstance();
	sipFactory.setPathName("gov.nist");
	Properties properties = new Properties();
	properties.setProperty("javax.sip.STACK_NAME", "TextClient");
	properties.setProperty("javax.sip.IP_ADDRESS", ip);
        
        
        //DEBUGGING: Information will go to files 
	//textclient.log and textclientdebug.log
	properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
	//properties.setProperty("gov.nist.javax.sip.SERVER_LOG",
	//	"textclient.txt");
	//properties.setProperty("gov.nist.javax.sip.DEBUG_LOG",
	//	"textclientdebug.log");

	sipStack = sipFactory.createSipStack(properties);
	headerFactory = sipFactory.createHeaderFactory();
	addressFactory = sipFactory.createAddressFactory();
	messageFactory = sipFactory.createMessageFactory();

	ListeningPoint tcp = sipStack.createListeningPoint(porta, "tcp");
	ListeningPoint udp = sipStack.createListeningPoint(porta, "udp");

	sipProvider = sipStack.createSipProvider(tcp);
	sipProvider.addSipListener(this);
	sipProvider = sipStack.createSipProvider(udp);
	sipProvider.addSipListener(this);


    }
    /**
     * Este método utiliza a pilha SIP para enviar uma mensagem. 
     */

    public void sendMessage(String to, String message) throws ParseException,
	    InvalidArgumentException, SipException {

	SipURI from = addressFactory.createSipURI(getUsuario(), getHost()
		+ ":" + getPort());
	javax.sip.address.Address fromNameAddress = addressFactory.createAddress(from);
	fromNameAddress.setDisplayName(getUsuario());
	FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress,
		"textclientv1.0");

	String username = to.substring(to.indexOf(":") + 1, to.indexOf("@"));
	String address = to.substring(to.indexOf("@") + 1);

	SipURI toAddress = addressFactory.createSipURI(username, address);
	javax.sip.address.Address toNameAddress = addressFactory.createAddress(toAddress);
	toNameAddress.setDisplayName(username);
	ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

	SipURI requestURI = addressFactory.createSipURI(username, address);
	requestURI.setTransportParam("udp");

	ArrayList viaHeaders = new ArrayList();
	ViaHeader viaHeader = headerFactory.createViaHeader(getHost(),
		getPort(), "udp", "branch1");
	viaHeaders.add(viaHeader);

	CallIdHeader callIdHeader = sipProvider.getNewCallId();

	CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1,
		Request.MESSAGE);

	MaxForwardsHeader maxForwards = headerFactory
		.createMaxForwardsHeader(70);

	Request request = messageFactory.createRequest(requestURI,
		Request.MESSAGE, callIdHeader, cSeqHeader, fromHeader,
		toHeader, viaHeaders, maxForwards);

	SipURI contactURI = addressFactory.createSipURI(getUsuario(),
		getHost());
	contactURI.setPort(getPort());
	javax.sip.address.Address contactAddress = addressFactory.createAddress(contactURI);
	contactAddress.setDisplayName(getUsuario());
	ContactHeader contactHeader = headerFactory
		.createContactHeader(contactAddress);
	request.addHeader(contactHeader);

	ContentTypeHeader contentTypeHeader = headerFactory
		.createContentTypeHeader("text", "plain");
	request.setContent(message, contentTypeHeader);

	sipProvider.sendRequest(request);
    }

    
    
    
    
    
    
/** Este método é chamado pela pilha SIP quando chega uma resposta. */
    public void processResponse(ResponseEvent evt) {
	Response response = evt.getResponse();
	int status = response.getStatusCode();

	if ((status >= 200) && (status < 300)) { //Success!
	    processadorMensagem.processarInfo("--Sent");
	    return;
	}

	processadorMensagem.processarErro("A mensagem anterior não foi enviada: " + status);
    }


    
    
    
    
    
    
    
    /** 
     * Este método é chamado pela pilha SIP quando chega um novo pedido (request). 
     */
    public void processRequest(RequestEvent evt) {
	Request req = evt.getRequest();

	String method = req.getMethod();
	if (!method.equals("MESSAGE")) { //Tipo do problema de requisião
	    processadorMensagem.processarErro("Problema de requisição tipo: " + method);
	    return;
	}

	FromHeader from = (FromHeader) req.getHeader("Remetente: ");
	processadorMensagem.processarMensagem(from.getAddress().toString(),
		new String(req.getRawContent()));
	Response response = null;
	try { //Responder se OK
	    response = messageFactory.createResponse(200, req);
	    ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
	    toHeader.setTag("888"); //This is mandatory as per the spec.
	    ServerTransaction st = sipProvider.getNewServerTransaction(req);
	    st.sendResponse(response);
	} catch (Throwable e) {
	    e.printStackTrace();
	    processadorMensagem.processarErro("Não pode ser enviada. " +
                    "Tente novamente.");
	}
    }
    /** 
     * Este método é chamado pela pilha SIP quando não há resposta a 
     * uma mensagem. Note que esta é tratada de maneira diferente a partir 
     * de uma mensagem de erro.
     */
    public void processTimeout(TimeoutEvent evt) {
	processadorMensagem
		.processarErro("A mensagem anterior não foi enviada: " + "timeout");
    }


    

    /** 
     * Este método é chamado pela pilha SIP quando há uma mensagem 
     * transmissão assíncrona de erro.  
     */
    public void processIOException(IOExceptionEvent evt) {
	processadorMensagem.processarErro("A mensagem anterior não foi enviada: "
		+ "Exceção de I/O");
    }

    
    public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
       
    }

    public void processDialogTerminated(DialogTerminatedEvent arg0) {
        
    }
    
     public void setProcessadorMensagem(IProcessadorMensagem processadorMensagem) {
        this.processadorMensagem = processadorMensagem;
    }

    public String getHost() {
	int port = sipProvider.getListeningPoint().getPort();
	String host = sipStack.getIPAddress();
	return host;
    }

    public int getPort() {
	int port = sipProvider.getListeningPoint().getPort();
	return port;
    }

    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
