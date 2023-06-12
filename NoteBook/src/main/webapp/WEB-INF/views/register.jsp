<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  
<html>
    <head>

        <title>Registration</title>


        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <link rel="stylesheet" href="/static/css/register.css">
    

    </head>
    <body>

        <nav class="navbar navbar-expand-lg">
            <a class="navbar-brand" href="#">NoteBook</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
              <i class="fas fa-bars"></i> <!-- Custom toggle icon using Font Awesome -->
      
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
                  <a class="nav-link" href="/user/login">Log In</a>
                </li>
              </ul>
            </div>
          </nav>


        <section class="vh-100" style="background-color: #508bfc;">
            <div class="container py-5 h-100">
              <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                  <div class="card shadow-2-strong" style="border-radius: 1rem;">
                    <div class="card-body p-5 text-center">
          
                      <h3 class="mb-5">Register</h3>
                    <form:form method="post" action="/user/register" modelAttribute="user">
                      
                      <input type="hidden" name="_csrf"  value="${_csrf.token}">

                        <div class="form-outline mb-4">
                        <form:input type="text" id="typeNameX-2" class="form-control form-control-lg" path="name" placeholder="Full Name" />
                      </div>

                      <div class="form-outline mb-4">
                        <form:input type="email" id="typeEmailX-2" class="form-control form-control-lg" path="email" placeholder="Email" />
                      </div>
          
                      <div class="form-outline mb-4">
                        <form:input type="password" id="typePasswordX-2" class="form-control form-control-lg" path="password" placeholder="Password"/>
                      </div>
          
                      <button class="btn btn-primary btn-lg btn-block" type="submit">Register</button>
          
                      <hr class="my-4">
          
                      <button class="btn btn-lg btn-block btn-primary" style="background-color: #dd4b39;"
                        type="submit"><a href="/oauth2/authorization/google" style="text-decoration: none; color:white;"><i class="fab fa-google me-2"></i> Sign Up with Google</a></button>
                      
                    </form:form>

                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>
    </body>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.min.js"></script>
</html>