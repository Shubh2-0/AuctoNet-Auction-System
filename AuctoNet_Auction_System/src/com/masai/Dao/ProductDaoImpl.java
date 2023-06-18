package com.masai.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.masai.Dto.Product;
import com.masai.Dto.ProductImpl;

public class ProductDaoImpl implements ProductDao {
	
	
	@Override
	public boolean updateQuantity(int productId, int quantity) {
		
		Connection con = null;

		try {
			
			con = DBUtils.getConnection();
			
			String UPDATE_QUERY2 = "UPDATE PRODUCT SET quantity = ? WHERE product_id = ?";
			PreparedStatement statement3 = con.prepareStatement(UPDATE_QUERY2);
			
			statement3.setInt(1, quantity);
			statement3.setInt(2, productId);
			
			int ans3 = statement3.executeUpdate();
			
			System.out.println(ans3+" UPDATE PRODUCT");
			
			if(ans3 > 0) return true;
			
			
		} catch (Exception e) {
			
			
			
		}finally {
			
			DBUtils.closeConnection(con);
			
		}
			
			
			
			
			return false;
			
			
	
		
		
		
		
	}
	
	
	@Override
	public int getGStPercentage(int productId) {
		
		
		Connection con = null;
		int gst = 0;

		try {
			
			con = DBUtils.getConnection();
			
			String UPDATE_QUERY2 = "SELECT GST_PER FROM CATEGORY WHERE CAT_ID = (SELECT CATEGORY_ID FROM PRODUCT WHERE PRODUCT_ID = ?)";
			PreparedStatement statement3 = con.prepareStatement(UPDATE_QUERY2);
			
			
			statement3.setInt(1, productId);
			
		   ResultSet set = statement3.executeQuery();
		   
		   
		   
		   
		   while(set.next()) {
			   
			   gst = set.getInt("gst_per");
			   
		   }
		   
		   
			
		   
			
			
		} catch (Exception e) {
			
			
			
		}finally {
			
			DBUtils.closeConnection(con);
			
		}
			
			
			
			
			
			
			
			return gst;
	
		
		
		
		
		
		
	}
	
	
	@Override
	public Product getProductById(int productId) {
		
		Connection con = null;
		Product product = null;

		try {
			
			con = DBUtils.getConnection();
			
			String SELECT_QUERY = "SELECT * FROM product WHERE product_id = ?";
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY);
			
			
			statement.setInt(1, productId);
			
		   ResultSet set = statement.executeQuery();
		   
		   if(set.isAfterLast() && set.getRow()==0) return null;
		   
		   
		   while(set.next()) {
			   
			   product = new ProductImpl();
			   
			   product.setProductCategoryId(set.getInt("category_id"));
			   product.setProductDescription(set.getString("description"));
			   product.setProductName(set.getString("product_name"));
			   product.setProductPrice(set.getDouble("price_per_piece"));
			   product.setProductSoldStatus(set.getInt("sold_status"));
			   product.setProductQuantity(set.getInt("quantity"));
			   product.setReturnPolicy(set.getInt("return_policy"));
			   
			   
		   }

			
			
		} catch (Exception e) {
			
			
			
		}finally {
			
			DBUtils.closeConnection(con);
			
		}
			
			
			
			
			
			
			
			return product;
	
		
		
		
		
	}
   
	
	@Override
	public boolean updateProductBySeller(Product product, int id) {
		
		
		Connection con = null;

		try {
			
			con = DBUtils.getConnection();
			
			String UPDATE_QUERY = "UPDATE product SET product_name = ?, price_per_piece = ?, quantity = ?, description = ?, return_policy = ? WHERE product_id = ?";
			PreparedStatement statement = con.prepareStatement(UPDATE_QUERY);
			
			
			statement.setString(1, product.getProductName());
			statement.setDouble(2, product.getProductPrice());
			statement.setInt(3, product.getProductQuantity());
			statement.setString(4, product.getProductDescription());
			statement.setInt(5, product.getReturnPolicy());
			statement.setInt(6, id);
			
			
			
			
		   if(statement.executeUpdate() > 0) return true;
		   
			
			
		} catch (Exception e) {
			
			System.out.println(e);
			e.printStackTrace();
			
			
		}finally {
			
			DBUtils.closeConnection(con);
			
		}
		
	
		
		
		return false;
	}
	
	
	@Override
	public boolean addProductBySeller(Product product) {
		Connection con = null;

		
		
		try {
			
			con = DBUtils.getConnection();
			
			String INSERT_QUERY = "INSERT INTO product ("
					+ "product_name, "
					+ "price_per_piece, "
					+ "seller_unique_num, "
					+ "seller_username, "
					+ "seller_name, "
					+ "quantity, "
					+ "description, "
					+ "category_id, "
					+ "sold_status, "
					+ "is_hide, "
					+ "return_policy) "
					+ "VALUES(?, ?, (SELECT SellerId FROM seller WHERE UserName = ?), ?, ?, ?, ?, ?, ?, ?, ?)";
			
			
			PreparedStatement statement = con.prepareStatement(INSERT_QUERY);
			
			statement.setString(1, product.getProductName());
			statement.setDouble(2, product.getProductPrice());
			statement.setString(3, product.getSellerId());
			statement.setString(4, product.getSellerId());
			statement.setString(5, product.getSellerName());
			statement.setInt(6, product.getProductQuantity());
			statement.setString(7, product.getProductDescription());
			statement.setInt(8, product.getProductCategoryId());
			statement.setInt(9, product.getProductSoldStatus());
			statement.setInt(10, 0);
			statement.setInt(11, product.getReturnPolicy());
			
			
			
		
			
		   if(statement.executeUpdate() > 0) return true;
		   
			
			
		} catch (Exception e) {
			
			System.out.println(e);
			e.printStackTrace();
			
			
		}finally {
			
			DBUtils.closeConnection(con);
			
		}
		
	
		
		
		return false;
	}

	
	@Override
	public int getProductIdByCategoryName(String catgoryName) {
		
		int n = 0;
		
		
		Connection con = null;

		try {
			
			con = DBUtils.getConnection();
			
			String SELECT_QUERY = "SELECT cat_id FROM category WHERE  cat_name = ?";
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY);
			
			
			statement.setString(1, catgoryName);
			
			ResultSet set = statement.executeQuery();
			
			if(set.isAfterLast() && set.getRow() == 0) return 0;
			
			while(set.next()) {
				
				n = set.getInt("cat_id");
				
			}
			

		   
			
			
		} catch (Exception e) {
			
			System.out.println(e);
			e.printStackTrace();
			
			
		}finally {
			
			DBUtils.closeConnection(con);
			
		}
		
	
		
		
		
		
		return n;
		
	}
	

	
	
}
