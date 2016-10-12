package server;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


import client.ClientRequestManager;


public class Server implements ServerRequestManager {

	private ArrayList<ClientRequestPair> resourceRequests;
	String address;
	Registry registry;
	
	private class ClientRequestPair {
		ClientRequestManager requester;
		String requesterInfo;
		
		public ClientRequestPair(ClientRequestManager requester, String requesterInfo) {
			this.requester = requester;
			this.requesterInfo = requesterInfo;
		}
	}
	
	public String printResourceRequests() {
		String res = "{";
		for (ClientRequestPair clientRequestPair : resourceRequests){
			res = res + "[";
			res = res + clientRequestPair.requesterInfo;
			res = res + "]";
		}
		res = res + "}";
		return res;
	}
	
	@Override
	public void requestResource(Remote requester) throws RemoteException {
		
		if (requester instanceof ClientRequestManager){
			ClientRequestManager clientRequester = (ClientRequestManager)requester;
			String requesterInfo = clientRequester.getClientInfo();
			ClientRequestPair clientRequestPair = new ClientRequestPair(clientRequester, requesterInfo);
			System.out.println("Received request from: " + requesterInfo);
			resourceRequests.add(clientRequestPair);
			
			System.out.println("Request queue:");
			System.out.println(printResourceRequests());
			
			//Processo é o único da fila
			if (resourceRequests.size() == 1) {
				resourceRequests.get(0).requester.receiveResource();
			}
			
		}
	}

	@Override
	public void freeResource() throws RemoteException{
		assert(resourceRequests.isEmpty() == false);
		System.out.println("Received free");
		resourceRequests.remove(0);
		System.out.println(printResourceRequests());
		if (!resourceRequests.isEmpty()) {
			resourceRequests.get(0).requester.receiveResource();
		}
	}
	
	public Server(String ip, int port) throws RemoteException {
		try
        {
            address = (InetAddress.getLocalHost()).toString();
        }
        catch (Exception e)
        {
            System.out.println("can't get inet address.");
        }
        System.out.println("this address=" + address + ",port=" + port);
        try
        {
        	System.setProperty("java.rmi.server.hostname", ip);
            ServerRequestManager stub = (ServerRequestManager)UnicastRemoteObject.exportObject(this, 0);
            registry = LocateRegistry.createRegistry(port);
            registry.rebind("rmiServer", stub);
            resourceRequests = new ArrayList<ClientRequestPair>();
        }
        catch (RemoteException e)
        {
            System.out.println("remote exception" + e);
        }
	}
	
	static public void main(String args[])
    {
        try
        {
        	String ip = (args.length >= 1)? args[0] : "localhost";
        	int port = (args.length >= 2)? Integer.parseInt(args[1]) : 3456;
        	@SuppressWarnings("unused")
        	Server server = new Server(ip, port);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
	
}
