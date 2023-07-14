<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>${note.name}</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/themes/prism.min.css">
  <script src="https://cdn.jsdelivr.net/npm/showdown@2.1.0/dist/showdown.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/prism.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/components/prism-java.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/components/prism-cpp.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/prismjs@1.27.0/components/prism-python.min.js"></script>
  <link rel="stylesheet" href="/static/css/creates.css">
  <link rel="stylesheet" href="/static/css/toast.css">
  <script src="/static/js/toast.js"></script>
  <script src="/static/js/creates.js"></script>


</head>
<body id="b">


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
          <a class="nav-link" href="/chat/">Chat</a>
        </li>
        <li class="nav-item my_li">
          <a class="nav-link" style="cursor:pointer" onclick="makePostRequest('/logout','${_csrf.token}')">Log Out</a>
        </li>
      </ul>
    </div>
  </nav>
  
  <br><br>

  <div class="container-fluid">
    <form:form method="PUT" action="/notes/edit" modelAttribute="note">
      <input type="hidden" name="_csrf" value="${_csrf.token}" >
      <div class="row">
        <div class="col">
        <label id="label_" for="note-name">Note Name:</label>
        <form:input type="text" class="form-control" id="name" placeholder="Enter note name" path="name" readonly ="true"/>
        </div>
      </div>
      

      <input type="hidden" name="_csrf" value="${_csrf.token}" id="csrfToken">
      <input type="hidden" name="id" value="${note.id}">

      <div class="row">
        <div class="col-md">
          <br>
        <label id="label_" for="visibility">Visibility:</label>
        <form:select class="form-control" id="visibility" path="visibility">
          <form:option value="Public" label="Public"/>
          <form:option value="Private" label="Private"/>
        </form:select>
        </div>
        <div class="col-md">
          <br>
          <label id="label_" for="file-upload">File Upload:</label>
          <input style="width:50%" type="file" class="form-control-file resource_file"  id="file-upload">
          </div>
          <div class="col-md url_field" style="display: none;">
            <br>
            <br>
            <div class="form-text">
            <a href="#">Url for the resource</a>
            </div>
          </div>


      </div>
      <br>
      <div class="markdown-container">
        <textarea class="form-control" id="markdown-textarea" rows="10" name="content">
${note.content}

        </textarea>
        <div class="note-preview" id="preview-section" style="display: none;"></div>
      </div>
      <div class="buttons-container">
        <button type="button" class="btn btn-primary" id="preview-button">Preview</button>
        <button type="button" class="btn btn-primary" id="original-button">Original</button>
        <button type="submit" class="btn btn-success">Update</button>
      </div>
    </form:form>
    </div>

  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  
</body>
</html>
