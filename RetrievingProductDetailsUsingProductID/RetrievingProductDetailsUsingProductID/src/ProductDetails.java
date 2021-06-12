import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecommerce.DBConnection;
/**
 * Servlet implementation class ProductDetails
 */
@WebServlet("/ProductDetails")
public class ProductDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ProductDetails() {
    	super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/html;charset=UTF-8");
        
        try {
            PrintWriter out = response.getWriter();
            
            
           InputStream in = getServletContext().getResourceAsStream("/WEB-INF/config.properties");
           Properties props = new Properties();
           props.load(in);
           
           DBConnection conn = new DBConnection(props.getProperty("url"), props.getProperty("userid"), props.getProperty("password"));
           Statement stmt = conn.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           
           String product_ID = request.getParameter("product_id");
           
           Integer productID;
           
           if (!product_ID.equals(null) && !product_ID.equals(""))
           {productID =Integer.valueOf(product_ID);}
           else {productID = null;}
           
           ResultSet rst = null;
           
           if(productID != null)
           {rst = stmt.executeQuery("select * from eproduct where eproduct.ID=" + product_ID);}


           
           if(rst.next())
           {
               RequestDispatcher rs = request.getRequestDispatcher("ProductInformation");
               request.setAttribute("ID", rst.getInt("ID") );
               request.setAttribute("name", rst.getString("name") );
               request.setAttribute("Date", rst.getTimestamp("date_added") );
               while (rst.next()) {
            	   request.setAttribute("ID", rst.getInt("ID") );
                   request.setAttribute("name", rst.getString("name") );
                   request.setAttribute("Date", rst.getTimestamp("date_added") );
           }

               rs.forward(request, response);
           }
           else
           {
              out.println("Invalid Production ID");
              RequestDispatcher rs = request.getRequestDispatcher("index.html");
              rs.include(request, response);

   	}
           
           stmt.close(); 
           conn.closeConnection();
           
   } catch (ClassNotFoundException e) {
           e.printStackTrace();
   } catch (SQLException e) {
           e.printStackTrace();
   }

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
