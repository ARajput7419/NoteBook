<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<html>
<head>
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<title>Chat</title>
<link rel="stylesheet" href="/static/css/chat.css">
<script>

let chats = [


                    <c:forEach var = "chat" items = "${chats}">

                    [
                        "${chat.key}" : {

                            <c:forEach var="chat_per_person" items="${chat.value}">

                            ${chat_per_person}

                            </c:forEach>

                        }
                    ]  

                    </c:forEach>


                ];

</script>
</head>
<body id="b_ody">
<div class="container">
<div class="row clearfix">
    <div class="col-lg-12">
        <div class="card chat-app">
            <div id="plist" class="people-list">
                
                <ul class="list-unstyled chat-list mt-2 mb-0">


                    <c:forEach var = "chat" items = "${chats}">

                        <li class="clearfix" id="${chats.key}">
                            <div class="about">
                                <div class="name">${chats.key}</div>
                            </div>
                        </li>

                    </c:forEach>
                    
                </ul>
            </div>
            <div class="chat">
                <div class="chat-header clearfix">
                    <div class="row">
                        <div class="col-lg-6">
                            <a href="javascript:void(0);" data-toggle="modal" data-target="#view_info">
                               
                            </a>
                            <div class="chat-about">
                                <h6 class="m-b-0" id="focused_user">Aiden Chavez</h6>
                            </div>
                        </div>
                        <div class="col-lg-1 offset-lg-5">
                           <button class="button" onclick="document.getElementById('messgae_focused').focus()">&#8964;</button>
                        </div>
                    </div>
                </div>
                <div class="chat-history">
                    <ul class="m-b-0">
                        <li class="clearfix">
                            <div class="message-data text-right">
                                <span class="message-data-time">10:10 AM, Today</span>
                                <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="avatar">
                            </div>
                            <div class="message other-message float-right"> Hi Aiden, how are you? How is the project coming along? </div>
                        </li>
                        <li class="clearfix">
                            <div class="message-data">
                                <span class="message-data-time">10:12 AM, Today</span>
                            </div>
                            <div class="message my-message">Are we meeting today?</div>                                    
                        </li>                               
                        <li class="clearfix">
                            <div class="message-data">
                                <span class="message-data-time">10:15 AM, Today</span>
                            </div>
                            <div class="message my-message">Project has been already finished and I have results to show you.</div>
                            </li>
                           
                    </ul>

                   
                    
                </div>

                <div class="chat-message clearfix" >
                    <div class="input-group mb-0">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fa fa-send"></i></span>
                        </div>
                        <input type="text" class="form-control" id="messgae_focused" placeholder="Enter text here...">                                    
                    </div>
                </div>

                
                
            </div>
            
        </div>
    </div>
</div>
</div>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.min.js"></script>
</body>
</html>