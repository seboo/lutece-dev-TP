package fr.paris.lutece.plugins.bp.service;

import java.util.Collection;

import fr.paris.lutece.plugins.bp.business.Project;
import fr.paris.lutece.plugins.bp.business.ProjectHome;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.security.LuteceUser;

public class ProjectCacheService extends AbstractCacheableService
{
	   private static final String CACHE_NAME = "project.projectCacheService";
	   private static final String KEY_CACHE = "ListeProjectFromCache";

	   private static ProjectCacheService _intance = new ProjectCacheService();
	   
	   private ProjectCacheService()
	   {
	       initCache();
	   }

	   public static ProjectCacheService getInstance(){
		 
		   return _intance;
	   }
	   
	   @Override
	   public String getName(  )
	   {
	       return CACHE_NAME;
	   }
	   
	   public String getKeyProject(  )
	   {
	       return KEY_CACHE;
	   }

	   public Project getProjectFromCache( int int_Id)
	   {
		   
		   Collection<Project> liste = this.getListeProjectFromCache();
		   Project r =null;
		   
		   for (Project project : liste) {
		        if (project.getId() == int_Id) {
		            r = project;
		            break;
		        }
		    }
		 
	        return r;
	    }
	   
	   public Collection<Project> getListeProjectFromCache()
	   {
		 
		   Collection<Project> r = (Collection<Project>) getFromCache(KEY_CACHE );
		   
		  
	        if( r == null )
	        { 
	            r = ProjectHome.getProjectsList();
	            putInCache( KEY_CACHE , r );
	        }
	        return r;
	    }
	   
	
	   private String getCacheKey( int Int_Id )
	   {
	        StringBuilder sbKey = new StringBuilder();
	        sbKey.append( "[project:" ).append( Int_Id ).append(" Cache]");
	        return sbKey.toString();
	   }
	}
