package Nemazat;
import gov.nist.javax.sip.stack.MessageProcessor;

import java.awt.List;
import java.net.InetAddress;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RecordRouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.swing.DefaultListModel;



public class SipLayer implements SipListener{
		
		static public DefaultListModel listDialogov = new DefaultListModel();
		static public DefaultListModel listTranzakcii = new DefaultListModel();
		static public DefaultListModel listMetod = new DefaultListModel();
        private MessageProcessor messageProcessor;
        private String username;
        private SipStack sipStack;
        private SipFactory sipFactory;
        private AddressFactory addressFactory;
        private HeaderFactory headerFactory;
        private MessageFactory messageFactory;
        private SipProvider sipProvider;
        public String MojaIP;
        public int MojPort;
        String predch;
        int cislo_dialogu=0;
        static public ArrayList<Request> list_req= new ArrayList<Request>();
        static public ArrayList<Response> list_res= new ArrayList<Response>();
        static public ArrayList<Request> list_dialog= new ArrayList<Request>();
        static public ArrayList<String> list_all = new ArrayList<String>();
        public HashMap<String,String> IP_hash = new HashMap<String,String>();
        public HashMap<String,Integer> port_hash = new HashMap<String,Integer>();
        public String branch;
        
        public SipLayer(String username, String ip, int port) throws
    PeerUnavailableException, TransportNotSupportedException,
    InvalidArgumentException, ObjectInUseException,
    TooManyListenersException {
        setUsername(username);

        MojaIP = ip;
        MojPort = port;
        sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("gov.nist");
        Properties properties = new Properties();
        properties.setProperty("javax.sip.STACK_NAME","TextClient");
        properties.setProperty("javax.sip.IP_ADDRESS",ip);
        properties.setProperty("javax.sip.AUTOMATIC_DIALOG_SUPPORT","off");
        sipStack = sipFactory.createSipStack(properties);
        headerFactory = sipFactory.createHeaderFactory();
        addressFactory = sipFactory.createAddressFactory();
        messageFactory = sipFactory.createMessageFactory();
        
        guiCko.L_dialogy.setModel(listDialogov);
        guiCko.L_metody.setModel(listMetod);
        guiCko.L_tranzakcie.setModel(listTranzakcii);
          @SuppressWarnings("deprecation")
        ListeningPoint tcp = sipStack.createListeningPoint(port, "tcp");
          @SuppressWarnings("deprecation")
        ListeningPoint udp = sipStack.createListeningPoint(port, "udp");

          sipProvider = sipStack.createSipProvider(tcp);
          sipProvider.addSipListener(this);
          sipProvider = sipStack.createSipProvider(udp);
          sipProvider.addSipListener(this);
        }

        private void setUsername(String username2) {
                username = username2;
        }

        @Override
        public void processDialogTerminated(DialogTerminatedEvent arg0) {
                // TODO Auto-generated method stub

        }

        @Override
        public void processIOException(IOExceptionEvent arg0) {
                // TODO Auto-generated method stub

        }

