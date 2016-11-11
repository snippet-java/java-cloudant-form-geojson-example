package com.ibm.sample.student.cloudant.geojson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.NoDocumentException;
import com.cloudant.http.Http;
import com.cloudant.http.HttpConnection;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/cloudant/geojson")
public class ListGeo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	CloudantConnectionService cloudantService = new CloudantConnectionService();
    	CloudantClient client = cloudantService.getConnection();	
		JsonObject output = new JsonObject();
	
    	try {
	    	String dbName = "item";
	    	Database db = client.database(dbName, false);
	
	    	JsonObject credentials = cloudantService.getCredentials();
	    	HttpConnection httpResponse = client.executeRequest(
	    			Http.GET(new URL("https://" + credentials.get("username").getAsString() + ".cloudant.com/" + db.info().getDbName() + "/_all_docs?include_docs=true")));
	
	    	JsonParser parser = new JsonParser(); 
			output = parser.parse(httpResponse.responseAsString()).getAsJsonObject();
	    	

	    	output.addProperty("type", "FeatureCollection");
	    	//Feature collection/array
	    	JsonArray features = new JsonArray();
		    	//each feature item to be inserted into features collection
			
			if(output.get("total_rows").getAsInt() > 0) {
				JsonArray dataRow = output.get("rows").getAsJsonArray();
				for(int i=0; i < dataRow.size(); i++) {
					JsonObject data = dataRow.get(i).getAsJsonObject();
					JsonObject doc = data.get("doc").getAsJsonObject();

			    	JsonObject feature = new JsonObject();
			    	feature.addProperty("type", "Feature");
			    	feature.addProperty("id", doc.get("_id").getAsString());

			    	//properties
			    	JsonObject featureProp = new JsonObject();
			    	featureProp.addProperty("updated", "updated");
			    	featureProp.addProperty("title", doc.get("name").getAsString());
			    	feature.add("properties", featureProp);
			    	
			    	//geometry
			    	JsonObject geometry = new JsonObject();
			    	JsonObject point = doc.get("point").getAsJsonObject();
			    	geometry.addProperty("type", point.get("type").getAsString());
			    	geometry.add("coordinates", point.get("coordinates").getAsJsonArray());
			    	feature.add("geometry", geometry);
			    	
			    	features.add(feature);
				}
			}

	    	output.add("features", features);
			
	    	//metadata
	    	JsonObject metadata = new JsonObject();
	    	metadata.addProperty("generated", new Date().getTime());
	    	metadata.addProperty("status", 200);
	    	metadata.addProperty("count", output.get("total_rows").getAsInt());
	    	output.add("metadata", metadata);
	    	
    	} catch(NoDocumentException ex) {
    		output.addProperty("err", "No Database/Document found");
    	}
    	
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
    }

}
