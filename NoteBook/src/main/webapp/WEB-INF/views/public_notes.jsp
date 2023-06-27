<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/public_notes.css">
    <title>Public Notes</title>

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
              <a class="nav-link" href="#">Chat</a>
            </li>
            <c:if test="${login}">
          <li class="nav-item my_li">
            <a class="nav-link" style="cursor:pointer" onclick="makePostRequest('/logout','${_csrf.token}')">Log Out</a>
          </li>
          </c:if>
          <c:if test="${!login}">
            <li class="nav-item my_li">
              <a class="nav-link" href="/user/login">Log In</a>
            </li>
            </c:if>
            <c:if test="${!login}">
            <li class="nav-item my_li">
              <a class="nav-link" href="/user/register">Register</a>
            </li>
            </c:if>
            
          </ul>
        </div>
      </nav>

    <div class="container-fluid mt-5 notes-container">
        <div class="row">
            
            <div class="col-md-4 offset-md-6">


                <form method="get" action="#">
                <div class="input-group mb-3 searchbar">
                    
                    <input style="font-weight: bold;" type="text" class="form-control" placeholder="Search your notes" aria-label="Search public notes" aria-describedby="button-addon2" id="notes_search">
                    <div class="input-group-append" style="margin-left: 1%;">
                      <button class="btn btn-outline-success" type="button" onclick = "searchNotes()" id="button-addon2">Search</button>
                    </div>
                  </div>
                  </form>

            </div>
        </div>
        <div class="row">
            <div class="col">
              <c:forEach var="note" items="${notes}">
                <div class="card card-spacing notes-card">
                    <div class="card-body">
                        <h5 class="card-title">${note.name}</h5>
                        <p class="card-text visibility_${note.id}"><span>Visibility:</span> ${note.visibility}</p>
                        <p class="card-text"><span>Author:</span> ${note.user.name}</p>
                        <hr>
                        <div class="card-buttons">
        
                            <a href="/notes/view/${note.id}" class="btn btn-primary">Read</a>                        </div>
                        <p class="card-timestamp"><span>Time Stamp:</span> ${note.timestamp}</p>
    
                    </div>
    
                </div>
                </c:forEach>
            </div>
        </div>
        

        <input type="hidden" id="current_page" value="1">
        <input type="hidden" id="start_page" value="1">
        <input type="hidden" id="keyword" value="${keyword}">

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
    


  <script src="/static/js/public_notes.js"></script>
  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.min.js"></script>
</body>

</html>
