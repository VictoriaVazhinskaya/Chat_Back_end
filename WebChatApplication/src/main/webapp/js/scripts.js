'use strict';

var userNameGlobal = 'Victoria';
var messagesForDeleting = -1;
//var messageCounter = 0;

var uniqueId = function() {
	var date = Date.now();
	var random = Math.random() * Math.random();

	return Math.floor(date * random).toString();
};

var singleMessage = function ( messageText){
	return {
        author: userNameGlobal,
        text:  messageText
	};
};

var appState = {
	mainUrl: 'chat',
	correspondenceList:[],
	token : 'TE11EN'
};


function run(){
	var buttonLogin = document.getElementsByClassName('login')[0];
    var buttonChange = document.getElementsByClassName('change-name')[0];
    var buttonSend = document.getElementsByClassName('send-message')[0];
    var messagesList = document.getElementById('correspondence');
   // var deleteButton = document.getElementsByClassName('delete')[0];
   // var editButton = document.getElementsByClassName('edit')[0];

 	buttonLogin.addEventListener('click', onLoginButtonClick);
 	buttonChange.addEventListener('click', onChangeButtonClick);
    buttonSend.addEventListener('click', onSendButtonClick);
   // messagesList.addEventListener('click', emit);
   // deleteButton.addEventListener('click', onDeleteButtonClick);
    //editButton.addEventListener('click', onEditButtonClick);

    setUserName();
    restore(function(){
    	setServerAvailable();
    });
}

function setServerStatus()
{
	document.getElementsByClassName("server-status_unavailable")[0].innerHTML = ""
}

function setUserName(){
	document.getElementsByClassName("current-user")[0].innerHTML = userNameGlobal;
}

function recreateAllMessages(allMessages){
	if(allMessages.length > 0){
	for(var i=0; i<allMessages.length; i++)
		addNewMessageInternal(allMessages[i]);
    }
}	

function onLoginButtonClick(){
	var userName = document.getElementById('name-field');

    if(!userName.value){
		return;
	}
	userNameGlobal = userName.value;
	document.getElementsByClassName("current-user")[0].innerHTML = userName.value;
	userName.value = "";
	
} 

function onChangeButtonClick(){
	var userName = document.getElementById('name-field');

    if(!userName.value){
		return;
	}
	userNameGlobal = userName.value;
	document.getElementsByClassName("current-user")[0].innerHTML = userName.value;
	userName.value = "";
	
} 

function onSendButtonClick(){
	//messageCounter++;
	//var userName = document.getElementsByClassName('current-user')[0];
	var message =  document.getElementsByClassName('write-message')[0];
	var newMessage = singleMessage(message.value);
	if(!message.value){
		return;
	}
	addNewMessage(newMessage);	
}

function addNewMessage(msg)
{
	post(appState.mainUrl, JSON.stringify(msg), function(){
		setServerAvailable();
		addNewMessageInternal(msg);
		document.getElementsByClassName('write-message')[0].value = "";
	});
}

function addNewMessageInternal(msg){
	var message = createMessage(msg);
	var messages = document.getElementById('correspondence');
    var messageList = appState.correspondenceList;
    
    messageList.push(msg);
    messages.appendChild(message);

}

function createMessage(msg){
	var itemDiv = document.createElement('div');
	var senderLi = document.createElement('dt');
	var text = document.createTextNode(msg.author);
	itemDiv.classList.add('item');
	itemDiv.setAttribute('id', uniqueId());

	senderLi.appendChild(text);
	senderLi.setAttribute('id', 'user1');

	var sentLi = document.createElement('dd');
	text = document.createTextNode(msg.text);
	sentLi.appendChild(text);
	sentLi.setAttribute('id', 'message');
	var editBox = document.createElement('dialog');
	editBox.classList.add('edit-message-box');

	var closeDButton = document.createElement('input');
	closeDButton.classList.add('close-dialog');
	closeDButton.setAttribute('type', 'button');
	closeDButton.setAttribute('value', 'x');

	var textForEditting = document.createTextNode(msg.text);

	var editText = document.createElement('textarea');
	editText.classList.add('edit-textarea');
	editText.style.backgroundColor = 'LightSkyBlue';
	editText.appendChild(textForEditting);

	var submitButton = document.createElement('input');
	submitButton.setAttribute('type', 'button');
	submitButton.setAttribute('value', 'send');
	submitButton.classList.add('send-edit-button');

	editBox.appendChild(closeDButton);
	editBox.appendChild(editText);
	editBox.appendChild(submitButton);
	itemDiv.appendChild(senderLi);
	itemDiv.appendChild(sentLi);
	itemDiv.appendChild(editBox);
	return itemDiv;
}

