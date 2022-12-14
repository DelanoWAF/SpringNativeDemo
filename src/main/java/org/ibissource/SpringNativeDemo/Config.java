// /*
//    Copyright 2022 WeAreFrank!

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
// */
package org.ibissource.SpringNativeDemo;

import java.util.ArrayList;
import java.util.List;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;

import liquibase.integration.spring.SpringLiquibase;
import nl.nn.testtool.MetadataExtractor;
import nl.nn.testtool.MetadataFieldExtractor;
import nl.nn.testtool.TestTool;
import nl.nn.testtool.echo2.ComparePane;
import nl.nn.testtool.echo2.ContentPane;
import nl.nn.testtool.echo2.DebugPane;
import nl.nn.testtool.echo2.Echo2Application;
import nl.nn.testtool.echo2.Tabs;
import nl.nn.testtool.echo2.TestPane;
import nl.nn.testtool.echo2.reports.CheckpointComponent;
import nl.nn.testtool.echo2.reports.ReportsComponent;
import nl.nn.testtool.echo2.reports.ReportsTreeCellRenderer;
import nl.nn.testtool.echo2.test.InfoPane;
import nl.nn.testtool.echo2.test.TestComponent;
import nl.nn.testtool.echo2.test.TreePane;
import nl.nn.testtool.filter.View;
import nl.nn.testtool.filter.Views;
import nl.nn.testtool.metadata.StatusExtractor;
import nl.nn.testtool.storage.CrudStorage;
import nl.nn.testtool.storage.LogStorage;
import nl.nn.testtool.storage.database.DatabaseStorage;
import nl.nn.testtool.storage.file.TestStorage;
import nl.nn.testtool.storage.memory.Storage;
import nl.nn.testtool.storage.xml.XmlStorage;
import nl.nn.testtool.transform.ReportXmlTransformer;

// This file contains most bean definitions from springTestToolTestWebapp.xml. This way of creating beans is used here as Micronaut does not recognize xml bean definitions and 
// requires the use of annotations.
// The annotations used are Spring annotations which are supported by Micronaut and translated into Micronaut annotations automatically.
// It should be possible to switch these out with Micronaut annotations manually.

@Configuration
@ImportResource("classpath:springTestToolTestWebapp.xml")
public class Config {

	@Bean
	MetadataExtractor metadataExtractor() {
		MetadataExtractor metadataExtractor = new MetadataExtractor();
		List<MetadataFieldExtractor> extraMetadataFieldExtractors = new ArrayList<MetadataFieldExtractor>();
		extraMetadataFieldExtractors.add(new StatusExtractor());
		metadataExtractor.setExtraMetadataFieldExtractors(extraMetadataFieldExtractors);
		return metadataExtractor;
	}

	@Bean
	@Primary
	Storage debugStorage(MetadataExtractor metadataExtractor) {
		Storage storage = new Storage();
		storage.setName("Debug");
		storage.setMetadataExtractor(metadataExtractor);
		return storage;
	}

	@Bean
	Storage testStorage(MetadataExtractor metadataExtractor) {
		Storage testStorage = new Storage();
		testStorage.setName("Test");
		return testStorage;
	}

	// @Bean
	// XmlStorage testStorage(MetadataExtractor metadataExtractor) {
	// 	XmlStorage testStorage = new XmlStorage();
	// 	testStorage.setName("Test");
	// 	testStorage.setReportsFolder("../ibis-ladybug-test-webapp/src/test/testtool");
	// 	return testStorage;
	// }

	@Bean
	Tabs tabs(DebugPane debugPane, TestPane testPane) {
		Tabs tabs = new Tabs();
		tabs.add(debugPane);
		tabs.add(testPane);
		return tabs;
	}

	@Bean
	Views views(
			@Qualifier("nameOnlyView") View nameonlyView,
			@Qualifier("whiteBoxView") View whiteboxView,
			@Qualifier("defaultView") String defaultView) {
		Views views = new Views();

		List<View> list = new ArrayList<View>();
		list.add(nameonlyView);
		list.add(whiteboxView);

		views.setViews(list);
		views.setDefaultView(defaultView);

		return views;
	}

	@Bean
	List<String> whiteBoxViewMetadataNames() {
		List<String> metadataNames = new ArrayList<String>();
		metadataNames.add("storageId");
		metadataNames.add("endTime");
		metadataNames.add("duration");
		metadataNames.add("name");
		metadataNames.add("correlationId");
		metadataNames.add("status");
		metadataNames.add("numberOfCheckpoints");
		metadataNames.add("estimatedMemoryUsage");
		metadataNames.add("storageSize");
		return metadataNames;
	}

	@Bean("whiteBoxView")
	View whiteBoxView(LogStorage debugStorage, List<String> metadataNames) {
		View whiteboxView = new View();
		whiteboxView.setName("White Box");
		whiteboxView.setStorage(debugStorage);
		whiteboxView.setMetadataNames(metadataNames);
		return whiteboxView;
	}

