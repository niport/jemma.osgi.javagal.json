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
import org.energy_home.jemma.zgd.jaxb.Info;
import org.energy_home.jemma.zgd.jaxb.SimpleDescriptor;

public class LocalServicesServlet extends CommonServlet {
	private static final long serialVersionUID = 1L;
	GatewayInterface gatewayInterface;

	public LocalServicesServlet(GatewayInterface _gatewayInterface) {
		gatewayInterface = _gatewayInterface;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	/*	HttpSession session = request.getSession(false);

		if (session == null) {
			sendErrorReply(response, GatewayConstants.GENERAL_ERROR, "User not logged");
			return;
		}
*/
		try {

			long timeout = getLongParameter(request, Resources.URI_PARAM_TIMEOUT);
			SimpleDescriptor simpleDescriptor = parseJsonBody(request, SimpleDescriptor.class);

			try {
				short endPoint = gatewayInterface.configureEndpoint(timeout, simpleDescriptor);
				if (endPoint > 0) {
					Info.Detail detail = new Info.Detail();
					detail.setEndpoint(endPoint);
					sendResult(response, detail);
				} else {
					sendErrorReply(response, GatewayConstants.GENERAL_ERROR, "Error creating end point. Not created.");
				}
			} catch (Throwable e) {
				sendErrorReply(response, GatewayConstants.GENERAL_ERROR, e.getMessage());
			}
		} catch (IllegalArgumentException e) {
			sendErrorReply(response, GatewayConstants.GENERAL_ERROR, e.getMessage());
		}
	}
}
