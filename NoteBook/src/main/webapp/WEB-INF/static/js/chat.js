let global_code_number = 0;

function convertToHtml(message) {
    var converter = new showdown.Converter();
   let convertedHtml  = converter.makeHtml(message);
   return convertedHtml;
}




function convertMessage(message){



    if(!message.includes("```")) return message;


    let new_message = '';

    while(message.includes("```")){
        
        let start_index = message.indexOf("```");

        let last_index = message.substring(start_index+3,message.length).indexOf("```");

        if(last_index == -1){
            new_message+=message;
            return new_message;
        }
        else{



            last_index +=  start_index + 3;

            let temp = message.substring(start_index+3,last_index);
            
            if( /^(java|python|cpp)[^a-zA-Z]+/.test(temp) )

            {

                let language = null;
                if( /^java[^a-zA-Z]+/.test(temp)) language = "java";
                if( /^cpp[^a-zA-Z]+/.test(temp)) language = "cpp";
                if( /^python[^a-zA-Z]+/.test(temp)) language = "python";

            new_message+=message.substring(0,start_index)+"<br>";

            let code_message =  message.substring(start_index+3,last_index);

            code_message = `
              <pre class="language-${language} ${global_code_number}">
<code class="${language}">
 ${language=='java'?code_message.substring(4,code_message.length):""}
 ${language=='python'?code_message.substring(6,code_message.length):""}
 ${language=='cpp'?code_message.substring(3,code_message.length):""}
</code>
               </pre>`+
               '<button class="btn btn-primary run-button i'+global_code_number+' "  onclick="runCode(event,'+global_code_number+')">Run</button>' +
             '<button class="btn btn-primary run-button input-button" onclick="showInput(event,'+global_code_number+')">Input</button>' +
             '<div class="input-block i' +global_code_number+'">' +
             '  <label class="code-labels" for="inputValue">Input:</label>' +
             '  <textarea rows = "4" style="background-color: black; color:white;font-weight: bold;" class="form-control"></textarea>' +
             '</div>' +
             '<div  style="text-align:left;" class="output-block i'+ global_code_number +'" id="outputBlock">' +
             '  <label class="code-labels" for="output">Output:</label><br>' +
             '  <pre style="overflow:visible;"</pre>' +
             '</div>';
          
            new_message+=code_message+"<br>";

            global_code_number++;


            }

            else {

                new_message+=message.substring(0,last_index+3);
            }

            message = message.substring(last_index+3,message.length);

        }


    }

   

    return new_message;


}

function addStyle(){

    let styleString = '<style>' +
                '  .code-block {' +
                '    background-color: #f1f1f1;' +
                '    padding: 10px;' +
                '  }' +
                '  .input-block,' +
                '  .output-block {' +
                '    display: none;' +
                '    margin-top: 2%;' +
                '  }' +
                '  .input-block.show,' +
                '  .output-block.show {' +
                '    display: block;' +
          
                '  }' +
  
                '  .output-block {' +
                '    background-color: black;' +
                '    color: white;' +
                '    padding: 10px;' +
                '    font-weight: bold;' +
                '    margin-top: 10px;' +
                '    overflow-x: scroll;' +
                '    overflow-y: scroll;' +
                '    height: 45%;' +
                '  }' +
                '  .run-button {' +
                '    margin-top: 10px;' +
                '  }' +
                '  .input-button {' +
                '    margin-left: 10%;' +
                '  }' +
                '  .code-labels {' +
                '    font-weight: bold;' +
                '  }' +
                '</style>';
  
   document.head.insertAdjacentHTML("beforeend",styleString);
  
  }
  
  let languages = ["cpp","java","python"];
    
  
    function runCode(event,index) {
      event.preventDefault();
      let csrfToken = document.getElementById("csrfToken").value;
      var outputBlock = document.querySelector(".output-block.i" + index ).lastElementChild;
      var outputBlockParent = document.querySelector(".output-block.i" + index );
      var inputBlock = document.querySelector(".input-block.i" + index ).lastElementChild;
      let pre = document.querySelector(`pre[class*="language-"][class~="${index}"]`);
      let code_field = pre.firstElementChild;
      if(code_field!= null){
        let code = code_field.innerText;
        
        let input = inputBlock.value;
        for(let lang of code_field.classList){
          if(lang == languages[0] || lang == languages[1] || lang == languages[2]){
            let data = {
              code:code,
              lang:lang,
              input:input
            }
            let meta = {
              method: "post",
              headers:{'Content-Type': 'application/json',
              'X-CSRF-Token': csrfToken
  
            }
              ,
            body: JSON.stringify(data)
            };
            console.log(meta);
            let request = fetch("/api/execution/exec",meta);
            request.then((response)=>{
              
              if(response.ok) return response.json();
              else{
                throw new CustomError(null,response.json());
              }
  
            })
            .then((result)=>{
              console.log(result);
              outputBlockParent.classList.add("show");
              outputBlock.style.color="white";
              outputBlock.innerText = result.output;
              
            })
            .catch((error)=>{
  
                error.data.then((error_detail)=>{
                  outputBlockParent.classList.add("show");
                  outputBlock.style.color="red";
                  outputBlock.innerText = error_detail.exception;
                });
            });
  
  
          }
        }
      }
    }
  
  
  function showInput(event,index) {
    event.preventDefault();
    var inputBlock = document.querySelector(".input-block.i"+index);
    if (inputBlock.classList.contains("show")) {
      inputBlock.classList.remove("show");
    } else {
      inputBlock.classList.add("show");
    }
  }
  




