<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Markets | VestaTrade</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"/>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@300;400;500;600;700&family=DM+Mono:wght@300;400;500&display=swap"/>
  <link rel="stylesheet" href="css/style.css"/>
</head>
<body>

<%@include file="headeruser.jsp" %>


<div class="container py-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card shadow-sm">
        <div class="card-header bg-primary text-white text-center">
          <h4 class="mb-0">Edit Profile Parameters</h4>
        </div>
        <div class="card-body">
          <%
             String msg1 = (String) request.getAttribute("msg");
             if (msg1 != null) {
          %>
             <div class="alert alert-info"><%=msg1%></div>
          <% } %>

          <form action="/updateProfile" method="post">
            
            <div class="mb-3">
              <label for="password" class="form-label">New Password</label>
              <input type="password" class="form-control" name="password" id="password" required>
            </div>
            
            <div class="d-grid gap-2 mt-4">
              <button class="btn btn-primary btn-lg" type="submit">Update Password</button>
            </div>
          </form>
          
          <div class="mt-4 text-muted small text-center">
             Notice: KYC details like PAN, Aadhar, Email, DOB, and Bank details cannot be updated online due to regulatory requirements. Please contact support.
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>
