package processing.testsimulator;

import networking.ICommunicator;
import networking.INotificationHandler;

import java.util.HashMap;

import infrastructure.validation.logger.*;

public class NetworkCommunicator implements ICommunicator{

	protected NetworkCommunicator() {};
	
	ILogger logger = LoggerFactory.getLoggerInstance();
	
	private HashMap<String, INotificationHandler> handlerMap = new HashMap < > ();
	
	@Override
	public void start() {
		logger.log(ModuleID.PROCESSING, LogLevel.SUCCESS, "Successfully call start API of the network.");
	}

	@Override
	public void stop() {
		logger.log(ModuleID.PROCESSING, LogLevel.SUCCESS, "Successfully call stop API of network.");
	}

	@Override
	public void send(String destination, String message, String identifier) {
		
		INotificationHandler handler = handlerMap.get(identifier);
		handler.onMessageReceived(message);
		logger.log(ModuleID.PROCESSING, LogLevel.INFO, "Received the sent message");
	}

	@Override
	public void subscribeForNotifications(String identifier, INotificationHandler handler) {
		
		if (handler == null) {
			logger.log(ModuleID.PROCESSING, LogLevel.WARNING, "Handler is invalid.");
		}
		
		else if(identifier=="" || identifier==null) {
			logger.log(ModuleID.PROCESSING, LogLevel.WARNING,"Provide a valid identifier");
		}
		else {
		
			if (handlerMap.containsKey(identifier)) {
				logger.log(ModuleID.PROCESSING, LogLevel.INFO,"Already have the specified identifier");
			}
			else {
				logger.log(ModuleID.PROCESSING, LogLevel.SUCCESS, "Subscribed network for any notifications.");
			}
			handlerMap.put(identifier,handler);
			
		}

	}
}