function emit(evtObj){
	if(evtObj.type === 'click' && (evtObj.target.nodeName == 'DD' || evtObj.target.nodeName == 'DT')){
		var clickedMessage = evtObj.target.parentElement;
		if(!clickedMessage.classList.contains("emit-style")){
		if(messagesForDeleting != -1){
		var lastClickedMessage = document.getElementById(messagesForDeleting);
        lastClickedMessage.classList.remove("emit-style");
        }
		clickedMessage.classList.add("emit-style");
		messagesForDeleting = clickedMessage.id;
		document.getElementsByClassName('edit')[0].style.backgroundColor = 'red';
		document.getElementsByClassName('delete')[0].style.backgroundColor = 'red';
	}
	else{
		clickedMessage.classList.remove("emit-style");
		//messagesForDeleting.splice(messagesForDeleting.indexOf(clickedMessage.id), 1);
		messagesForDeleting = -1;
		//if(messagesForDeleting == null){
        document.getElementsByClassName('edit')[0].style.backgroundColor = 'black';
		document.getElementsByClassName('delete')[0].style.backgroundColor = 'black';
	}
	}

	}



function onDeleteButtonClick(){
	//var parentElement = document.getElementById('correspondence');
    /*for(var i=0; i<messagesForDeleting.length; i++){
    	var childElement = document.getElementById(messagesForDeleting[i]);
    	parentElement.removeChild(childElement);
    }*/
    if(messagesForDeleting != -1){
    var messageList = appState.correspondenceList;  
    var parentElement = document.getElementById('correspondence');   	
    	for(var k=0; k<messageList.length; k++){
    		if( messagesForDeleting == messageList[k].id){  
    		//var childElement = document.getElementById(messagesForDeleting[i]); 		
    		deleteMsg(appState.mainUrl + "?id(" + messageList[k].id + ")", function(){
    			setServerAvailable();    			
    			deleteInner(parentElement, messagesForDeleting);
    	        messagesForDeleting = -1; 
    	        messageList.splice(k, 1); 
    	        document.getElementsByClassName('edit')[0].style.backgroundColor = 'black';
		        document.getElementsByClassName('delete')[0].style.backgroundColor = 'black';   			
    		});
    		break;
    	    }
    	}
   // messagesForDeleting.splice(0, messagesForDeleting.length);   	
}
}

function deleteInner(parentElement, id){
	var childElement = document.getElementById(id);
	parentElement.removeChild(childElement);
}

function onEditButtonClick(){
	//var size = messagesForDeleting.length;
	if(messagesForDeleting != -1){
	var childElement = document.getElementById(messagesForDeleting);
	childElement.getElementsByTagName('dialog')[0].show();
	var closeDialogBoxButton = childElement.getElementsByTagName('dialog')[0].getElementsByClassName('close-dialog')[0];
	var sendEditMessageButton = childElement.getElementsByTagName('dialog')[0].getElementsByClassName('send-edit-button')[0];
    var editTextArea = childElement.getElementsByTagName('dialog')[0].getElementsByTagName('textarea')[0];
    closeDialogBoxButton.addEventListener('click', onCloseDButtonClick);
	sendEditMessageButton.addEventListener('click', onEditInsightDBoxButtonClick);
	editTextArea.addEventListener('click', changeEditTextAreaBGColor);
}
}

