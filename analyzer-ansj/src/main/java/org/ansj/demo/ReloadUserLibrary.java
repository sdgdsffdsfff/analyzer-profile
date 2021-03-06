package org.ansj.demo;

import java.io.File;

import love.cq.domain.Forest;
import love.cq.library.Library;

import org.ansj.library.UserDefineLibrary;

/**
 * 重新加载用户自定义辞典的两种方式
 * 
 * @author wanggang
 *
 */
public class ReloadUserLibrary {
	public static void main(String[] args) {
		// 从文件中reload
		loadFormFile();
		// 通过内存中reload
		loadFormStr();
	}

	private static void loadFormStr() {
		// TODO Auto-generated method stub
		Forest forest = new Forest();

		Library.insertWord(forest, "中国  nature  1000");
		// 将新构建的辞典树替换掉旧的。
		UserDefineLibrary.FOREST = forest;
	}

	private static void loadFormFile() {
		// TODO Auto-generated method stub
		// make new forest
		Forest forest = new Forest();

		UserDefineLibrary.loadFile(forest, new File("new_Library_Path"));

		// 将新构建的辞典树替换掉舊的。
		UserDefineLibrary.FOREST = forest;
	}
}
