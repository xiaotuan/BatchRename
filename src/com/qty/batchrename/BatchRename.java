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
		
		printDebugMessage("参数个数: " + argMaps.size());
		if (argMaps.size() == 1 && argMaps.containsKey("help")) {
			showHelper();
			return;
		}
		
		if (!argMaps.containsKey("path")) {
			printErrorMessage("需要传递重命名文件的目录路径");
			return;
		}
		
		String path = argMaps.get("path");
		String userDir = System.getProperty("user.dir");
		printDebugMessage("当前目录路径: " + userDir);
		File dirFile = new File(argMaps.get("path"));
		printDebugMessage("传入的路径是否是绝对路径: " + dirFile.isAbsolute());
		if (!dirFile.isAbsolute()) {
			dirFile = new File(userDir + File.separator + path);
		}
		
		if (!dirFile.isDirectory()) {
			printErrorMessage(dirFile.getPath() + " 不是一个目录");
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
				System.out.println(dirFile.getAbsolutePath() + " 目录中没有找到文件。");
				return;
			} else {
				printArrayList("目录中包含如下文件: ", files);
			}
		}
		
		printDebugMessage("排序文件...");
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
		
		printArrayList("排序后的文件列表: ", files);
		
		String prefix = "";
		if (argMaps.containsKey("prefix")) {
			prefix = argMaps.get("prefix");
			System.out.println("重命名文件名前缀为: " + prefix);
		}
		int startNumber = 0;
		if (argMaps.containsKey("number")) {
			startNumber = Integer.parseInt(argMaps.get("number"));
			System.out.println("文件起始编号为: " + startNumber);
		}
		printDebugMessage("开始重命名文件。。。");
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
		
		printArrayList("重命名后的文件列表: ", files);
		
		System.out.println("完成。。。");
	}
	
	private static void showHelper() {
		System.out.println("");
		System.out.println("使用方法:");
		System.out.println("    -P / --path <path> 指定需要重命名文件所在的文件夹路径");
		System.out.println("    -p / --prefix <string> 指定重命名文件名前缀");
		System.out.println("    -n / --number <int> 指定文件起始编号");
		System.out.println("");
	}

	private static HashMap<String, String> parseArgs(String[] args) {
		printDebugMessage("参数: " + Arrays.toString(args));
		if (args == null || args.length == 0) {
			printErrorMessage("未设置参数");
			return null;
		}
		HashMap<String, String> results = new HashMap<>();
		String key = null;
		for (int i = 0; i < args.length; i++) {
			printDebugMessage("参数(" + i + "): " + args[i]);
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
				case "--prefix":
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
								printErrorMessage("文件起始编号必须是整数。\n" + e.getLocalizedMessage());
								System.exit(-1);
							}
						}
						results.put(key, args[i].trim());
						key = null;
					} else {
						System.out.println("未知参数: " + args[i].trim());
						System.exit(-2);
						key = null;
					}
					break;
			}
			
		}
		if (key != null) {
			printErrorMessage("参数不正确");
			System.exit(-3);
		}
		return results;
	}
	
	private static void printErrorMessage(String msg) {
		System.err.println("错误: " + msg);
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

