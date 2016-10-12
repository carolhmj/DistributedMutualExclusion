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

	private ArrayList<ClientRequestManager> resourceRequests;
	String address;
	Registry registry;
	
	@Override
	public void requestResource(Remote requester) throws RemoteException {
		
		if (requester instanceof ClientRequestManager){
			ClientRequestManager clientRequester = (ClientRequestManager)requester;
			if (resourceRequests.isEmpty()) {
				clientRequester.receiveResource();
			} else {
				resourceRequests.add(clientRequester);
			}
		}
	}

	@Override
	public void freeResource() throws RemoteException{
		resourceRequests.remove(0);
		if (!resourceRequests.isEmpty()) {
			resourceRequests.get(0).receiveResource();
		}
	}
	
	public Server() throws RemoteException {
		try
        {
            address = (InetAddress.getLocalHost()).toString();
        }
        catch (Exception e)
        {
            System.out.println("can't get inet address.");
        }
        int port = 3232;
        System.out.println("this address=" + address + ",port=" + port);
        try
        {
            ServerRequestManager stub = (ServerRequestManager)UnicastRemoteObject.exportObject(this, 0);
            registry = LocateRegistry.createRegistry(port);
            //System.setProperty("java.rmi.server.hostname","192.168.25.4");
            registry.rebind("rmiServer", stub);
            resourceRequests = new ArrayList<ClientRequestManager>();
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
        	@SuppressWarnings("unused")
			Server server = new Server();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
	
}
