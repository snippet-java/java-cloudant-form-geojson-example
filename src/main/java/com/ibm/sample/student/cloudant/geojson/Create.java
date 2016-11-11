package com.ibm.sample.student.cloudant.geojson;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@WebServlet(urlPatterns={"/cloudant/create", "/cloudant/set"})
public class Create extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
    	CloudantClient client = new CloudantConnectionService().getConnection();

		String docId = request.getParameter("id");
		String name = request.getParameter("name");
		String desc = request.getParameter("description");
		String rawPoint = request.getParameter("point");
    	
		//populate the geojson with info from Request
    	JsonObject geoJson = new JsonObject();
    	geoJson.addProperty("_id", docId);
    	geoJson.addProperty("name", name);
    	geoJson.addProperty("description", desc);
    	JsonObject point = new JsonObject();
		String[] rawCoors = rawPoint.split(",");
		point.addProperty("type", "Point");
		JsonArray coors = new JsonArray();
		if(rawCoors.length == 2) {
			for(String coor : rawCoors) {
				coors.add(Float.parseFloat(coor));
			}
		}
		//set to default coor (-78,50) if no valid coor found
		else {
			coors.add(-78);
			coors.add(50);
		}
		point.add("coordinates", coors);
		geoJson.add("point", point);

    	String dbname = "item";
    	Database db = client.database(dbname, false);
    	Response dbResponse = db.save(geoJson);
    		
		JsonObject output = new JsonObject();
		//for success insertion
		if(dbResponse.getStatusCode() < 400) {
			output.add("doc", geoJson);	
	    
			//dbResponse json data
	    	JsonObject dbResponseJson = new JsonObject();
	    	dbResponseJson.addProperty("status", dbResponse.getStatusCode() + " - " + dbResponse.getReason());
	    	dbResponseJson.addProperty("id", dbResponse.getId());
	    	dbResponseJson.addProperty("rev", dbResponse.getRev());
	    
			output.add("data", dbResponseJson);
		}
		else {
			output.addProperty("err", dbResponse.getStatusCode() + " - " + dbResponse.getReason());
		}
		out.println(output);
    }

    private static final long serialVersionUID = 1L;
}
