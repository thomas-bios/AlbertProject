<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>


<sql:setDataSource
  driver="com.mysql.jdbc.Driver"
  url="jdbc:mysql://cs3.calstatela.edu/cs3220stu97"
  user="cs3220stu97"
  password="TZ*JTLXb"/>

<c:if test="${sessionScope.user_id_string == null}">
	<jsp:forward page="home.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>iNuage</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css">
	<style>
		a:link, a:visited {
			text-decoration: none;
		}
	</style>
</head>

<body style="background-color: #303030; color: #D3D3D3;">
	<div class="container">
		<div class="jumbotron jumbotron-fluid bg-dark">
			<div class="container">
				<h1 class="display-4">iNuage</h1>
				<p class="lead">Welcome <strong style="color: #FCFD5F;">${sessionScope.user_name_string}</strong>, this is your online iNuage repository</p>
			</div>
		</div>

		<div class="btn-toolbar justify-content-between" role="toolbar" aria-label="Toolbar with button groups">
			<div class="btn-group" role="group" aria-label="First group">
				<button class="btn btn-secondary" data-toggle="modal" data-target="#uploader">UPLOAD</button>
				<button class="btn btn-secondary" data-toggle="modal" data-target="#searcher">SEARCH</button>
				<button class="btn btn-secondary" data-toggle="modal" data-target="#directorer">NEW DIRECTORY</button>
			</div>

			<a href="iNuage/action?c=logout"><button type="button" class="btn btn-secondary">LOG OUT</button></a>
		</div>

		<c:if test="${not empty param.upload && param.upload == '0'}">
			<div class="alert alert-success" role="alert">Your file was successfully uploaded.</div>
		</c:if>

        <c:if test="${not empty param.upload && param.upload == '1'}">
            <div class="alert alert-danger" role="alert">Please provide a file to upload.</div>
        </c:if>
  
        <c:if test="${not empty param.upload && param.upload == '2'}">
            <div class="alert alert-danger" role="alert">Unknown error. Please try again later.</div>
        </c:if>
        
        <c:if test="${not empty param.upload && param.upload == '3'}">
            <div class="alert alert-danger" role="alert">This file already exist on your iNuage.</div>
        </c:if>

        <c:if test="${not empty param.status && param.status == '01'}">
            <div class="alert alert-success" role="alert">Your file was successfully deleted.</div>
        </c:if>

        <c:if test="${not empty param.status && param.status == '02'}">
            <div class="alert alert-success" role="alert">Your file was successfully renamed.</div>
        </c:if>
        
        <c:if test="${not empty param.status && param.status == '03'}">
            <div class="alert alert-success" role="alert">Your file shareability was successfully changed.</div>
        </c:if>
        <!-- Search results -->
        <c:if test="${not empty param.search}">
	        <div class="my-3 p-3 rounded shadow-sm">
	            <h6 class="border-bottom border-gray pb-2 mb-0">Search results</h6>
	            
                <sql:query var = "searchResults">
                    SELECT * from jenuage_docs Where name LIKE '%${param.search}%';
                </sql:query>
	            
	            <c:if test="${searchResults.rowCount == 0}">
                    <br><h5> Your search <strong style="color: #FCFD5F;">- ${param.search} -</strong> did not match any documents. </h5>
	            </c:if>
	            
	            <c:if test="${searchResults.rowCount > 0}">
		            <c:forEach var="row" items="${searchResults.rows}">
	                    <div class="media text-muted pt-3">
	                        <div class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">
	                            <div class="d-flex justify-content-between align-items-center w-100">
	                                <h6><strong class="text-white">${row.name}</strong></h6>
	                        
	                                <div style="text-align: right;"><h5>
	                                   <a href="iNuage/action?c=down&fid=${row.file_id}" style="color: #3F6CDE;"><i class="fas fa-download"></i></a>
	                                   <c:if test="${row.share == 0}">
	                                       <a href="iNuage/action?c=sha&fid=${row.file_id}&state=0" style="color: #CCCCCC;"><i class="fas fa-share-alt-square"></i></a>
	                                   </c:if>
	                                   <c:if test="${row.share == 1}">
	                                       <a href="iNuage/action?c=sha&fid=${row.file_id}&state=1" style="color: #4CAA46;"><i class="fas fa-share-alt-square"></i></a>
	                                   </c:if>                   
	                                   <a href="iNuage?rename=${row.file_id}" style="color: #DCD650;"><i class="fas fa-pen" ></i></a>
	                                   <a href="iNuage/action?c=del&fid=${row.file_id}" style="color: #B93842;"><i class="fas fa-trash"></i></a>
	                                </h5></div>
	                            </div>
	                            <span class="d-block">${row.date}</span>
	                        </div>
	                    </div>
	                </c:forEach>
	                <br>
	            </c:if>
	            
	        </div>
        </c:if>
        
		<!-- Available files -->
        <div class="my-3 p-3 rounded shadow-sm">
            <h6 class="border-bottom border-gray pb-2 mb-0">My Files</h6>

		    <!-- DISPLAY FILES-->
		    <sql:query var = "files">
		        SELECT * from jenuage_docs where user = "${sessionScope.user_id_string}";
		    </sql:query>
	        <c:if test="${files.rowCount == 0}">
	            <div class="media text-muted pt-3">
	                <div class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">
	                    <div class="d-flex justify-content-between align-items-center w-100">
	                        <a href="iNuage/New_User_GUIDE.txt"><strong class="text-gray-dark">New_User_GUIDE.txt</strong></a>
	                    </div>
	                    <span class="d-block"> 15 Jun 1987 </span>
	                </div>
	            </div>
	        </c:if>
	        <c:if test="${files.rowCount > 0}">
	            <c:forEach var="row" items="${files.rows}">
	                <div class="media text-muted pt-3">
	                    <div class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">
	                        <div class="d-flex justify-content-between align-items-center w-100">
                                <h6><strong class="text-white">${row.name}</strong></h6>
                                
                                 <c:if test="${not empty param.rename && param.rename == row.file_id}">	                    
		                             <form method="get" action="iNuage/action" class="form-inline">
										<div class="form-group mb-2">
										  <label for="c" class="sr-only">Action Type</label>
										  <input type="hidden" readonly class="form-control-plaintext" id="c" value="ren" name="c">
										  <label for="fid" class="sr-only">File ID</label>
										  <input type="hidden" readonly class="form-control-plaintext" id="fid" value="${param.rename}" name="fid">
										</div>
										<div class="form-group mx-sm-3 mb-2">
										  <label for="newname" class="sr-only">New Name</label>
										  <input type="text" class="form-control" id="newname" placeholder="thing.jpg" name="newname">
										</div>
	                                    <input type="submit" class="btn btn-primary mb-2" value="Rename">
	                                </form>
	                            </c:if>

	                            <c:if test="${empty param.rename}">
		                            <div style="text-align: right;"><h5>
			                           <a href="iNuage/Download?fid=${row.file_id}" style="color: #3F6CDE;" class="tooltip-test" title="Download"><i class="fas fa-download"></i></a>
			                           <c:if test="${row.share == 0}">
			                               <a href="iNuage/action?c=sha&fid=${row.file_id}&state=0" style="color: #CCCCCC;" class="tooltip-test" title="Share"><i class="fas fa-share-alt-square"></i></a>
			                           </c:if>
			                           <c:if test="${row.share == 1}">
			                               <a href="iNuage/action?c=sha&fid=${row.file_id}&state=1" style="color: #4CAA46;" class="tooltip-test" title="Stop sharing"><i class="fas fa-share-alt-square"></i></a>
			                           </c:if>                   
	                                   <a href="iNuage?rename=${row.file_id}" style="color: #DCD650;" class="tooltip-test" title="Rename"><i class="fas fa-pen" ></i></a>
	                                   <a href="iNuage/action?c=del&fid=${row.file_id}" style="color: #B93842;" class="tooltip-test" title="Delete"><i class="fas fa-trash"></i></a>
		                            </h5></div>
	                            </c:if>
	                        </div>
	                        <span class="d-block">${row.date}</span>
	                    </div>
	                </div>
	            </c:forEach>
	        </c:if>
		</div>
		<!--------------------->


        <!-- UPLOAD PANEL -->
        <div class="modal fade" id="uploader" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content bg-dark">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Upload a new file</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form action="iNuage/Upload" method="post" enctype="multipart/form-data">
                        <div class="modal-body">
                            <input type="file" name="file"/><br/>  
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="submit" name="upload" value="Upload" class="btn btn-success">Upload</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <!-- SEARCH PANEL -->
        <div class="modal fade" id="searcher" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content bg-dark">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Search for a file</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form action="iNuage" method="get">
                        <div class="modal-body">
                           <div class="col-auto">
                              <label class="sr-only" for="inlineFormInputGroup">Name of File</label>
                              <div class="input-group mb-2">
                                <div class="input-group-prepend">
                                  <div class="input-group-text">?</div>
                                </div>
                                <input type="text" class="form-control" id="inlineFormInputGroup" type="text" required name="search" placeholder="name_of_file.png">
                              </div>
                            </div>  
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <!-- NEW DIRECTORY PANEL -->
        <div class="modal fade" id="directorer" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content bg-dark">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Name of the new folder</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form action="iNuage/action" method="post">
                        <div class="modal-body">
                           <div class="col-auto">
                              <label class="sr-only" for="inlineFormInputGroup">Name of the new folder</label>
                              <div class="input-group mb-2">
                                <div class="input-group-prepend">
                                  <div class="input-group-text">?</div>
                                </div>
                                <input type="text" class="form-control" id="inlineFormInputGroup" type="text" required name="dir" placeholder="ULTRA_SECRET_FILES">
                              </div>
                            </div>  
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
		<!-- DELETE ACCOUNT PANEL -->
		<div class="modal fade" id="accountDeleter" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content bg-dark">
					<div class="modal-header">
						<h5 class="modal-title" id="exampleModalLabel">Upload a new file</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">Are you sure you want to permanently delete your iNuage account ?</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">No</button>
						<a href="iNuage/action?c=delete"><button type="button" class="btn btn-success">Yes</button></a>
					</div>
				</div>
			</div>
		</div>

		<small class="d-block text-right mt-3" data-toggle="modal" data-target="#accountDeleter" style="cursor: pointer; color: #0376D1;">Delete Account</small>
	</div>
</body>

<!-- FOR DISPLAY PURPOSE ONLY // From bootstrap-->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</html>