<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<html>
  <head>
    <title>NoteBook</title>
    
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" href="/static/css/home.css">
    <script src="/static/js/home.js"></script>

  </head>
  <body>
    <!-- Navigation bar -->
    <nav class="navbar navbar-expand-lg">
      <a class="navbar-brand" href="#">NoteBook</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <i class="fas fa-bars"></i> <!-- Custom toggle icon using Font Awesome -->

      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ml-auto">
          <c:if test="${login}">
          <li class="nav-item my_li">
            <a class="nav-link" href="/notes/private">My Notes</a>
          </li>
          </c:if>
          <li class="nav-item my_li">
            <a class="nav-link" href="/notes/public">Public Notes</a>
          </li>
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
          <li class="nav-item my_li">
            <a class="nav-link" href="/user/register">Register</a>
          </li>
        </ul>
      </div>
    </nav>

    <!-- Jumbotron with search bar -->
    <div class="jumbotron">
      <h1 class="display-4" id="heading">Welcome to NoteBook!</h1>
      <p class="lead">Create your own notes, search for public notes, and chat with other users.</p>
      <hr class="my-4">
      <form method="get" action="/notes/public">
        <div class="input-group mb-3">
          <input type="text" class="form-control" placeholder="Search public notes by keyword or topic" aria-label="Search public notes" aria-describedby="button-addon2" name="keyword">
          <div class="input-group-append" style="margin-left: 1%;">
            <button class="btn btn-outline-success" type="submit" id="button-addon2">Search</button>
          </div>
        </div>
      </form>
    </div>

    <!-- Recent and featured notes -->
    <div class="container">
      <div class="row">
        <div class="col-md-6">
          <h2>Recent Public Notes</h2>
          <ul class="list-group">
            <c:forEach var="note" items="${notes}">
              <li class="list-group-item"><a href="/notes/view/${note.id}">${note.name}</a></li>
            </c:forEach>
          </ul>
        </div>
       </div>
       <div class="row" style="margin-top: 5%;">
        <div class="col-md-6">
          <h2>Featured Notes</h2>
          <div class="card">
            <div class="card-body">
              <h5 class="card-title">Tips for Writing Clean Notes</h5>
              <p class="card-text"><ul>
               <strong><li>Use Meaningful Names</li></strong>
               <strong><li>Modularization</li></strong>
               <strong><li>Comments and Documentation</li></strong>
               <strong><li>Avoid Code Duplication</li></strong>
               <strong><li>Keep Functions Small and Single-Purpose</li></strong>
              </ul>
            </div>
        </div>
      </div>
      </div>
    </div>
  </div>

<hr>

  <!-- Footer -->
  <footer class="bg-light" style="margin-top: 50px;">
    <div class="container">
      <div class="row">
        <div class="col-md-4">
          <h5>About</h5>
          <p>NoteBook is a website for creating and sharing personal notes on coding and technical topics. Users can also search for public notes and chat with other users.</p>
        </div>
        <div class="col-md-4">
          <h5>Contact</h5>
          <p>Email: aniketranag123@gmail.com</p>
        </div>
        <div class="col-md-4">
          <h5>Follow Us</h5>
          <a href="#"><i class="fab fa-facebook fa-2x mr-3"></i></a>
          <a href="#"><i class="fab fa-twitter fa-2x mr-3"></i></a>
          <a href="#"><i class="fab fa-instagram fa-2x"></i></a>
        </div>
      </div>
    </div>
  </footer>


  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.min.js"></script>

</body>
</html>
