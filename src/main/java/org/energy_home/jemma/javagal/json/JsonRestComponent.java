/**
 * This file is part of JEMMA - http://jemma.energy-home.org
 * (C) Copyright 2013 Telecom Italia (http://www.telecomitalia.it)
 *
 * JEMMA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License (LGPL) version 3
 * or later as published by the Free Software Foundation, which accompanies
 * this distribution and is available at http://www.gnu.org/licenses/lgpl.html
 *
 * JEMMA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License (LGPL) for more details.
 */
package org.energy_home.jemma.javagal.json;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.energy_home.jemma.zgd.GatewayInterface;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class JsonRestComponent {

	private final Logger LOG = LoggerFactory.getLogger("REST-JSON");
	private HttpService httpService;
	
	private ServletContainer servlets;

	protected void activate() {
		LOG.debug("Activated");		
	}

	protected void deactivate() {
		LOG.debug("Dectivated");
	}
	
	protected void bindHttpService(HttpService s) {
		this.httpService = s;
	}

	protected void unbindHttpService(HttpService s) {
		if (this.httpService == s) {
			this.httpService = null;
		}
	}

	@SuppressWarnings("unchecked")
	protected void bindComponentFactory(ComponentFactory s) {
		ComponentInstance instance = s.newInstance(null);
		GatewayInterface gatewayInterface = (GatewayInterface) instance.getInstance();	
		servlets = new ServletContainer(httpService, gatewayInterface);
		servlets.register();
		LOG.debug("Bound ComponentFactory");
	}

	protected void unbindComponentFactory(ComponentFactory s) {
		LOG.debug("Unbind ComponentFactory");
		servlets.unregister();
	}
}
