<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
                <p class="lead">Welcome %username%, this is your online iNuage repository</p>
            </div>
        </div>

		<!-- Available files -->
		<div class="my-3 p-3 rounded shadow-sm">
			<div class="form-group row border-bottom border-gray pb-2 mb-0 ">
                <h6 class="col-sm-6">My Files</h6>
                <div class="col-sm-5"><i class="fas fa-file-upload" data-toggle="modal" data-target="#uploader" style="color: green;"></i></div>
                <span class="float-right"><a href="#"> Log out </a></span>
            </div>
        </div>


	        <!-- Uploader -->
	        <div class="modal fade" id="uploader" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	          <div class="modal-dialog" role="document">
	            <div class="modal-content bg-dark">
	              <div class="modal-header">
	                <h5 class="modal-title" id="exampleModalLabel">Upload a new file</h5>
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                  <span aria-hidden="true">&times;</span>
	                </button>
	              </div>
	              <div class="modal-body">
	                    <!-- FORM D'UPLOAD DE FILES -->
	              </div>
	              <div class="modal-footer">
	                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
	                <button type="button" class="btn btn-success">Upload</button>
	              </div>
	            </div>
	          </div>
	        </div>
	        
	        
			<!-- New file template -->
			<div class="media text-muted pt-3">
				<div class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">
					<div class="d-flex justify-content-between align-items-center w-100">
						<a href="New_User_GUIDE.txt"><strong class="text-gray-dark">New_User_GUIDE.txt</strong></a>
						<a href="#">Delete</a>
					</div>
					<span class="d-block"> 15 Jun 1987 </span>
				</div>
			</div>
			<!-- ----------------- -->

			<small class="d-block text-right mt-3"><a href="#">Delete Account </a></small>
		</div>
	</div>
</body>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</html>
