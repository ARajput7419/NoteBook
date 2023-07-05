function chatClicked(event , email,chats){

    let sendMessageInputTag = document.getElementsByClassName("sendMessageInput")[0];
    sendMessageInputTag.onkeydown = `sendMessage(event,${email})`;
    let target = event.target;
    target.style = "";
    let chat_of_clicked_user = chats[email];
    let focused_user = document.getElementById("focused_user");
    focused_user.innerText = email;
    
    let chat = "";

    let container = document.getElementById("chat_data");
    
    for(let single_chat  in  chat_of_clicked_user)
    {

        let side= single_chat["from"] == email ? "" : "text-right";


        chat += `

                            <li class="clearfix">
                                <div class="message-data ${side}">
                                    <span class="message-data-time">${single_chat['timestamp']}</span>
                                </div>
                                <div class="message other-message float-right">${single_chat["message"]}</div>
                            </li>

        `;
    }

    container.innerHTML = chat;


}

function sendMessage(event,receiver){

    if(event.keyCode == 13){
        console.log(event);
        let sender = actualUser;
        let messageField = document.getElementsByClassName("sendMessageInput")[0];
        send(sender,receiver,messageField.value);
        messageField.value = "";
    }

}

let stompClient = null;

function connect() {
    let csrfToken = document.getElementById("csrfToken").value;
    var socket = new SockJS('/chat_connection');
    stompClient = Stomp.over(socket);
    stompClient.connect({'X-CSRF-TOKEN': csrfToken}, function (frame) {
        console.log('Connected: ' + frame);
        subscribe(actualUser);
    });
}





function initializeUserIds(){

    if(user_id == null) {

        user_id = new Set();
        
        let list = document.getElementsByClassName("user_id");

        for(let single_user of  list){

            user_id.add(single_user.innerText.trim());

        }

    }


}

function subscribe(username){

    if(stompClient == null) {
        connect();
    }

    stompClient.subscribe(`/chat/user/${username}/private`, function (message) {
        let focused_user = document.getElementById("focused_user").innerText.trim();
        let search_user = document.getElementById("search_user");
        let chat_data = document.getElementById("chat_data");
        let actual_message = message.body;
        let sender = actual_message['sender'];
        let m = actual_message['message'];
        let timestamp = actual_message['timestamp'];
        if(chats[sender] == null) chats[sender] = [];
        if(user_id.has(sender)){

            
            chats[sender].push({'message':m,'timestamp':timestamp,'from':sender,'to':actualUser});
            if(focused_user == sender){
                
                chat_data.insertAdjacentHTML("beforeend",` <li class="clearfix">
                <div class="message-data text-right">
                    <span class="message-data-time">${timestamp}</span>
                </div>
                <div class="message other-message float-right">${message}</div>
            </li>`);


            }
            else{

                let list = document.getElementsByClassName("user_id");
                let todelete = null;
                for( let people of list){
                    if(people.innerText.trim() == sender){
                        todelete = people; 
                        break;
                    }
                }
                todelete.remove();

                search_user.insertAdjacentHTML("afterend",`<li class="clearfix" onclick="chatClicked(event,${sender},chats)">
                            <div class="about">
                                <div class="name user_id" style="font-weight:bold;">${sender}</div>
                            </div>
                        </li>`);


            }
        }   
        else{

            user_id.add(sender);

            search_user.insertAdjacentHTML("afterend",`<li class="clearfix" onclick="chatClicked(event,${sender},chats)">
                            <div class="about">
                                <div class="name user_id" style="font-weight:bold;">${sender}</div>
                            </div>
                        </li>`);
            chats[sender] = [
                {
                    'message':m,
                    'timestamp':timestamp,
                    'from':sender,
                    'to':actualUser
                }
            ]


        }     

    });

}
let user_id = null;

function send(sender,receiver,message){


    
    if(stompClient == null){
        connect();
    }

    let date = new Date();
    
    let timestamp = date.getDate() + "-"+date.getMonth()+"-" + date.getFullYear() + " " + date.getHours()+":"+date.getMinutes();

    stompClient.send("/chat/send", {}, JSON.stringify(
        {   'sender':sender,
            'receiver':receiver,
            'message':message,
            'timestamp':timestamp
        }
        
        ));

        let focused_user = document.getElementById("focused_user");
        let container = document.getElementById("chat_data");
        if(chats[receiver] == null) chats[receiver] = [];
        chats[receiver].push({'message':message,'from':sender,'to':receiver,'timestamp':timestamp});
        if(focused_user == sender){
            container.insertAdjacentHTML("beforeend",` <li class="clearfix">
                <div class="message-data">
                    <span class="message-data-time">${timestamp}</span>
                </div>
                <div class="message other-message float-right">${message}</div>
            </li>`);
        }

}

window.onresize = function(){
    if(window.innerWidth<=766){
        document.getElementById("pre_button").style.display="block";
    }
    else{
        document.getElementById("pre_button").style.display="none";
        let people_list = document.getElementsByClassName("first_segment")[0];
        people_list.style.zIndex = -10;

    }
}

function makePostRequest(url,token){


    let f = document.createElement("form");
    f.action=url;
    f.method="post";
    f.type="hidden";
    let hidden_input = document.createElement("input");
    hidden_input.type="hidden";
    hidden_input.name="_csrf";
    hidden_input.value=token;
    f.appendChild(hidden_input);
    document.body.appendChild(f);
    f.submit();

}


function userExists(username){

    let search_user = document.getElementById("search_user");
    let container = document.getElementById("chat_data");
    let focused_user = document.getElementById("focused_user");
    if(user_id.has(username)){

        
        let list = document.getElementsByClassName("user_id");
        let todelete = null;
        for( let people of list){
            if(people.innerText.trim() == username){
                todelete = people; 
                break;
            }
        }
        todelete.remove();

    }
    else{
        
        user_id.add(username);
    }
    search_user.insertAdjacentHTML("afterend",`<li class="clearfix" onclick="chatClicked(event,${username},chats)">
                            <div class="about">
                                <div class="name user_id" style="font-weight:bold;">${username}</div>
                            </div>
                        </li>`);
    focused_user.innerText = username;
    container.innerText = "";

}



function searchUser(){

    let username = document.getElementById("usernameInput").value;

    if(username!=null){


        let promise = fetch(`/api/user/exists?email=${username}`);
        promise.then((response)=>{
            if(!response.ok){
                toast("User Does Not Exists");
            }
            else {
                userExists(username);
            }
        });

    }
    else{
        toast("Enter Username");
    }

}

function pressedBackButton(){


    let people_list = document.getElementsByClassName("first_segment")[0];
    people_list.style.display="block";
    document.getElementsByClassName("second_segment")[0].style.display = "none";


}


window.onload = function (){
    initializeUserIds();
    connect();
}