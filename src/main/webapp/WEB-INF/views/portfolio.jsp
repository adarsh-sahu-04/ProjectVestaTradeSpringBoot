<%@ page import="com.ignishers.pojo.PortfolioItem"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Portfolio | VestaTrade</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"/>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@300;400;500;600;700&family=DM+Mono:wght@300;400;500&display=swap"/>
  <link rel="stylesheet" href="css/style.css"/>
</head>
<body>

<%@include file="headeruser.jsp" %>

<div class="container py-5">
  <h2 class="mb-4">My Portfolio Returns</h2>
  
  <div class="table-responsive">
    <table class="table table-bordered table-striped">
      <thead class="table-dark">
        <tr>
          <th>Stock</th>
          <th>Quantity</th>
          <th>Avg Buy Price</th>
          <th>Current Price</th>
          <th>Current Value</th>
          <th>Gain/Loss (%)</th>
          <th>Gain/Loss (₹)</th>
        </tr>
      </thead>
      <tbody>
      <%
          List<PortfolioItem> portfolio = (List<PortfolioItem>) request.getAttribute("portfolio");
          if (portfolio != null && !portfolio.isEmpty()) {
              for(PortfolioItem item : portfolio) {
                  boolean isProfit = item.getProfitLoss().compareTo(java.math.BigDecimal.ZERO) >= 0;
                  String colorClass = isProfit ? "text-success" : "text-danger";
                  String plusSign = isProfit ? "+" : "";
      %>
          <tr>
            <td>
              <strong><%=item.getStockSymbol()%></strong><br/>
              <small class="text-muted"><%=item.getCompanyName()%></small>
            </td>
            <td><%=item.getQuantity()%></td>
            <td>₹<%=item.getAveragePrice()%></td>
            <td>₹<%=item.getCurrentPrice()%></td>
            <td>₹<%=item.getTotalValue()%></td>
            <td class="<%=colorClass%>"><strong><%=plusSign%><%=item.getProfitLossPercentage()%>%</strong></td>
            <td class="<%=colorClass%>"><strong><%=plusSign%>₹<%=item.getProfitLoss()%></strong></td>
          </tr>
      <%
              }
          } else {
      %>
          <tr>
            <td colspan="7" class="text-center">No current holdings in your portfolio. Go to Markets to start trading!</td>
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