	@Bean("nameOnlyView")
	View nameOnlyView(LogStorage debugStorage) {
		View nameOnlyView = new View();
		nameOnlyView.setName("Report Name Only");
		nameOnlyView.setStorage(debugStorage);
		List<String> metadataNames = new ArrayList<String>();
		metadataNames.add("storageId");
		metadataNames.add("name");
		nameOnlyView.setMetadataNames(metadataNames);
		return nameOnlyView;
	}

	@Bean("defaultView")
	String defaultView() {
		String defaultView = new String("White Box");
		return defaultView;
	}

	@Bean
	String xsltResource() {
		return "ladybug/default.xslt";
	}

	// @Bean
	// Echo2Application echo2Application(ReportsTreeCellRenderer reportsTreeCellRenderer, Tabs tabs, ReportXmlTransformer reportXmlTransformer) {
	// 	Echo2Application echo2Application = new Echo2Application();
	// 	echo2Application.setContentPane(new ContentPane());
	// 	echo2Application.setReportsTreeCellRenderer(reportsTreeCellRenderer);
	// 	echo2Application.setTabs(tabs);
	// 	echo2Application.setReportXmlTransformer(reportXmlTransformer);
	// 	return echo2Application;
	// }

	@Bean
	TestTool testTool(Storage debugStorage, Views views) {
		TestTool testTool = new TestTool();
		testTool.setConfigName("Ladybug Test Webapp");
		testTool.setConfigVersion("1.0");
		testTool.setMaxCheckpoints(1000);
		testTool.setMaxMemoryUsage(1000000);
		testTool.setMaxMessageLength(100000);
		testTool.setRegexFilter(".*");
		testTool.setDebugStorage(debugStorage);
		// testTool.setTestStorage(testStorage);
		testTool.setViews(views);
		return testTool;
	}

	@Bean
	ReportsTreeCellRenderer reportsTreeCellRenderer() {
		ReportsTreeCellRenderer reportsTreeCellRenderer = new ReportsTreeCellRenderer();
		return reportsTreeCellRenderer;
	}

	// @Bean
	// TestPane testPane(CrudStorage testStorage, TestTool testTool, LogStorage debugStorage,
	// 		ReportXmlTransformer reportXmlTransformer) {
	// 	TestPane testPane = new TestPane();

	// 	TreePane treePane = new TreePane();
	// 	treePane.setStorage(testStorage);
	// 	testPane.setTreePane(treePane);

	// 	TestComponent testComponent = new TestComponent();
	// 	testComponent.setTestTool(testTool);
	// 	testComponent.setDebugStorage(debugStorage);
	// 	testComponent.setTestStorage(testStorage);
	// 	testComponent.setReportXmlTransformer(reportXmlTransformer);

	// 	InfoPane infoPane = new InfoPane();
	// 	infoPane.setTestComponent(testComponent);

	// 	testPane.setInfoPane(infoPane);
	// 	return testPane;
	// }

	// @Bean
	// nl.nn.testtool.echo2.reports.InfoPane infoPane() {
	// 	return new nl.nn.testtool.echo2.reports.InfoPane();
	// }

	// Not being detected?
	// @Bean
	// nl.nn.testtool.echo2.reports.TreePane treePane() {
	// 	nl.nn.testtool.echo2.reports.TreePane treePane = new TreePane();
	// 	return treePane;
	// }

	// @Bean
	// CheckpointComponent checkpointComponent() {
	// 	return new CheckpointComponent();
	// }

	@Bean
	DebugPane debugPane(
			ReportsTreeCellRenderer reportsTreeCellRenderer,
			TestTool testTool,
			Storage testStorage,
			ReportsComponent reportsComponent,
			nl.nn.testtool.echo2.reports.TreePane treePane,
			ReportXmlTransformer reportXmlTransformer) {
		DebugPane debugPane = new DebugPane();
		debugPane.setReportsTreeCellRenderer(reportsTreeCellRenderer);
		debugPane.setTestTool(testTool);
		debugPane.setTestStorage(testStorage);
		debugPane.setReportsComponent(reportsComponent);
		debugPane.setTreePane(treePane);
		debugPane.setReportXmlTransformer(reportXmlTransformer);
		return debugPane;
	}

	@Bean
	ReportsComponent reportsComponent(TestTool testTool, Views views, MetadataExtractor metadataExtractor) {
		ReportsComponent reportsComponent = new ReportsComponent();
		reportsComponent.setTestTool(testTool);
		reportsComponent.setViews(views);
		reportsComponent.setMetadataExtractor(metadataExtractor);
		return reportsComponent;
	}

