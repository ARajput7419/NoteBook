function chatClicked(email,chats){


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

function sendMessage(event){

    if(event.keyCode == 13){
        
        // send the message 

        

    }

}