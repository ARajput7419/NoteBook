<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  


<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/my_notes.css">
    <script src="/static/js/toast.js"></script>
    <link rel="stylesheet" href="/static/css/toast.css">
    <title>My Notes</title>

</head>

<body>

    <nav class="navbar navbar-expand-lg">
        <a class="navbar-brand" href="/">NoteBook</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <i class="fas fa-bars"></i> 
  
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav ml-auto">
            <li class="nav-item my_li">
              <a class="nav-link" href="#">Public Notes</a>
            </li>
            <li class="nav-item my_li">
              <a class="nav-link" href="#">Chat</a>
            </li>
            <li class="nav-item my_li">
                <a class="nav-link" href="/notes/create">Create Note</a>
              </li>
            <li class="nav-item my_li">
              <a class="nav-link" href="#">Log Out</a>
            </li>
            
            
          </ul>
        </div>
      </nav>

    <div class="container-fluid mt-5 notes-container">
        <div class="row">
           
        <div class="col-md-2">
            
                <button class="btn btn-primary switch-button">Resources</button>
            
        </div>
            
            <div class="col-md-4 offset-md-6">


                <form method="get" method="/notes/private/search">
                <div class="input-group mb-3 searchbar">
                    
                    <input style="font-weight: bold;" type="text" class="form-control" placeholder="Search your notes" aria-label="Search public notes" aria-describedby="button-addon2">
                    <div class="input-group-append" style="margin-left: 1%;">
                      <button class="btn btn-outline-success" type="button" id="button-addon2">Search</button>
                    </div>
                  </div>
                  </form>

            </div>
        </div>
        <div class="row">
            <div class="col parent">
            <c:forEach var="note" items="${notes}">
            <div class="card card-spacing notes-card">
                <div class="card-body">
                    <h5 class="card-title">${note.name}</h5>
                    <p class="card-text visibility_${note.id}"><span>Visibility:</span> ${note.visibility}</p>
                    <p class="card-text"><span>Author:</span> ${note.user.name}</p>
                    <hr>
                    <div class="card-buttons">
                        <a onclick="deleteNote(`${note.id}')" class="btn btn-danger">Delete</a>
                        <a href="/notes/view/${note.id}" class="btn btn-primary">Read</a>
                        <a onclick="changeVisibility('${note.visibility}',${note.id})" class="btn btn-success vis">Change Visibility</a>
                    </div>
                    <p class="card-timestamp"><span>Time Stamp:</span> ${note.timestamp}</p>

                </div>

            </div>
            </c:forEach>
            </div>
        </div>
        
        <input type="hidden" id="current_page" value="1">
        <input type="hidden" id="start_page" value="1">
        <input type="hidden" id="keyword" value="">
        <input type="hidden" id="resource_current_page" value="1">
        <input type="hidden" id="resource_start_page" value="1">
        <input type="hidden" id="resource_keyword" value="">
        <div class="row">

            <div class="col">

                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-center mt-4">
                        <li class="page-item p0 disabled">
                            <a class="page-link" onclick="pagination(0,'${total_pages_notes}')" tabindex="-1" aria-disabled="true">Previous</a>
                        </li>
                        <c:if test="${total_pages_notes < 1}">
                        <li class="page-item p1 disabled"><a class="page-link pp1" onclick="pagination(1,'${total_pages_notes}')">1</a></li>
                        </c:if>
                        <c:if test="${total_pages_notes>=1}">
                        <li class="page-item p1 active"><a class="page-link pp1" onclick="pagination(1,'${total_pages_notes}')">1</a></li>
                        </c:if>
                        <c:if test="${total_pages_notes<2}">
                            <li class="page-item p2 disabled"><a class="page-link pp2" onclick="pagination(2,'${total_pages_notes}')">2</a></li>
                        </c:if>
                        <c:if test="${total_pages_notes>=2}">
                            <li class="page-item p2 "><a class="page-link pp2" onclick="pagination(2,'${total_pages_notes}')">2</a></li>
                        </c:if>
                        <c:if test="${total_pages_notes<3}">
                            <li class="page-item p3 disabled"><a class="page-link pp3" onclick="pagination(3,'${total_pages_notes}')">3</a></li>
                        </c:if>
                        <c:if test="${total_pages_notes>=3}">
                            <li class="page-item p3 "><a class="page-link pp3" onclick="pagination(3,'${total_pages_notes}')">3</a></li>
                        </c:if>
                        <c:if test="${total_pages_notes<=3}">
                            <li class="page-item p4 disabled">
                                <a class="page-link" onclick="pagination(4,'${total_pages_notes}')">Next</a>
                            </li>    
                    </c:if>
                    <c:if test="${total_pages_notes>3}">
                        <li class="page-item p4">
                            <a class="page-link" onclick="pagination(4,'${total_pages_notes}')">Next</a>
                        </li>    
                </c:if>
                        
                    </ul>
                </nav>

            </div>

        </div>

    </div>





    <div class="container-fluid resource-container mt-5">
      

        <div class="row">
           
            <div class="col-md-2">
                
                    <button class="btn btn-primary notes_button">Notes</button>
                
            </div>
                
                <div class="col-md-4 offset-md-6">
    
                    <form method="get" action="/resources/private/search">
                    <div class="input-group mb-3 searchbar">
                        <input style="font-weight: bold;" type="text" class="form-control" placeholder="Search your resources" aria-label="Search public notes" aria-describedby="button-addon2">
                        <div class="input-group-append" style="margin-left: 1%;">
                          <button class="btn btn-outline-success" type="button" id="button-addon2">Search</button>
                        </div>
                      </div>
                      </form>
    
                </div>
            </div>


        <div class="row">
            <div class="col resource_parent">
            <c:forEach> var="resource" items="${resources}">
            <div class="card card-spacing resource-card">
                <div class="card-body">
                    <h5 class="card-title">${resource.name}</h5>
                    <p class="card-text"><span>Access Link:</span> <a href="${resource.location}">${resource.location}</a></p>
                    <p class="card-text"><span>Author:</span>${resource.user.name}</p>
                    <p class="card-text resource_visibility_${note.id}"><span>Visibility:</span>${resource.visibility}</p>
                    <hr>
                    <div class="card-buttons">
                        <a href="#" class="btn btn-danger">Delete</a>
                        <a onclick="copyTextToClipboard(${resource.location})" class="btn btn-primary">Copy Link</a>
                        <a href="#" class="btn btn-success vis">Change Visibility</a>

                    </div>
                    <p class="card-timestamp"><span>Time stamp</span>${resource.timestamp}</p>
                </div>
            </div>
            </c:forEach>
            </div>
        </div>
        
        <div class="row">

            <div class="col">

                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-center mt-4">
                        <li class="page-item rp0 disabled">
                            <a class="page-link" onclick="resourcePagination(0,'${total_pages_resources}')" tabindex="-1" aria-disabled="true">Previous</a>
                        </li>
                        <c:if test="${total_pages_resources < 1}">
                        <li class="page-item rp1 disabled"><a class="page-link rpp1" onclick="resourcePagination(1,'${total_pages_resources}')">1</a></li>
                        </c:if>
                        <c:if test="${total_pages_resources>=1}">
                        <li class="page-item rp1 active"><a class="page-link rpp1" onclick="resourcePagination(1,'${total_pages_resources}')">1</a></li>
                        </c:if>
                        <c:if test="${total_pages_resources<2}">
                            <li class="page-item rp2 disabled"><a class="page-link rpp2" onclick="resourcePagination(2,'${total_pages_resources}')">2</a></li>
                        </c:if>
                        <c:if test="${total_pages_resources>=2}">
                            <li class="page-item rp2 "><a class="page-link rpp2" onclick="resourcePagination(2,'${total_pages_resources}')">2</a></li>
                        </c:if>
                        <c:if test="${total_pages_resources<3}">
                            <li class="page-item rp3 disabled"><a class="page-link rpp3" onclick="resourcePagination(3,'${total_pages_resources}')">3</a></li>
                        </c:if>
                        <c:if test="${total_pages_resources>=3}">
                            <li class="page-item rp3 "><a class="page-link rpp3" onclick="resourcePagination(3,'${total_pages_resources}')">3</a></li>
                        </c:if>
                        <c:if test="${total_pages_resources<=3}">
                            <li class="page-item rp4 disabled">
                                <a class="page-link" onclick="resourcePagination(4,'${total_pages_resources}')">Next</a>
                            </li>    
                    </c:if>
                    <c:if test="${total_pages_resources>3}">
                        <li class="page-item rp4">
                            <a class="page-link" onclick="resourcePagination(4,'${total_pages_resources}')">Next</a>
                        </li>    
                </c:if>
                        
                    </ul>
                </nav>

            </div>

        </div>

        </div>

        
    </div>


  <script src="/static/js/my_notes.js"></script>
  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.min.js"></script>
</body>
</html>