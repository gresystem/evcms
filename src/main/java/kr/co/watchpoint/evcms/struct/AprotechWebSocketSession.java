package kr.co.watchpoint.evcms.struct;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import kr.co.watchpoint.evcms.socket.handler.SocketHandler;

public class AprotechWebSocketSession 
{
	public enum MachineState
	{
		 CONNECT
		,DISCONNECT
		,BOOTCOMPLETE
		,AUTHORIZE
		,STARTTRANSACTION
		,STOPTRANSACTION
		,METER
		,FIRMWARESTATUSNOTIFICATION
	}
	 
	private WebSocketSession webSocketSession;

	private int transactionId1 = -1;	
	private String idTag1 = "";
	private float lastPower1 = 0;
	

	private int transactionId2 = -1;
	private String idTag2 = "";
	private float lastPower2 = 0;
	
	private MachineState machineState = MachineState.DISCONNECT;
	
	public AprotechWebSocketSession(WebSocketSession webSocketSession)
	{
		this.webSocketSession = webSocketSession;
		this.machineState = MachineState.CONNECT;
	}
	
	public void updateIdTag(int connectId, String idTag)
	{
		if (connectId == 1)
		{
			this.idTag1 = idTag;
		}
		
		if (connectId == 2)
		{
			this.idTag2 = idTag;
		}		
	}	
	
	public String getIdTag(int connectId)
	{
		if (connectId == 1)
		{
			return this.idTag1;
		}
		
		if (connectId == 2)
		{
			return this.idTag2;
		}		
		
		return "unknown";
	}
	
	public void updateTransactionId(int transactionId, int connectId)
	{
		if (connectId == 1)
		{
			this.transactionId1 = transactionId;
		}
		
		if (connectId == 2)
		{
			this.transactionId2 = transactionId;
		}		
	}	
	
	public void updateLastPower(int connectId, float power)
	{
		if (connectId == 1)
		{
			this.lastPower1 = power;
		}
		
		if (connectId == 2)
		{
			this.lastPower2 = power;
		}		
	}

	public float getLastPower(int connectId)
	{
		if (connectId == 1)
		{
			return this.lastPower1;
		}
		
		if (connectId == 2)
		{
			return this.lastPower2;
		}		
		
		return 0;
	}
	
	public int getTransactionId(int connectId)
	{
		if (connectId == 1)
		{
			return this.transactionId1;
		}
		
		if (connectId == 2)
		{
			return this.transactionId2;
		}
		
		return -1;
	}	
	
	public int getConnectId(int transactionId)
	{
		if (this.transactionId1 == transactionId)
		{
			return 1;
		}
		
		if (this.transactionId2 == transactionId)
		{
			return 2;
		}		
		
		return 0;
	}

	public void updateMachineState(MachineState machineState)
	{
		this.machineState = machineState;
	}
	
	public void updateWebSocketSession(WebSocketSession webSocketSession)
	{
		this.webSocketSession = webSocketSession;
	}
	
	public void send(String msg, String uniqueId, String messageId, String data) throws Exception
	{
		SocketHandler.reqMap.put(uniqueId, new ReqMessageInfo(messageId, data));
		webSocketSession.sendMessage(new TextMessage(msg));
	}
}
