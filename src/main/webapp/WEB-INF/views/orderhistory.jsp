<%@ page import="com.ignishers.pojo.Order"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Order History | VestaTrade</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"/>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@300;400;500;600;700&family=DM+Mono:wght@300;400;500&display=swap"/>
  <link rel="stylesheet" href="css/style.css"/>
</head>
<body>

<%@include file="headeruser.jsp" %>

<div class="container py-5">
  <h2 class="mb-4">My Order History</h2>
  
  <div class="table-responsive">
    <table class="table table-bordered table-striped">
      <thead class="table-dark">
        <tr>
          <th>Order ID</th>
          <th>Stock Symbol</th>
          <th>Type</th>
          <th>Quantity</th>
          <th>Price per Share</th>
          <th>Total Amount</th>
          <th>Date</th>
        </tr>
      </thead>
      <tbody>
      <%
          List<Order> orders = (List<Order>) request.getAttribute("orders");
          if (orders != null && !orders.isEmpty()) {
              for(Order order : orders) {
      %>
          <tr>
            <td><%=order.getId()%></td>
            <td><strong><%=order.getStockId()%></strong></td>
            <td>
              <span class="badge <%= "BUY".equals(order.getOrderType().name()) ? "bg-success" : "bg-danger" %>">
                <%=order.getOrderType()%>
              </span>
            </td>
            <td><%=order.getQuantity()%></td>
            <td>₹<%=order.getPricePerShare()%></td>
            <td>₹<%=order.getTotalAmount()%></td>
            <td><%=order.getPlacedAt()%></td>
          </tr>
      <%
              }
          } else {
      %>
          <tr>
            <td colspan="7" class="text-center">No orders found. Start trading to build your portfolio!</td>
          </tr>
      <%
          }
      %>
      </tbody>
    </table>
  </div>
</div>

<%@include file="footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>
