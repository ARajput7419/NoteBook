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
        let sender = actualUser;
        send(sender,receiver,message);
    }

}

let stompClient = null;

function connect() {
    var socket = new SockJS('/chat_connection');
    stompClient = Stomp.over(socket);
    stompClient.connect({'X-CSRF-TOKEN': ''}, function (frame) {
        console.log('Connected: ' + frame);
    });
}


connect();


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
        initializeUserIds();
        if(user_id.has(sender)){

            chats[sender].push({'message':m,'timestamp':timestamp});
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
                    'timestamp':timestamp
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

    stompClient.send("/chat/send", {}, JSON.stringify(
        {   'sender':sender,
            'receiver':receiver,
            'message':message}
        
        ));

        let focused_user = document.getElementById("focused_user");
        let container = document.getElementById("chat_data");
        chats[receiver].push({});







}

window.onresize = function(){
    if(window.innerWidth<=766){
        document.getElementById("pre_button").style.display="block";
    }
    else{
        document.getElementById("pre_button").style.display="none";
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