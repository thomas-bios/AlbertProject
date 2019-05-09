<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>JeNuage</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <style>
        .info {
	       color: #CB3B7D;
        }

        a:link, a:visited {
            text-decoration: none;
        }
    </style>
</head>

<body style="background-color: #303030; color: #D3D3D3;">
	<div class="container">
		<h1 class="display-3">
			JeNuage <small>%Username%</small>
		</h1>
		<span class="float-right"><a href="Todo?act=D"> Disconnect</a></span>

		<!-- Form d'upload des files -->
		<form class="form-inline" action="Todo" method="post">
			<div class="form-group mx-sm-3 mb-2">
				<input type="text" class="form-control" name="msg" style="width: 300px;" placeholder="Make cupcakes with François">
			</div>
			<input class="btn btn-primary mb-2" type="submit" name="submitBtn" value="Add Task">
		</form>
		<br>

		<!-- Available files -->
		<div class="my-3 p-3 rounded shadow-sm">
			<h6 class="border-bottom border-gray pb-2 mb-0">My Files</h6>

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
</html>
