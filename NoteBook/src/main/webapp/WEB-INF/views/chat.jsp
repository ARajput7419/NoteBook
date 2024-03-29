<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  



<html>
<head>
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/themes/prism.min.css">
<script src="https://cdn.jsdelivr.net/npm/showdown@2.1.0/dist/showdown.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/prism.min.js"></script>
<title>Chat</title>
<link rel="stylesheet" href="/static/css/chat.css">
<script src="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/components/prism-java.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/components/prism-cpp.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/components/prism-python.min.js"></script>
<link rel="stylesheet" href="/static/css/toast.css">
<script src="/static/js/toast.js"></script>
<script>

let actualUser = '${actualUser}';

let chats = {


                    <c:forEach var = "chat" items = "${chats}">


                    "${chat.key}" : [
                        

                            <c:forEach var="chat_per_person" items="${chat.value}">

                            {

                               
                                "message"    :  null ,
                                "message_id" : ${chat_per_person.id},
                                "timestamp" : "${chat_per_person.timestamp}",
                                "from" : "${chat_per_person.from.email}",
                                "to" : "${chat_per_person.to.email}"

                            } ,

                            </c:forEach>

                       
                    ] ,  

                    </c:forEach>


    };








</script>
<script src="/static/js/chat.js"></script>

</head>
<body id="b_ody">

    <nav class="navbar navbar-expand-lg">
        <a class="navbar-brand" href="/">NoteBook</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <i class="fas fa-bars"></i>
  
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav ml-auto">
            
            <li class="nav-item my_li">
              <a class="nav-link" href="/notes/private">My Notes</a>
            </li>
            
            <li class="nav-item my_li">
              <a class="nav-link" href="/notes/public">Public Notes</a>
            </li>
           
            <li class="nav-item my_li">
              <a class="nav-link" style="cursor:pointer" onclick="makePostRequest('/logout','${_csrf.token}')">Log Out</a>
            </li>
            
         
          </ul>
        </div>
      </nav>

      <br>
<div class="container">
<div class="row clearfix">
    <div class="col-lg-12">
        <div class="card chat-app">
            <input type="hidden" name="_csrf" value="${_csrf.token}" id="csrfToken">
            <div id="plist" class="people-list first_segment">
                
                <ul class="list-unstyled chat-list mt-2 mb-0 interacted_people">


                    
                        <div class="input-group mb-3" id="search_user">
                          <input id="usernameInput" type="text" style="font-weight:bold;" class="form-control" placeholder="Username"  aria-describedby="button-addon2">
                          <div class="input-group-append" style="margin-left: 5%;">
                            <button class="btn btn-outline-success" type="button" id="button-addon2" onclick="searchUser()">Chat</button>
                          </div>
                        </div>
                      

                    <c:forEach var = "chat" items = "${chats}">

                        <li class="clearfix user_id" onclick="chatClicked(event,'${chat.key}',chats)">
                            <div class="about">
                                <div class="name">${chat.key}</div>
                            </div>
                        </li>

                    </c:forEach>
                    
                </ul>
            </div>
            <div class="chat">
                <div class="chat-header clearfix">
                    <div class="row">
                        
                        <div class="col-lg-6 offset-4">
                            <a href="javascript:void(0);" data-toggle="modal" data-target="#view_info">
                               
                            </a>
                            <div class="chat-about">
                                <h6 class="m-b-0" style="font-weight:bold;" id="focused_user"></h6>
                            </div>
                        </div>
                        
                    </div>
                    <div class="row ">
                        <!-- <div class="col-1 " id="pre_button">
                            <a href="javascript:void(0);"  onclick="pressedBackButton()" class="previous round special_button">&#8249;</a>
                         </div> -->
                         <!-- <div class="col-1 offset-9" style="text-align: right;">
                            <a href="javascript:void(0);" class="special_button round previous" onclick="document.getElementById('messgae_focused').focus()">&#8964;</a>
                         </div> -->
                    </div>
                </div>
                <div class="chat-history">

                    <ul class="m-b-0" id="chat_data">
                        
                           
                    </ul>

                   
                    
                </div>

                       
            </div>
            
        </div>
        
    </div>
</div>
</div>
<div class="chat-message clearfix"  style="margin-bottom: 30px;margin-left: 9%; margin-right:9%">
    <div class="input-group mb-0">
        <div class="input-group-prepend">
            <span class="input-group-text"><i class="fa fa-send"></i></span>
        </div>
        <textarea style="height:5%" type="text" class="form-control sendMessageInput"  id="messgae_focused"></textarea> 
                                          
    </div>
    <br>
    <div class="input-group-append">
        <button class="btn btn-outline-success" type="button" id="send_button" onclick="sendMessage(event)">Send</button>
    </div> 
</div>  
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.min.js"></script>
</body>
<c:forEach var = "chat" items = "${chats}">            

<c:forEach var="chat_per_person" items="${chat.value}">

<pre style="display:none;" id="messageId_${chat_per_person.id}">${chat_per_person.message}</pre>

</c:forEach>

</c:forEach>
</html>