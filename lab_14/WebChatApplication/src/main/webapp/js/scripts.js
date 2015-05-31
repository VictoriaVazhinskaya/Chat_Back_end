'use strict';

var userNameGlobal = 'Victoria';
var messagesForDeleting = -1;
var numberOfChanging = 0;
var clickedDiv;
var sameUser = false;
var isUpdated = true;

var uniqueId = function() {
	var date = Date.now();
	var random = Math.random() * Math.random();

	return Math.floor(date * random).toString();
};

var singleMessage = function ( messageText){
	return {
        author: userNameGlobal,
        text:  messageText,
        id: uniqueId()
	};
};

var appState = {
	mainUrl: 'chat',
	token : 'TN11EN'
};


function run(){
	setUserName();
	var buttonLogin = document.getElementsByClassName('login')[0];
    var buttonChange = document.getElementsByClassName('change-name')[0];
    var buttonSend = document.getElementsByClassName('send-message')[0];
    var messagesList = document.getElementById('correspondence');
    var deleteButton = document.getElementsByClassName('delete')[0];
    var editButton = document.getElementsByClassName('edit')[0];

 	buttonLogin.addEventListener('click', onLoginButtonClick);
 	buttonChange.addEventListener('click', onChangeButtonClick);
    buttonSend.addEventListener('click', onSendButtonClick);
	messagesList.addEventListener('click', emit);
    deleteButton.addEventListener('click', onDeleteButtonClick);
    editButton.addEventListener('click', onEditButtonClick);


    doPolling();
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

		numberOfChanging++;
		//updateToken(numberOfChanging);
		addNewMessageInternal(msg);
	});
}

function addNewMessageInternal(msg){
	var message = createMessage(msg);
	var messages = document.getElementById('correspondence');
    messages.appendChild(message);
	document.getElementsByClassName('write-message')[0].value = "";
}

function createMessage(msg){
	var itemDiv = document.createElement('div');
	var senderLi = document.createElement('dt');
	var text = document.createTextNode(msg.author);
	itemDiv.classList.add('item');

	itemDiv.setAttribute('id', msg.id);

	senderLi.appendChild(text);
	senderLi.classList.add('user');

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
			sameUser = false;
			document.getElementsByClassName('edit')[0].style.backgroundColor = 'black';
			document.getElementsByClassName('delete')[0].style.backgroundColor = 'black';
        }
		clickedMessage.classList.add("emit-style");
		messagesForDeleting = clickedMessage.id;
			clickedDiv = clickedMessage;

			var userName = clickedMessage.firstChild.firstChild.nodeValue;
			if(userName == userNameGlobal) {
				sameUser = true;
				document.getElementsByClassName('edit')[0].style.backgroundColor = 'red';
				document.getElementsByClassName('delete')[0].style.backgroundColor = 'red';
			}
			else sameUser = false;
	}
	else{
		clickedMessage.classList.remove("emit-style");
		messagesForDeleting = -1;
			sameUser = false;
		//if(messagesForDeleting == null){
        document.getElementsByClassName('edit')[0].style.backgroundColor = 'black';
		document.getElementsByClassName('delete')[0].style.backgroundColor = 'black';
	}
	}

	}

function findId(id){
	var  childElement = document.getElementById(id);
	return childElement;
}

function onDeleteButtonClick(){
    if(messagesForDeleting != -1 && (sameUser == true)){
		deleteMsg(appState.mainUrl + "?id=" +  messagesForDeleting, function(){
			numberOfChanging++;
			deleteMessageInternal(clickedDiv);
		});
}
}

function deleteMessageInternal(childElement){
	var parentElement = document.getElementById('correspondence');
	parentElement.removeChild(childElement);
	messagesForDeleting = -1;
	document.getElementsByClassName('edit')[0].style.backgroundColor = 'black';
	document.getElementsByClassName('delete')[0].style.backgroundColor = 'black';
}


function formToken(number){
	var requestNumber;
	if(isUpdated == true) {
		requestNumber = 10;
	}
	else requestNumber = 11 + 8*number;
	return "TN" + requestNumber + "EN";
}

/*function updateToken(number){
	var token = 11 + 8*number;
	appState.token = 'TN' + token + 'EN';
}*/

function onEditButtonClick(){
	//var size = messagesForDeleting.length;
	if(messagesForDeleting != -1 && sameUser == true){
	var childElement = clickedDiv;
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
	var newMessage = dialogBox.getElementsByTagName('textarea')[0].value;
	if(!newMessage){
		return;
	}

	var messageId = oldMessage.id;

	put(appState.mainUrl, "{" + "\"id\":\"" + messageId + "\", \"text\":\"" + newMessage + "\"}", function(){
		numberOfChanging++;
		editInternal(oldMessage, newMessage);
	});
}

function editInternal(oldMessage, newMessage){

	oldMessage.getElementsByTagName('dd')[0].innerHTML = newMessage;
	if(oldMessage.getElementsByTagName('dialog')[0].open == true)
	oldMessage.getElementsByTagName('dialog')[0].close();
	if(oldMessage.classList.contains("emit-style"))
	oldMessage.classList.remove("emit-style");
	document.getElementsByClassName('edit')[0].style.backgroundColor = 'black';
	document.getElementsByClassName('delete')[0].style.backgroundColor = 'black';

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

function doPolling() {
	function loop() {
		//console.error(numberOfChanging);
		var url = appState.mainUrl + '?token=' + formToken(numberOfChanging);
		//console.error(url);
		get(url, function(responseText) {
			var response = JSON.parse(responseText);

			var method = response.method;
			if(isUpdated == true) {
				appState.token = response.token;
				numberOfChanging = response.requests;

				isUpdated = false;
				recreateAllMessages(response.messages);
			}
			else
			updateHistory(response.messages, method, response.token);
			setServerAvailable();
			setTimeout(loop, 500);
		}, function(error) {
			defaultErrorHandler(error);
			setTimeout(loop, 500);
});
}

loop();
}

function updateHistory(messages, method, token){
	if(method.localeCompare("post") == 0){
		addNewMessageInternal(messages[0]);
		numberOfChanging++;
		appState.token = token;
	}

	if(method.localeCompare("delete") == 0){
		//console.error("LOCAL DELETE: " + messages[0].id);
		deleteMessageInternal(findId(messages[0].id));
		numberOfChanging++;
		appState.token = token;
	}

	if(method.localeCompare("put") == 0){
		console.error("put");
		editInternal(findId(messages[0].id), messages[0].text );
		numberOfChanging++;
		appState.token = token;
	}
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
	xhr.open(method , url, true);

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
		numberOfChanging = 0;
        continueWithError(errMsg);
    };

    xhr.send(data);
}

