<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<title>WeLearn</title>
</head>
<style type="text/css">
<%@ include file="../css/style.css" %>
</style>
<body>
<%@ include file="../partials/header.jsp" %>
<div class="container" >
<p>WeLearn is a Learning Management system with various courses and each of them are classified into <br>
three major categories for the convenience of the user. So When adding the course please make sure you<br>
stick to our policy of letting the user to choose their levels!</p>
<h3>Happy Teaching!</h3>
<form action="add-course" method="post">
<table>
<tr>
<td><label>Course Name</label></td>
<td><input type="text" name="courseName" required></td>
<td><p class="error"> ${courseNameError }</p></td>
</tr>
<tr>
<td><label>Chapters</label></td>
<td><input type="text" name="chapters" required></td>
</tr>
<tr>
<td><label>Price</label></td>
<td><input type="text" name="price" required></td>
</tr>
<tr>
<td><label>Course Description</label></td>
<td><textarea style="resize:none" name="courseDescription" required></textarea></td>
</tr>
</table>
<input type="submit" value="Add Course">
</form>
<a href="/DWP/admin/view-course" style="float:right;margin-left:10px;"><button>View Courses Created By You</button></a>
<a href="../user/enrollcourse" style="float:right;"><button>Back to Dashboard</button></a>
</div>
</body>
</html>