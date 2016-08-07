     		var webSocket = 
      		new WebSocket('ws://' + window.document.location.host + '/BounceEndpoint');
      		
    	webSocket.onerror = function(event) {
      		onError(event)
    	};
    	webSocket.onopen = function(event) {
    	};
    	webSocket.onmessage = function(event) {
    	};    
    	function onMessage(event) {
    		var eventData = event.data;
    		if(eventData.indexOf('/BOUNCECOUNT') == 0){
    			//trim the /BOUNCECOUNT string from the message
         		var bounceArea = document.getElementById('bouncecount')
          		bounceArea.innerHTML = event.data.substr(13) + " bounces";
    		} else {
     		var messagesArea = document.getElementById('messages')
      		messagesArea.innerHTML +=  eventData+'\n';
      		messagesArea.scrollTop = messagesArea.scrollHeight;
   		}
    	}
    	
    	function onError(event) {
      		alert("An error has occurred. More info:\n"+event.data);
    	}
    	function send() {
      		var message = document.getElementById('inputmessage').value;
      		webSocket.send(message);
      		document.getElementById('inputmessage').value = "";
      		return false;
    	}
    
 