<%@ page import="com.ignishers.pojo.Stock"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Trading | VestaTrade</title>
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
          <h4 class="mb-0">Trade Stock</h4>
        </div>
        <div class="card-body">
          <%
             String msg1 = (String) request.getAttribute("msg1");
             if (msg1 != null) {
          %>
             <div class="alert alert-info"><%=msg1%></div>
          <% } %>

          <%
             Stock stk = (Stock) request.getAttribute("stock");
             if (stk != null) {
          %>
          <div class="mb-4 text-center">
            <h3><%=stk.getCompanyName()%> (<%=stk.getSymbol()%>)</h3>
            <h4 class="text-success">Current Price: ₹<%=stk.getCurrentPrice()%></h4>
            <p class="text-muted">Available Shares: <%=stk.getAvailableShares()%></p>
          </div>

          <form action="/processTrade" method="post">
            <input type="hidden" name="symbol" value="<%=stk.getSymbol()%>"/>
            
            <div class="mb-3">
              <label for="quantity" class="form-label">Quantity</label>
              <input type="number" class="form-control" id="quantity" name="quantity" min="1" required>
            </div>
            
            <div class="mb-3">
              <label for="tradeType" class="form-label">Trade Type</label>
              <select name="tradeType" id="tradeType" class="form-select" required>
                <option value="BUY">BUY</option>
                <option value="SELL">SELL</option>
              </select>
            </div>
            
            <div class="d-grid gap-2">
              <button class="btn btn-primary btn-lg" type="submit">Execute Trade</button>
            </div>
          </form>
          <% } else { %>
              <div class="alert alert-danger">Stock not found to trade. Go back to Markets.</div>
          <% } %>
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