package client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

import server.ServerRequestManager;

public class Client implements ClientRequestManager {
	Random randInt;
	ServerRequestManager remoteServer;
	ClientRequestManager remoteClient;
	Registry registry;
	

  public static String getPID() {
	    String processName =
	      java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
	    return processName;
	  }
	
	@Override
	public void receiveResource() throws RemoteException {
		System.out.println("Obtained resource. " + getPID());
	    try {
	    	Thread.sleep(randInt.nextInt(20000));
	    } catch (InterruptedException e) {

	    }
	    System.out.println("Resource use completed! " + getPID());
	    remoteServer.freeResource();
	}
	
	public Client(String serverAddr){
		
		
		randInt = new Random();
		
		try {
			registry = LocateRegistry.getRegistry(serverAddr, 3232);
			remoteServer = (ServerRequestManager) registry.lookup("rmiServer");
	        remoteClient = (ClientRequestManager) UnicastRemoteObject.exportObject(this, 0);

			
		} catch (RemoteException | NotBoundException e) {
			System.out.println("Couldn't find server on registry table.");
			e.printStackTrace();
		}
		
	}
	
	private void run(){
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			
			}
			
			System.out.println("Resource requested. " + getPID());
			try {
				remoteServer.requestResource(remoteClient);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	static public void main(String[] args) {
		String serverAddr = "localhost";
		if (args.length > 0) {
			serverAddr = args[0];
		}
		Client client = new Client(serverAddr);
		client.run();
	}

	@Override
	public String getClientInfo() throws RemoteException {
		String hostName;
		try {
			hostName = (InetAddress.getLocalHost()).toString();
		} catch (UnknownHostException e) {
			hostName = "unknownHost";
		} 
		return hostName + " # " + getPID();
	}

}