function onCloseDButtonClick(evtObj){
	var dialogBox = evtObj.target.parentElement;
	var currentMessage = dialogBox.parentElement;
	dialogBox.close();
    currentMessage.classList.remove("emit-style");
	messagesForDeleting = -1;
    document.getElementsByClassName('edit')[0].style.backgroundColor = 'black';
    document.getElementsByClassName('delete')[0].style.backgroundColor = 'black';
	
}

function changeEditTextAreaBGColor(evtObj){
	var dialogBox = evtObj.target.parentElement;
	dialogBox.getElementsByTagName('textarea')[0].style.backgroundColor = "White";
}

function onEditInsightDBoxButtonClick(evtObj){
	
		var dialogBox = evtObj.target.parentElement;
		var oldMessage =  dialogBox.parentElement;
		var newMessage = dialogBox.getElementsByTagName('textarea')[0];
		var messageList = appState.correspondenceList;      
        if(!newMessage.value){
        	return;
        }
        var messageId = oldMessage.id;
        for(var i=0; i<messageList.length; i++){
            if(messageList[i].id == messageId){           	
            	put(appState.mainUrl, "{" + "\"id\":\"" + messageId + "\",\"message\":\"" + newMessage.value + "\"}", function(){
            	setServerAvailable();
            	messageList[i].message = newMessage.value;
            	oldMessage.getElementsByTagName('dd')[0].innerHTML = newMessage.value;
                oldMessage.getElementsByTagName('dialog')[0].close();	
            	});           	
                break;
            }
        }
        
}


function get(url, continueWith, continueWithError) {
	ajax('GET', url, null, continueWith, continueWithError);
}

function post(url, data, continueWith, continueWithError) {
	ajax('POST', url, data, continueWith, continueWithError);	
}

function deleteMsg(url, continueWith, continueWithError) {
	ajax('DELETE', url, null, continueWith, continueWithError);
}

function put(url, data, continueWith, continueWithError) {
	ajax('PUT', url, data, continueWith, continueWithError);	
}


function restore(continueWith) {
	var url = appState.mainUrl + '?token=' + appState.token;

	get(url, function(responseText) {
		console.assert(responseText != null);

		var response = JSON.parse(responseText);

		appState.token = response.token;
		recreateAllMessages(response.messages);

		continueWith && continueWith();
	});
}

function isError(text) {
	if(text == "")
		return false;
	
	try {
		var obj = JSON.parse(text);
	} catch(ex) {
		return true;
	}

	return !!obj.error;
}

function output(value){
	var output = document.getElementById('output');
	output.style.visibility = 'visible';
	output.innerText = JSON.stringify(value, null, 2);
}

function defaultErrorHandler(message) {
	console.error(message);
	output(message);
    setServerUnavailable();
}

function setServerAvailable(){
   var output = document.getElementById('output');
   output.style.visibility = 'hidden'; 
   var serverStatus = document.getElementsByClassName('server-status')[0];
   serverStatus.style.backgroundColor = '#3CB371';
   serverStatus.innerText = 'server: available';
}

function setServerUnavailable(){
   var serverStatus = document.getElementsByClassName('server-status')[0];
   serverStatus.style.backgroundColor = '#FF0000';
   serverStatus.innerText = 'server: unavailable';
}


function ajax(method, url, data, continueWith, continueWithError) {
	var xhr = new XMLHttpRequest();

	continueWithError = continueWithError || defaultErrorHandler;
	xhr.open(method || GET, url, true);

	xhr.onload = function () {
		if (xhr.readyState !== 4)
			return;

		if(xhr.status != 200) {
			continueWithError('Error on the server side, response ' + xhr.status + " status != 200");
			return;
		}

		if(isError(xhr.responseText)) {
			continueWithError('Error on the server side, response ' + xhr.responseText);
			return;
		}

		continueWith(xhr.responseText);
	};    

    xhr.ontimeout = function () {
    	continueWithError('Server timed out !');
    }

    xhr.onerror = function (e) {
    	var errMsg = 'Server connection error !\n'+
    	'\n' +
    	'Check if \n'+
    	'- server is active\n'+
    	'- server sends header "Access-Control-Allow-Origin:*"';

        continueWithError(errMsg);
    };

    xhr.send(data);
}

