<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${sessionScope.user_id_string != null}">
    <jsp:forward page="iNuage.jsp"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>iNuage</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css">
  <style>
    .card-header {
      color: white;
      font-weight: bold;
    }
    
    .card-header-icons>a:link, .card-header-icons>a:visited {
      color: gray;
    }
    
    .card-header-icons>a:hover {
      color: red;
    }
    
    .card-body {
      background-color: #434343
    }
    
    .card-footer  a:link, .card-footer a:visited {
      color: gray;
      text-decoration: none;
    }
    
    .card-footer {
      background-color: #313538;
    }
  </style>
</head>

<body style="background-color: #303030; color: #D3D3D3;">
  <div class="container-fluid">
    <div class="row">
      <div class="col-md"></div>
      <div class="col-md">
        <img src="iNuage/images/iNuage_logo.png" alt="">
      </div>
      <div class="col-md"></div>
    </div>

    <div class="row text-center">
      <div class="col-md"></div>
      <div class="col-md">

        <!-- ERROR LOGS START-->
        <c:if test="${not empty param.status && param.status == '11'}">
          <div class="card bg-danger">
            <div class="card-header">LOGIN ERROR</div>
            <div class="card-body">
              <p class="card-text">Username or password incorrect</p>
            </div>
          </div>
          <br>
        </c:if>

        <c:if test="${not empty param.status && param.status == '21'}">
          <div class="card bg-danger">
            <div class="card-header">SIGN-UP ERROR</div>
            <div class="card-body">
              <p class="card-text"> Username already used</p>
            </div>
          </div>
          <br>
        </c:if>
        
        <c:if test="${not empty param.status && param.status == '22'}">
             <div class="card bg-danger">
                 <div class="card-header">SIGN-UP ERROR</div>
                 <div class="card-body">
                     <p class="card-text"> Passwords doesn't match</p>
                 </div>
             </div>
             <br>
         </c:if>
         
         <c:if test="${not empty param.status && param.status == '3'}">
             <div class="card bg-danger">
                 <div class="card-header">UNKNOWN ERROR</div>
                 <div class="card-body">
                     <p class="card-text">Our fabulous French technicians are working hard to resolve this problem. Please try again later.</p>
                 </div>
             </div>
             <br>
         </c:if>
         
         <c:if test="${not empty param.status && param.status == '4'}">
             <div class="card bg-warning">
                 <div class="card-header">LOGED OUT</div>
                 <div class="card-body">
                     <p class="card-text">You have been successfully logged out of iNuage.</p>
                 </div>
             </div>
             <br>
         </c:if>
         
         <c:if test="${not empty param.status && param.status == '5'}">
             <div class="card bg-warning">
                 <div class="card-header">ACCOUNT DELETED</div>
                 <div class="card-body">
                     <p class="card-text">Your account have been successfully deleted. We are sorry to see you leave so soon !</p>
                 </div>
             </div>
             <br>
         </c:if>
         
         <c:if test="${not empty param.status && param.status == '0'}">
             <div class="card bg-warning">
                 <div class="card-header">ACCOUNT CREATION SUCCESS</div>
                 <div class="card-body">
                     <p class="card-text">Your account was successfully created, you may now login.</p>
                 </div>
             </div>
             <br>
         </c:if>
        <!-- ERROR LOGS END-->

      </div>
      <div class="col-md"></div>
    </div>

    <div class="row">
      <div class="col-md"></div>

      <div class="col-md">
        <!-- CARD LOGIN START-->
        <div class="card text-center bg-success">
          <form method="post" action="iNuage/LoginServlet">
            <div class="card-header">LOGIN</div>
            <div class="card-body">
              <div class="form-group row">
                <label for="userName" class="col-sm-3 col-form-label">Username</label>
                <div class="col-sm-9">
                  <input class="form-control" type="text" required name="userName" placeholder="Enter your Username" /><br>
                </div>
              </div>

              <div class="form-group row">
                <label for="password" class="col-sm-3 col-form-label">Password</label>
                <div class="col-sm-9">
                  <input class="form-control" type="password" required name="password" placeholder="Enter your Password" /><br>
                </div>
              </div>

              <div class="form-group row">
                <div class="col-md">
                  <input class="form-check-input" type="checkbox" name="stayco" value="true" /> 
                  <label class="form-check-label" for="stayco">Remember me </label>
                </div>
              </div>
            </div>
            <div class="card-footer text-muted">
              <input class="btn btn-success" type="submit"
                value="Access iNuage">
            </div>
          </form>
        </div>
        <!-- CARD LOGIN END-->
      </div>

      <div class="col-md">
        <!-- CARD SIGN-UP START -->
        <div class="card text-center bg-primary">
          <form method="post" action="iNuage/SignupServlet">
            <div class="card-header">SIGN-UP</div>
            <div class="card-body">
              <div class="form-group row">
                <label for="userName" class="col-sm-3 col-form-label"> Username </label>
                <div class="col-sm-9">
                  <input class="form-control" type="text" required name="userName" placeholder="Choose a Username" /><br>
                </div>
              </div>

              <div class="form-group row">
                <label for="password" class="col-sm-3 col-form-label"> Password </label>
                <div class="col-sm-9">
                  <input class="form-control" type="password" required name="password" placeholder="Enter a Password" /><br>
                </div>
              </div>

              <div class="form-group row">
                <label for="password2" class="col-sm-3 col-form-label"> Password </label>
                <div class="col-sm-9">
                  <input class="form-control" type="password" required name="password2" placeholder="Confirm the Password" />
                </div>
              </div>
            </div>
            <div class="card-footer text-muted">
              <input class="btn btn-primary" type="submit" value="Join iNuage">
            </div>
          </form>
        </div>
        <!-- CARD SIGN-UP END -->
      </div>

      <div class="col-md"></div>
    </div>
  </div>
</body>
</html>