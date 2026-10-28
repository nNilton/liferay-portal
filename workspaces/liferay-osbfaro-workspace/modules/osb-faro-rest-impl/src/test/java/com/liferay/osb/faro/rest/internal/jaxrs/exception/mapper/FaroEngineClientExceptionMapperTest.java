/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.rest.internal.jaxrs.exception.mapper;

import com.liferay.osb.faro.engine.client.exception.FaroEngineClientException;
import com.liferay.osb.faro.engine.client.exception.NoSuchEntryException;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.vulcan.jaxrs.exception.mapper.Problem;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Nilton Vieira
 */
public class FaroEngineClientExceptionMapperTest {

	@Before
	public void setUp() {
		HttpHeaders httpHeaders = Mockito.mock(HttpHeaders.class);

		Mockito.when(
			httpHeaders.getAcceptableMediaTypes()
		).thenReturn(
			Collections.singletonList(MediaType.APPLICATION_JSON_TYPE)
		);

		ReflectionTestUtil.setFieldValue(
			_faroEngineClientExceptionMapper, "httpHeaders", httpHeaders);
	}

	@Test
	public void testToResponse() {
		_assertResponse(
			"Check the parameter values against the tool description.",
			Response.Status.BAD_REQUEST,
			new FaroEngineClientException("Unrecognized name: bogus", 400));
		_assertResponse(
			"Internal Server Error", Response.Status.INTERNAL_SERVER_ERROR,
			new FaroEngineClientException("Internal Server Error", 500));
		_assertResponse(
			"Not Found", Response.Status.NOT_FOUND, new NoSuchEntryException());
	}

	private void _assertResponse(
		String expectedTitle, Response.Status expectedStatus,
		FaroEngineClientException faroEngineClientException) {

		Response response = _faroEngineClientExceptionMapper.toResponse(
			faroEngineClientException);

		Assert.assertEquals(
			expectedStatus.getStatusCode(), response.getStatus());

		Problem problem = (Problem)response.getEntity();

		Assert.assertEquals(expectedTitle, problem.getTitle());
		Assert.assertNull(problem.getType());
	}

	private final FaroEngineClientExceptionMapper
		_faroEngineClientExceptionMapper =
			new FaroEngineClientExceptionMapper();

}