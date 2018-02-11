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
package org.energy_home.jemma.javagal.json.servlet;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.energy_home.jemma.zgd.GatewayConstants;
import org.energy_home.jemma.zgd.jaxb.Address;
import org.energy_home.jemma.zgd.jaxb.Info;
import org.energy_home.jemma.zgd.jaxb.Status;

import com.google.gson.Gson;

public class CommonServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Gson gson;

	public CommonServlet() {
		gson = new Gson();
	}

	protected void sendErrorReply(HttpServletResponse response, int errorCode, String errorMessage) throws IOException {
		Info info = new Info();
		Status status = new Status();
		status.setCode((short) errorCode);
		status.setMessage(errorMessage);
		info.setStatus(status);
		Info.Detail detail = new Info.Detail();
		info.setDetail(detail);
		response.getOutputStream().print(gson.toJson(info));
	}

	protected void sendSuccess(HttpServletResponse response) throws IOException {
		Info.Detail detail = new Info.Detail();
		Info info = new Info();
		Status status = new Status();
		status.setCode((short) GatewayConstants.SUCCESS);
		info.setStatus(status);
		// info.setRequestIdentifier(Util.getRequestIdentifier());
		info.setDetail(detail);

		response.getOutputStream().print(gson.toJson(info));
	}

	protected void sendResult(HttpServletResponse response, Info.Detail details) throws IOException {
		Info info = new Info();
		Status status = new Status();
		status.setCode((short) GatewayConstants.SUCCESS);
		info.setStatus(status);
		info.setDetail(details);

		response.getOutputStream().print(gson.toJson(info));
	}

	protected void sendResult(HttpServletResponse response, Status status) throws IOException {
		Info.Detail detail = new Info.Detail();
		Info info = new Info();
		status.setCode((short) GatewayConstants.SUCCESS);
		info.setStatus(status);
		info.setDetail(detail);

		response.getOutputStream().print(gson.toJson(info));
	}

	protected long getLongParameter(HttpServletRequest request, String name) {

		String param = request.getParameter(name);

		long value;

		if (param != null) {
			try {
				value = Long.decode(param);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(name + " parameter must be a number");
			}
		} else {
			throw new IllegalArgumentException("Missing mandatory " + name + " parameter.");
		}

		return value;
	}

	protected boolean getBooleanParameter(HttpServletRequest request, String name) {

		String param = request.getParameter(name);

		boolean value;

		if (param != null) {
			value = Boolean.valueOf(param);
		} else {
			throw new IllegalArgumentException("Missing mandatory " + name + " parameter.");
		}

		return value;
	}

	protected long getLongParameter(HttpServletRequest request, String name, long defaultValue) {

		String param = request.getParameter(name);
		if (param == null) {
			return defaultValue;
		}

		return this.getLongParameter(request, name);
	}

	protected Address getAddressParameter(HttpServletRequest request, String name) {

		String param = request.getParameter(name);

		Address address = new Address();
		if (param.length() == 16) {
			BigInteger addressBigInteger = BigInteger.valueOf(Long.parseLong(param, 16));
			address.setIeeeAddress(addressBigInteger);
		} else if (param.length() == 4) {
			Integer addressInteger = Integer.parseInt(param, 16);
			address.setNetworkAddress(addressInteger);
		} else {
			throw new IllegalArgumentException("Wrong address parameter: " + param);
		}

		return address;
	}

	@SuppressWarnings("unused")
	protected <T> T parseJsonBody(HttpServletRequest request, Class<T> clazz) throws IOException {
		StringBuilder sb = new StringBuilder();
		String s;

		while ((s = request.getReader().readLine()) != null) {
			sb.append(s);
		}

		return gson.fromJson(sb.toString(), clazz);
	}
}
