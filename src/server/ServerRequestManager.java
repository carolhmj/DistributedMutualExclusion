package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRequestManager extends Remote {
	void requestResource(Remote requester) throws RemoteException;
	void freeResource() throws RemoteException;
}
