package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRequestManager extends Remote{
	public void receiveResource() throws RemoteException;
	public String getClientInfo() throws RemoteException;
}
