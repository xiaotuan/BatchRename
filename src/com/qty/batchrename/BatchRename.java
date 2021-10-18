package com.qty.batchrename;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class BatchRename {
	
	private static final boolean DEBUG = false;

	public static void main(String[] args) {
		if (args.length == 0) {
			showHelper();
			return;
		}
		
		HashMap<String, String> argMaps = parseArgs(args);
		
		if (argMaps == null) {
			return;
		}
		
		printDebugMessage("��������: " + argMaps.size());
		if (argMaps.size() == 1 && argMaps.containsKey("help")) {
			showHelper();
			return;
		}
		
		if (!argMaps.containsKey("path")) {
			printErrorMessage("��Ҫ�����������ļ���Ŀ¼·��");
			return;
		}
		
		String path = argMaps.get("path");
		String userDir = System.getProperty("user.dir");
		printDebugMessage("��ǰĿ¼·��: " + userDir);
		File dirFile = new File(argMaps.get("path"));
		printDebugMessage("�����·���Ƿ��Ǿ���·��: " + dirFile.isAbsolute());
		if (!dirFile.isAbsolute()) {
			dirFile = new File(userDir + File.separator + path);
		}
		
		if (!dirFile.isDirectory()) {
			printErrorMessage(dirFile.getPath() + " ����һ��Ŀ¼");
			return;
		}
		
		File[] originFiles = dirFile.listFiles();
		ArrayList<File> files = new ArrayList<File>();
		for (int i = 0; i < originFiles.length; i++) {
			if (originFiles[i].exists() && originFiles[i].isFile()) {
				files.add(originFiles[i]);
			}
		}
		
		if (DEBUG) {
			if (files.size() == 0) {
				System.out.println(dirFile.getAbsolutePath() + " Ŀ¼��û���ҵ��ļ���");
				return;
			} else {
				printArrayList("Ŀ¼�а��������ļ�: ", files);
			}
		}
		
		printDebugMessage("�����ļ�...");
		Collections.sort(files, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				
				String name1 = o1.getName().substring(0, o1.getName().indexOf("."));
				String name2 = o2.getName().substring(0, o1.getName().indexOf("."));
				printDebugMessage("Name 1: " + name1 + ", Name 2: " + name2);
				if (name1.length() == name2.length()) {
					return name1.compareTo(name2);
				} else {
					return name1.length() - name2.length();
				}
			}
		});
		
		printArrayList("�������ļ��б�: ", files);
		
		String prefix = "";
		if (argMaps.containsKey("prefix")) {
			prefix = argMaps.get("prefix");
			System.out.println("�������ļ���ǰ׺Ϊ: " + prefix);
		}
		int startNumber = 0;
		if (argMaps.containsKey("number")) {
			startNumber = Integer.parseInt(argMaps.get("number"));
			System.out.println("�ļ���ʼ���Ϊ: " + startNumber);
		}
		printDebugMessage("��ʼ�������ļ�������");
		File file = null;
		int maxWidth = ((files.size() + startNumber) + "").length();
		for (int i = 0; i < files.size(); i++) {
			file = files.get(i);
			String fileName = String.format("%s%0" + maxWidth + "d%s", 
					prefix, startNumber, file.getName().substring(file.getName().lastIndexOf(".")));
			File renameFile = new File(file.getParentFile().getAbsolutePath() + File.separator + fileName);
			printDebugMessage(file.getName() + " ---> " + renameFile.getName());
			file.renameTo(renameFile);
			startNumber++;
		}
		
		printArrayList("����������ļ��б�: ", files);
		
		System.out.println("��ɡ�����");
	}
	
	private static void showHelper() {
		System.out.println("");
		System.out.println("ʹ�÷���:");
		System.out.println("    -P / --path <path> ָ����Ҫ�������ļ����ڵ��ļ���·��");
		System.out.println("    -p / --Prefix <string> ָ���������ļ���ǰ׺");
		System.out.println("    -n / --number <int> ָ���ļ���ʼ���");
		System.out.println("");
	}

	private static HashMap<String, String> parseArgs(String[] args) {
		printDebugMessage("����: " + Arrays.toString(args));
		if (args == null || args.length == 0) {
			printErrorMessage("δ���ò���");
			return null;
		}
		HashMap<String, String> results = new HashMap<>();
		String key = null;
		for (int i = 0; i < args.length; i++) {
			printDebugMessage("����(" + i + "): " + args[i]);
			switch (args[i]) {
				case "-P":
				case "--path":
					key = "path";
					break;
					
				case "-h":
				case "--help":
					results.put("help", "help");
					break;
					
				case "-p":
				case "--Prefix":
					key = "prefix";
					break;
					
				case "-n":
				case "--number":
					key = "number";
					break;
				
				default:
					if (key != null) {
						if (key.equals("number")) {
							try {
								Integer.parseInt(args[i].trim());
							} catch (Exception e) {
								printErrorMessage("�ļ���ʼ��ű�����������\n" + e.getLocalizedMessage());
								System.exit(-1);
							}
						}
						results.put(key, args[i].trim());
						key = null;
					} else {
						System.out.println("δ֪����: " + args[i].trim());
						System.exit(-2);
					}
					break;
			}
			
		}
		return results;
	}
	
	private static void printErrorMessage(String msg) {
		System.err.println("����: " + msg);
		System.err.println("");
	}
	
	private static void printDebugMessage(String msg) {
		if (DEBUG) System.out.println(msg);
	}
	
	private static void printArrayList(String title, ArrayList<File> files) {
		if (DEBUG) {
			System.out.println("\n" + title);
			for (int i = 0; i < files.size(); i++) {
				System.out.println(files.get(i).getName());
			}
			System.out.println("");
		}
	}
}
