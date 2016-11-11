package com.ibm.sample.student.cloudant.geojson;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.DocumentConflictException;
import com.cloudant.client.org.lightcouch.NoDocumentException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/cloudant/update")
public class Update extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
    	
    	CloudantClient client = new CloudantConnectionService().getConnection();	
		JsonObject output = new JsonObject();

		String docId = request.getParameter("id");
		String name = request.getParameter("name");
		String desc = request.getParameter("description");
		String rawPoint = request.getParameter("point");
		JsonObject point = null;
		String[] rawCoors = rawPoint.split(",");
		if(rawCoors.length == 2) {
			point = new JsonObject();
			point.addProperty("type", "Point");
			JsonArray coors = new JsonArray();
			for(String coor : rawCoors) {
				coors.add(Float.parseFloat(coor));
			}
			point.add("coordinates", coors);
		}

		//to determine whether to create new or update existing doc
		boolean createNew = false;
		
		try {
	    	String dbName = "item";
	    	Database db = client.database(dbName, false);
	
	    	db.find(docId);
	    	InputStream is = db.find(docId);
			int i;
			char c;
			String doc = "";
			while((i=is.read())!=-1)
	         {
	            c=(char)i;
	            doc += c;
	         }
			JsonParser parser = new JsonParser();
			JsonObject docJson = parser.parse(doc).getAsJsonObject();
			docJson.addProperty("name", name);
			docJson.addProperty("description", desc);
			if(point != null)
				docJson.add("point", point);
			
			db.update(docJson);
			output.add("doc", docJson);
	    	
    	} catch(NoDocumentException ex) {
    		createNew = true;
    		output.addProperty("err", ex.getReason());
    	} catch(DocumentConflictException ex) {
    		output.addProperty("err", ex.getReason());
    	}
		//to create new document
		if(createNew) {
			new Create().doGet(request, response);
		}
		else {
			out.println(output);
		}
		
    }

}
