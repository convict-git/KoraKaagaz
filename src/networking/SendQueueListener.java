package networking;
import networking.queueManagement.*;
import networking.utility.*;
public class SendQueueListener implements Runnable {
    public String [] splitAddress(String destination){
        return destination.split(":");
    }
    public void run(){
        
        while(LanCommunicator.isRunning){
            OutgoingPacket out = LanCommunicator.SendQueue.front();
            String [] destination = this.splitAddress(out.destination);
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
            
        }
    }
}
