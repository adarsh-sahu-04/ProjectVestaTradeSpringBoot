package com.ignishers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ignishers.daoimpl.OrderDaoImpl;
import com.ignishers.daoimpl.StockDaoImpl;
import com.ignishers.daoimpl.UserDaoImpl;
import com.ignishers.enums.OrderType;
import com.ignishers.pojo.Customer;
import com.ignishers.pojo.Order;
import com.ignishers.pojo.Stock;
import com.ignishers.pojo.User;
import com.ignishers.pojo.Wallet;
import com.ignishers.repository.WallerRepository;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.ignishers.pojo.PortfolioItem;

@Controller
public class CustomerController {
	@Autowired
	private UserDaoImpl userdao;
	
	@Autowired
	public StockDaoImpl stockdao;

	@Autowired
	private OrderDaoImpl orderDao;

	@Autowired
	private WallerRepository walletRepository;
	
	@GetMapping("/customerhome")
	public String customerhomepage(Model model) {
		List<Stock> lst = stockdao.getAllStocks();
		model.addAttribute("stocks", lst);
		return "customerhome";
	}
	@GetMapping("/orderhistory")
	public String orderHistoryPage(HttpSession session, Model model) {
		User u = (User) session.getAttribute("user");
		if (u != null && u instanceof Customer) {
			List<Order> orders = orderDao.getOrdersByCustomerId(u.getId());
			model.addAttribute("orders", orders);
		}
		return "orderhistory";		
	}

	@GetMapping("/portfolio")
	public String portfoliopage(HttpSession session, Model model) {
		User u = (User) session.getAttribute("user");
		if (u != null && u instanceof Customer) {
			List<Order> orders = orderDao.getOrdersByCustomerId(u.getId());
			
			Map<String, PortfolioItem> portfolioMap = new HashMap<>();
			for(Order o : orders) {
				String symbol = o.getStockId();
				PortfolioItem item = portfolioMap.getOrDefault(symbol, new PortfolioItem());
				item.setStockSymbol(symbol);
				Stock stk = stockdao.getStock(symbol);
				if(stk != null) {
					item.setCompanyName(stk.getCompanyName());
					item.setCurrentPrice(stk.getCurrentPrice());
				} else {
					item.setCurrentPrice(BigDecimal.ZERO);
				}
				
				if(o.getOrderType() == OrderType.BUY) {
					int newQty = item.getQuantity() + o.getQuantity();
					BigDecimal totalCost = item.getAveragePrice().multiply(BigDecimal.valueOf(item.getQuantity()));
					totalCost = totalCost.add(o.getTotalAmount());
					if (newQty > 0) {
						item.setAveragePrice(totalCost.divide(BigDecimal.valueOf(newQty), 2, RoundingMode.HALF_UP));
					}
					item.setQuantity(newQty);
				} else if (o.getOrderType() == OrderType.SELL) {
					item.setQuantity(item.getQuantity() - o.getQuantity());
				}
				portfolioMap.put(symbol, item);
			}
			
			List<PortfolioItem> portfolio = new ArrayList<>();
			for(PortfolioItem item : portfolioMap.values()) {
				if(item.getQuantity() > 0) {
					BigDecimal totalValue = item.getCurrentPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
					BigDecimal totalCost = item.getAveragePrice().multiply(BigDecimal.valueOf(item.getQuantity()));
					BigDecimal profitLoss = totalValue.subtract(totalCost);
					BigDecimal profitLossPercentage = BigDecimal.ZERO;
					if(totalCost.compareTo(BigDecimal.ZERO) > 0) {
						profitLossPercentage = profitLoss.divide(totalCost, 4, RoundingMode.HALF_UP)
								.multiply(BigDecimal.valueOf(100));
					}
					item.setTotalValue(totalValue);
					item.setProfitLoss(profitLoss);
					item.setProfitLossPercentage(profitLossPercentage);
					portfolio.add(item);
				}
			}
			model.addAttribute("portfolio", portfolio);
		}
		return "portfolio";		
	}
	@GetMapping("/wallet")
	public String walletpage(HttpSession session, Model model) {
		User u = (User) session.getAttribute("user");
		if (u != null && u instanceof Customer) {
			Optional<Wallet> optWallet = walletRepository.findByCustomerId(u.getId());
			Wallet w = optWallet.orElseGet(() -> {
				Wallet newWallet = new Wallet();
				newWallet.setCustomerId(u.getId());
				newWallet.setBalance(BigDecimal.ZERO);
				return walletRepository.save(newWallet);
			});
			model.addAttribute("wallet", w);
		}
		return "wallet";		
	}

	@GetMapping("/addFunds")
	public ModelAndView addFunds(HttpSession session, @RequestParam("amount")BigDecimal amount) 
	{
		User u = (User) session.getAttribute("user");
		if (u == null || !(u instanceof Customer)) {
			return new ModelAndView("login", "msg", "Please login first");
		}
		Optional<Wallet> optWallet = walletRepository.findByCustomerId(u.getId());
		Wallet w = optWallet.orElseGet(() -> {
			Wallet newWallet = new Wallet();
			newWallet.setCustomerId(u.getId());
			newWallet.setBalance(BigDecimal.ZERO);
			return newWallet;
		});
		w.setBalance(w.getBalance().add(amount));
		walletRepository.save(w);
		
		ModelAndView mv = new ModelAndView("wallet", "msg", "Successfully added ₹" + amount + " to wallet.");
		mv.addObject("wallet", w);
		return mv;
	}

