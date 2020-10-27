package networking;
import networking.queueManagement.*;
import networking.utility.*;
import java.net.*;
public class SendQueueListener implements Runnable {
    public String [] splitAddress(String destination){
        return destination.split(":");
    }
    public void run(){
        
        while(LanCommunicator.isRunning){
            OutgoingPacket out = LanCommunicator.SendQueue.front();
            String [] dest = this.splitAddress(out.destination);
            String message = out.message;
            String identifier = out.identifier;
            if(identifier == "processing"){
                message = "p" + message;
            }
            else if(identifier == "content"){
                message = "c" + message;
            }
            else{
                continue;
            }
            try{
                String ip = dest[0];
                int port = Integer.parseInt(dest[1]);
                Socket sock = new Socket(ip, port);
                
            }
        }
    }
}
