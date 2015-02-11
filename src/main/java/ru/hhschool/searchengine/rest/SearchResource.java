/**
 * Created by Earlviktor on 11.02.2015.
 */

package ru.hhschool.searchengine.rest;

import ru.hhschool.searchengine.engine.SearchEngine;
import ru.hhschool.searchengine.model.Document;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("engine")
public class SearchResource {

    SearchEngine engine;

    @Inject
    public SearchResource(SearchEngine engine) {
        this.engine = engine;
    }

    @GET
    @Produces("application/json")
    @Path("/search")
    public Response search(@QueryParam("query") String query, @QueryParam("logic") String logic, @QueryParam("count") String count){
        int intCount;
        if(query == null){
            return Response.status(Response.Status.BAD_REQUEST).entity(new ServerMessage("Query was empty")).build();
        }
        if(logic == null){
            logic = "or";
        }else {
            logic = logic.trim();
            if(!logic.equals("or") && !logic.equals("and")){
                return Response.status(Response.Status.BAD_REQUEST).entity(new ServerMessage("Logic type unknown")).build();

            }
        }

        if(count == null){
            intCount = 10;
        }else{
            try{
                intCount = Integer.parseInt(count);
            }catch(NumberFormatException e){
                return Response.status(Response.Status.BAD_REQUEST).entity(new ServerMessage("Count is not a number")).build();
            }
        }

        return Response.status(Response.Status.OK).entity(engine.search(query, logic, intCount)).build();
    }


    @POST
    @Consumes("application/json")
    @Path("/index")
    public Response index(Document document){
        engine.index(document);
        return Response.status(Response.Status.OK).entity(new ServerMessage("Successfully indexed document with id: "+document.getId())).build();
    }
}
