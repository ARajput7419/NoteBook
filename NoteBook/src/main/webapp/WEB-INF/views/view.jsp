
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Note Name</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/themes/prism.min.css">
  <script src="https://cdn.jsdelivr.net/npm/showdown@2.1.0/dist/showdown.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/prism.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/components/prism-java.min.js"></script>
  <link href="/static/css/view.css" rel="stylesheet">
</head>
<body id="b">


  <nav class="navbar navbar-expand-lg">
    <a class="navbar-brand" href="#">NoteBook</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <i class="fas fa-bars"></i> <!-- Custom toggle icon using Font Awesome -->

    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav ml-auto">
        <c:if test="${login}">
        <li class="nav-item my_li">
          <a class="nav-link" href="#">My Notes</a>
        </li>
        </c:if>
        <li class="nav-item my_li">
          <a class="nav-link" href="#">Public Notes</a>
        </li>
        <li class="nav-item my_li">
          <a class="nav-link" href="#">Chat</a>
        </li>
        <c:if test="${!login}">
        <li class="nav-item my_li">
          <a class="nav-link" href="/user/login">Log In</a>
        </li>
        </c:if>
        <c:if test="${login}">
          <li class="nav-item my_li">
            <a class="nav-link" style="cursor:pointer" onclick="makePostRequest('/logout','${_csrf.token}')">Log Out</a>
          </li>
          </c:if>
          <c:if test="${!login}">
        <li class="nav-item my_li">
          <a class="nav-link" href="#">Register</a>
        </li>
        </c:if>
      </ul>
    </div>
  </nav>
  
  <br><br>

  <div class="view container-fluid actual_content">

    <c:if test="${edit}">
    <div class="row">
        <div class="col-md-1 offset-md-11">
            <button class="btn btn-success">Edit</button>
        </div>

    </div>
    </c:if>
    <br>


            


    </div>
    
    <div style="display: none;" id="markdown_content">
        ${note.content}
    </div>
    <input type="hidden" id="csrfToken" name="_csrf" value="${_csrf.token}">

  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  <script src="/static/js/view.js"></script>
  <script>


    function convertToHtml() {
        var markdownInput = document.getElementById('markdown_content').innerText;
        var htmlOutput = document.getElementsByClassName('actual_content')[0];
        var converter = new showdown.Converter();
        let convertedHtml  = converter.makeHtml(markdownInput);
        htmlOutput.insertAdjacentHTML("beforeend",convertedHtml);
        Prism.highlightAll();
}


    window.onload = function() { 
       
        convertToHtml();
        addStyle();
        appendCodeSections();
    }

  </script>
</body>
</html>
