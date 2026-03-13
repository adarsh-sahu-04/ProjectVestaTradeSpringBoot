package com.ignishers.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ignishers.daoimpl.StockDaoImpl;
import com.ignishers.daoimpl.UserDaoImpl;
import com.ignishers.enums.AccountStatus;
import com.ignishers.pojo.Customer;
import com.ignishers.pojo.Stock;
import com.ignishers.pojo.User;

@Controller
public class AdminController {

	@Autowired
	private UserDaoImpl userdao;
	
	@Autowired
	public StockDaoImpl stockdao;

	
	
	@GetMapping("/adminhome")
	public String adminhomepage(Model model) {
		List<Stock> lst = stockdao.getAllStocks();
		model.addAttribute("stocks", lst);
		return "adminhome";
	}
	@GetMapping("/adminstock")
    public String adminstockpage() {
        return "adminstock";
    }
	@GetMapping("/editcustomers")
    public String editcustomerspage() {
        return "editcustomers";
    }
	@GetMapping("/amdcustomer")
	public String amdcustomerpage() {
		return "amdcustomer";
	}
	@GetMapping("/getcustomer")
	public String getcustomerpage() {
		return "getcustomer";
	}
	@GetMapping("/getstock")
	public String getstockpage() {
		return "getstock";
	}
	
	
	@GetMapping("/getallcst")
	public String getallcstpage(Model model) {
		List<User> lst= userdao.getAllUser();
		model.addAttribute("users", lst);
		return "getallcst";
	}
	
	@GetMapping("/verifypage")
	public String verifycustomerspage(Model model) {
		List<User> lst = userdao.getAllUser();
		List<Customer> unverifiedCustomers = new ArrayList<>();
		for(User u : lst) {
			if(u instanceof Customer && !u.getAccountStatus().equals(AccountStatus.VERIFIED)) {
				unverifiedCustomers.add((Customer)u);
			}
		}
		model.addAttribute("unverifiedCustomers", unverifiedCustomers);
		return "verifypage";
	}
	
	@PostMapping("/updateAccountStatus")
	public String updateAccountStatus(@RequestParam String email, @RequestParam String newStatus, Model model) {
		User existingUser = userdao.checkExistence(email);
		
		if(existingUser != null) {
			existingUser.setAccountStatus(AccountStatus.valueOf(newStatus));
			if(existingUser.getAccountStatus().equals(AccountStatus.VERIFIED))
				MailSender.sendMailAfterVerify(existingUser);
			if(existingUser.getAccountStatus().equals(AccountStatus.REJECTED))
				MailSender.sendMailAfterRejection(existingUser);
			if(existingUser.getAccountStatus().equals(AccountStatus.SUSPENDED))
				MailSender.sendMailAfterSuspention(existingUser);
			userdao.updateUser(existingUser);
			model.addAttribute("msg2", "Account status updated successfully for: " + email);
		} else {
			model.addAttribute("msg2", "User not found.");
		}
		
		List<User> lst = userdao.getAllUser();
		java.util.List<Customer> unverifiedCustomers = new java.util.ArrayList<>();
		for(User u : lst) {
			if(u instanceof Customer && !u.getAccountStatus().equals(AccountStatus.VERIFIED)) {
				unverifiedCustomers.add((Customer)u);
			}
		}
		model.addAttribute("unverifiedCustomers", unverifiedCustomers);
		return "verifypage";
	}
	
	
	
	@PostMapping("/amdcst")
	public ModelAndView amdcst(
	        @RequestParam String email,
	        @RequestParam String mobile,
	        @RequestParam String bankAccount,
	        @RequestParam String ifscCode,
	        @RequestParam String password,
	        @RequestParam String b1) {          // b1 = "Update" / "Delete" / "Block"

	    ModelAndView mv = null;
	    User existingUser = userdao.checkExistence(email);
	    
	    if(existingUser == null || !(existingUser instanceof Customer)) {
	    	mv = new ModelAndView("amdcustomer", "msg", "No customer found with email: " + email);
	    	return mv;
	    }
	    
	    Customer cst = (Customer) existingUser;
	    
	    if(b1.equals("Update")) {
            cst.setMobile(mobile);
            cst.setBankAccount(bankAccount);
            cst.setIfscCode(ifscCode);
	        cst.setPassword(password);
	        if(userdao.updateUser(cst))
	        	mv = new ModelAndView("amdcustomer", "msg", "Customer Updated Successfully.");
	        else
	        	mv = new ModelAndView("amdcustomer", "msg", "Customer Could not be Updated");
	    }
	    if(b1.equals("Delete")) {
	    	if(userdao.deleteUser(cst))
	        	mv = new ModelAndView("amdcustomer", "msg", "Customer Deleted Successfully.");
	        else
	        	mv = new ModelAndView("amdcustomer", "msg", "Customer Could not be Deleted");
	    }
	    if(b1.equals("Block")) {
	    	cst.setAccountStatus(AccountStatus.SUSPENDED);
	    	MailSender.sendMailAfterSuspention(cst);
	    	if(userdao.updateUser(cst))
	        	mv = new ModelAndView("amdcustomer", "msg", "Customer Blocked Successfully.");
	        else
	        	mv = new ModelAndView("amdcustomer", "msg", "Customer Could not be Blocked");
	    }
	    return mv;
		
	}
	
	@PostMapping("/getcst")
	public ModelAndView getcst(
			@RequestParam String email) {
		ModelAndView mv = null;
	    User user = userdao.checkExistence(email);
	    
	    if(user == null || !(user instanceof Customer)) {
	    	mv = new ModelAndView("getcustomer", "msg", "No customer found with email: " + email);
	    	return mv;
	    }
	    mv = new ModelAndView("getcst", "user", user);
	    return mv;
	}
	
	
	

	
}
