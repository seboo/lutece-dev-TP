/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.bp.web;

import fr.paris.lutece.plugins.bp.business.Project;
import fr.paris.lutece.plugins.bp.business.ProjectHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.resource.ExtendableResourcePluginActionManager;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Project features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageProjects.jsp", controllerPath = "jsp/admin/plugins/bp/", right = "BP_MANAGEMENT" )
public class ProjectJspBean extends ManageBpJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    /**
         *
         */
    private static final long serialVersionUID = 1L;

    // templates
    private static final String TEMPLATE_MANAGE_PROJECTS = "/admin/plugins/bp/manage_projects.html";
    private static final String TEMPLATE_CREATE_PROJECT = "/admin/plugins/bp/create_project.html";
    private static final String TEMPLATE_MODIFY_PROJECT = "/admin/plugins/bp/modify_project.html";

    // Parameters
    private static final String PARAMETER_ID_PROJECT = "id";
    private static final String PARAMETER_COUT_PROJET = "cout";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_PROJECTS = "bp.manage_projects.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_PROJECT = "bp.modify_project.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_PROJECT = "bp.create_project.pageTitle";

    // Markers
    private static final String MARK_PROJECT_LIST = "project_list";
    private static final String MARK_PROJECT = "project";
    private static final String JSP_MANAGE_PROJECTS = "jsp/admin/plugins/bp/ManageProjects.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_PROJECT = "bp.message.confirmRemoveProject";
    private static final String PROPERTY_DEFAULT_LIST_PROJECT_PER_PAGE = "bp.listProjects.itemsPerPage";
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "bp.model.entity.project.attribute.";

    // Views
    private static final String VIEW_MANAGE_PROJECTS = "manageProjects";
    private static final String VIEW_CREATE_PROJECT = "createProject";
    private static final String VIEW_MODIFY_PROJECT = "modifyProject";

    // Actions
    private static final String ACTION_CREATE_PROJECT = "createProject";
    private static final String ACTION_MODIFY_PROJECT = "modifyProject";
    private static final String ACTION_REMOVE_PROJECT = "removeProject";
    private static final String ACTION_CONFIRM_REMOVE_PROJECT = "confirmRemoveProject";

    // Infos
    private static final String INFO_PROJECT_CREATED = "bp.info.project.created";
    private static final String INFO_PROJECT_UPDATED = "bp.info.project.updated";
    private static final String INFO_PROJECT_REMOVED = "bp.info.project.removed";

    //Error
    private static final String ERROR_COUT_VALUE = "bp.validation.projet.Cout.value1";

    // Session variable to store working values
    private Project _project;

    @View( value = VIEW_MANAGE_PROJECTS, defaultView = true )
    public String getManageProjects( HttpServletRequest request )
    {
        _project = null;

        List<Project> listProjects = (List<Project>) ProjectHome.getProjectsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_PROJECT_LIST, listProjects, JSP_MANAGE_PROJECTS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_PROJECTS, TEMPLATE_MANAGE_PROJECTS, model );
    }

    /**
     * Returns the form to create a project
     *
     * @param request The Http request
     * @return the html code of the project form
     */
    @View( VIEW_CREATE_PROJECT )
    public String getCreateProject( HttpServletRequest request )
    {
        _project = ( _project != null ) ? _project : new Project(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_PROJECT, _project );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_PROJECT, TEMPLATE_CREATE_PROJECT, model );
    }

    /**
     * Process the data capture form of a new project
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_PROJECT )
    public String doCreateProject( HttpServletRequest request )
    {
        int _ncout;

        populate( _project, request );

        // Check constraints
        if ( !validateBean( _project, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_PROJECT );
        }

        _ncout = Integer.parseInt( request.getParameter( PARAMETER_COUT_PROJET ) );

        //Check cout constraint   
        if ( !isMultip_5( _ncout ) )
        {
            addError( ERROR_COUT_VALUE, getLocale(  ) );

            return redirectView( request, VIEW_CREATE_PROJECT );
        }

        ProjectHome.create( _project );
        addInfo( INFO_PROJECT_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_PROJECTS );
    }

    /**
     * Manages the removal form of a project whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_PROJECT )
    public String getConfirmRemoveProject( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PROJECT ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_PROJECT ) );
        url.addParameter( PARAMETER_ID_PROJECT, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_PROJECT,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a project
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage projects
     */
    @Action( ACTION_REMOVE_PROJECT )
    public String doRemoveProject( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PROJECT ) );
        ProjectHome.remove( nId );
        addInfo( INFO_PROJECT_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_PROJECTS );
    }

    /**
     * Returns the form to update info about a project
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_PROJECT )
    public String getModifyProject( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PROJECT ) );

        if ( ( _project == null ) || ( _project.getId(  ) != nId ) )
        {
            _project = ProjectHome.findByPrimaryKey( nId );
        }
        //plugin extends
       
        Map<String, Object> model = getModel(  );
        model.put( MARK_PROJECT, _project );
        ExtendableResourcePluginActionManager.fillModel( request, getUser( ), model, String.valueOf(nId), Project.PROPERTY_RESOURCE_TYPE );
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_PROJECT, TEMPLATE_MODIFY_PROJECT, model );
    }

    /**
     * Process the change form of a project
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_PROJECT )
    public String doModifyProject( HttpServletRequest request )
    {
        populate( _project, request );

        int _ncout = Integer.parseInt( request.getParameter( PARAMETER_COUT_PROJET ) );

        //Check cout constraint   
        if ( !isMultip_5( _ncout ) )
        {
            addError( ERROR_COUT_VALUE, getLocale(  ) );

            return redirectView( request, VIEW_CREATE_PROJECT );
        }

        // Check constraints
        if ( !validateBean( _project, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_PROJECT, PARAMETER_ID_PROJECT, _project.getId(  ) );
        }

        ProjectHome.update( _project );
        addInfo( INFO_PROJECT_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_PROJECTS );
    }

    //Check cout constraint
    private boolean isMultip_5( int n )
    {
        if ( ( n % 5 ) == 0 )
        {
            return true;
        }

        return false;
    }
}
