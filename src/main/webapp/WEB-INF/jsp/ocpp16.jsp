<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<meta charset="UTF-8">
	<title>OCPP1.6</title>
	<style>
		*{
			margin:0;
			padding:0;
		}
		.container{
			width: 1000px;
			margin: 0 auto;
			padding: 25px
		}
		.container h1{
			text-align: left;
			padding: 5px 5px 5px 15px;
			color: #FFBB00;
			border-left: 3px solid #FFBB00;
			margin-bottom: 20px;
		}
		.message{
			background-color: #000;
			width: 1000px;
			height: 500px;
			overflow: auto;
		}
		.message p{
			color: #fff;
			text-align: left;
		}
		input{
			width: 330px;
			height: 25px;
		}
	</style>
</head>

<script type="text/javascript">

	var ws;
	var connected = false;
	var timerId;
	var meterValue = 0;
	var sendedStartTransactionMessage = 0;
	var lastTransactionId = 0;

	function wsEvent() {
		ws.onopen = function(data){
			connected = true;
			$("#id_message").append("<p>server connected</p>");
		}
		
		ws.onmessage = function(data) {

			var msg = data.data;
			if(msg != null && msg.trim() != ''){
				$("#id_message").append("<p>recv-> " + msg + "</p>");
				if (sendedStartTransactionMessage == 1) {
					sendedStartTransactionMessage = 0;

					var spos = msg.lastIndexOf(':');
					var epos = msg.lastIndexOf('}');
					
					lastTransactionId = msg.substring(spos+1, epos);
				}
			}
		}

	}
		
	function connectServer() {

		if (connected == true) {
			alert('서버에 연결되어 있습니다.');
			return;
		}

		$("#id_message").append("<p>server connecting...</p>");
		
		ws = new WebSocket("ws://ocpp16.gresystem.co.kr/webServices/ocpp/W00001", "ocpp1.6");
		wsEvent();
	}

	function sendMessage(msg) {
		if (connected == false) {
			alert('서버에 연결되지 않았습니다.');
			return;
		}

		$("#id_message").append("<p>send-> " + msg + "</p>");
		
		ws.send(msg);
	}
	
	function sendBootNotification() {
		sendMessage('[2,"1000","BootNotification",{"chargePointModel":"ev100","chargePointVendor":"aprotech"}]');
	}

	function sendStatusNotificationAvailable() {
		sendMessage('[2,"1001","StatusNotification",{"connectorId":1,"errorCode":"NoError","status":"Available"}]');
	}

	function sendAuthorize() {
		sendMessage('[2,"1002","Authorize",{"idTag":"W00000000000000001"}]');
	}

	function sendStatusNotificationPreparing() {
		sendMessage('[2,"1003","StatusNotification",{"connectorId":1,"errorCode":"NoError","status":"Preparing"}]');
	}

	function sendStartTransaction() {
		var currentTimeString = getCurrentTime();
		
		meterValue = 0.3252
		sendedStartTransactionMessage = 1;
		sendMessage('[2,"1004","StartTransaction",{"connectorId":1,"idTag":"W00000000000000001","meterStart":' + meterValue + ',"timestamp":"' + currentTimeString + '"}]');
	}

	function sendStatusNotificationCharging() {
		sendMessage('[2,"1005","StatusNotification",{"connectorId":1,"errorCode":"NoError","status":"Charging"}]');
	}

	function sendMeterValueStart() {
		timerId = setInterval(sendMeterValue, 1000);
	}

	function sendMeterValue() {
		var currentTimeString = getCurrentTime();

		meterValue = meterValue + 1.2538;
		
		sendMessage('[2,"1007","MeterValue",{"connectorId":1,"meterValue":[{"sampledValue":[{"context":"Sample_Periodic","format":"Raw","unit":"Wh","value":"' + meterValue + '"}, {"context":"Sample_Periodic","format":"Raw","unit":"Wh","value":"' + meterValue + '"}],"timestamp":"' + currentTimeString + '"}],"transactionId":0}]');
	}
		
	function sendMeterValueStop() {
		clearTimeout(timerId);
	}

	function sendStatusNotificationFinishing() {
		sendMessage('[2,"1008","StatusNotification",{"connectorId":1,"errorCode":"NoError","status":"Finishing"}]');
	}

	function sendStopTransaction() {
		var currentTimeString = getCurrentTime();
		
		sendMessage('[2,"1009","StopTransaction",{"idTag":"W00000000000000001","meterStop":' + meterValue + ',"reason":"Local","timestamp":"' + currentTimeString + '","transactionId":' + lastTransactionId + '}]');
	}

	function sendUvCardRegStatus(status) {
		sendMessage('[2,"1009","DataTransfer",{"vendorId":"UNIEV","messageId":"uvCardRegStatus","data":{"status":"' + status + '", "memberId":"craypas"}}]');
	}

	function sendUvGetIdTagProfile() {
		sendMessage('[2,"1009","DataTransfer",{"vendorId":"GRE","messageId":"uvGetIdTagProfile","data":{"idTag":"W00000000000000001"}}]');
	}
	
	function sendUvCardReg() {	
		sendMessage('[2,"1009","DataTransfer",{"vendorId":"UNIEV","messageId":"uvCardReg","data":{"token":"W00000000000000001", "memberId":"craypas"}}]');
	}

	function sendUvStartCardRegModeConf() {	
		sendMessage('[3,"1009",{"status":"Rejected"}]');
	}
		
	function sendUvStartCardRegMode(mid) {

		var machine = $('#id_machine').val();
		machine = machine.trim();
		
		if (machine.length < 1) {
			alert('단말기 아이디를 입력하세요.');
			
			return;
		}
				
		var dataValue = {
				memberId: mid,
				machineNum: machine
            };
            
	    $.ajax({
	        type: "post",
	        url: "http://ocpp16.gresystem.co.kr/evm/start-card_regist_mode",
	        contentType: "application/json",
	        data: JSON.stringify(dataValue),
	        dataType: 'json',
	        async: false,
	        success: function (data) {

				var jsonData = JSON.parse(JSON.stringify(data));
				if (jsonData.result == "success")
				{
					alert('충전기에 카드 등록 모드 진입 메시지를 전송했습니다.');
				}
				else if (jsonData.result == "session not found")
				{
					alert('충전기가 서버에 연결되어있지 않습니다.');
				}					
				else
				{
				    alert('처리중 에러발생.(' + jsonData.result + ')');
				}
	        },
	        error: function (xhr, status, error) {
	            var errorMessage = $.parseJSON(xhr.responseText);
	            alert(errorMessage.Message);
	        }
	    });			
	}

	function sendUvStopCardReqMode(mid) {

		var machine = $('#id_machine').val();
		machine = machine.trim();
		
		if (machine.length < 1) {
			alert('단말기 아이디를 입력하세요.');
			
			return;
		}
				
		var dataValue = {
				memberId: mid,
				machineNum: machine
            };
            
	    $.ajax({
	        type: "post",
	        url: "http://ocpp16.gresystem.co.kr/evm/stop-card_regist_mode",
	        contentType: "application/json",
	        data: JSON.stringify(dataValue),
	        dataType: 'json',
	        async: false,
	        success: function (data) {

				var jsonData = JSON.parse(JSON.stringify(data));
				if (jsonData.result == "success")
				{
					alert('충전기에 카드 등록 모드 해제 메시지를 전송했습니다.');
				}
				else if (jsonData.result == "session not found")
				{
					alert('충전기가 서버에 연결되어있지 않습니다.');
				}					
				else
				{
				    alert('처리중 에러발생.(' + jsonData.result + ')');
				}
	        },
	        error: function (xhr, status, error) {
	            var errorMessage = $.parseJSON(xhr.responseText);
	            alert(errorMessage.Message);
	        }
	    });			
	}
	
	function closeServer() {
		if (connected == false) {
			alert('서버에 연결되어있지 않습니다.');
			return;
		}

		ws.close();
		connected = false;
		
		$("#id_message").append("<p>server disconnected</p>");
	}
	
	function getCurrentTime() {
		var currentTime = new Date()
		var currentTimeString = currentTime.getFullYear() + "-" + ("0" + (currentTime.getMonth() + 1)).slice(-2) + "-" + ("0" + currentTime.getDate()).slice(-2) 
								+ "T" 
								+ ("0" + currentTime.getHours()).slice(-2) + ":" + ("0" + currentTime.getMinutes()).slice(-2) + ":" + ("0" + currentTime.getSeconds()).slice(-2)
								+ "." + ("00" + currentTime.getMilliseconds()).slice(-3)
								+ "Z";		
		return currentTimeString; 
	}
		
