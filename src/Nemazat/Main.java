package Nemazat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TooManyListenersException;

import javax.sip.InvalidArgumentException;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.TransportNotSupportedException;

public class Main {
	public static String IP,protokol;
	public static int port;
	public static void main(String[] args){
try {
	guiCko.nakresli();
	FileInputStream citaj = new FileInputStream("F:"+"\\"+"skola"+"\\"+"New folder"+"\\"+"New folder"+"\\"+"Nemazat"+"\\"+"src"+"\\"+"Nemazat"+"\\"+"conf");
	BufferedReader br = new BufferedReader(new InputStreamReader(citaj));
	String riadok;
	int index=0;
	
	while ((riadok = br.readLine()) != null)   {
			index++;
		  if(index==1) IP = riadok.substring(riadok.indexOf(':')+2);
		  if(index==2) port = Integer.parseInt(riadok.substring(riadok.indexOf(':')+2));
		  if(index==3) protokol = riadok.substring(riadok.indexOf(':')+2);
		  }
	SipLayer sip = new SipLayer("Server",IP,port);
	
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}
	
}
