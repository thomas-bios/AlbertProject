<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Je Nuage</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

</head>
<body>
  <h1>Welcome on Je Nuage</h1>

  <form method="post" action="JeNuage/LoginServlet">
      <h4>Log In</h4>
      Please enter your username    
      <input type="text" required name="userName"/><br>    
      Please enter your password
      <input type="password" required name="password"/><br>
      <input type="checkbox" name="stayco" value="true"/>Stay connected ?<br>
      
      <input type="submit" value="submit">      
    
  </form>
  
  <c:if test="${not empty param.status && param.status == '11'}">
      <p>User name or password incorrect</p>
  </c:if>
  
  <form method="post" action="JeNuage/SignupServlet">
      <h4>Sign up</h4>
      Choose username
      <input type="text" required name="userName"/><br>    
      Set password
      <input type="password" required name="password"/><br>
      Retype password
      <input type="password" required name="password2"/>
      
      <input type="submit" value="submit">      
    
  </form>
  <c:if test="${not empty param.eNew && (param.status == '21' || param.eNew == '22')}">
      <p>error</p>
  </c:if>

</body>
</html>