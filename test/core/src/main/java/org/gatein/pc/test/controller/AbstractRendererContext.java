/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2008, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package org.gatein.pc.test.controller;

import org.gatein.pc.controller.PortletControllerContext;
import org.gatein.pc.controller.PortletController;
import org.gatein.pc.controller.state.PortletPageNavigationalState;
import org.gatein.pc.controller.impl.AbstractPortletControllerContext;
import org.gatein.pc.api.invocation.response.PortletInvocationResponse;
import org.gatein.pc.api.PortletInvokerException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.util.List;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public abstract class AbstractRendererContext implements RendererContext
{

   /** . */
   private final AbstractPortletControllerContext portletControllerContext;

   protected AbstractRendererContext(AbstractPortletControllerContext portletControllerContext)
   {
      this.portletControllerContext = portletControllerContext;
   }

   public HttpServletRequest getClientRequest()
   {
      return portletControllerContext.getClientRequest();
   }

   public HttpServletResponse getClientResponse()
   {
      return portletControllerContext.getClientResponse();
   }

   public PortletControllerContext getPortletControllerContext()
   {
      return portletControllerContext;
   }

   public PortletInvocationResponse render(List<Cookie> cookies, PortletPageNavigationalState pageNavigationalState, String windowId) throws PortletInvokerException
   {
      return new PortletController().render(portletControllerContext, cookies, pageNavigationalState, windowId);
   }
}
