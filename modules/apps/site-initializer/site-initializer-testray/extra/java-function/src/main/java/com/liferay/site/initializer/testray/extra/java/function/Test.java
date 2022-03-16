package com.liferay.site.initializer.testray.extra.java.function;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.lang.Thread;
import java.lang.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.liferay.site.initializer.testray.extra.java.function.ImportResults;


public class Test extends Thread {

	Node testcaseNode; long testrayBuildId; long testrayProjectId;
	long testrayRunId; Map<String, Object> testrayCasePropertiesMap;

	public Test(
		Node testcaseNode, long testrayBuildId, long testrayProjectId,
		long testrayRunId, Map<String, Object> testrayCasePropertiesMap){
		this.testcaseNode = testcaseNode;
		this.testrayBuildId = testrayBuildId;
		this.testrayProjectId = testrayProjectId;
		this.testrayRunId=testrayRunId;
		this.testrayCasePropertiesMap = testrayCasePropertiesMap;
	}

	public void run(){



		try{

			ImportResults test = new ImportResults();
			test._addTestrayCase(
				testcaseNode, testrayBuildId, testrayProjectId, testrayRunId,
				testrayCasePropertiesMap);

		}
		catch(Exception e){
			e.printStackTrace();
		}

	}
}