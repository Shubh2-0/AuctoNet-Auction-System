package com.masai.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.masai.Dto.*;

public class SellerDaoImpl implements SellerDao {
 
	static Connection con = null;
	static Buyer buyer = null;
	static Seller seller = null;
	static BuyerDao buyerDao = new BuyerDaoImpl();
	static ProductDao productDao = new ProductDaoImpl();
	
	
	
	
	@Override
	public Seller loginSeller(String username, String password) {
	
		seller = null;
		
	try {
		
		con = DBUtils.getConnection();
		
		String SELECT_QUERY = "SELECT UserName, password, first_name, last_name, mobile_no, address, income, pending_amount FROM seller WHERE username = ? AND password = ? AND is_deleted = 0";
		
		PreparedStatement statement = con.prepareStatement(SELECT_QUERY); 
		
		statement.setString(1, username);
		statement.setString(2, password);
		
		
		ResultSet set = statement.executeQuery();
		
		while(set.next()) {
			seller = new Seller(set.getString(1), 
					set.getString(2), 
					set.getString(3), 
					set.getString(4), 
					set.getString(5), 
					set.getString(6),
					set.getDouble(7)
					);
			seller.setPendingAmount(set.getDouble(8));
		}
	
		
		
	} catch (Exception e) {
		
		System.out.println(e);
		
	}
		
		
		
	return seller;
		
		
	}
	
	
	
	
	
	@Override
    public double getSellerIncome(String username) {
		
		double income = 0.0;
		
		try {
			
			con = DBUtils.getConnection();
			
			String SELECT_QUERY = "SELECT income FROM seller WHERE username = ? AND is_deleted != 1";
			
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY); 
			
			statement.setString(1, username);
			
			
			ResultSet set = statement.executeQuery();
			
			while(set.next()) {
				
				income = set.getDouble("income");
				
			}
		
			
			
		} catch (Exception e) {
			
			System.out.println(e);
			
		}
		
