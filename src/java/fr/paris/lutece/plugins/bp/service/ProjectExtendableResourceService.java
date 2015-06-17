package fr.paris.lutece.plugins.bp.service;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.bp.business.Project;
import fr.paris.lutece.plugins.bp.business.ProjectHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.portal.service.resource.IExtendableResourceService;

public class ProjectExtendableResourceService implements IExtendableResourceService
{
	
	private static final String MESSAGE_PROJECT_RESOURCE_TYPE_DESCRIPTION = "bp.message.resource"; 
	
	
	/**
	 * 
	 */
	@Override
	public boolean isInvoked( String strResourceType )
	{
		
		return Project.PROPERTY_RESOURCE_TYPE.equals( strResourceType );
	}

	/**
	 * 
	 */
	@Override
	public IExtendableResource getResource( String strIdResource, String strResourceType )
	{
		if ( StringUtils.isNotBlank( strIdResource ) && StringUtils.isNumeric( strIdResource ) )
		{
			int nIdProject = Integer.parseInt( strIdResource );
			return ProjectHome.findByPrimaryKey( nIdProject );
		}
		return null;
	}

     /**
	 *
	 */
	@Override
	public String getResourceType( )
	{
		return Project.PROPERTY_RESOURCE_TYPE;
	}

       /**
	 * {@inheritDoc}
	 */
	@Override
	public String getResourceTypeDescription( Locale locale )
	{
		return I18nService.getLocalizedString( MESSAGE_PROJECT_RESOURCE_TYPE_DESCRIPTION, locale );
	}


	@Override
	public String getResourceUrl(String strIdResource, String strResourceType) {
		if ( StringUtils.isNotBlank( strIdResource ) && StringUtils.isNumeric( strIdResource ) )
        {

        }

        return null;
		//return Project.PROPERTY_RESOURCE_TYPE;
	}
}
