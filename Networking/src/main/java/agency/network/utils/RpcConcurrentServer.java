package agency.network.utils;

import agency.network.rpcprotocol.ClientRpcReflectionWorker;
import agency.services.IAgencyServices;

import java.net.Socket;

public class RpcConcurrentServer extends AbsConcurrentServer{
    private IAgencyServices agencyServer;

    public RpcConcurrentServer(int port, IAgencyServices agencyServer) {
        super(port);
        this.agencyServer = agencyServer;
        System.out.println("Agency - RpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        System.out.println("RpcConcurrentServer in -createWorker-");
        ClientRpcReflectionWorker worker = new ClientRpcReflectionWorker(agencyServer,client);
        Thread tw=new Thread(worker);
        return tw;
    }
    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }

}