function chatClicked(event , email,chats){

    let sendMessageButton = document.getElementById("send_button");
    sendMessageButton.setAttribute("onclick",`sendMessage(event,"${email}")`);
    let target = event.target;
    target.style = "";
    let chat_of_clicked_user = chats[email];
    let focused_user = document.getElementById("focused_user");
    focused_user.innerText = email;
    
    let chat = "";

    let container = document.getElementById("chat_data");
    
    for(let single_chat  of  chat_of_clicked_user)
    {

        let time_side= single_chat["from"] == email ? "" : "text-right";

        let message_side = single_chat["from"] == email ? "" : "float-right";

        let style = `width:70%;overflow:wrap;text-align:${single_chat["from"]==email?"left":"right"}`;


        let message = single_chat['message'];

        if(message == null){
            message = document.getElementById(`messageId_${single_chat['message_id']}`).innerText;
            message = message.replace(/>/g,"&gt;");
            message = message.replace(/</g,"&lt;");
           
        }

        message = convertMessage(message);

        chat += `

                            <li class="clearfix">
                                <div class="message-data ${time_side}">
                                    <span class="message-data-time">${single_chat['timestamp']}</span>
                                </div>
                                <div class="message other-message ${message_side}" style="${style}">${message}</div>
                            </li>

        `;
    }

    container.innerHTML = chat;


    Prism.highlightAll();

}

function sendMessage(event,receiver){

        let sender = actualUser;
        let messageField = document.getElementsByClassName("sendMessageInput")[0];
        send(sender,receiver,messageField.value);
        messageField.value = "";
        messageField.focus();
        const last = document.querySelector("#chat_data").lastElementChild;
        last.scrollIntoView(); 
        Prism.highlightAll();
    

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
        let actual_message = JSON.parse(message.body);
        let sender = actual_message['sender'];
        let m = actual_message['message'];
        m = m.replace(/>/g,"&gt;");
        m = m.replace(/</g,"&lt;");
        let timestamp = actual_message['timestamp'];
        if(chats[sender] == null) chats[sender] = [];
        if(user_id.has(sender)){

            
            chats[sender].push({'message':m,'timestamp':timestamp,'from':sender,'to':actualUser});
            if(focused_user == sender){
                
                chat_data.insertAdjacentHTML("beforeend",` <li class="clearfix">
                <div class="message-data ">
                    <span class="message-data-time">${timestamp}</span>
                </div>
                <div class="message other-message" style="width:70%;overflow:wrap;text-align:left;">${convertMessage(m)}</div>
            </li>`);


            }
            else{

                let list = document.getElementsByClassName("user_id");
                let todelete = null;
                for( let people of list){

                    let name = people.firstElementChild.firstElementChild;

                    if(name.innerText.trim() == sender){
                        todelete = people; 
                        break;
                    }
                }
            todelete.remove();
               

                search_user.insertAdjacentHTML("afterend",`<li class="clearfix user_id" onclick="chatClicked(event,'${sender}',chats)">
                            <div class="about">
                                <div class="name " style="font-weight:bold;">${sender}</div>
                            </div>
                        </li>`);


            }
        }   
        else{

            user_id.add(sender);

            search_user.insertAdjacentHTML("afterend",`<li class="clearfix user_id" onclick="chatClicked(event,'${sender}',chats)">
                            <div class="about">
                                <div class="name " style="font-weight:bold;">${sender}</div>
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
        const last = document.querySelector("#chat_data").lastElementChild;
        last.scrollIntoView();
        Prism.highlightAll();
    });

}
let user_id = null;

function send(sender,receiver,message){


    
    if(stompClient == null){
        connect();
    }


    if(receiver == sender) return;

    message = message.replace(/>/g,"&gt;");

    message = message.replace(/</g,"&lt;");

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
        if(focused_user.innerText.trim() == receiver){
            container.insertAdjacentHTML("beforeend",` <li class="clearfix">
                <div class="message-data text-right">
                <span class="message-data-time">${timestamp}</span>
               
                </div>
                <div class="message other-message float-right" style=" width:70%;overflow:wrap; text-align:right;">${convertMessage(message)}</div>
               
            </li>`);
        }

}

// window.onresize = function(){
//     if(window.innerWidth<=766){
//         document.getElementById("pre_button").style.display="block";
//     }
//     else{
//         document.getElementById("pre_button").style.display="none";
//         let people_list = document.getElementsByClassName("first_segment")[0];
//         people_list.style.zIndex = -10;

//     }
// }

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

            let name = people.firstElementChild.firstElementChild;

            if(name.innerText.trim() == username){
                todelete = people; 
                break;
            }
        }
        todelete.remove();

    }
    else{
        
        user_id.add(username);
    }
    search_user.insertAdjacentHTML("afterend",`<li class="clearfix user_id" onclick="chatClicked(event,${username},chats)">
                            <div class="about">
                                <div class="name">${username}</div>
                            </div>
                        </li>`);
    focused_user.innerText = username;
    container.innerText = "";
    let sendMessageButton = document.getElementById("send_button");
    sendMessageButton.setAttribute("onclick",`sendMessage(event,"${username}")`);

}



function searchUser(){

    let username = document.getElementById("usernameInput").value;

    if(username!=null){



        if(username == actualUser){
            toast("It is you");
            return;
        }


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

// function pressedBackButton(){


//     let people_list = document.getElementsByClassName("first_segment")[0];
//     people_list.style.display="block";
//     document.getElementsByClassName("second_segment")[0].style.display = "none";


// }


window.onload = function (){
    addStyle();
    initializeUserIds();
    connect();
}