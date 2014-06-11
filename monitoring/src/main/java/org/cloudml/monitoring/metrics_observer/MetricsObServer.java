/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cloudml.monitoring.metrics_observer;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.routing.Template;

public abstract class MetricsObServer extends Component {

	private Class<? extends ResultsHandler> resultHandler;

	public MetricsObServer(int listeningPort, Class<? extends ResultsHandler> resultHandler) {
		super();
		getServers().add(Protocol.HTTP, listeningPort);
		getClients().add(Protocol.FILE);
		this.resultHandler = resultHandler;
		getDefaultHost().attach("", new ObServerApp());
	}
	
	public class ObServerApp extends Application {
		@Override
		public Restlet createInboundRoot() {
			Router router = new Router(getContext());
			router.setDefaultMatchingMode(Template.MODE_EQUALS);

			router.attach("/v1/results", resultHandler);

			return router;
		}
	}
	
	
}