	@PostMapping("/withdrawFunds")
	public ModelAndView withdrawFunds(HttpSession session, @RequestParam(value="amount", required=false, defaultValue="0") BigDecimal amount) {
		User u = (User) session.getAttribute("user");
		if (u == null || !(u instanceof Customer)) {
			return new ModelAndView("login", "msg", "Please login first");
		}
		Optional<Wallet> optWallet = walletRepository.findByCustomerId(u.getId());
		Wallet w = optWallet.orElseGet(() -> {
			Wallet newWallet = new Wallet();
			newWallet.setCustomerId(u.getId());
			newWallet.setBalance(BigDecimal.ZERO);
			return newWallet;
		});
		
		if(w.getBalance().compareTo(amount) < 0) {
			ModelAndView mv = new ModelAndView("wallet", "msg", "Insufficient balance! Available: ₹" + w.getBalance());
			mv.addObject("wallet", w);
			return mv;
		}
		
		w.setBalance(w.getBalance().subtract(amount));
		walletRepository.save(w);
		
		ModelAndView mv = new ModelAndView("wallet", "msg", "Successfully withdrew ₹" + amount + " from wallet.");
		mv.addObject("wallet", w);
		return mv;
	}

	@GetMapping("/editprofile")
	public String editprofilepage(HttpSession session, Model model) {
		User u = (User) session.getAttribute("user");
		if (u != null) {
			model.addAttribute("cst", u);
		}
		return "editprofile";		
	}

	@PostMapping("/updateProfile")
	public ModelAndView updateProfile(HttpSession session, @RequestParam("password") String password) {
		User u = (User) session.getAttribute("user");
		if (u == null || !(u instanceof Customer)) {
			return new ModelAndView("login", "msg", "Please login first");
		}
		
		u.setPassword(password);
		userdao.updateUser(u);
		
		session.setAttribute("user", u);
		return new ModelAndView("editprofile", "msg", "Password updated successfully.");
	}
	@PostMapping("/trading")
	public ModelAndView tradingpage(@RequestParam String symbol) {
		Stock stk = stockdao.getStock(symbol);
		return new ModelAndView("trading", "stock", stk);
	}
	
	@PostMapping("/processTrade")
	public ModelAndView processTrade(
			@RequestParam("symbol") String symbol,
			@RequestParam("quantity") Integer quantity,
			@RequestParam("tradeType") String tradeType,
			HttpSession session) {
		
		User u = (User) session.getAttribute("user");
		if (u == null || !(u instanceof Customer)) {
			return new ModelAndView("login", "msg", "Please login first");
		}
		
		Stock stk = stockdao.getStock(symbol);
		if (stk == null) {
			return new ModelAndView("trading", "msg1", "Stock not found!");
		}
		
		OrderType type = OrderType.valueOf(tradeType);
		
		Optional<Wallet> optWallet = walletRepository.findByCustomerId(u.getId());
		Wallet wallet = optWallet.orElseGet(() -> {
			Wallet newWallet = new Wallet();
			newWallet.setCustomerId(u.getId());
			newWallet.setBalance(BigDecimal.ZERO);
			return walletRepository.save(newWallet);
		});
		
		BigDecimal totalCost = stk.getCurrentPrice().multiply(BigDecimal.valueOf(quantity));
		
		if(type == OrderType.BUY) {
			if(wallet.getBalance().compareTo(totalCost) < 0) {
				ModelAndView mv = new ModelAndView("trading", "stock", stk);
				mv.addObject("msg1", "Insufficient wallet balance. You need ₹" + totalCost + " but have ₹" + wallet.getBalance());
				return mv;
			}
			if(stk.getAvailableShares() < quantity) {
				ModelAndView mv = new ModelAndView("trading", "stock", stk);
				mv.addObject("msg1", "Not enough shares available in market. Available: " + stk.getAvailableShares());
				return mv;
			}
			stk.setAvailableShares(stk.getAvailableShares() - quantity);
			stockdao.updateStock(stk);
			
			wallet.setBalance(wallet.getBalance().subtract(totalCost));
			walletRepository.save(wallet);
			
		} else if (type == OrderType.SELL) {
			List<Order> userOrders = orderDao.getOrdersByCustomerId(u.getId());
			int heldQuantity = 0;
			for(Order o : userOrders) {
				if(o.getStockId().equals(symbol)) {
					if(o.getOrderType() == OrderType.BUY) {
						heldQuantity += o.getQuantity();
					} else if (o.getOrderType() == OrderType.SELL) {
						heldQuantity -= o.getQuantity();
					}
				}
			}
			if(heldQuantity < quantity) {
				ModelAndView mv = new ModelAndView("trading", "stock", stk);
				mv.addObject("msg1", "You do not own enough shares to sell. Held: " + heldQuantity);
				return mv;
			}
			stk.setAvailableShares(stk.getAvailableShares() + quantity);
			stockdao.updateStock(stk);
			
			wallet.setBalance(wallet.getBalance().add(totalCost));
			walletRepository.save(wallet);
		}
		
		Order order = new Order();
		order.setCustomerId(u.getId());
		order.setStockId(stk.getSymbol());
		order.setOrderType(type);
		order.setQuantity(quantity);
		order.setPricePerShare(stk.getCurrentPrice());
		
		orderDao.addOrder(order);
//		--------sends mail-----
		if(order.getOrderType().equals(OrderType.BUY))
			MailSender.sendMailBuyStock(u, order);
		else
			MailSender.sendMailSellStock(u, order);
		
		ModelAndView mv = new ModelAndView("trading", "stock", stk);
		mv.addObject("msg1", "Trade executed successfully!");
		return mv;
	}
	
}
