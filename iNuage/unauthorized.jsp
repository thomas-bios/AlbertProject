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
        <div class="card text-center bg-danger">
            <div class="card-header">UNAUTHORIZED</div>
            <div class="card-body">
              You are not authorized to access this file. 
            </div>
            <div class="card-footer text-muted">
              <a href="iNuage" ><input class="btn btn-danger" type="submit" value="Get Back"></a>
            </div>
        </div>
      </div>

      <div class="col-md"></div>
    </div>
  </div>
</body>
</html>