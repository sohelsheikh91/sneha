package com.accenture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class ValidationCheck {
	private static ArrayList<Object> data = new ArrayList<Object>();
	private static ArrayList<String> headerNames = new ArrayList<String>();
	private static ArrayList<Integer> hyperlinkColumns = new ArrayList<Integer>();

	private static ArrayList<Object> summaryData = new ArrayList<Object>();
	private static ArrayList<String> summaryHeaderNames = new ArrayList<String>();

	public static String status = "notused";
	public static String Mongodefinition = "Notfound";

	private static long startTime = 0l;
	private static long endTime = 0l;
	
	private static long startTimeTotal = 0l;
	private static long endTimeTotal = 0l;

	public static String getTimeDuration(long startTime, long endTime) {
		long diff = startTime - endTime;
		long diffSeconds = Math.abs(diff / 1000 % 60);
		long diffMinutes = Math.abs(diff / (60 * 1000) % 60);
		long diffHours = Math.abs(diff / (60 * 60 * 1000) % 24);
		long diffDays = Math.abs(diff / (24 * 60 * 60 * 1000));
		return "" + diffHours + " Hrs " + diffMinutes + " Mins " + diffSeconds + " Secs.";
	}

	public static void main(String[] args) throws Exception {

		ValidationCheck.startTimeTotal = System.currentTimeMillis();
		System.out.println("Code Validator");
		String inputPath;
		String inputDMPath;
		Scanner in = new Scanner(System.in);
		System.out.println("Enter location of main Folder");
	//	inputPath = "../gst-mule-parent";
		inputPath = in.nextLine();
		System.out.println("Enter location of mappings main Folder");
		inputDMPath = in.nextLine();
		System.out.println("+++++++++++++++++++++");
		System.out.println(System.getProperty("user.dir"));
		File file = new File("newfile.txt");
		boolean fvar = file.createNewFile();
		System.out.println("+++++++++++++++++++++");
		

		// Setting the destination path as the input Path for now
		String destinationPath = inputPath;
		File parentDir = new File(inputPath);

		String MappingPath = new StringBuilder().append(inputDMPath).append("/mappings").toString();
		File MappingPathDir = new File(MappingPath);
		
		String varDir = new StringBuilder().append(inputPath).append("/src").append("/main")
				.append("/app").toString();
		
		ValidationCheck.startTime = System.currentTimeMillis();
		System.out.print("Task Started: Unused POJO...");
		UnusedPOJO(parentDir, varDir);
		headerNames.add("FlowVarNotation");
		headerNames.add("xmlfileName");
		hyperlinkColumns.add(1);
		WriteToExcel.writeXLSFile("Unused_POJO", headerNames, ValidationCheck.data, hyperlinkColumns,
				destinationPath);
		summaryData.add("Unused POJO : Mixed POJO Flow Var Notation");
		summaryData.add(data.size() / headerNames.size());
		hyperlinkColumns.clear();
		headerNames.clear();
		ValidationCheck.data.clear();
		ValidationCheck.endTime = System.currentTimeMillis();
		System.out.println("Task Completed!" + " | " + ValidationCheck.getTimeDuration(startTime, endTime));


		ValidationCheck.startTime = System.currentTimeMillis();
		System.out.print("Task Started: Relative Path Validation...");
		RelativePathValidation(MappingPathDir, inputPath);
		headerNames.add("CSV File Path");
		headerNames.add("GRF File Name");
		hyperlinkColumns.add(1);
		WriteToExcel.writeXLSFile("Relative_Path_Validation", headerNames, ValidationCheck.data, hyperlinkColumns,
				destinationPath);
		summaryData.add("Relative Path Validation : CSV File paths");
		summaryData.add(data.size() / headerNames.size());
		hyperlinkColumns.clear();
		headerNames.clear();
		ValidationCheck.data.clear();
		ValidationCheck.endTime = System.currentTimeMillis();
		System.out.println("Task Completed!" + " | " + ValidationCheck.getTimeDuration(startTime, endTime));

		
		String ramlDir = new StringBuilder().append(inputPath).append("/src").append("/main")
				.append("/api").toString();

		File currentVarDir = new File(varDir);

		ValidationCheck.startTime = System.currentTimeMillis();
		System.out.print("Task Started: Unused GV...");
		UnusedGV(currentVarDir, varDir);
		headerNames.add("GVNotation");
		headerNames.add("propertiesfileName");
		hyperlinkColumns.add(1);
		WriteToExcel.writeXLSFile("Unused_GV", headerNames, ValidationCheck.data, hyperlinkColumns, destinationPath);
		summaryData.add("Unused GV : GV Notations");
		summaryData.add(data.size() / headerNames.size());
		hyperlinkColumns.clear();
		headerNames.clear();
		ValidationCheck.data.clear();
		ValidationCheck.endTime = System.currentTimeMillis();
		System.out.println("Task Completed!" + " | " + ValidationCheck.getTimeDuration(startTime, endTime));

		ValidationCheck.startTime = System.currentTimeMillis();
		System.out.print("Task Started: Unused Flow Var...");
		UnusedFlowVar(parentDir, varDir);
		headerNames.add("FlowVarNotation");
		headerNames.add("xmlfileName");
		hyperlinkColumns.add(1);
		WriteToExcel.writeXLSFile("Unused_Flow_Var", headerNames, ValidationCheck.data, hyperlinkColumns,
				destinationPath);
		summaryData.add("Unused Flow Var : Flow Var Notation");
		summaryData.add(data.size() / headerNames.size());
		hyperlinkColumns.clear();
		headerNames.clear();
		ValidationCheck.data.clear();
		ValidationCheck.endTime = System.currentTimeMillis();
		System.out.println("Task Completed!" + " | " + ValidationCheck.getTimeDuration(startTime, endTime));

		ValidationCheck.startTime = System.currentTimeMillis();
		System.out.print("Task Started: RAML Refernce Miss...");
		RAMLReferenceMiss(parentDir);
		headerNames.add("JSON File Name");
		headerNames.add("Raml File Name");
		headerNames.add("Line Number in Raml File");
		hyperlinkColumns.add(1);
		WriteToExcel.writeXLSFile("Missing_References_in_Raml", headerNames, ValidationCheck.data, hyperlinkColumns,
				destinationPath);
		summaryData.add("Missing References in Raml : Json File Names");
		summaryData.add(data.size() / headerNames.size());
		hyperlinkColumns.clear();
		headerNames.clear();
		ValidationCheck.data.clear();
		ValidationCheck.endTime = System.currentTimeMillis();
		System.out.println("Task Completed!" + " | " + ValidationCheck.getTimeDuration(startTime, endTime)); 

		ValidationCheck.startTime = System.currentTimeMillis();
		System.out.print("Task Started: Mongo Config Check...");
		MongoConfigCheck(parentDir, varDir);
		headerNames.add("xmlfileName");
		headerNames.add("configName");
		headerNames.add("configReferenceNamelineno");
		headerNames.add("MissingconfigReferenceDefinition");
		headerNames.add("configReferenceDefinitionlineno");
		headerNames.add("MissingParamDef");
		headerNames.add("MissingGVDefinition");
		headerNames.add("NotationGV");
		headerNames.add("IsGVValueMissing");
		headerNames.add("propertiesfileName");
		headerNames.add("NotationDefinitionlineno");
		hyperlinkColumns.add(0);
		hyperlinkColumns.add(9);
		WriteToExcel.writeXLSFile("Mongo_Config_Check", headerNames, ValidationCheck.data, hyperlinkColumns,
				destinationPath);
		summaryData.add("Mongo Config Check : Config Names");
		summaryData.add(data.size() / headerNames.size());
		hyperlinkColumns.clear();
		headerNames.clear();
		ValidationCheck.data.clear();
		ValidationCheck.endTime = System.currentTimeMillis();
		System.out.println("Task Completed!" + " | " + ValidationCheck.getTimeDuration(startTime, endTime));

	/*	ValidationCheck.startTime = System.currentTimeMillis();
		System.out.print("Task Started: AMQP Config Check...");
		amqpConfigCheck(parentDir, varDir);
		ValidationCheck.headerNames.add("xmlfileName");
		ValidationCheck.headerNames.add("configName");
		ValidationCheck.headerNames.add("configReferenceNamelineno");
		ValidationCheck.headerNames.add("MissingconfigReferenceDefinition");
		ValidationCheck.headerNames.add("configReferenceDefinitionlineno");
		ValidationCheck.headerNames.add("MissingParamDef");
		ValidationCheck.headerNames.add("MissingGVDefinition");
		ValidationCheck.headerNames.add("NotationGV");
		ValidationCheck.headerNames.add("IsGVValueMissing");
		ValidationCheck.headerNames.add("propertiesfileName");
		ValidationCheck.headerNames.add("NotationDefinitionlineno");
		hyperlinkColumns.add(0);
		hyperlinkColumns.add(9);
		WriteToExcel.writeXLSFile("AMQP_Config_Check", headerNames, ValidationCheck.data, hyperlinkColumns,
				destinationPath);
		summaryData.add("AMQP Config Check : Config Names");
		summaryData.add(data.size() / headerNames.size());
		hyperlinkColumns.clear();
		headerNames.clear();
		ValidationCheck.data.clear();
		ValidationCheck.endTime = System.currentTimeMillis();
		System.out.println("Task Completed!" + " | " + ValidationCheck.getTimeDuration(startTime, endTime));
*/
		ValidationCheck.startTime = System.currentTimeMillis();
		System.out.print("Task Started: Hard Coding Check...");
		HardCodingCheck(MappingPathDir, inputPath);
		ValidationCheck.headerNames.add("occurence");
		ValidationCheck.headerNames.add("lineno");
		ValidationCheck.headerNames.add("grfFileName");
		hyperlinkColumns.add(2);
		WriteToExcel.writeXLSFile("Hard_Coding_Check", headerNames, ValidationCheck.data, hyperlinkColumns,
				destinationPath);
		summaryData.add("Hard Coding Check : Hard Coding occurances");
		summaryData.add(data.size() / headerNames.size());
		hyperlinkColumns.clear();
		headerNames.clear();
		ValidationCheck.data.clear();
		ValidationCheck.endTime = System.currentTimeMillis();
		System.out.println("Task Completed!" + " | " + ValidationCheck.getTimeDuration(startTime, endTime));

		ValidationCheck.startTime = System.currentTimeMillis();
		System.out.print("Task Started: Check for Duplicate GV Defination...");
		CheckForDuplicateGVDefinitions(parentDir, varDir);
		ValidationCheck.headerNames.add("GV");
		ValidationCheck.headerNames.add("propertyfileName");
		ValidationCheck.headerNames.add("linenumber1");
		ValidationCheck.headerNames.add("linenumber2");
		hyperlinkColumns.add(1);
		WriteToExcel.writeXLSFile("Check_For_Duplicate_GV_Def", headerNames, ValidationCheck.data, hyperlinkColumns,
				destinationPath);
		summaryData.add("Check for duplicate GV definitions : GV Definitions");
		summaryData.add(data.size() / headerNames.size());
		hyperlinkColumns.clear();
		headerNames.clear();
		ValidationCheck.data.clear();
		ValidationCheck.endTime = System.currentTimeMillis();
		System.out.println("Task Completed!" + " | " + ValidationCheck.getTimeDuration(startTime, endTime));

	/*	ValidationCheck.startTime = System.currentTimeMillis();
		System.out.print("Task Started: Check for Mule Deploy Definition...");
		CheckForMuleDeployDefinition(parentDir, varDir);
		headerNames.add("xml file name");
		headerNames.add("xml Def found");
		hyperlinkColumns.add(0);
		WriteToExcel.writeXLSFile("Check_Mule_Deploy_Definition", headerNames, ValidationCheck.data, hyperlinkColumns,
				destinationPath);
		summaryData.add("Check Mule Deploy Definition : XML Defs found");
		summaryData.add(data.size() / headerNames.size());
		hyperlinkColumns.clear();
		headerNames.clear();
		ValidationCheck.data.clear();
		ValidationCheck.endTime = System.currentTimeMillis();
		System.out.println("Task Completed!" + " | " + ValidationCheck.getTimeDuration(startTime, endTime)); */
		

		ValidationCheck.startTime = System.currentTimeMillis();
		System.out.print("Task Started: Creating Summary...");
		summaryHeaderNames.add("Module Name");
		summaryHeaderNames.add("No. of Faults");
		WriteToExcel.writeXLSFile("Summary", summaryHeaderNames, summaryData, hyperlinkColumns, destinationPath);
		hyperlinkColumns.clear();
		summaryHeaderNames.clear();
		summaryData.clear();
		ValidationCheck.endTime = System.currentTimeMillis();
		System.out.println("Task Completed!" + " | " + ValidationCheck.getTimeDuration(startTime, endTime));

		ValidationCheck.endTimeTotal = System.currentTimeMillis();
		System.out.println("Validation Completed!" + " | " + ValidationCheck.getTimeDuration(startTimeTotal, endTimeTotal));
		in.close();
	}

	/* CSV Validation */

	public static void RelativePathValidation(File mappingsPathDir, String moveto) throws Exception {
		try {

			File[] files = mappingsPathDir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					RelativePathValidation(file, moveto);
				} else // if file
				{
					// System.out.println("file:" + file.getCanonicalPath());
					String grffileName = file.getCanonicalPath();
					try (BufferedReader br = new BufferedReader(new FileReader(grffileName))) {
						String line;
						while ((line = br.readLine()) != null) {
							if (line.contains(".csv") && line.contains("fileURL=")) {

								String Firstindex = "fileURL=";
								String extension = ".csv";
								// String MappingPath= new
								// StringBuilder().append(moveto).append("/gst-api").append("/mappings").toString();
								String path = line.substring(line.indexOf(Firstindex) + 9,
										(line.indexOf(Firstindex) + 9) + 11);
								// System.out.println("path:" + path);
								String csvpath = line.substring(line.indexOf(Firstindex) + 9, line.lastIndexOf(".csv"));
								String finalcsvpath = csvpath.concat(extension);
								if (!path.equals("${app_home}")) {
									// System.out.println("addToRelativePathMissList");
									addToRelativePathMissList(finalcsvpath, grffileName);
								}
							}
						}

					} catch (IOException e) {
						e.printStackTrace();

					}

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addToRelativePathMissList(String finalcsvpath, String grffileName) {
		// System.out.println("csvPath :" + finalcsvpath);
		// System.out.println("grffileName :" + grffileName);

		ValidationCheck.data.add(finalcsvpath);
		ValidationCheck.data.add(grffileName);
	}

	public static void addToCSVMissList(String finalcsvpath, String grffileName) {
		// System.out.println("csvPath :" + finalcsvpath);
		// System.out.println("grffileName :" + grffileName);

		ValidationCheck.data.add(finalcsvpath);
		ValidationCheck.data.add(grffileName);

	}

	public static void CSVExistanceValidation(File mappingsPathDir, String moveto) throws Exception {
		try {

			File[] files = mappingsPathDir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					RelativePathValidation(file, moveto);
				} else // if file
				{
					// System.out.println("file:" + file.getCanonicalPath());
					String grffileName = file.getCanonicalPath();
					try (BufferedReader br = new BufferedReader(new FileReader(grffileName))) {
						String line;
						while ((line = br.readLine()) != null) {
							if (line.contains(".csv") && line.contains("fileURL=")) {

								String Firstindex = "fileURL=";
								String extension = ".csv";
								// String MappingPath= new
								// StringBuilder().append(moveto).append("/gst-api").append("/mappings").toString();
								String path = line.substring(line.indexOf(Firstindex) + 9,
										(line.indexOf(Firstindex) + 9) + 11);
								// System.out.println("path:" + path);
								String csvpath = line.substring(line.indexOf(Firstindex) + 9, line.lastIndexOf(".csv"));
								String finalcsvpath = csvpath.concat(extension);
								String CSVPath = new StringBuilder().append(moveto)
										.append("/gst-api/src/main/resources").toString();

								// System.out.println("CSV Existance is
								// called");
								CSVExistence(CSVPath, finalcsvpath, grffileName);

							}
						}

					} catch (IOException e) {
						e.printStackTrace();

					}

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void CSVExistence(String CSVPath, String finalcsvpath, String grffileName) {
		String substr = "${app_home}/";
		if (finalcsvpath.contains("${app_home}/")) {
			if (finalcsvpath.contains("${app_home}/classes/")) {
				substr = "${app_home}/classes/";
			}
			String JustNameCSV = finalcsvpath.substring(finalcsvpath.indexOf(substr) + substr.length());
			String finalPath = new StringBuilder().append(CSVPath).append("/").append(JustNameCSV).toString();
			File csvfile = new File(finalPath); // csv file location
			if (!csvfile.exists()) {
				// System.out.println("addToCSVMissList");
				addToCSVMissList(finalcsvpath, grffileName);
			}

		}
	}

	public static void UnusedGV(File dir, String moveto) throws Exception {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					UnusedGV(file, moveto);
				} else // if file is .properties
				{
					if (file.isFile() && getFileExtensionName(file).indexOf("ies") != -1) {
						// System.out.println("file:" +
						// file.getCanonicalPath());
						String propertiesfileName = file.getCanonicalPath();
						try (BufferedReader br = new BufferedReader(new FileReader(propertiesfileName))) {
							String line;
							while ((line = br.readLine()) != null && !propertiesfileName.contains("mule-app.properties")
									&& !propertiesfileName.contains("wssecurity.properties")
									&& !propertiesfileName.contains("MetaData.properties")
									&& !propertiesfileName.contains("mule-deploy.properties")
									&& !propertiesfileName.contains("mvn-eclipse-cache.properties")) {
								// System.out.println("path:" + line);
								if (line.length() > 0) {
									String x = line.substring(0, 1);
									if (!x.equals("#") && !x.equals(" ")) {
										String substr = "=";
										String GV = line.substring(0, line.indexOf(substr));
										String GVNotation = new StringBuilder().append("${").append(GV).append("}")
												.toString();
										// System.out.println("GVNotation:" +
										// GVNotation);

										File rootDirectory = new File(moveto); // current
																				// directory

										CheckGVUsage(rootDirectory, GVNotation, propertiesfileName);
										if (status.equals("notused")) {
											addToUnusedGVList(GVNotation, propertiesfileName);
										}
										status = "notused";
									}
								}
							}

						} catch (IOException e) {
							e.printStackTrace();

						}
					}
				}
			}
			// writeToUnusedGVdataSheet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// src/main/app ${gst-sprintqueryorderfulfillmentservice} .Prop file
	public static void CheckGVUsage(File dir, String GVNotation, String fileName) {
		try {
			File[] files = dir.listFiles();

			for (int i = 0; (i < files.length && status.equals("notused")); i++) {
				File file = files[i];
				if (file.isDirectory()) {
					CheckGVUsage(file, GVNotation, fileName);
				} else {
					if (file.isFile() && getFileExtensionName(file).indexOf("xml") != -1) // if
																							// file
																							// is
																							// .xml
					{
						String xmlfileName = file.getCanonicalPath();
						// System.out.println("xmlfileName:" + xmlfileName);
						try (BufferedReader br = new BufferedReader(new FileReader(xmlfileName))) {
							String line;
							while ((line = br.readLine()) != null && status.equals("notused")) {

								if (line.contains(GVNotation)) {
									status = "used";
									break;

								}
							}

						} catch (IOException e) {
							e.printStackTrace();

						}
					}
				} // end
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void addToUnusedGVList(String GVNotation, String propertiesfileName) {
		GVNotation = GVNotation.replace("${", "");
		GVNotation = GVNotation.replace("}", "");
		if (!GVNotation.endsWith("ws.host") && !GVNotation.endsWith("ws.port") && !GVNotation.endsWith("ws.jaxbpackage")
				&& !GVNotation.endsWith("ws.securityEnabled") && !GVNotation.endsWith("serviceType")
				&& !GVNotation.endsWith("returnClass")) {
			ValidationCheck.data.add(GVNotation);
			ValidationCheck.data.add(propertiesfileName);
		}
	}

	public static String getFileExtensionName(File f) {
		if (f.getName().indexOf(".") == -1) {
			return "";
		} else {
			return f.getName().substring(f.getName().length() - 3, f.getName().length());
		}
	}

	public static void UnusedFlowVar(File dir, String moveto) throws Exception {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					UnusedFlowVar(file, moveto);
				} else // if file is .xml
				{
					if (file.isFile() && getFileExtensionName(file).indexOf("xml") != -1 && (!(file.getCanonicalPath().contains("target")))) {
						String xmlfileName = file.getCanonicalPath();
						try (BufferedReader br = new BufferedReader(new FileReader(xmlfileName))) {
							String line;
							while ((line = br.readLine()) != null && !xmlfileName.contains("mule-project.xml")
									&& !xmlfileName.contains("pom.xml")) {
								if (line.contains("<set-variable variableName=")) {
									String Firstindex = "variableName=";
									StringBuilder sb = new StringBuilder();
									String FlowVar = line.substring(line.indexOf(Firstindex) + 14,
											line.indexOf("\"", (line.indexOf(Firstindex) + 14)));

									// Check type 1 notation
									String FlowVarNotation = new StringBuilder().append("#[").append("flowVars.")
											.append(FlowVar).append("]").toString();
									File rootDirectory = new File(moveto); // current
																			// directory
									CheckGVUsage(rootDirectory, FlowVarNotation, xmlfileName);

									// Check type 2 notation
									String FlowVarNotation2 = new StringBuilder().append("#[").append("flowVars['")
											.append(FlowVar).append("']]").toString();

									if (status.equals("notused")) {
										CheckGVUsage(rootDirectory, FlowVarNotation2, xmlfileName);
									}

									// Check type 3 notation
									String FlowVarNotation3 = new StringBuilder().append("#[").append(FlowVar)
											.append("]").toString();

									if (status.equals("notused")) {
										CheckGVUsage(rootDirectory, FlowVarNotation3, xmlfileName);
									}

									// Check type 4 notation
									String FlowVarNotation4 = new StringBuilder().append("#[?").append("flowVars.")
											.append(FlowVar).toString();

									if (status.equals("notused")) {
										CheckGVUsage(rootDirectory, FlowVarNotation4, xmlfileName);
									}

									// Check type 5 notation
									String FlowVarNotation5 = new StringBuilder().append("#[?").append("flowVars['")
											.append(FlowVar).append("']").toString();

									if (status.equals("notused")) {
										CheckGVUsage(rootDirectory, FlowVarNotation5, xmlfileName);
									}

									// Check type 6 notation
									String FlowVarNotation6 = new StringBuilder().append("flowVars.").append(FlowVar)
											.toString();

									if (status.equals("notused")) {
										CheckGVUsage(rootDirectory, FlowVarNotation6, xmlfileName);
									}

									// Check type 7 notation
									String FlowVarNotation7 = new StringBuilder().append("flowVars['").append(FlowVar)
											.append("']").toString();

									if (status.equals("notused")) {
										CheckGVUsage(rootDirectory, FlowVarNotation7, xmlfileName);
									}

									// Check type 8 notation
									String FlowVarNotation8 = new StringBuilder().append("#[!").append(FlowVar)
											.toString();

									if (status.equals("notused")) {
										CheckGVUsage(rootDirectory, FlowVarNotation8, xmlfileName);
									}

									// Check type 9 notation
									String FlowVarNotation9 = new StringBuilder().append("#[(").append(FlowVar)
											.toString();

									if (status.equals("notused")) {
										CheckGVUsage(rootDirectory, FlowVarNotation9, xmlfileName);
									}

									// Check type 10 notation
									String FlowVarNotation10 = new StringBuilder().append("#[").append(FlowVar)
											.append(" ==").toString();

									if (status.equals("notused")) {
										CheckGVUsage(rootDirectory, FlowVarNotation10, xmlfileName);
									}

									// Persist Not Used Flow Variables
									if (status.equals("notused")) {
										addToUnusedFlowVarList(FlowVar, xmlfileName);
									}

									status = "notused";
								}
							}

						} catch (IOException e) {
							e.printStackTrace();

						}
					}
				}
			}
			// writeToUnusedFlowVarsSheet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addToUnusedFlowVarList(String FlowVarNotation, String xmlfileName) {

		FlowVarNotation = FlowVarNotation.replace("${", "");
		FlowVarNotation = FlowVarNotation.replace("}", "");
		// System.out.print("Flow Var : " + FlowVarNotation);
		// System.out.println("xml : " + xmlfileName);
		ValidationCheck.data.add(FlowVarNotation);
		ValidationCheck.data.add(xmlfileName);

	}

	public static void RAMLReferenceMiss(File dir) throws Exception {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory())
				{
						RAMLReferenceMiss(file);
				} else // if file is .raml
				{
					if (file.isFile() && getFileExtensionName(file).indexOf("aml") != -1 && (!(file.getCanonicalPath().contains("target"))) 
							&& (!(file.getCanonicalPath().contains("test")))) {
						String ramlfileName = file.getCanonicalPath();
						String FinalResourceLocation = "";
						String ramlfilelocation;
						try (LineNumberReader br = new LineNumberReader(new FileReader(ramlfileName))) {
							String line;
							while ((line = br.readLine()) != null) {
								if (line.length() > 0) {
									String x = line.substring(0, 1);
									if (!x.equals("#") && line.contains("!include")) {
										String Firstindex = "!include ";
										if(System.getProperty("user.dir").contains("\\"))
										{
											String ResourceLocation = line.substring(line.indexOf(Firstindex) + 9);
											ResourceLocation = ResourceLocation.replaceAll("/", "\\\\");
											ramlfilelocation = ramlfileName.substring(0,
												ramlfileName.lastIndexOf("\\"));
											FinalResourceLocation = new StringBuilder().append(ramlfilelocation)
												.append('\\').append(ResourceLocation).toString();
										}
										else
										{
											String ResourceLocation = line.substring(line.indexOf(Firstindex) + 9);
									/*		ResourceLocation = ResourceLocation.replaceAll("/", "\\\\"); */
											ramlfilelocation = ramlfileName.substring(0,ramlfileName.lastIndexOf("/"));
											FinalResourceLocation = new StringBuilder().append(ramlfilelocation)
												.append("//").append(ResourceLocation).toString();
										}
										File ResourceFile = new File(FinalResourceLocation); // resource
																								// directory
										if (!ResourceFile.exists()) {
											int lineno = br.getLineNumber();
											String linenumber = "" + lineno;
											addToRAMLReferenceMissList(FinalResourceLocation, linenumber, ramlfileName,
													dir);
										}
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addToRAMLReferenceMissList(String ResourceFile, String linenumber, String ramlfileName,
			File outputPath) {
		ValidationCheck.data.add(ResourceFile);
		ValidationCheck.data.add(ramlfileName);
		ValidationCheck.data.add(linenumber);
	}

	public static String configName = "";
	public static String IsMissingconfigReferenceDefinition = "N/A";
	public static String MissingParamDef = "N/A";
	public static String IsGVDefinitionMissing = "N/A";
	public static String IsGVValueMissing = "N/A";
	public static String configReferenceDefinitionlineno = "N/A";
	public static String NotationGV = "N/A";
	public static String propertyfileName = "N/A";
	public static String configReferenceNamelineno = "";
	public static String NotationDefinitionlineno = "N/A";
	public static String Function = "";

	public static void MongoConfigCheck(File dir, String moveto) throws Exception {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					MongoConfigCheck(file, moveto);
				} else // if file is .xml
				{
					if (file.isFile() && getFileExtensionName(file).indexOf("xml") != -1 && !(file.getCanonicalPath().contains("target"))) {
						String xmlfileName = file.getCanonicalPath();
						try (LineNumberReader br = new LineNumberReader(new FileReader(xmlfileName))) {
							String line;
							String DuplicateConfigRef = "";
							while ((line = br.readLine()) != null)// Get
																	// configReference
																	// name
							{
								if (line.length() > 0) {
									// String x = line.substring(0,4);
									if (/* !x.equals("<!--") && */ line.contains("<mongo:")
											&& line.contains("config-ref=")) {
										String Firstindex = "config-ref=";
										String c1 = line.substring(line.indexOf("config-ref=") + 12);
										configName = c1.substring(0, c1.indexOf(" ") - 1);
										if (!DuplicateConfigRef.contains(new StringBuilder().append("|")
												.append(configName).append("|").toString())) {
											int lineno = br.getLineNumber();
											configReferenceNamelineno = "" + lineno;
											File rootDirectory = new File(moveto); // current
																					// directory
											CheckForMongoConfigReference(rootDirectory, xmlfileName, configName,
													moveto);// look for
															// configReference
															// definition
											if (Mongodefinition.equals("Notfound")) {
												IsMissingconfigReferenceDefinition = "true";
												addToNullMongoConfig(xmlfileName, configName, configReferenceNamelineno,
														IsMissingconfigReferenceDefinition,
														configReferenceDefinitionlineno, MissingParamDef, NotationGV,
														IsGVDefinitionMissing, IsGVValueMissing, propertyfileName,
														NotationDefinitionlineno);
												IsMissingconfigReferenceDefinition = "N/A";
											} else {
												if (Mongousername.equals("notfound")) {
													MissingParamDef = "username";
													IsMissingconfigReferenceDefinition = "false";
													addToNullMongoConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
												if (Mongopassword.equals("notfound")) {
													MissingParamDef = "password";
													IsMissingconfigReferenceDefinition = "false";
													addToNullMongoConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
												if (Mongodatabase.equals("notfound")) {
													MissingParamDef = "database";
													IsMissingconfigReferenceDefinition = "false";
													addToNullMongoConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
												if (Mongohost.equals("notfound")) {
													MissingParamDef = "host";
													IsMissingconfigReferenceDefinition = "false";
													addToNullMongoConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
												if (Mongoport.equals("notfound")) {
													MissingParamDef = "port";
													IsMissingconfigReferenceDefinition = "false";
													addToNullMongoConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
											}
											DuplicateConfigRef = new StringBuilder().append(DuplicateConfigRef)
													.append("|").append(configName).append("|").toString();
											configReferenceNamelineno = "";
										}
										configName = "";
										Mongodefinition = "Notfound";
										Mongousername = "notfound";
										Mongopassword = "notfound";
										Mongodatabase = "notfound";
										Mongohost = "notfound";
										Mongoport = "notfound";
									}
								}
							}

						} catch (IOException e) {
							e.printStackTrace();

						}
					}
				}
			}
			// writeToMongoConfigMissSheet();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Function = "";
	}

	public static String Mongousername = "notfound";
	public static String Mongopassword = "notfound";
	public static String Mongodatabase = "notfound";
	public static String Mongohost = "notfound";
	public static String Mongoport = "notfound";
	public static String GVValue = "notfound";

	public static void CheckForMongoConfigReference(File rootDirectory, String xmlfileName, String configName,
			String moveto) throws IOException {

		try (LineNumberReader br = new LineNumberReader(new FileReader(xmlfileName))) {
			String line;
			Function = "MONGO";
			while ((line = br.readLine()) != null)// look for configReference
													// definition
			{
				if (line.length() > 0) {
					// String x = line.substring(0,4);
					if (/* !x.equals("<!--") && */ line.contains("<mongo:config name=")
							&& line.contains(new StringBuilder().append("<mongo:config name=").append("\"")
									.append(configName).append("\"").toString())) {
						int lineno = br.getLineNumber();
						configReferenceDefinitionlineno = "" + lineno;
						Mongodefinition = "found";
						IsMissingconfigReferenceDefinition = "false";
						if (line.contains("username=")) {
							Mongousername = "found";

							String u1 = line.substring(line.indexOf("username=") + 12);
							String u2 = u1.substring(0, u1.indexOf("}"));
							String usernameNotation = new StringBuilder().append(u2).append("=").toString();
							File Directory = new File(moveto);
							CheckContainsValue(Directory, usernameNotation, xmlfileName, configName);
							GVValue = "notfound";
							if (IsGVDefinitionMissing.equals("true")) {
								addToNullMongoConfig(xmlfileName, configName, configReferenceNamelineno,
										IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
										MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
										propertyfileName, NotationDefinitionlineno);
								IsGVDefinitionMissing = "N/A";

							}
						}
						if (line.contains("password=")) {
							Mongopassword = "found";
							String u1 = line.substring(line.indexOf("password=") + 12);
							String u2 = u1.substring(0, u1.indexOf("}"));
							String usernameNotation = new StringBuilder().append(u2).append("=").toString();
							CheckContainsValue(rootDirectory, usernameNotation, xmlfileName, configName);
							GVValue = "notfound";
							if (IsGVDefinitionMissing.equals("true")) {
								addToNullMongoConfig(xmlfileName, configName, configReferenceNamelineno,
										IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
										MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
										propertyfileName, NotationDefinitionlineno);
								IsGVDefinitionMissing = "N/A";

							}
						}
						if (line.contains("database=")) {
							Mongodatabase = "found";
							String u1 = line.substring(line.indexOf("database=") + 12);
							String u2 = u1.substring(0, u1.indexOf("}"));
							String usernameNotation = new StringBuilder().append(u2).append("=").toString();
							CheckContainsValue(rootDirectory, usernameNotation, xmlfileName, configName);
							GVValue = "notfound";
							if (IsGVDefinitionMissing.equals("true")) {
								addToNullMongoConfig(xmlfileName, configName, configReferenceNamelineno,
										IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
										MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
										propertyfileName, NotationDefinitionlineno);
								IsGVDefinitionMissing = "N/A";

							}
							IsGVDefinitionMissing = "N/A";
						}
						if (line.contains("host=")) {
							Mongohost = "found";
							String u1 = line.substring(line.indexOf("host=") + 8);
							String u2 = u1.substring(0, u1.indexOf("}"));
							String usernameNotation = new StringBuilder().append(u2).append("=").toString();
							CheckContainsValue(rootDirectory, usernameNotation, xmlfileName, configName);
							GVValue = "notfound";
							if (IsGVDefinitionMissing.equals("true")) {
								addToNullMongoConfig(xmlfileName, configName, configReferenceNamelineno,
										IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
										MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
										propertyfileName, NotationDefinitionlineno);
								IsGVDefinitionMissing = "N/A";

							}
							IsGVDefinitionMissing = "N/A";
						}
						if (line.contains("port=")) {
							Mongoport = "found";
							String u1 = line.substring(line.indexOf("port=") + 8);
							String u2 = u1.substring(0, u1.indexOf("}"));
							String usernameNotation = new StringBuilder().append(u2).append("=").toString();
							CheckContainsValue(rootDirectory, usernameNotation, xmlfileName, configName);
							GVValue = "notfound";
							if (IsGVDefinitionMissing.equals("true")) {
								addToNullMongoConfig(xmlfileName, configName, configReferenceNamelineno,
										IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
										MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
										propertyfileName, NotationDefinitionlineno);
								IsGVDefinitionMissing = "N/A";

							}
							IsGVDefinitionMissing = "N/A";
						}
						configReferenceDefinitionlineno = "";
						IsMissingconfigReferenceDefinition = "N/A";
						NotationGV = "N/A";
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public static void CheckContainsValue(File rootDirectory, String Notation, String xmlfileName, String configName) {
		try {
			File[] files = rootDirectory.listFiles();
			for (int i = 0; (i < files.length && IsGVValueMissing.equals("N/A") && GVValue.equals("notfound")); i++) {
				File file = files[i];
				if (file.isDirectory()) {
					CheckContainsValue(file, Notation, xmlfileName, configName);
				} else // if file is .properties
				{
					if (file.isFile() && getFileExtensionName(file).indexOf("ies") != -1) {
						// System.out.println("file:" +
						// file.getCanonicalPath());
						propertyfileName = file.getCanonicalPath();
						try (LineNumberReader br = new LineNumberReader(new FileReader(propertyfileName))) {
							String line;

							while ((line = br.readLine()) != null && Function.equals("MONGO")
									&& GVValue.equals("notfound") && !propertyfileName.contains("mule-app.properties")
									&& !propertyfileName.contains("wssecurity.properties")
									&& !propertyfileName.contains("MetaData.properties")
									&& !propertyfileName.contains("mule-deploy.properties")) {
								// System.out.println("path:" + line);
								if (line.length() > 0) {
									String x = line.substring(0, 1);
									if (!x.equals("#") && line.contains(Notation)) {
										IsGVDefinitionMissing = "false";
										String substr = "=";
										int l = Notation.length();
										String value = line.substring(line.indexOf(Notation) + l);
										int lineno = br.getLineNumber();
										NotationDefinitionlineno = "" + lineno;
										NotationGV = Notation.substring(0, line.indexOf(substr));
										if (value.equals("")) {
											IsGVValueMissing = "true";
											addToNullMongoConfig(xmlfileName, configName, configReferenceNamelineno,
													IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
													MissingParamDef, NotationGV, IsGVDefinitionMissing,
													IsGVValueMissing, propertyfileName, NotationDefinitionlineno);
											IsGVValueMissing = "N/A";
											NotationGV = "N/A";
											NotationDefinitionlineno = "N/A";
											IsGVDefinitionMissing = "N/A";
											GVValue = "found";

										} else {
											GVValue = "found";
											NotationGV = "N/A";
											NotationDefinitionlineno = "N/A";
											break;

										}
									} else {
										IsGVDefinitionMissing = "true";
										String substr = "=";
										NotationGV = Notation.substring(0, Notation.indexOf(substr));
										NotationDefinitionlineno = "N/A";
										IsGVValueMissing = "N/A";
									}
								}
							}

							while ((line = br.readLine()) != null && Function.equals("AMQP")
									&& GVValue.equals("notfound") && !propertyfileName.contains("mule-app.properties")
									&& !propertyfileName.contains("wssecurity.properties")
									&& !propertyfileName.contains("MetaData.properties")
									&& !propertyfileName.contains("mule-deploy.properties")) {
								// System.out.println("path:" + line);
								if (line.length() > 0) {
									String x = line.substring(0, 1);
									if (!x.equals("#") && line.contains(Notation)) {
										IsGVDefinitionMissing = "false";
										String substr = "=";
										int l = Notation.length();
										String value = line.substring(line.indexOf(Notation) + l);
										int lineno = br.getLineNumber();
										NotationDefinitionlineno = "" + lineno;
										NotationGV = Notation.substring(0, line.indexOf(substr));
										if (value.equals("")) {
											IsGVValueMissing = "true";
											addToNullamqpConfig(xmlfileName, configName, configReferenceNamelineno,
													IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
													MissingParamDef, NotationGV, IsGVDefinitionMissing,
													IsGVValueMissing, propertyfileName, NotationDefinitionlineno);
											IsGVValueMissing = "N/A";
											NotationGV = "N/A";
											NotationDefinitionlineno = "N/A";
											IsGVDefinitionMissing = "N/A";
											GVValue = "found";

										} else {
											GVValue = "found";
											NotationGV = "N/A";
											NotationDefinitionlineno = "N/A";
											break;

										}
									} else {
										IsGVDefinitionMissing = "true";
										String substr = "=";
										NotationGV = Notation.substring(0, Notation.indexOf(substr));
										NotationDefinitionlineno = "N/A";
										IsGVValueMissing = "N/A";
									}
								}
							}

						} catch (IOException e) {
							e.printStackTrace();

						}
						propertyfileName = "N/A";
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addToNullMongoConfig(String xmlfileName, String configName, String configReferenceNamelineno,
			String MissingconfigReferenceDefinition, String configReferenceDefinitionlineno, String MissingParamDef,
			String NotationGV, String MissingGVDefinition, String IsGVValueMissing, String propertiesfileName,
			String NotationDefinitionlineno) {

		ValidationCheck.data.add(xmlfileName);
		ValidationCheck.data.add(configName);
		ValidationCheck.data.add(configReferenceNamelineno);
		ValidationCheck.data.add(MissingconfigReferenceDefinition);
		ValidationCheck.data.add(configReferenceDefinitionlineno);
		ValidationCheck.data.add(MissingParamDef);
		ValidationCheck.data.add(MissingGVDefinition);
		ValidationCheck.data.add(NotationGV);
		ValidationCheck.data.add(IsGVValueMissing);
		ValidationCheck.data.add(propertiesfileName);
		ValidationCheck.data.add(NotationDefinitionlineno);
	}

	public static String validateConnections = "notfound";
	public static String dynamicNotification = "notfound";
	public static String fallbackAddresses = "notfound";
	public static String ackMode = "notfound";
	public static String prefetchSize = "notfound";
	public static String amqpdefinition = "Notfound";
	public static String username = "notfound";
	public static String password = "notfound";
	public static String prefetchCount = "notfound";
	public static String host = "notfound";

	public static void amqpConfigCheck(File dir, String moveto) throws Exception {
		try {
			File[] files = dir.listFiles();
			configName = "";
			IsMissingconfigReferenceDefinition = "N/A";
			MissingParamDef = "N/A";
			IsGVDefinitionMissing = "N/A";
			IsGVValueMissing = "N/A";
			configReferenceDefinitionlineno = "N/A";
			NotationGV = "N/A";
			propertyfileName = "N/A";
			configReferenceNamelineno = "";
			NotationDefinitionlineno = "N/A";
			GVValue = "notfound";
			for (File file : files) {
				if (file.isDirectory()) {
					amqpConfigCheck(file, moveto);
				} else // if file is .xml
				{
					if (file.isFile() && getFileExtensionName(file).indexOf("xml") != -1) {
						String xmlfileName = file.getCanonicalPath();
						try (LineNumberReader br = new LineNumberReader(new FileReader(xmlfileName))) {
							String line;
							String DuplicateConfigRef = "";
							while ((line = br.readLine()) != null)// Get
																	// configReference
																	// name
							{
								if (line.length() > 0) {
									// String x = line.substring(0,4);
									if (/* !x.equals("<!--") && */ line.contains("amqp:")
											&& line.contains("connector-ref=")) {
										String Firstindex = "connector-ref=";
										String c1 = line.substring(line.indexOf("connector-ref=") + 15);
										configName = c1.substring(0, c1.indexOf(" ") - 1);
										if (!DuplicateConfigRef.contains(new StringBuilder().append("|")
												.append(configName).append("|").toString())) {
											int lineno = br.getLineNumber();
											configReferenceNamelineno = "" + lineno;

											File rootDirectory = new File(moveto); // current
																					// directory
											CheckForamqpConfigReference(rootDirectory, xmlfileName, configName, moveto);// look
																														// for
																														// configReference
																														// definition
											if (amqpdefinition.equals("Notfound")) {
												IsMissingconfigReferenceDefinition = "true";
												addToNullamqpConfig(xmlfileName, configName, configReferenceNamelineno,
														IsMissingconfigReferenceDefinition,
														configReferenceDefinitionlineno, MissingParamDef, NotationGV,
														IsGVDefinitionMissing, IsGVValueMissing, propertyfileName,
														NotationDefinitionlineno);
												IsMissingconfigReferenceDefinition = "N/A";
											} else {
												if (validateConnections.equals("notfound")) {
													MissingParamDef = "validateConnections";
													IsMissingconfigReferenceDefinition = "false";
													addToNullamqpConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
												if (dynamicNotification.equals("notfound")) {
													MissingParamDef = "dynamicNotification";
													IsMissingconfigReferenceDefinition = "false";
													addToNullamqpConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
												if (fallbackAddresses.equals("notfound")) {
													MissingParamDef = "fallbackAddresses";
													IsMissingconfigReferenceDefinition = "false";
													addToNullamqpConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
												if (username.equals("notfound")) {
													MissingParamDef = "username";
													IsMissingconfigReferenceDefinition = "false";
													addToNullamqpConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
												if (password.equals("notfound")) {
													MissingParamDef = "password";
													IsMissingconfigReferenceDefinition = "false";
													addToNullamqpConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
												if (host.equals("notfound")) {
													MissingParamDef = "host";
													IsMissingconfigReferenceDefinition = "false";
													addToNullamqpConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
												if (prefetchCount.equals("notfound")) {
													MissingParamDef = "prefetchCount";
													IsMissingconfigReferenceDefinition = "false";
													addToNullamqpConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
												if (prefetchSize.equals("notfound")) {
													MissingParamDef = "prefetchSize";
													IsMissingconfigReferenceDefinition = "false";
													addToNullamqpConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
												if (ackMode.equals("notfound")) {
													MissingParamDef = "ackMode";
													IsMissingconfigReferenceDefinition = "false";
													addToNullamqpConfig(xmlfileName, configName,
															configReferenceNamelineno,
															IsMissingconfigReferenceDefinition,
															configReferenceDefinitionlineno, MissingParamDef,
															NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
															propertyfileName, NotationDefinitionlineno);
													MissingParamDef = "N/A";
													IsMissingconfigReferenceDefinition = "N/A";
												}
											}
											DuplicateConfigRef = new StringBuilder().append(DuplicateConfigRef)
													.append("|").append(configName).append("|").toString();
											configReferenceNamelineno = "";
										}
										configName = "";
										amqpdefinition = "Notfound";
										validateConnections = "notfound";
										dynamicNotification = "notfound";
										fallbackAddresses = "notfound";
										username = "notfound";
										password = "notfound";
										host = "notfound";
										prefetchCount = "notfound";
										prefetchSize = "notfound";
										ackMode = "notfound";
									}
								}
							}

						} catch (IOException e) {
							e.printStackTrace();

						}
					}
				}
			}
			// writeToamqpConfigMissSheet();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Function = "";
	}

	public static void CheckForamqpConfigReference(File rootDirectory, String xmlfileName, String configName,
			String moveto) throws IOException {

		try (LineNumberReader br = new LineNumberReader(new FileReader(xmlfileName))) {
			String line;
			Function = "AMQP";
			while ((line = br.readLine()) != null)// look for configReference
													// definition
			{
				if (line.length() > 0) {
					// String x = line.substring(0,4);
					if (/* !x.equals("<!--") && */ line.contains("<amqp:connector name=")
							&& line.contains(new StringBuilder().append("<amqp:connector name=").append("\"")
									.append(configName).append("\"").toString())) {
						int lineno = br.getLineNumber();
						configReferenceDefinitionlineno = "" + lineno;
						amqpdefinition = "found";
						IsMissingconfigReferenceDefinition = "false";
						if (line.contains("username=")) {
							username = "found";
							if (line.contains("username=\"${")) {
								String u1 = line.substring(line.indexOf("username=") + 12);
								String u2 = u1.substring(0, u1.indexOf("}"));
								String usernameNotation = new StringBuilder().append(u2).append("=").toString();
								File Directory = new File(moveto);
								CheckContainsValue(Directory, usernameNotation, xmlfileName, configName);
								GVValue = "notfound";
								if (IsGVDefinitionMissing.equals("true")) {
									addToNullamqpConfig(xmlfileName, configName, configReferenceNamelineno,
											IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
											MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
											propertyfileName, NotationDefinitionlineno);
									IsGVDefinitionMissing = "N/A";

								}
							}
						}
						if (line.contains("password=")) {
							password = "found";
							if (line.contains("password=\"${")) {
								String u1 = line.substring(line.indexOf("password=") + 12);
								String u2 = u1.substring(0, u1.indexOf("}"));
								String usernameNotation = new StringBuilder().append(u2).append("=").toString();
								CheckContainsValue(rootDirectory, usernameNotation, xmlfileName, configName);
								GVValue = "notfound";
								if (IsGVDefinitionMissing.equals("true")) {
									addToNullamqpConfig(xmlfileName, configName, configReferenceNamelineno,
											IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
											MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
											propertyfileName, NotationDefinitionlineno);
									IsGVDefinitionMissing = "N/A";

								}
							}
						}
						if (line.contains("validateConnections=")) {
							validateConnections = "found";
							if (line.contains("validateConnections=\"${")) {
								String u1 = line.substring(line.indexOf("validateConnections=") + 23);
								String u2 = u1.substring(0, u1.indexOf("}"));
								String usernameNotation = new StringBuilder().append(u2).append("=").toString();
								CheckContainsValue(rootDirectory, usernameNotation, xmlfileName, configName);
								GVValue = "notfound";
								if (IsGVDefinitionMissing.equals("true")) {
									addToNullamqpConfig(xmlfileName, configName, configReferenceNamelineno,
											IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
											MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
											propertyfileName, NotationDefinitionlineno);
									IsGVDefinitionMissing = "N/A";

								}
								IsGVDefinitionMissing = "N/A";
							}
						}
						if (line.contains("host=")) {
							host = "found";
							if (line.contains("host=\"${")) {
								String u1 = line.substring(line.indexOf("host=") + 8);
								String u2 = u1.substring(0, u1.indexOf("}"));
								String usernameNotation = new StringBuilder().append(u2).append("=").toString();
								CheckContainsValue(rootDirectory, usernameNotation, xmlfileName, configName);
								GVValue = "notfound";
								if (IsGVDefinitionMissing.equals("true")) {
									addToNullamqpConfig(xmlfileName, configName, configReferenceNamelineno,
											IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
											MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
											propertyfileName, NotationDefinitionlineno);
									IsGVDefinitionMissing = "N/A";

								}
								IsGVDefinitionMissing = "N/A";
							}
						}
						if (line.contains("dynamicNotification=")) {

							dynamicNotification = "found";
							if (line.contains("dynamicNotification=\"${")) {
								String u1 = line.substring(line.indexOf("dynamicNotification=") + 23);
								String u2 = u1.substring(0, u1.indexOf("}"));
								String usernameNotation = new StringBuilder().append(u2).append("=").toString();
								CheckContainsValue(rootDirectory, usernameNotation, xmlfileName, configName);
								GVValue = "notfound";
								if (IsGVDefinitionMissing.equals("true")) {
									addToNullamqpConfig(xmlfileName, configName, configReferenceNamelineno,
											IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
											MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
											propertyfileName, NotationDefinitionlineno);
									IsGVDefinitionMissing = "N/A";

								}
								IsGVDefinitionMissing = "N/A";
							}
						}
						if (line.contains("fallbackAddresses=")) {
							fallbackAddresses = "found";
							if (line.contains("fallbackAddresses=\"${")) {
								String u1 = line.substring(line.indexOf("fallbackAddresses=") + 21);
								String u2 = u1.substring(0, u1.indexOf("}"));
								String usernameNotation = new StringBuilder().append(u2).append("=").toString();
								File Directory = new File(moveto);
								CheckContainsValue(Directory, usernameNotation, xmlfileName, configName);
								GVValue = "notfound";
								if (IsGVDefinitionMissing.equals("true")) {
									addToNullamqpConfig(xmlfileName, configName, configReferenceNamelineno,
											IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
											MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
											propertyfileName, NotationDefinitionlineno);
									IsGVDefinitionMissing = "N/A";

								}
							}
						}
						if (line.contains("ackMode=")) {
							ackMode = "found";
							if (line.contains("ackMode=\"${")) {
								String u1 = line.substring(line.indexOf("ackMode=") + 11);
								String u2 = u1.substring(0, u1.indexOf("}"));
								String usernameNotation = new StringBuilder().append(u2).append("=").toString();
								CheckContainsValue(rootDirectory, usernameNotation, xmlfileName, configName);
								GVValue = "notfound";
								if (IsGVDefinitionMissing.equals("true")) {
									addToNullamqpConfig(xmlfileName, configName, configReferenceNamelineno,
											IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
											MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
											propertyfileName, NotationDefinitionlineno);
									IsGVDefinitionMissing = "N/A";

								}
							}
						}
						if (line.contains("prefetchSize=")) {
							prefetchSize = "found";
							if (line.contains("prefetchSize=\"${")) {
								String u1 = line.substring(line.indexOf("prefetchSize=") + 16);
								String u2 = u1.substring(0, u1.indexOf("}"));
								String usernameNotation = new StringBuilder().append(u2).append("=").toString();
								CheckContainsValue(rootDirectory, usernameNotation, xmlfileName, configName);
								GVValue = "notfound";
								if (IsGVDefinitionMissing.equals("true")) {
									addToNullamqpConfig(xmlfileName, configName, configReferenceNamelineno,
											IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
											MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
											propertyfileName, NotationDefinitionlineno);
									IsGVDefinitionMissing = "N/A";

								}
								IsGVDefinitionMissing = "N/A";
							}
						}
						if (line.contains("prefetchCount=")) {
							prefetchCount = "found";
							if (line.contains("prefetchCount=\"${")) {
								String u1 = line.substring(line.indexOf("prefetchCount=") + 17);
								String u2 = u1.substring(0, u1.indexOf("}"));
								String usernameNotation = new StringBuilder().append(u2).append("=").toString();
								CheckContainsValue(rootDirectory, usernameNotation, xmlfileName, configName);
								GVValue = "notfound";
								if (IsGVDefinitionMissing.equals("true")) {
									addToNullamqpConfig(xmlfileName, configName, configReferenceNamelineno,
											IsMissingconfigReferenceDefinition, configReferenceDefinitionlineno,
											MissingParamDef, NotationGV, IsGVDefinitionMissing, IsGVValueMissing,
											propertyfileName, NotationDefinitionlineno);
									IsGVDefinitionMissing = "N/A";

								}
								IsGVDefinitionMissing = "N/A";
							}
						}
						configReferenceDefinitionlineno = "N/A";
						IsMissingconfigReferenceDefinition = "N/A";
						NotationGV = "N/A";
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public static void addToNullamqpConfig(String xmlfileName, String configName, String configReferenceNamelineno,
			String MissingconfigReferenceDefinition, String configReferenceDefinitionlineno, String MissingParamDef,
			String NotationGV, String MissingGVDefinition, String IsGVValueMissing, String propertiesfileName,
			String NotationDefinitionlineno) {

		ValidationCheck.data.add(xmlfileName);
		ValidationCheck.data.add(configName);
		ValidationCheck.data.add(configReferenceNamelineno);
		ValidationCheck.data.add(MissingconfigReferenceDefinition);
		ValidationCheck.data.add(configReferenceDefinitionlineno);
		ValidationCheck.data.add(MissingParamDef);
		ValidationCheck.data.add(MissingGVDefinition);
		ValidationCheck.data.add(NotationGV);
		ValidationCheck.data.add(IsGVValueMissing);
		ValidationCheck.data.add(propertiesfileName);
		ValidationCheck.data.add(NotationDefinitionlineno);
	}

	public static void HardCodingCheck(File mappingsPathDir, String moveto) throws Exception {
		try {
			File[] files = mappingsPathDir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					HardCodingCheck(file, moveto);
				} else // if file is .grf
				{
					if (file.isFile() && (getFileExtensionName(file).indexOf("grf") != -1)) {
						String grfFileName = file.getCanonicalPath();

						try (LineNumberReader br = new LineNumberReader(new FileReader(grfFileName))) {
							String line;
							while ((line = br.readLine()) != null) {
								while ((line.contains("[CDATA[//MEL"))) {
									while ((!(line).contains("</Node>"))) {
										// Get configReference name
										if (line.length() > 0) {
											// String y = line.substring(0,4);
											// if(!y.equals("<!--")){
											String s = "=";
											int l = br.getLineNumber();
											String lineno = "" + l;
											String NHC = line.trim();
											while ((!NHC.equals("")) && (line.length() > 0) && NHC.endsWith(";")
													&& NHC.contains(s)) {
												String HC = "";
												HC = NHC.substring(NHC.indexOf("=") + 1).trim();
												// write a break condition; a
												// line which does not contain
												// == or = will also be the
												// input now
												String FHC = "";
												int literalType = 2;
												if (HC.startsWith("\"")) {
													FHC = HC.substring(1);
													FHC = FHC.substring(0, FHC.indexOf("\""));
													literalType = 0;
												} else if (HC.startsWith("\'")) {
													FHC = HC.substring(1);
													FHC = FHC.substring(0, FHC.indexOf("\'"));
													literalType = 1;
												} else {
													if (HC.indexOf(" ") != -1) {
														FHC = HC.substring(0, HC.indexOf(" "));
														literalType = 2;
													} else {
														FHC = HC.substring(0, HC.indexOf(";"));
														literalType = 2;
													}
													if (FHC.contains("input") || FHC.contains("output")
															|| FHC.contains("(") || HC.contains("new ")) {
														break;
													}
												}
												String occurence = "";
												if (literalType == 0)
													occurence = new StringBuilder().append("= \"").append(FHC)
															.append("\"").toString();
												else if (literalType == 1)
													occurence = new StringBuilder().append("= \'").append(FHC)
															.append("\'").toString();
												else
													occurence = new StringBuffer().append("= ").append(FHC).toString();
												addToHardcodingOccurence(occurence, lineno, grfFileName);
												NHC = HC.substring(HC.indexOf(FHC));
											}
											line = br.readLine();
										} else
											line = br.readLine();
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();

						}
					}
				}
			}
			// writeToHardcodingOccurenceSheet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addToHardcodingOccurence(String occurence, String lineno, String xmlfileName) {

		ValidationCheck.data.add(occurence);
		ValidationCheck.data.add(lineno);
		ValidationCheck.data.add(xmlfileName);

		// System.out.println("occurence : " + occurence);
		// System.out.println("lineno : " + lineno);
		// System.out.println("xmlfileName : " + xmlfileName);

	}

	public static void CheckForDuplicateGVDefinitions(File dir, String moveto) throws Exception {
		int counter = 0;
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					CheckForDuplicateGVDefinitions(file, moveto);
				} else // if file is .properties
				{
					if (file.isFile() && getFileExtensionName(file).indexOf("ies") != -1 && !(file.getCanonicalPath().contains("target"))) {
						// System.out.println("file:" +
						// file.getCanonicalPath());
						String propertiesfileName = file.getCanonicalPath();
						String DuplicateConfigRef = "";
						String GVname = "";
						try (LineNumberReader br = new LineNumberReader(new FileReader(propertiesfileName))) {
							String line;
							while ((line = br.readLine()) != null && !propertiesfileName.contains("mule-app.properties")
									&& !propertiesfileName.contains("wssecurity.properties")
									&& !propertiesfileName.contains("MetaData.properties")
									&& !propertiesfileName.contains("mule-deploy.properties")) {
								counter++;
								// System.out.println("path:" + line);
								// if(!DuplicateConfigRef.contains(new
								// StringBuilder().append("|").append(GVname).append("|").toString())){
								if (line.length() > 0 && !line.equals(" ") && !line.startsWith(" #")) {
									String x = line.substring(0, 1);
									if (!x.equals("#")) {
										String substr = "=";
										String GV = line.substring(0, line.indexOf(substr));
										GVname = GV;
										String GV1 = new StringBuilder().append(GV).append("=").toString();
										int lineno = br.getLineNumber();
										String ll = "" + lineno;
										if (!DuplicateConfigRef.contains(new StringBuilder().append("|").append(GVname)
												.append("|").toString())) {
											CheckForDuplicacy(file, GV1, lineno);
											DuplicateConfigRef = new StringBuilder().append(DuplicateConfigRef)
													.append("|").append(GVname).append("|").toString();
										}
										// System.out.println(DuplicateGVdefinitions);
									}
								}

							}
							// }

						} catch (IOException e) {
							e.printStackTrace();

						}
					}
				}
			}
			// writeToDuplicateGVdefinitionsSheet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void CheckForDuplicacy(File file, String GV, int lineno) throws FileNotFoundException {
		try (LineNumberReader br = new LineNumberReader(new FileReader(file))) {
			String line;
			String add = "false";
			String Linenumbers = "";
			// String linenumber2="";
			String linenumber1 = "";
			String propertyfileName = file.getCanonicalPath();
			while ((line = br.readLine()) != null) {
				if (line.contains(GV)) {
					int w = br.getLineNumber();
					// linenumber2 = "" + w;
					linenumber1 = "" + lineno;
					if (w != lineno) {
						Linenumbers = new StringBuilder().append(Linenumbers).append("|").append(w).append("|")
								.toString();
						add = "true";
					}
				}
			}
			if (file.length() != 0 && add.equals("true")) {
				addToDuplicateGVdefinitionsList(GV, propertyfileName, linenumber1, Linenumbers);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addToDuplicateGVdefinitionsList(String GV, String propertyfileName, String linenumber1,
			String linenumber2) {
		ValidationCheck.data.add(GV);
		ValidationCheck.data.add(propertyfileName);
		ValidationCheck.data.add(linenumber1);
		ValidationCheck.data.add(linenumber2);

		// System.out.println("GV : " + GV);
		// System.out.println("propertyfileName : " + propertyfileName);
		// System.out.println("linenumber1 : " + linenumber1);
		// System.out.println("linenumber2 : " + linenumber2);

	}

	public static String xmlDefFound = "false";
	public static String SamePath = "true";

	public static void CheckForMuleDeployDefinition(File dir, String moveto) throws Exception {
		try {

			File[] files = dir.listFiles();
			File rootDirectory = new File(moveto);
			for (File file : files) {
				if (file.isDirectory()) {
					CheckForMuleDeployDefinition(file, moveto);
				} else // if file is .xml
				{
					if (file.isFile() && getFileExtensionName(file).indexOf("xml") != -1) {
						String xmlfileName = file.getCanonicalPath();
						CheckPresenceInMuleDeploy(rootDirectory, xmlfileName);
						if (xmlDefFound.equals("false") && SamePath.equals("false")) {
							addToMissInMuleDeploy(xmlfileName, xmlDefFound);
						}
						xmlDefFound = "false";
						SamePath = "true";
						deploy = "notfound";
					}
				}
			}
			// writeToMissInMuleDeploy();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String deploy = "notfound";

	public static void CheckPresenceInMuleDeploy(File rootDirectory, String xmlfileName) {

		try {

			File[] files = rootDirectory.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					CheckPresenceInMuleDeploy(file, xmlfileName);
				} else // if file is .properties
				{
					if (file.isFile() && file.getCanonicalPath().contains("mule-deploy.properties")
							&& deploy.equals("notfound")) {
						deploy = "found";
						String M = file.getCanonicalPath();
						String Mpath = M.substring(0, M.lastIndexOf("mule-deploy.properties"));
						String XPath = xmlfileName.substring(0, xmlfileName.lastIndexOf("\\"));
						String NXPath = new StringBuilder().append(XPath).append("\\").toString();
						int Q = Mpath.length();
						String A = "";
						if (XPath.length() >= Mpath.length() && NXPath.contains(Mpath)) {
							String Z = xmlfileName.substring(xmlfileName.indexOf(Mpath) + Q);
							while (!Z.equals("") && Z.contains("\\")) {
								String K = Z.substring(0, Z.indexOf("\\"));
								String O = Z.substring(Z.indexOf("\\") + 1);
								A = new StringBuilder().append(A).append(K).append("/").toString();
								if (!O.contains("\\")) {
									A = new StringBuilder().append(A).append(O).toString();
								}
								Z = O;
							}
							if (NXPath.contains(Mpath)) {
								if (!Mpath.equals(NXPath)) {
									LineNumberReader br = new LineNumberReader(new FileReader(M));
									String line;
									while ((line = br.readLine()) != null) {
										if (line.length() > 0 && line.contains(A)) {
											xmlDefFound = "true";
										}
									}
									SamePath = "false";
								}

							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void addToMissInMuleDeploy(String xmlfileName, String xmlDefFound) {
		ValidationCheck.data.add(xmlfileName);
		ValidationCheck.data.add(xmlDefFound);
	}
	
	public static void UnusedPOJO(File dir, String moveto) throws Exception {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					UnusedPOJO(file, moveto);
				} else // if file is .xml
				{
					if (file.isFile() && getFileExtensionName(file).indexOf("xml") != -1 && (!(file.getCanonicalPath().contains("target")))) {
						String xmlfileName = file.getCanonicalPath();
						try (BufferedReader br = new BufferedReader(new FileReader(xmlfileName))) {
							String line;
							while ((line = br.readLine()) != null && !xmlfileName.contains("mule-project.xml")
									&& !xmlfileName.contains("pom.xml")) {
								if (line.contains("flowVars.") && line.contains("get") && line.contains("()")) {
										addToUnusedPOJOList(line, xmlfileName);
								}
							}

						} catch (IOException e) {
							e.printStackTrace();

						}
					}
				}
			}
			// writeToUnusedFlowVarsSheet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addToUnusedPOJOList(String FlowVarNotation, String xmlfileName) {

		FlowVarNotation = FlowVarNotation.replace("${", "");
		FlowVarNotation = FlowVarNotation.replace("}", "");
		// System.out.print("Flow Var : " + FlowVarNotation);
		// System.out.println("xml : " + xmlfileName);
		ValidationCheck.data.add(FlowVarNotation);
		ValidationCheck.data.add(xmlfileName);

	}
	
	
}