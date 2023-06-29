function chatClicked(email,chats){

    let focused_user = document.getElementById("focused_user");
    focused_user.innerText = email;
    let chat = `

                        <li class="clearfix">
                            <div class="message-data text-right">
                                <span class="message-data-time">${timestamp}</span>
                            </div>
                            <div class="message other-message float-right">${message}</div>
                        </li>

    `;



}