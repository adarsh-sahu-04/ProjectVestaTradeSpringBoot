<%@ page import="com.ignishers.pojo.Wallet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Wallet | VestaTrade</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"/>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@300;400;500;600;700&family=DM+Mono:wght@300;400;500&display=swap"/>
  <link rel="stylesheet" href="css/style.css"/>
  <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
</head>
<body>

<%@include file="headeruser.jsp" %>

<div class="container py-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card shadow-sm text-center">
        <div class="card-header bg-dark text-white text-center">
          <h4 class="mb-0">My Wallet Account</h4>
        </div>
        <div class="card-body py-5">
          <%
             String msg1 = (String) request.getAttribute("msg");
             if (msg1 != null) {
          %>
             <div class="alert alert-info"><%=msg1%></div>
          <% } %>

          <%
             Wallet w = (Wallet) request.getAttribute("wallet");
             if (w != null) {
          %>
          <h1 class="display-3 text-success">₹<%=w.getBalance()%></h1>
          <p class="text-muted mb-5">Available Trading Balance</p>

          <form action="addFunds" id="paymentForm" method="post" class="mt-4">
            <div class="input-group mb-3">
              <span class="input-group-text">₹</span>
              <input type="number" class="form-control form-control-lg" name="amount" id="amount" placeholder="Amount to add" min="1" required>
              <button class="btn btn-primary" type="submit">Add Funds</button>
            </div>
          </form>

          <form action="withdrawFunds" method="post" class="mt-4">
            <div class="input-group mb-3">
              <span class="input-group-text">₹</span>
              <input type="number" class="form-control form-control-lg" name="amount" placeholder="Amount to withdraw" min="1" required>
              <button class="btn btn-warning" type="submit">Withdraw</button>
            </div>
          </form>
          <% } else { %>
              <div class="alert alert-danger">Error loading wallet information. Please contact support.</div>
              <form action="/addFunds" method="post" class="mt-4">
                 <button class="btn btn-primary" type="submit">Initialize Wallet</button>
              </form>
          <% } %>
        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/main.js"></script>
<script>
        document.getElementById("paymentForm").addEventListener("submit", function (e) {
            e.preventDefault();

            const amount = document.getElementById("amount").value;

            fetch("${pageContext.request.contextPath}/order/create", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: "amount=" + amount
            })
            .then(res => {
                if (!res.ok) throw new Error("Server error: " + res.status);
                return res.json();
            })
            .then(order => {
                const options = {
                    key: "rzp_test_SQc4j5P0Dh1RxX",
                    amount: order.amount,
                    currency: "INR",
                    name: "VestaTrade Pvt. Ltd.",
                    order_id: order.id,
                    handler: function (response) {
                        //alert("Payment successful! Payment ID: " + response.razorpay_payment_id);
                    	window.location="addFunds?amount="+document.getElementById("amount").value;
                    }
                };

                const rzp1 = new Razorpay(options);
                rzp1.open();
            })
            .catch(err => alert("Error creating order: " + err));
        });
    </script>
</body>
</html>