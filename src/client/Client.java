package client;

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
	

  public static long getPID() {
	    String processName =
	      java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
	    return Long.parseLong(processName.split("@")[0]);
	  }
	
	@Override
	public void receiveResource() throws RemoteException {
		System.out.println("Obtained resource. " + getPID());
	    try {
	    	Thread.sleep(randInt.nextInt(6000));
	    } catch (InterruptedException e) {

	    }
	    System.out.println("Resource use completed! " + getPID());
	    remoteServer.freeResource();
	}
	
	public Client(){
		
		
		randInt = new Random();

		try {
			registry = LocateRegistry.getRegistry("127.0.1.1",3232);
			remoteServer = (ServerRequestManager) registry.lookup("rmiServer");
	        remoteClient = (ClientRequestManager)UnicastRemoteObject.exportObject(this, 0);

			
		} catch (RemoteException | NotBoundException e) {
			System.out.println("Couldn't find server on registry table.");
			e.printStackTrace();
		}
		
	}
	
	private void run(){
		while(true){
			try {
				System.out.println("Resource requested. " + getPID());

				remoteServer.requestResource(remoteClient);
				Thread.sleep(10000);
			} catch (RemoteException | InterruptedException e) {
				e.printStackTrace();
			}

		}
		
	}
	
	static public void main(String[] args) {

		Client client = new Client();
		client.run();
	}

}
