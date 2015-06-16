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
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Project xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = "project", pageTitleI18nKey = "bp.xpage.project.pageTitle", pagePathI18nKey = "bp.xpage.project.pagePathLabel" )
public class ProjectXPage extends MVCApplication
{
    // Templates
    private static final String TEMPLATE_MANAGE_PROJECTS = "/skin/plugins/bp/manage_projects.html";
    private static final String TEMPLATE_DETAILS_PROJECT = "/skin/plugins/bp/details_project.html";

    // Parameters
    private static final String PARAMETER_ID_PROJECT = "id";

    // Markers
    private static final String MARK_PROJECT_LIST = "project_list";
    private static final String MARK_PROJECT = "project";

    // Views
    private static final String VIEW_MANAGE_PROJECTS = "manageProjects";
    private static final String VIEW_DETAILS_PROJECT = "detailsProject";

    // Session variable to store working values
    private Project _project;

    @View( value = VIEW_MANAGE_PROJECTS, defaultView = true )
    public XPage getManageProjects( HttpServletRequest request )
    {
        _project = null;

        Map<String, Object> model = getModel(  );
        model.put( MARK_PROJECT_LIST, ProjectHome.getProjectsList(  ) );

        return getXPage( TEMPLATE_MANAGE_PROJECTS, request.getLocale(  ), model );
    }

    /**
     *
     * @param request
     * @return
     */
    @View( VIEW_DETAILS_PROJECT )
    public XPage getDetailsProjects( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PROJECT ) );

        if ( ( _project == null ) || ( _project.getId(  ) != nId ) )
        {
            _project = ProjectHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_PROJECT, _project );

        return getXPage( TEMPLATE_DETAILS_PROJECT, request.getLocale(  ), model );
    }
}