</script>
<body>
	<div id="container" class="container">
		<h1>OCPP1.6 Message Test</h1>

		<table class="inputTable">
			<tr>
				<td><button onclick="connectServer()">서버 연결</button>&nbsp;&nbsp;<button onclick="closeServer()">연결 종료</button></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>표준 메시지</td>
			</tr>						
			<tr>
				<td><button onclick="sendBootNotification()">BootNotification Send</button></td>
			</tr>			
			<tr>
				<td><button onclick="sendStatusNotificationAvailable()">StatusNotification(Available) Send</button></td>
			</tr>
			<tr>
				<td><button onclick="sendAuthorize()">Authorize Send</button></td>
			</tr>
			<tr>
				<td><button onclick="sendStatusNotificationPreparing()">StatusNotification(Preparing) Send</button></td>
			</tr>			
			<tr>
				<td><button onclick="sendStartTransaction()">StartTransaction Send</button></td>
			</tr>
			<tr>
				<td><button onclick="sendStatusNotificationCharging()">StatusNotification(Charging) Send</button></td>
			</tr>			
			<tr>
				<td><button onclick="sendMeterValueStart()">MeterValue Send Start</button></td>
			</tr>
			<tr>
				<td><button onclick="sendMeterValueStop()">MeterValue Send End</button></td>
			</tr>			
			<tr>
				<td><button onclick="sendStatusNotificationFinishing()">StatusNotification(Finishing) Send</button></td>
			</tr>			
			<tr>
				<td><button onclick="sendStopTransaction()">StopTransaction Send</button></td>
			</tr>			
			<tr>
				<td><button onclick="sendStatusNotificationAvailable()">StatusNotification(Available) Send</button></td>
			</tr>		
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>커스텀 메시지</td>
			</tr>	
			<tr>
				<td><button onclick="sendUvCardRegStatus('CardAuthMode')">uvCardRegStatus(CardAuthMode)</button></td>
			</tr>
			<tr>
				<td><button onclick="sendUvCardRegStatus('Rejected')">uvCardRegStatus(Rejected)</button></td>
			</tr>			
			<tr>
				<td><button onclick="sendUvCardReg()">uvCardReg</button></td>
			</tr>
			<tr>
				<td><button onclick="sendUvGetIdTagProfile()">uvGetIdTagProfile</button></td>
			</tr>			
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>사용자 사이트 송신 메시지</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>				
			<tr>
				<td>단말기 아이디 : <input type="text" id='id_machine'></td>
			</tr>			
			<tr>
				<td><button onclick="sendUvStartCardRegMode('craypas')">uvStartCardRegMode Send</button></td>
			</tr>
			<tr>
				<td><button onclick="sendUvStartCardRegModeConf()">uvStartCardRegModeConf Send</button></td>
			</tr>
			<tr>
				<td><button onclick="sendUvStopCardReqMode('craypas')">uvStopCardReqMode Send</button></td>
			</tr>						
													
		</table>
		</br></br>
		<div id="id_message" class="message">
		</div>
	</div>
</body>
</html>