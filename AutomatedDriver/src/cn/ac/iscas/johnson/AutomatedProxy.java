package cn.ac.iscas.johnson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

// listen on port(default is 10001), and relay to analysis module
public class AutomatedProxy {
	private final int MAX_LENGTH = 1024;
	// port
	final int port = 10001;
	// data received
	private StringBuilder sb;
	
	private ServerSocket server;
	private Socket socket;
	//private Reader reader;
	
	public AutomatedProxy(){
		sb = new StringBuilder();
		//reader = new InputStreamReader(null);
	}
	
	// clear all buffers
	public void clearBuffer(){
		sb.delete(0, sb.length() - 1);
	}
	
	// release resource explicitly
	public void releaseResource() throws IOException{
		if(socket != null){
			socket.close();
		}
		if(server != null){
			server.close();
		}
	}
	
	// listen on port
	public void receiveInfo() throws IOException{
		ServerSocket server = new ServerSocket(port);
		Socket socket = server.accept();
		Reader reader = new InputStreamReader(socket.getInputStream());
		
		char chars[] = new char[MAX_LENGTH];
		int ilen;
		
	    while ((ilen=reader.read(chars)) != -1) {  
	    	 String str = new String(chars, 0, ilen);
	    	 
	    	 // delimiter is defined by ourself. Of course, you can define your delimiter 
	    	 // only if the delimiter is not confused to valid content
	    	 int iLast = str.indexOf("finished!");
	    	 
	    	 if(iLast == -1){
	    		 sb.append(str);
	    	 }else{
	    		 sb.append(str.substring(0, iLast - 1));
	    		 // todo: you should do something when you receive some covered information
	    		 // combining cfg, relation on the cfg, and the covered information, which is the next
	    		 // operation should be determined.
	    		 // analyzeAndDriver();
	    		 System.out.println("received str: " + sb.toString());
	    		 //break;
	    	 }
		}  
	    
	    // for testing only
	    
	    // driver analysis	    
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		AutomatedProxy ap = new AutomatedProxy();
		ap.receiveInfo();
	}

}