	@Bean
	ComparePane comparePane(
			ReportsTreeCellRenderer reportsTreeCellRenderer,
			TestTool testTool,
			nl.nn.testtool.storage.memory.Storage testStorage,
			nl.nn.testtool.echo2.reports.TreePane treePane1, nl.nn.testtool.echo2.reports.TreePane treePane2,
			ReportXmlTransformer reportXmlTransformer,
			ReportsComponent reportsComponent) {
		ComparePane comparePane = new ComparePane();
		comparePane.setReportsTreeCellRenderer(reportsTreeCellRenderer);
		comparePane.setTestTool(testTool);
		comparePane.setTestStorage(testStorage);
		comparePane.setTreePane1(treePane1);
		comparePane.setTreePane2(treePane2);
		comparePane.setReportXmlTransformer(reportXmlTransformer);

		ReportsComponent reportsComponent1 = new ReportsComponent();
		reportsComponent1.setAddCompareButton(true);
		reportsComponent1.setAddSeparateOptionsRow(true);
		reportsComponent1.setFocusMaxMetadataTableSize(false);

		ReportsComponent reportsComponent2 = new ReportsComponent();
		reportsComponent1.setAddCompareButton(true);
		reportsComponent1.setAddSeparateOptionsRow(true);
		reportsComponent1.setFocusMaxMetadataTableSize(false);

		comparePane.setReportsComponent1(reportsComponent1);
		comparePane.setReportsComponent2(reportsComponent2);
		return comparePane;
	}

	@Bean
	ReportXmlTransformer reportXmlTransformer() {
		ReportXmlTransformer reportXmlTransformer = new ReportXmlTransformer();
		reportXmlTransformer.setXsltResource("nl/nn/testtool/test/junit/transformReport.xslt");
		return reportXmlTransformer;
	}

	@Bean("fileStorage")
	nl.nn.testtool.storage.file.Storage fileStorage(){
		nl.nn.testtool.storage.file.Storage fileStorage = new nl.nn.testtool.storage.file.Storage();
		fileStorage.setName("fileStorage");
		fileStorage.setReportsFilename("../ibis-ladybug/data/file-storage/ladybug.tts");
		fileStorage.setMetadataFilename("../ibis-ladybug/data/file-storage/ladybug.ttm");
		
		List<String> metadataNames = new ArrayList<String>();
		metadataNames.add("storageId");
		metadataNames.add("endTime");
		metadataNames.add("duration");
		metadataNames.add("name");
		metadataNames.add("correlationId");
		metadataNames.add("status");
		metadataNames.add("numberOfCheckpoints");
		metadataNames.add("estimatedMemoryUsage");
		metadataNames.add("storageSize");

		fileStorage.setPersistentMetadata(metadataNames);
		return fileStorage;
	}

	@Bean("databaseStorage")
	DatabaseStorage databaseStorage(){
		DatabaseStorage databaseStorage = new DatabaseStorage();
		databaseStorage.setName("databaseStorage");
		databaseStorage.setTable("LADYBUG");

		List<String> metadataNames = new ArrayList<String>();
		metadataNames.add("storageId");
		metadataNames.add("endTime");
		metadataNames.add("duration");
		metadataNames.add("name");
		metadataNames.add("correlationId");
		metadataNames.add("status");
		metadataNames.add("numberOfCheckpoints");
		metadataNames.add("estimatedMemoryUsage");
		metadataNames.add("storageSize");

		databaseStorage.setMetadataColumns(metadataNames);

		List<String> integerColumns = new ArrayList<String>();
		integerColumns.add("storageId");
		integerColumns.add("numberOfCheckpoints");
		databaseStorage.setIntegerColumns(integerColumns);

		List<String> longColums = new ArrayList<String>();
		longColums.add("estimatedMemoryUsage");
		longColums.add("storageSize");
		databaseStorage.setLongColumns(longColums);
	
		List<String> timestampColumns = new ArrayList<String>();
		timestampColumns.add("endTime");
		databaseStorage.setTimestampColumns(timestampColumns);

		return databaseStorage;		
	}

	@Bean("dataSource")
	JdbcDataSource dataSource(){
		JdbcDataSource jdbcDataSource = new JdbcDataSource();
		jdbcDataSource.setURL("jdbc:h2:../ibis-ladybug/data/database-storage/ladybug");
		return jdbcDataSource;
	}

	@Bean
	SpringLiquibase liquibase(JdbcDataSource dataSource){
		SpringLiquibase springLiquibase = new SpringLiquibase();
		springLiquibase.setDataSource(dataSource);
		springLiquibase.setChangeLog("classpath:LadybugDatabaseChangelog.xml");
		return springLiquibase;
	}
	
	@Bean
	SpringLiquibase liquibase2(JdbcDataSource dataSource){
		SpringLiquibase springLiquibase = new SpringLiquibase();
		springLiquibase.setDataSource(dataSource);
		springLiquibase.setChangeLog("classpath:ProofOfMigrationDatabaseChangelog.xml");
		return springLiquibase;
	}

}
