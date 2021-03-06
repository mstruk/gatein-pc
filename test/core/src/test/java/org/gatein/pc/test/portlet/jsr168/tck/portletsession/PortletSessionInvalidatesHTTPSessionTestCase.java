/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2006, Red Hat Middleware, LLC, and individual                    *
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
package org.gatein.pc.test.portlet.jsr168.tck.portletsession;

import org.gatein.pc.test.unit.annotations.TestCase;
import org.gatein.pc.test.unit.Assertion;
import org.gatein.pc.test.unit.PortletTestCase;
import org.gatein.pc.test.unit.PortletTestContext;
import org.gatein.pc.test.unit.actions.PortletRenderTestAction;
import org.gatein.pc.test.portlet.framework.UTP1;
import org.jboss.unit.driver.DriverResponse;
import org.jboss.unit.driver.response.EndTestResponse;
import static org.jboss.unit.api.Assert.assertNotNull;
import static org.jboss.unit.api.Assert.assertEquals;

import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
@TestCase({Assertion.JSR168_116})
public class PortletSessionInvalidatesHTTPSessionTestCase
{
   public PortletSessionInvalidatesHTTPSessionTestCase(PortletTestCase seq)
   {
      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws IOException, PortletException
         {
            try
            {
               HttpSessionEvents.activate();
               PortletSession session = request.getPortletSession();
               assertNotNull(session);
               String sessionId = session.getId();
               List events = HttpSessionEvents.getEvents();
               assertEquals(1, events.size());
               HttpSessionEvents.Event createdEvent = (HttpSessionEvents.Event)events.get(0);
               assertEquals(sessionId, createdEvent.getSessionId());
               assertEquals(HttpSessionEvents.Event.CREATED, createdEvent.getType());
               HttpSessionEvents.desactivate();

               //
               HttpSessionEvents.activate();
               session.invalidate();
               events = HttpSessionEvents.getEvents();
               assertEquals(1, events.size());
               HttpSessionEvents.Event destroyedEvent = (HttpSessionEvents.Event)events.get(0);
               assertEquals(sessionId, destroyedEvent.getSessionId());
               assertEquals(HttpSessionEvents.Event.DESTROYED, destroyedEvent.getType());
               HttpSessionEvents.desactivate();
            }
            finally
            {
               // Cleanup
               HttpSessionEvents.desactivate();
            }

            //
            return new EndTestResponse();
         }
      });
   }
}
