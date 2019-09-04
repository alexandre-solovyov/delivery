package delivery;

import org.springframework.stereotype.Component;
import org.apache.poi.xssf.usermodel.*;
import org.hibernate.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;


@Component
public class ProductDao extends GenericDao {

    @SuppressWarnings({ "deprecation", "unchecked" })
    public List<Product> findAll() {

        List<Product> products = MyTransaction.session.createQuery("From Product").list();
        return products;
    }
    
    @SuppressWarnings({ "deprecation", "unchecked" })
    public Product getByCode(int code) {

		Query<Product> query = MyTransaction.session.createQuery("From Product where code=:code");
    	query.setParameter("code", code);
    	List<Product> products = query.list();
    	if(products.isEmpty())
    		return null;
    	else
    		return products.get(0);
    }

    public void readFromExcel(byte[] bytes, String sheetName, User producer,
    		                  MyTransaction tr) throws IOException {

    	try {
    		XSSFWorkbook excelBook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
    		if(sheetName.length()==0)
    			sheetName = excelBook.getSheetName(0);
    		//System.out.println("Sheet: " + sheetName);
    		
    		XSSFSheet excelSheet = excelBook.getSheet(sheetName);
    	
    		//System.out.println("readFromExcel");
    		//System.out.println(excelSheet.getFirstRowNum());
    		//System.out.println(excelSheet.getLastRowNum());
    	
    		int first = excelSheet.getFirstRowNum() + 1;
    		int last = excelSheet.getLastRowNum();
    		for(int i=first; i<=last; i++)
    		{
    			XSSFRow row = excelSheet.getRow(i);
    		
    			int code = Integer.parseInt(row.getCell(0).getRawValue());
    			String name = row.getCell(1).getStringCellValue();
    			double price =  Double.parseDouble(row.getCell(2).getRawValue());
    		
    			//System.out.println(code + " " + name + ": " + price);
    		
    			Product product = getByCode(code);
    			if(product==null)
    			{
    				product = new Product(code, name, price, producer);
    				MyTransaction.session.save(product);
    			}
    			else {
    				product.setName(name);
    				product.setPrice(price);
    				product.setProducer(producer);
    				MyTransaction.session.update(product);
    			}
    		}
        
    		excelBook.close();
    	}
    	catch(Exception e) {
    		tr.rollback();
    	}
    }
}