		return income;
		
		
	}
	
	
	
	
	@Override
	public double refundToBuyer(int productId, int quantity) {
		
		double amount = 0;
		
		Product product = productDao.getProductById(productId);
        int GST = productDao.getGStPercentage(productId);
        
        
        amount = (product.getProductPrice()*quantity);
        amount = (amount*GST/100) + amount;
		
        
        
	       try {
			
			con = DBUtils.getConnection();
			
			String UPDATE_QUERY = "UPDATE seller SET pending_amount = (SELECT pending_amount WHERE UserName = ?) + ? WHERE UserName = ?;";
			
			PreparedStatement statement = con.prepareStatement(UPDATE_QUERY); 
			
			statement.setString(1, product.getSellerId());
			statement.setDouble(2, amount);
			statement.setString(3, product.getSellerId());
			
			
			if(statement.executeUpdate() > 0) return amount;
			
		
			
			
		} catch (Exception e) {
			
			System.out.println(e);
			
		}
        
        
		return 0;
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	@Override
	public Seller getSellerByUsername(String username) {
		
		
		Connection con = null;
		
		Seller seller2 = null;
		
		try {
			
			con = DBUtils.getConnection();
			
			String SELECT_QUERY = "SELECT UserName, password, first_name ,last_name, mobile_no, "
					+ "address, income, pending_amount  FROM SELLER WHERE username = ? AND is_deleted != 1";
			
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY); 
			
			statement.setString(1, username);
			
			
			
			ResultSet set = statement.executeQuery();
			
			while(set.next()) {
				seller2 = new Seller(set.getString(1), 
						set.getString(2), 
						set.getString(3), 
						set.getString(4), 
						set.getString(5), 
						set.getString(6),
						set.getDouble(7)
						);
				seller2.setPendingAmount(set.getDouble(8));
			}
		
			
			
		} catch (Exception e) {
			
			System.out.println(e);
			
		}finally {
			
			DBUtils.closeConnection(con);
		}
			
			
			
		return seller2;
		
		
		
		
		
		
	}
	
	
	
	
	
	@Override
	public String updateSellerDetails(Seller u) {
		
		if(!u.getFirstName().equals("")) {
			seller.setFirstName(u.getFirstName());
		}
		if(!u.getLastName().equals("")) {
			seller.setLastName(u.getLastName());
		}
		
		if(!u.getMobileNo().equals("")) {
			seller.setMobileNo(u.getMobileNo());
		}
		
		if(!u.getAddress().equals("")) {
			seller.setAddress(u.getAddress());
		}
		
		
		
		try {
			
			con = DBUtils.getConnection();
			
			String UPDATE_QUERY = "UPDATE seller SET  first_name = ?, last_name = ?, mobile_no = ? , address = ? WHERE UserName = ? AND password = ?";
			
			PreparedStatement statement = con.prepareStatement(UPDATE_QUERY);
			
			statement.setString(1, seller.getFirstName());
			statement.setString(2, seller.getLastName());
			statement.setString(3, seller.getMobileNo());
			statement.setString(4, seller.getAddress());
			statement.setString(5, seller.getSellerUserName());
			statement.setString(6, seller.getPassword());
			
			
			int ans = statement.executeUpdate();
			
			if(ans > 0)
				return "Information Updated Successfullt...";
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		
		return "Faild to Update....";
		
	}

	
	
	
	@Override
	public String registerNewSeller(Seller s) {
		
		seller = s;
		
		try {
			
			con = DBUtils.getConnection();
			String INSERT_QUERY = "INSERT INTO seller(UserName, password, first_name, last_name, mobile_no, address, income, is_deleted) VALUES(?,?,?,?,?,?,?,?)";
			
			PreparedStatement statement = con.prepareStatement(INSERT_QUERY);
			
			statement.setString(1, s.getSellerUserName());
			statement.setString(2, s.getPassword());
			statement.setString(3, s.getFirstName());
			statement.setString(4, s.getLastName());
			statement.setString(5, s.getMobileNo());
			statement.setString(6, s.getAddress());
			statement.setDouble(7, s.getIncome());
			statement.setInt(8, 0);
			
			int ans = statement.executeUpdate();
			
			if(ans > 0)
				return "Acount Created Successfully....";
			
		    	
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return "Something Went Wrong...";
		
	}
	
	
	
	
	
	@Override
	public ResultSet getAllSellerProducts(String username) {
		
		 
			
			try {
				
				con = DBUtils.getConnection();
				String SELECT_QUERY = "SELECT product_id AS 'Product ID', product_name AS 'Name', price_per_piece AS 'Price Per Unit', quantity AS Quantity, description AS Description, category_id AS 'Category Number' FROM product WHERE seller_username = ?";
				
				
				PreparedStatement statement = con.prepareStatement(SELECT_QUERY);
				
				statement.setString(1, username);
				
				ResultSet set = statement.executeQuery();
				
				statement.setString(1, seller.getSellerUserName());
				
					
					
					
					return set;
					
						
						
		
					
				
				
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
			}
			
			
			
			return null;
			
		
	}
	
	
	
	
	
	@Override
	public ResultSet getAllSellerProductsOrderByQuantity(String username) {
		
		 
			
			try {
				
				con = DBUtils.getConnection();
				String SELECT_QUERY = "SELECT product_id AS 'Product ID', product_name AS 'Name', price_per_piece AS 'Price Per Unit', quantity AS Quantity, description AS Description, category_id AS 'Category Number' FROM product WHERE seller_username = ? ORDER BY quantity";
				
				
				PreparedStatement statement = con.prepareStatement(SELECT_QUERY);
				
				statement.setString(1, username);
				
				ResultSet set = statement.executeQuery();
				
				statement.setString(1, seller.getSellerUserName());
				
					
					
					
					return set;
					
						
						
		
					
				
				
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
			}
			
			
			
			return null;
			
		
	}
	
	
	
	
	@Override
	public boolean unhideSellerProducts(String username) {
		     
          try {
			
			con = DBUtils.getConnection();
			
			String SELECT_QUERY = "UPDATE product SET is_hide = 0, deleted_date = null WHERE seller_username = ?";
			
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY); 
			
			statement.setString(1, username);
			
			
			int ans = statement.executeUpdate();
			
			if(ans > 0) return true;
			
			
			
			
		} catch (Exception e) {
			
			System.out.println(e);
			
		}
			
			
		return false;
			
		
	}
	
	
	
	
	@Override
	public boolean hideSellerProducts(String username) {
		     
          try {
			
			con = DBUtils.getConnection();
			
			String SELECT_QUERY = "UPDATE product SET is_hide = 1 WHERE seller_username = ?";
			
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY); 
			
			statement.setString(1, username);
		
			
			
			int ans = statement.executeUpdate();
			
			if(ans > 0 ) return true;
			
			
			
			
		} catch (Exception e) {
			
			System.out.println(e);
			
		}
			
			
		return false;
			
		
	}
	
	
	
	
	
	 @Override
	public ResultSet getSellerAllProductsByCategory(String item, String username) {
			try {
				
				con = DBUtils.getConnection();
				String SELECT_QUERY = "SELECT product_id AS Product_ID, product_name AS Product_Name, price_per_piece AS Price_Per_Piece, seller_name AS Seller_Name, quantity, description, category_id AS Category_ID FROM product WHERE CATEGORY_ID = (SELECT CAT_ID FROM CATEGORY WHERE CAT_NAME = ?) AND seller_username = ?";
				
				PreparedStatement statement = con.prepareStatement(SELECT_QUERY);
				
				statement.setString(1, item);
				statement.setString(2, username);
				
				ResultSet set = statement.executeQuery();
				
					
					
					
					return set;
				
				
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
			}
			
			
			
			return null;
			
			
		}
	
	 
	 
	 
	@Override
    public boolean amountWithrowBySeller(double amount,String unsername) {
			
			
			try {
				
				con = DBUtils.getConnection();
				
				String SELECT_QUERY = "UPDATE seller SET income = ? WHERE username = ?";
				
				PreparedStatement statement = con.prepareStatement(SELECT_QUERY); 
				
				statement.setDouble(1, amount);
				statement.setString(2, unsername);
				
			
				
				int ans = statement.executeUpdate();
				
				if(ans > 0) return true;
				
				
				
			} catch (Exception e) {
				
				System.out.println(e);
				
			}
				
				
		
			
			
			
			
			
			return false;
		} 
	
	
	
	
	@Override
	public boolean deleteSeller(String username,String password) {
		
		try {
			
			con = DBUtils.getConnection();
			
			String SELECT_QUERY = "UPDATE seller SET deleted_date = NOW() , is_deleted = 1 WHERE username = ? AND password = ?";
			
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY); 
			
			statement.setString(1, username);
			statement.setString(2, password);
			
			
			int ans = statement.executeUpdate();
			
			if(ans > 0 && this.hideSellerProducts(username)) return true;
			
			
			
			
		} catch (Exception e) {
			
			System.out.println(e);
			
		}
			
			
		return false;
			
		
		
		
	}
	
	
	@Override
	public boolean reopenSeller(String username,String password) {
		
		try {
			
			con = DBUtils.getConnection();
			
			String SELECT_QUERY = "UPDATE seller SET deleted_date = null , is_deleted = 0 WHERE username = ? AND password = ?";
			
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY); 
			
			statement.setString(1, username);
			statement.setString(2, password);
			
			
			int ans = statement.executeUpdate();
			
			if(ans > 0 && this.hideSellerProducts(username)) return true;
			
			
			
			
		} catch (Exception e) {
			
			System.out.println(e);
			
		}
			
			
		return false;
			
		
		
		
	}
	
	
	@Override
	public double getSellerPendingAmount(String username) {
		
		double amount = 0;
		
		
         try {
			
			con = DBUtils.getConnection();
			String SELECT_QUERY = "SELECT pending_amount FROM seller WHERE UserName = ?";
			
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY);
			
			statement.setString(1, username);
	
			
			ResultSet set = statement.executeQuery();
			
				
			if(set.isAfterLast() && set.getRow()==0) return 0;
				
			while (set.next()) {
				
			  amount = set.getDouble(1);
				
			}	
				
				
			
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		
		
		
		
		return amount;
	}
	
	
	@Override
	public boolean addSellerPendingAmount(String username,double newPaidAmount) {
		
		double amount = this.getSellerPendingAmount(username);
		
		amount+=newPaidAmount;
		
		
         try {
			
			con = DBUtils.getConnection();
			String SELECT_QUERY = "UPDATE seller SET pending_amount = ? WHERE UserName = ?";
			
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY);
			
			statement.setDouble(1, amount);
			statement.setString(2, username);
	
			
			if(statement.executeUpdate() > 0 ) return true;
				
			
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		
		
		
		
		return false;
	}
	
	
	
	
	@Override
	public List<Integer> getSellerProductsID(String username){
		
		List<Integer> list = new ArrayList<>();
		
		try {
			
			con = DBUtils.getConnection();
			String SELECT_QUERY = "SELECT product_id FROM product WHERE seller_username = ?";
			
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY);
			
			statement.setString(1, username);
	
			
			ResultSet set = statement.executeQuery();
			
				
			if(set.isAfterLast() && set.getRow()==0) return null;
				
			while (set.next()) {
				
				list.add(set.getInt(1));
				
			}	
				
				
			
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		
		
		
		
		return list;
	}
	
	@Override
	public ResultSet getAllSellerProductsOrderByPrice(String username) {
		
		 
			
			try {
				
				con = DBUtils.getConnection();
				String SELECT_QUERY = "SELECT product_id AS 'Product ID', product_name AS 'Name', price_per_piece AS 'Price Per Unit', quantity AS Quantity, description AS Description, category_id AS 'Category Number' FROM product WHERE seller_username = ? ORDER BY price_per_piece";
				
				
				PreparedStatement statement = con.prepareStatement(SELECT_QUERY);
				
				statement.setString(1, username);
				
				ResultSet set = statement.executeQuery();
				
				statement.setString(1, seller.getSellerUserName());
				
					
					
					
					return set;
					
						
						
		
					
				
				
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
			}
			
			
			
			return null;
			
		
	}
	
    
	@Override
	public boolean sellerPaidPendingAmount(double pendingAamount, String username,  double income) {
		

		 
		  
		
         try {
			
			con = DBUtils.getConnection();
			
			String UPDATE_QUERY = "UPDATE seller set  pending_amount = ? , income = ? FROM seller WHERE UserName = ?";
			
			PreparedStatement statement = con.prepareStatement(UPDATE_QUERY);
			
			statement.setDouble(1, pendingAamount);
			statement.setDouble(2, income);
			statement.setString(3, username);
	
			
              
			if(statement.executeUpdate() > 0) return true;
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		
		
		
		
		return false;
	}
	

	
	

}
