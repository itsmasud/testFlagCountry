package com.fieldnation.fntools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

/**
 * Loads a simple config file into memory. Config files are of the following
 * form
 * 
 * ### Comments start with a #
 * key = value
 * 
 * @author mc
 * 
 */
public class ConfigFile {
	private static final Hashtable<String, ConfigFile> _configFiles = new Hashtable<String, ConfigFile>();

	private final Hashtable<String, String> _parameters;
	private final String _filename;


	/**
	 * Loads the config file at filename and caches the data. If the config file
	 * is already loaded, then the cache is returned.
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static ConfigFile getInstance(String filename) throws IOException {
		if (_configFiles.containsKey(filename)) {
			return _configFiles.get(filename);
		}

		ConfigFile config = new ConfigFile(filename);

		_configFiles.put(filename, config);

		return config;
	}


	private ConfigFile(String file) throws IOException {
		_parameters = new Hashtable<String, String>();
		_filename = file;

		loadFile();
	}


	private void loadFile() throws IOException {

		File file = new File(_filename);

		BufferedReader stream = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));

		String line = null;
		while ((line = stream.readLine()) != null) {

			line = line.trim();

			if (line.startsWith("#")) {
				continue;
			}

			if (!line.contains("=")) {
				continue;
			}

			String name = line.substring(0, line.indexOf("=")).trim().toLowerCase();
			String value = line.substring(line.indexOf("=") + 1).trim();

			_parameters.put(name, value);
		}

		stream.close();

	}


	/**
	 * Returns the value of the parameter named name. Names are case
	 * insensitive.
	 * 
	 * @param name
	 * @return
	 */
	public String getString(String name) {
		if (!_parameters.containsKey(name.toLowerCase())) {
			return null;
		}

		return _parameters.get(name.toLowerCase());
	}
}