        @Override
        public void processRequest(RequestEvent arg0) {
        String meno,IP;
        int port;
        Request request = arg0.getRequest();
        System.out.println(request);
        list_req.add(request);
        list_all.add(request.toString());
        switch(request.getMethod()){
        
        case "REGISTER":
        	Response response;
     
        	FromHeader from = (FromHeader) request.getHeader("From");
        	ToHeader to = (ToHeader) request.getHeader("To");
        	try {
				to.setTag(CreateTag());
			} catch (ParseException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
        	request.setHeader(to);
        	IP = from.getAddress().toString();
        	meno = IP.substring(IP.indexOf(':')+1,IP.indexOf('@'));
        	ViaHeader via = (ViaHeader) request.getHeader("Via");
        	port = via.getPort();
        	IP = via.getHost();
        	setIP(meno,IP);
        	setPort(meno,port);
        	System.out.println("Meno:" + meno + "IP" + IP + "Port " + port);
        	
        	try {
				response = messageFactory.createResponse(200,request);
				System.out.println(response);
				sipProvider.sendResponse(response);
			} catch (Exception e) {
				e.printStackTrace();
			}
        	break;
        case "SUBSCRIBE":
        	try {
        		ExpiresHeader expires = headerFactory.createExpiresHeader(20);
            	request.addHeader(expires);
				response = messageFactory.createResponse(200,request);
				sipProvider.sendResponse(response);
			} catch (Exception e) {
				e.printStackTrace();
			}
        	break;
        	
        case "INVITE":
        	from = (FromHeader) request.getHeader("From");
        	to = (ToHeader) request.getHeader("To");
        

        	addToMetod(request.getMethod(),from.toString(),to.toString(),request.getRequestURI().toString());
        	predch = request.getMethod();
        	String prijmatel;
        	String dialog;
        	
        	list_dialog.add(request );
        	
        	try {
				//to.setTag(CreateTag());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        	cislo_dialogu++;
        	CallIdHeader call = (CallIdHeader)request.getHeader("Call-ID"); 
        	dialog = "From tag: " + from.getTag() + "To tag: "+to.getTag() + "Call-ID: " + call.getCallId();
        	listDialogov.addElement(cislo_dialogu+" "+dialog);
        	
        	int Port;
        	IP = to.getAddress().toString();
        	Response response2;
        	meno = IP.substring(IP.indexOf(':')+1,IP.indexOf('@'));
        	if((prijmatel=getIP(meno)) == null)
        	{	
        	try {
				response2 = messageFactory.createResponse(404,request);
				sipProvider.sendResponse(response2);	
        	} catch (Exception e) {
				e.printStackTrace();
			}
        	}
        	else
        		{
        	//******************Odoslanie trying********************************
        		try {
    				response2 = messageFactory.createResponse(100,request);
    				sipProvider.sendResponse(response2);	
            	} catch (Exception e) {
    				e.printStackTrace();
    			}
        	//****************Preposlanie Invite*********************************
        		Port = getPort(meno);
        		try {
        			SipURI uri = addressFactory.createSipURI(meno, prijmatel+":"+Port);
        			request.setRequestURI(uri);
        			branch = CreateBranch();
					ViaHeader Via = headerFactory.createViaHeader(MojaIP, MojPort, "UDP",branch);
					request.addFirst(Via);
					Address rra = addressFactory.createAddress(addressFactory.createURI("sip:" + MojaIP + ":" + MojPort + ";lr"));
					RecordRouteHeader rr = headerFactory.createRecordRouteHeader(rra);
					request.addHeader(rr);
					MaxForwardsHeader forvard = (MaxForwardsHeader)request.getHeader("Max-Forwards");
					forvard.decrementMaxForwards();
					
					sipProvider.sendRequest(request);
					System.out.println("Odoslany invite: \n"+request);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		}
        	break;
        	
        case "BYE":
        case "CANCEL":
        case "ACK":
        	from = (FromHeader) request.getHeader("From");
        	to = (ToHeader) request.getHeader("To");
        	addToMetod(request.getMethod(),from.toString(),to.toString(),request.getRequestURI().toString());
        	if(request.getMethod().equals("ACK"))
        	{	
        	if(predch.equals("OK"))
        	{
        		System.out.println("ACK na OK");
        		list_dialog.add(request);
        	}
        	}
        	if(request.getMethod().equals("BYE"))
        	{
        		list_dialog.add(request);
        	}
        	request.removeHeader("Route");
        	to = (ToHeader)request.getHeader("To");
        	IP = to.getAddress().toString();
        	meno = IP.substring(IP.indexOf(':')+1,IP.indexOf('@'));
        	prijmatel=getIP(meno);
        	Port = getPort(meno);
    		try {
   
    			SipURI uri = addressFactory.createSipURI(meno, prijmatel+":"+Port);
    			request.setRequestURI(uri);
    			//********************overenie ci ide o novu tranzakciu alebo len ACK ktore nie je odpoved na 2xx
    			if(request.getMethod().equals("ACK") && predch.equals("OK") 
    					|| request.getMethod().equals("BYE"))
    			{
    			branch = CreateBranch()+1;	
    			}
    			predch = request.getMethod();
				ViaHeader Via = headerFactory.createViaHeader(MojaIP, MojPort, "UDP", branch);
				request.addFirst(Via);
				Address rra = addressFactory.createAddress(addressFactory.createURI("sip:" + MojaIP + ":" + MojPort + ";lr"));
				RecordRouteHeader rr = headerFactory.createRecordRouteHeader(rra);
				request.addHeader(rr);
				MaxForwardsHeader forvard = (MaxForwardsHeader)request.getHeader("Max-Forwards");
				if((boolean) (forvard != null))
				{
				forvard.decrementMaxForwards();
				}
				sipProvider.sendRequest(request);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		break;
        }
        }

        @Override
        public void processResponse(ResponseEvent arg0) {
                Response response;
                response = arg0.getResponse();
                list_res.add(response);
                list_all.add(response.toString());
                System.out.println(response);
                switch(response.getReasonPhrase())
                {
                case "Ringing":
                case "OK":
                case "Decline":
                case "Busy Here":
                case "Request Terminated":
                	FromHeader from = (FromHeader) response.getHeader("From");
                	ToHeader to = (ToHeader) response.getHeader("To");
                	addToMetod(response.getReasonPhrase(),from.toString(),to.toString(),response.toString());
                	if(response.getReasonPhrase().equals("OK")) predch="OK";
                	response.removeFirst("Via");
                	predch = response.getReasonPhrase();
                	try {
                		System.out.println("Odoslany response");
						sipProvider.sendResponse(response);
					} catch (Exception e) {
						e.printStackTrace();
					}
                	break;
                
                }

        }

        @Override
        public void processTimeout(TimeoutEvent arg0) {
                // TODO Auto-generated method stub

        }

        @Override
        public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
                // TODO Auto-generated method stub

        }
        public void setIP(String meno,String ip)
        {
        	IP_hash.put(meno, ip);
        }
        public void setPort(String meno, int port)
        {
        	port_hash.put(meno, port);
        }
        public String getIP(String meno)
        {
        	return IP_hash.get(meno);
        }
        public int getPort(String meno)
        {
        	return port_hash.get(meno);
        }
        
        public String CreateBranch()
        {
        	SimpleDateFormat simpleDateFormat =
        	        new SimpleDateFormat("yyyymmss");
        	Date date = new Date();
        	
        	return ("z9hG4bK"+simpleDateFormat.format(date));
        }
        int cislo =0;
        int poradie = 0;
        String cele = "1";
        public void addToMetod(String metod,String from,String to,String komu)
        {	if(metod.equals("INVITE"))
        		{ 
        		cislo = 0;
        		poradie++;
        		}
        	if(metod.equals("BYE") || metod.equals("ACK") && predch.equals("OK")
        			|| metod.equals("INVITE")) cislo++;
        	cele = poradie+" "+cislo;
        	listMetod.addElement(cele+" "+metod + " From: " + from.substring(from.indexOf("sip:")+4,from.indexOf('>')) + " To: " + to.substring(to.indexOf("To:<sip:")+10,to.indexOf('>')));
        }
        public String CreateTag()
        {
        	SimpleDateFormat simpleDateFormat =
        	        new SimpleDateFormat("YYYYMMmmss");
        	Date date = new Date();
        	
        	return (String)(simpleDateFormat.format(date));
        }

}