package com.bilgeadam.constants;

public class RestApis {
	private static final String VERSION = "/v1";
	private static final String DEVELOPER = "/dev";
	private static final String TEST = "/test";
	
	private static final String ROOT = VERSION+DEVELOPER;
	
	public static final String FILE = ROOT + "/file";
	public static final String FOLDER = ROOT + "/folder";

	public static final String UPLOAD_FILE="/upload-file";
	public static final String UPLOAD_FILE_="/upload-file-to-folder";
	public static final String DOWNLOAD_FILE="/download-file";
	public static final String DELETE_FILE="/delete-file";
	public static final String GET_ALL_FILES="/get-all-files";
	public static final String RENAME_FILE="/rename-file";
	public static final String GET_FILE_URL="/get-file-url";
	public static final String GET_ALL_FILES_URL="/get-all-files-url";

	public static final String CREATE_FOLDER="/create-folder";
	public static final String UPDATE_FOLDER="/update-folder";
	public static final String MOVE_FOLDER="/move-folder";
	public static final String LIST_FOLDER="/list-folder";
	public static final String DELETE_FOLDER="/delete-folder";

}