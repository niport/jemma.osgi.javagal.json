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
 *
 */
package org.energy_home.jemma.javagal.json;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.energy_home.jemma.javagal.json.constants.ResourcePathURIs;
import org.energy_home.jemma.javagal.json.constants.Resources;
import org.energy_home.jemma.javagal.json.servlet.AllLqiInformationsServlet;
import org.energy_home.jemma.javagal.json.servlet.AllPermitJoinServlet;
import org.energy_home.jemma.javagal.json.servlet.ChannelServlet;
import org.energy_home.jemma.javagal.json.servlet.FrequencyAgilityServlet;
import org.energy_home.jemma.javagal.json.servlet.GetInfoBaseAttributesServlet;
import org.energy_home.jemma.javagal.json.servlet.LocalServicesServlet;
import org.energy_home.jemma.javagal.json.servlet.NodeDescriptorAndServicesServlet;
import org.energy_home.jemma.javagal.json.servlet.NodeServicesServlet;
import org.energy_home.jemma.javagal.json.servlet.RecoveryGalServlet;
import org.energy_home.jemma.javagal.json.servlet.ResetServlet;
import org.energy_home.jemma.javagal.json.servlet.StartUpServlet;
import org.energy_home.jemma.javagal.json.servlet.VersionServlet;
import org.energy_home.jemma.javagal.json.servlet.WsnNodesServlet;
import org.energy_home.jemma.zgd.GatewayInterface;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class register on the HttpService all the servlets.
 * 
 * TODO: Use whiteboard pattern.
 * 
 * TODO: Let CommonServlet have a method that returns the root URL of the servlet itself!
 * 
 * TODO: we can remember the registered servlet resources.
 * 
 */
public class ServletContainer implements HttpSessionListener {

	private final Logger LOG = LoggerFactory.getLogger("REST-JSON");

	HttpService http;
	GatewayInterface gatewayInterface;
	String prefix = "/json";

	public ServletContainer(HttpService service, GatewayInterface _gatewayInterface) {
		this.http = service;
		gatewayInterface = _gatewayInterface;
	}

	public void register() {
		try {
			
			/* json/version */
			http.registerServlet(prefix + Resources.GW_ROOT_URI + ResourcePathURIs.VERSION, new VersionServlet(gatewayInterface), null,
					null);
			/* json/net/default/channel */
			http.registerServlet(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.CHANNEL, new ChannelServlet(gatewayInterface),
					null, null);
			/*
			 * json/net/default/wsnnodes GET
			 * 
			 * Leave DELETE
			 */
			http.registerServlet(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.WSNNODES, new WsnNodesServlet(gatewayInterface),
					null, null);
			
			
			/* json/net/default/allwsnnodes/lqi */
			http.registerServlet(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.ALLWSNNODES + ResourcePathURIs.LQIINFORMATION,
					new AllLqiInformationsServlet(gatewayInterface), null, null);
			/*
			 * json/net/default/localnode/frequencyagility?timeout={0:x8}&
			 * scanChannel={1:x2}&scanDuration={2:x2}
			 */
			http.registerServlet(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.URI_FREQUENCY_AGILITY,
					new FrequencyAgilityServlet(gatewayInterface), null, null);
			/*
			 * json/net/default/wsnnodes/nodedescriptorservicelist?timeout={0:x8}
			 * &address={1:x2/8}
			 */
			http.registerServlet(
					prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.WSNNODES + ResourcePathURIs.NODEDESCRIPTORSERVICELIST,
					new NodeDescriptorAndServicesServlet(gatewayInterface), null, null);

			/*
			 * Defines Reset route "json/reset?timeout={0,08x}&startMode={1,01x}"
			 */
			http.registerServlet(prefix + Resources.GW_ROOT_URI + ResourcePathURIs.RESET, new ResetServlet(gatewayInterface), null,
					null);
			/*
			 * Defines PermitjoinAll route "json/net/default/allwsnnodes/permitjoin"
			 */
			http.registerServlet(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.ALLPERMIT_JOIN,
					new AllPermitJoinServlet(gatewayInterface), null, null);
			/*
			 * Defines StartupGatewayDevice route
			 * "json/startup?timeout={0,08x}&start=true" POST Defines
			 * readStartupAttributeSet route
			 * "json/startup?timeout={0,08x}&index={1,01x}" GET
			 */
			http.registerServlet(prefix + Resources.GW_ROOT_URI + ResourcePathURIs.STARTUP, new StartUpServlet(gatewayInterface), null,
					null);
			/*
			 * Defines GetServiceDescriptor route
			 * "json/net/default/wsnnodes/services/"
			 */
			http.registerServlet(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.WSNNODES + ResourcePathURIs.SERVICES,
					new NodeServicesServlet(gatewayInterface), null, null);
			/*
			 * Defines LocalServices route "/net/default/localnode/services"
			 */
			http.registerServlet(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.LOCALNODE_SERVICES,
					new LocalServicesServlet(gatewayInterface), null, null);

			/*
			 * Defines InfoBase route "/net/default/ib"
			 */
			http.registerServlet(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.INFOBASE,
					new GetInfoBaseAttributesServlet(gatewayInterface), null, null);

			http.registerServlet(prefix + Resources.GW_ROOT_URI + ResourcePathURIs.RECOVERY, new RecoveryGalServlet(gatewayInterface),
					null, null);

		} catch (ServletException e) {
			LOG.error("Exception", e);
		} catch (NamespaceException e) {
			LOG.error("Exception", e);
		}
	}

	public void unregister() {
		/* Remove route json/version */
		http.unregister(prefix + Resources.GW_ROOT_URI + ResourcePathURIs.VERSION);
		/* Remove route json/net/default/channel */
		http.unregister(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.CHANNEL);
		/* Remove route json/net/default/wsnnodes */
		http.unregister(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.WSNNODES);
		/* Remove route json/net/default/allwsnnodes/lqi */
		http.unregister(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.ALLWSNNODES + ResourcePathURIs.LQIINFORMATION);
		/* Remove route json/net/default/localnode/frequencyagility */
		http.unregister(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.URI_FREQUENCY_AGILITY);
		/*
		 * Remove route
		 * json/net/default/wsnnodes/nodedescriptorservicelist?timeout={0:x8}&
		 * address={1:x2/8}
		 */
		http.unregister(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.WSNNODES + ResourcePathURIs.NODEDESCRIPTORSERVICELIST);

		/*
		 * Remove route "json/reset"
		 */
		http.unregister(prefix + Resources.GW_ROOT_URI + ResourcePathURIs.RESET);

		/*
		 * Remove route "/net/default/allwsnnodes/permitjoin"
		 */
		http.unregister(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.ALLPERMIT_JOIN);

		/*
		 * Remove route "json/startup"
		 */
		http.unregister(prefix + Resources.GW_ROOT_URI + ResourcePathURIs.STARTUP);
		/*
		 * Remove route "json/net/default/wsnnodes/services/"
		 */
		http.unregister(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.WSNNODES + ResourcePathURIs.SERVICES);
		/*
		 * Defines LocalServices route "/net/default/localnode/services"
		 */
		http.unregister(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.LOCALNODE_SERVICES);

		/*
		 * Defines InfoBaseAttribute route "/net/default/ib"
		 */
		http.unregister(prefix + Resources.NWT_ROOT_URI + ResourcePathURIs.INFOBASE);

	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {

	}
}
