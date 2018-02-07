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
package org.energy_home.jemma.javagal.json.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.energy_home.jemma.javagal.json.constants.Resources;
import org.energy_home.jemma.zgd.GatewayConstants;
import org.energy_home.jemma.zgd.GatewayInterface;
import org.energy_home.jemma.zgd.jaxb.Address;
import org.energy_home.jemma.zgd.jaxb.Info;
import org.energy_home.jemma.zgd.jaxb.ServiceDescriptor;

public class NodeServicesServlet extends CommonServlet {
	private static final long serialVersionUID = 1L;

	GatewayInterface gatewayInterface;

	public NodeServicesServlet(GatewayInterface _gatewayInterface) {
		gatewayInterface = _gatewayInterface;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			long timeout = getLongParameter(request, Resources.URI_PARAM_TIMEOUT);
			Address address = getAddressParameter(request, Resources.URI_ADDR);
			short endpoint = (short) getLongParameter(request, Resources.PARAMETER_ENDPOINT);

			try {
				ServiceDescriptor serviceDescriptor = gatewayInterface.getServiceDescriptorSync(timeout, address, endpoint);

				Info.Detail detail = new Info.Detail();
				detail.setServiceDescriptor(serviceDescriptor);

				sendResult(response, detail);
			} catch (Throwable e) {
				sendErrorReply(response, GatewayConstants.GENERAL_ERROR, e.getMessage());
			}
		} catch (IllegalArgumentException e) {
			sendErrorReply(response, GatewayConstants.GENERAL_ERROR, e.getMessage());
		}
	}

}
